import random
import re

from aiogram.filters import Command, StateFilter
from aiogram import Router, types
from aiogram.fsm.context import FSMContext
from aiogram.types import Message, CallbackQuery

import handling.callbacks as all_callbacks
import reporting.callbacks as report_callbacks

from handling.dialog import DialogStates
from reporting.callbacks import ReportCallbackProvider
from mail.client import Client

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
            "expected_code": code
        })
        await state.set_state(DialogStates.AuthorizationConfirmationAwaiting)
        await msg.answer(f'Confirmation code was sent to {user_email}. '
                         f'In order to finish authorization, send it here.'
                         f'\n\nIf you figured out that email address contains mistake, '
                         f'press the button below.',
                         reply_markup=all_callbacks.get_email_kb())

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
async def auth_code_process(message: Message, state: FSMContext):
    expected_code = (await state.get_data())["expected_code"]
    if expected_code == message.text:
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
    keyboard = report_provider.get_callback(1)
    await state.set_state(DialogStates.Reporting)
    await msg.answer('Report a failure!', reply_markup=keyboard['keyboard'])


@router.callback_query(StateFilter(DialogStates.Reporting), report_callbacks.ReportCallback.filter())
async def report_processing(
        query: CallbackQuery,
        callback_data: report_callbacks.ReportCallback,
        state: FSMContext,
        report_provider: ReportCallbackProvider
):
    go_to = callback_data.window_id
    callback_properties = report_provider.get_callback(go_to)
    if callback_properties['action'] == 'cancel':
        await state.set_state(DialogStates.Authorized)
        await query.message.delete()
    elif callback_properties['action'] == 'report':
        print(callback_properties['meta'], (await state.get_data())['email'])
        await state.set_state(DialogStates.Authorized)
        await query.message.edit_text('Report is sent. Thank you for assistance!')
    else:
        await query.message.edit_reply_markup(reply_markup=report_provider.get_callback(go_to)['keyboard'])


@router.message(StateFilter(DialogStates.Authorized), Command("logout"))
async def user_logout_init(msg: Message, state: FSMContext):
    await msg.answer('See you! Logging out...')
    await state.clear()
