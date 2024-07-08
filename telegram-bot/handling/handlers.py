import datetime
import os
import random
import re
from os import getenv

from aiogram import Router, types, F
from aiogram.exceptions import TelegramBadRequest
from aiogram.filters import Command, StateFilter
from aiogram.fsm.context import FSMContext
from aiogram.types import Message, CallbackQuery, ReplyKeyboardRemove

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

    regexp = re.compile('^(\\w)\\.(\\w+)@(innopolis\\.university|innopolis\\.ru)$')
    if regexp.match(user_email):
        code = str(random.randint(100000, 999999))
        mail_client.send_authentication_code(user_email, code)
        await msg.answer(f'Confirmation code was sent to <code>{user_email}</code>. '
                         f'In order to finish authorization, send it here. '
                         f'Code expires in <b>{os.getenv("EMAIL_CODE_EXPIRATION_MINS")} minutes.</b>'
                         f'\n\nIf you figured out that email address contains mistake, '
                         f'press the button below.',
                         reply_markup=all_callbacks.get_email_kb())
        await state.update_data({
            "email": user_email,
            "expected_code": code,
            "code_expiration": datetime.datetime.now() +
                               datetime.timedelta(minutes=float(os.getenv("EMAIL_CODE_EXPIRATION_MINS")))
        })
        await state.set_state(DialogStates.AuthorizationConfirmationAwaiting)

    else:
        await msg.answer('Sorry, seems like it is not a correct Innopolis University '
                         'email address.\n\n'
                         '<b><u>Please, send your Innopolis University email in order to authorize</u></b>')


@router.callback_query(StateFilter(DialogStates.AuthorizationConfirmationAwaiting),
                       all_callbacks.EmailCallback.filter())
async def auth_email_revert(callback: types.CallbackQuery, state: FSMContext):
    await callback.message.answer(text='Please, enter your Innopolis University email again in order to authorize.')
    await state.set_data({})
    await state.set_state(DialogStates.AuthorizationAwaiting)
    await callback.answer('Email reset')


@router.message(StateFilter(DialogStates.AuthorizationConfirmationAwaiting))
async def auth_code_process(message: Message, state: FSMContext, mail_client: Client):
    expected_code = (await state.get_data())["expected_code"]
    expiration_time = (await state.get_data())["code_expiration"]
    user_email = (await state.get_data())["email"]

    if datetime.datetime.now() > expiration_time:
        await message.answer(
            f"Your authorization code has expired\n\n"
            f"New one was sent to previously `{user_email}`",
            reply_markup=all_callbacks.get_email_kb(),
            parse_mode="Markdown"
        )
        code = str(random.randint(100000, 999999))
        await state.update_data(
            code_expiration=datetime.datetime.now() +
                            datetime.timedelta(minutes=float(getenv("EMAIL_CODE_EXPIRATION_MINS"))),
            expected_code=code
        )
        mail_client.send_authentication_code(user_email, code)
    elif expected_code == message.text:
        await message.answer("You are authorized now! Enjoy the usage of monidorm!",
                             reply_markup=all_callbacks.get_main_menu_kb())
        await state.set_state(DialogStates.Authorized)
        await state.update_data(expected_code="")
        await state.update_data(alerts=list())
    else:
        await message.answer("The code seems to be incorrect. Please, try again or reset the email.")


@router.message(StateFilter(DialogStates.Authorized), F.text == "⚠️ Report")
async def failure_report_init(
        msg: Message,
        report_provider: ReportCallbackProvider,
        state: FSMContext
):
    callback = report_provider.get_callback(1)
    user_data = await state.get_data()

    transition_path = callback.get_path()
    if transition_path not in user_data['alerts']:
        keyboard = callback.keyboard(False)
    else:
        keyboard = callback.keyboard(True)

    dumb_msg = await msg.answer('.', reply_markup=ReplyKeyboardRemove())
    await dumb_msg.delete()

    await msg.answer(
        'Report a failure or update preferences!',
        reply_markup=keyboard
    )

    await state.set_state(DialogStates.Reporting)
    await msg.delete()


@router.message(
    StateFilter(
        DialogStates.Authorized,
        DialogStates.Reporting,
        DialogStates.ReportCommentAwaiting
    ), Command("menu")
)
async def interrupt_and_go_to_menu(msg: Message, state: FSMContext):
    await msg.answer('You are at the main menu now 😊', reply_markup=all_callbacks.get_main_menu_kb())
    await state.set_state(DialogStates.Authorized)
    await msg.delete()


@router.callback_query(StateFilter(DialogStates.Reporting), report_callbacks.ReportingKbCallback.filter())
async def report_processing(
        query: CallbackQuery,
        callback_data: report_callbacks.ReportingKbCallback,
        state: FSMContext,
        report_provider: ReportCallbackProvider
):
    go_to = callback_data.window_id
    callback = report_provider.get_callback(go_to)
    user_data = await state.get_data()
    if isinstance(callback, CancelCallback):
        await query.message.reply(text="You are at the main menu now 😊",
                                  reply_markup=all_callbacks.get_main_menu_kb())
        await query.message.delete()
        await state.set_state(DialogStates.Authorized)
    elif isinstance(callback, CategoryCallback):
        await query.message.edit_text(
            'If you would like to provide some commentaries on failure — '
            'please, send a message with them, otherwise press the button below\n\n'
            'If you would like go back — press the corresponding button',
            reply_markup=all_callbacks.get_detailed_report_kb(callback.parent_id())
        )
        await state.update_data(report={
            'placement': callback.placements(),
            'category': callback.category()
        })
        await state.set_state(DialogStates.ReportCommentAwaiting)
    elif isinstance(callback, TransitionCallback):
        transition_path = callback.get_path()
        if transition_path not in user_data['alerts']:
            keyboard = callback.keyboard(False)
        else:
            keyboard = callback.keyboard(True)
        await query.message.edit_reply_markup(reply_markup=keyboard)


