from aiogram.filters import Command, StateFilter
from aiogram import Router, F
from aiogram.fsm.context import FSMContext
from aiogram.types import Message

from dialog import DialogStates

router = Router()


@router.message(StateFilter(None, DialogStates.Unauthorized))
async def auth_init(_: Message, state: FSMContext):
    pass


@router.message(StateFilter(DialogStates.AuthorizationAwaiting))
async def auth_email_process(_: Message, state: FSMContext):
    pass


@router.message(StateFilter(DialogStates.AuthorizationAwaiting))
async def auth_email_revert(_: Message, state: FSMContext):
    pass


@router.message(StateFilter(DialogStates.AuthorizationConfirmationAwaiting))
async def auth_code_process(_: Message, state: FSMContext):
    pass


@router.message(StateFilter(DialogStates.Authorized), Command("report"))
async def failure_report_init(_: Message, state: FSMContext):
    pass


@router.message(StateFilter(DialogStates.Authorized), Command("logout"))
async def user_logout_init(_: Message, state: FSMContext):
    pass
