import datetime
import os
import random
import re
from os import getenv

from aiogram import Router, types
from aiogram.filters import Command, StateFilter
from aiogram.fsm.context import FSMContext
from aiogram.types import Message, CallbackQuery

import handling.callbacks as all_callbacks
import reporting.callbacks as report_callbacks
from handling.dialog import DialogStates
from mail.client import Client
from reporting.callbacks import *
from reporting.reporter import Reporter, ReportBuilder

router = Router()


@router.message(StateFilter(None))
async def auth_init(msg: Message, state: FSMContext):
    await msg.answer('Hello! In order to use monidorm bot, you must authorize. '
                     'Please, send your Innopolis University email.')
    await state.set_state(state=DialogStates.AuthorizationAwaiting)


@router.message(StateFilter(DialogStates.AuthorizationAwaiting))
async def auth_email_process(msg: Message, state: FSMContext, mail_client: Client):
    user_email = msg.text.strip()

    regexp = re.compile('^(\\w)\\.(\\w+)@innopolis\\.university$')
    if regexp.match(user_email):
        code = str(random.randint(100000, 999999))
        mail_client.send_authentication_code(user_email, code)
        await state.update_data({
            "email": user_email,
            "expected_code": code,
            "code_expiration": datetime.datetime.now() +
                               datetime.timedelta(minutes=float(os.getenv("EMAIL_CODE_EXPIRATION_MINS")))
        })
        await state.set_state(DialogStates.AuthorizationConfirmationAwaiting)
        await msg.answer(f'Confirmation code was sent to `{user_email}`. '
                         f'In order to finish authorization, send it here.'
                         f'\n\nIf you figured out that email address contains mistake, '
                         f'press the button below.',
                         reply_markup=all_callbacks.get_email_kb(), parse_mode="Markdown")

    else:
        await msg.answer('Sorry, seems like it is not a correct Innopolis University '
                         'email address. Please, try again.')


@router.callback_query(StateFilter(DialogStates.AuthorizationConfirmationAwaiting),
                       all_callbacks.EmailCallback.filter())
async def auth_email_revert(callback: types.CallbackQuery, state: FSMContext):
    await state.set_data({})
    await state.set_state(DialogStates.AuthorizationAwaiting)
    await callback.message.answer(text='Please, enter your Innopolis University email again in order to authorize.')


@router.message(StateFilter(DialogStates.AuthorizationConfirmationAwaiting))
async def auth_code_process(message: Message, state: FSMContext, mail_client: Client):
    expected_code = (await state.get_data())["expected_code"]
    expiration_time = (await state.get_data())["code_expiration"]
    user_email = (await state.get_data())["email"]

    if datetime.datetime.now() > expiration_time:
        code = str(random.randint(100000, 999999))
        await state.update_data(
            code_expiration=datetime.datetime.now() +
                            datetime.timedelta(minutes=float(getenv("EMAIL_CODE_EXPIRATION_MINS"))),
            expected_code=code
        )
        mail_client.send_authentication_code(user_email, code)
        await message.answer(
            f"Your authorization code has expired\n\n"
            f"New one was sent to previously `{user_email}`",
            reply_markup=all_callbacks.get_email_kb(),
            parse_mode="Markdown"
        )
    elif expected_code == message.text:
        await state.set_state(DialogStates.Authorized)
        await state.update_data(expected_code="")
        await message.answer("You are authorized now! Enjoy the usage of monidorm!")
    else:
        await message.answer("The code seems to be incorrect. Please, try again or reset the email.")


@router.message(StateFilter(DialogStates.Authorized), Command("report"))
async def failure_report_init(
        msg: Message,
        report_provider: ReportCallbackProvider,
        state: FSMContext
):
    initial_transition = report_provider.get_callback(1)
    await state.set_state(DialogStates.Reporting)
    await msg.answer('Report a failure!', reply_markup=initial_transition.keyboard())


@router.message(
    StateFilter(
        DialogStates.Authorized,
        DialogStates.Reporting,
        DialogStates.ReportCommentAwaiting
    ), Command("menu")
)
async def interrupt_and_go_to_menu(msg: Message, state: FSMContext):
    await state.set_state(DialogStates.Authorized)
    await msg.answer('Everything is interrupted, you are at the main menu now.')


@router.callback_query(StateFilter(DialogStates.Reporting), report_callbacks.ReportingKbCallback.filter())
async def report_processing(
        query: CallbackQuery,
        callback_data: report_callbacks.ReportingKbCallback,
        state: FSMContext,
        report_provider: ReportCallbackProvider
):
    go_to = callback_data.window_id
    callback = report_provider.get_callback(go_to)
    if isinstance(callback, CancelCallback):
        await state.set_state(DialogStates.Authorized)
        await query.message.delete()
    elif isinstance(callback, CategoryCallback):
        await state.update_data(report={
            'placement': callback.placements(),
            'category': callback.category()
        })
        # TODO: Question - is it better to send report+comment together, or perform update with comment?
        await state.set_state(DialogStates.ReportCommentAwaiting)
        await query.message.edit_text(
            'Thank you for the assistance!\n\n'
            'If you would like to provide some commentaries on failure — '
            'please, send a message with them, otherwise press the button below',
            reply_markup=all_callbacks.get_detailed_report_kb()
        )
    elif isinstance(callback, TransitionCallback):
        await query.message.edit_reply_markup(reply_markup=callback.keyboard())


@router.message(StateFilter(DialogStates.Authorized), Command("logout"))
async def user_logout_init(msg: Message, state: FSMContext):
    await msg.answer('See you! Logging out...')
    await state.clear()


@router.callback_query(StateFilter(DialogStates.ReportCommentAwaiting),
                       all_callbacks.DetailedReportCallback.filter())
async def send_report_no_comment(query: CallbackQuery, state: FSMContext, reporter: Reporter):
    # TODO: send report to API
    user_data = await state.get_data()
    user_report = (ReportBuilder()
                   .add_category(user_data['report']['category'])
                   .add_placement(user_data['report']['placement'])
                   .add_email(user_data['email'])
                   .stamp_datetime()
                   ).build()
    reporter.report(user_report)

    await query.message.edit_text('Report is sent. Thank you!')
    await state.set_state(DialogStates.Authorized)


@router.message(StateFilter(DialogStates.ReportCommentAwaiting))
async def extend_report_with_comment(msg: Message, state: FSMContext, reporter: Reporter):
    description = msg.text
    user_data = await state.get_data()
    report = user_data["report"]
    report["description"] = description

    await state.update_data(report=report)

    user_report = (ReportBuilder()
                   .add_category(report['category'])
                   .add_placement(report['placement'])
                   .add_email(user_data['email'])
                   .stamp_datetime()
                   .add_description(report['description'])
                   ).build()
    reporter.report(user_report)

    await msg.answer('Report is sent! Thank you for detailed failure description!')
    await state.set_state(DialogStates.Authorized)