@router.callback_query(StateFilter(DialogStates.Reporting), report_callbacks.AlertsStatusCallback.filter())
async def alerts_status_switch(
        query: CallbackQuery,
        callback_data: report_callbacks.AlertsStatusCallback,
        state: FSMContext,
        report_provider: ReportCallbackProvider
):
    user_data = await state.get_data()
    if callback_data.enable:
        user_data['alerts'].append(callback_data.path)
        keyboard = report_provider.get_callback(callback_data.window_id).keyboard(True)
        await query.answer('Alerts enabled')
    else:
        user_data['alerts'].remove(callback_data.path)
        keyboard = report_provider.get_callback(callback_data.window_id).keyboard(False)
        await query.answer('Alerts disabled')

    await state.set_data(user_data)
    await query.message.edit_reply_markup(reply_markup=keyboard)


@router.message(StateFilter(DialogStates.Authorized), F.text == "👋 Logout")
async def user_logout_init(msg: Message, state: FSMContext):
    await msg.answer('See you! Successfully logged out', reply_markup=ReplyKeyboardRemove())
    await state.clear()


@router.callback_query(StateFilter(DialogStates.ReportCommentAwaiting),
                       all_callbacks.FinalizeReport.filter(F.action == all_callbacks.Action.report))
async def send_report_no_comment(query: CallbackQuery, state: FSMContext, reporter: Reporter):
    await query.message.answer(
        'Report is sent. Thank you!',
        reply_markup=all_callbacks.get_main_menu_kb()
    )
    await query.message.delete()

    user_data = await state.get_data()
    user_report = (ReportBuilder()
                   .add_category(user_data['report']['category'])
                   .add_placement(user_data['report']['placement'])
                   .add_email(user_data['email'])
                   .stamp_datetime()
                   ).build()
    reporter.report(user_report)
    await state.set_state(DialogStates.Authorized)


@router.message(StateFilter(DialogStates.ReportCommentAwaiting))
async def extend_report_with_comment(msg: Message, state: FSMContext, reporter: Reporter):
    description = msg.text
    user_data = await state.get_data()
    report = user_data["report"]
    report["description"] = description

    user_report = (ReportBuilder()
                   .add_category(report['category'])
                   .add_placement(report['placement'])
                   .add_email(user_data['email'])
                   .stamp_datetime()
                   .add_description(report['description'])
                   ).build()
    reporter.report(user_report)

    prev_msg_id = msg.message_id - 1

    try:
        await msg.bot.delete_message(chat_id=msg.chat.id, message_id=prev_msg_id)
    except TelegramBadRequest:
        pass

    await msg.answer('Report is sent! Thank you for detailed failure description!',
                     reply_markup=all_callbacks.get_main_menu_kb())
    await state.set_state(DialogStates.Authorized)
    await state.update_data(report=report)


@router.callback_query(StateFilter(DialogStates.ReportCommentAwaiting),
                       all_callbacks.FinalizeReport.filter(F.action == all_callbacks.Action.abort))
async def report_processing(
        query: CallbackQuery,
        callback_data: all_callbacks.FinalizeReport,
        state: FSMContext,
        report_provider: ReportCallbackProvider
):
    go_to = callback_data.back_window
    callback = report_provider.get_callback(go_to)
    user_data = await state.get_data()

    transition_path = callback.get_path()
    if transition_path not in user_data['alerts']:
        keyboard = callback.keyboard(False)
    else:
        keyboard = callback.keyboard(True)
    await query.message.edit_text(
        text='Report a failure or update preferences!',
        reply_markup=keyboard
    )
    await state.set_state(DialogStates.Reporting)


@router.message(StateFilter(DialogStates.Authorized), F.text == "📔 My alerts")
async def show_subscribed_alerts(
        msg: Message,
        state: FSMContext,
        report_provider: ReportCallbackProvider
):
    user_alerts = (await state.get_data())['alerts']
    if len(user_alerts) == 0:
        answer = ("You don't have any active failure alerts subscriptions. "
                  "You can enable them in the reporting stage")
    else:
        alerts = list()
        for alert in user_alerts:
            alerts.append("> " + ', '.join(report_provider.get_human_readable_path_en(alert)))
        alerts.sort()
        answer = f"<b>Your alerts here</b>\n\n{'\n'.join(alerts)}"
    await msg.answer(text=answer)


@router.callback_query(lambda _: True)
async def outdated_button_pressed(
        query: CallbackQuery
):
    """
    React on outdated button pressures with its removal
    :param query: button pressure
    :return: None
    """
    await query.answer(text='The action is outdated')
    await query.message.delete()


@router.message(lambda _: True)
async def invalid_message(message: Message):
    """
    React on unexpected messages with its removal
    and reaction
    :param message: incoming message
    :return: None
    """
    await message.answer(
        '<b>Oops!</b> Seems like you are typing something on the stage '
        'what it is not expected here. Please, use <b>/menu</b> command if you stuck '
        'to get into main menu 😊'
    )
    await message.delete()
