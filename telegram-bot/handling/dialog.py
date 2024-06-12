from aiogram.fsm.state import StatesGroup, State


class DialogStates(StatesGroup):
    # any message from unknown state
    AuthorizationAwaiting = State()
    # successfully sent email
    AuthorizationConfirmationAwaiting = State()
    # successfully input confirmation code
    Authorized = State()
    # Reporting state
    Reporting = State()
