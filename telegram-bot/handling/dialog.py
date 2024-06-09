from aiogram.fsm.state import StatesGroup, State


class DialogStates(StatesGroup):
    # intentional log out
    Unauthorized = State()
    # any message from unknown or unauthorized state
    AuthorizationAwaiting = State()
    # successfully sent email
    AuthorizationConfirmationAwaiting = State()
    # successfully input confirmation code
    Authorized = State()
