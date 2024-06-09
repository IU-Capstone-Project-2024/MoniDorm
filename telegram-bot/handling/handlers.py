import re

from aiogram.filters import Command, StateFilter
from aiogram import Router, types
from aiogram.fsm.context import FSMContext
from aiogram.types import Message

import handling.callbacks as kb

from handling.dialog import DialogStates

router = Router()


@router.message(StateFilter(None))
async def auth_init(msg: Message, state: FSMContext):
    # TODO: check with database whether the user is logged in
    await msg.answer('Hello! In order to use monidorm bot, you must authorize. '
                     'Please, send your Innopolis University email.')
    await state.set_state(state=DialogStates.AuthorizationAwaiting)


@router.message(StateFilter(DialogStates.AuthorizationAwaiting))
async def auth_email_process(msg: Message, state: FSMContext):
    user_email = msg.text.strip()

    regexp = re.compile('^(\\w)\\.(\\w+)@innopolis\\.university$')
    if regexp.match(user_email):
        # TODO: send email anyhow
        await state.update_data({
            "user_email": user_email,
            "code": "228"
        })
        await state.set_state(DialogStates.AuthorizationConfirmationAwaiting)
        await msg.answer(f'Confirmation code was sent to {user_email}. '
                         f'In order to finish authorization, send it here.'
                         f'\n\nIf you figured out that email address contains mistake, '
                         f'press the button below.',
                         reply_markup=kb.get_email_kb())

    else:
        await msg.answer('Sorry, seems like it is not a correct Innopolis University '
                         'email address. Please, try again.')


@router.callback_query(StateFilter(DialogStates.AuthorizationConfirmationAwaiting), kb.EmailCallbackFactory.filter())
async def auth_email_revert(callback: types.CallbackQuery, state: FSMContext):
    await state.set_data({})
    await state.set_state(DialogStates.AuthorizationAwaiting)
    await callback.message.answer(text='Please, enter your Innopolis University email again in order to authorize.')


@router.message(StateFilter(DialogStates.AuthorizationConfirmationAwaiting))
async def auth_code_process(message: Message, state: FSMContext):
    expected_code = (await state.get_data())["code"]
    if expected_code == message.text:
        await state.set_state(DialogStates.Authorized)
        await state.set_data({})
        await message.answer("You are authorized now! Enjoy the usage of monidorm!")
    else:
        await message.answer("The code seems to be incorrect. Please, try again or reset the email.")


@router.message(StateFilter(DialogStates.Authorized), Command("report"))
async def failure_report_init(msg: Message, state: FSMContext):
    pass


@router.message(StateFilter(DialogStates.Authorized), Command("logout"))
async def user_logout_init(msg: Message, state: FSMContext):
    await msg.answer('See you! Logging out...')
    await state.clear()
    # TODO: delete entry from database
