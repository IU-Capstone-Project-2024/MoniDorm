from aiogram.fsm.state import StatesGroup, State


class DialogStates(StatesGroup):
    """
    Representation of a set of dialog states
    """
    # any message from unknown state
    AuthorizationAwaiting = State()
    # successfully sent email
    AuthorizationConfirmationAwaiting = State()
    # successfully input confirmation code
    Authorized = State()
    # Reporting state
    Reporting = State()
    # Awaiting report comment state
    ReportCommentAwaiting = State()
