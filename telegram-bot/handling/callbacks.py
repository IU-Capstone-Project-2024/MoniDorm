from enum import Enum

from aiogram.filters.callback_data import CallbackData
from aiogram.utils.keyboard import InlineKeyboardBuilder, ReplyKeyboardBuilder
from aiogram.types import ReplyKeyboardMarkup


class EmailCallback(CallbackData, prefix="email"):
    pass


def get_email_kb():
    builder = InlineKeyboardBuilder()
    builder.button(text="📨 Change email", callback_data=EmailCallback())
    return builder.as_markup()


class Action(str, Enum):
    report = "report"
    abort = "abort"


class FinalizeReport(CallbackData, prefix="report_details"):
    action: Action
    back_window: int


def get_detailed_report_kb(parent_window: int):
    builder = InlineKeyboardBuilder()
    builder.button(text="🛎 Report as is", callback_data=FinalizeReport(action=Action.report, back_window=parent_window))
    builder.button(text="👈 Back", callback_data=FinalizeReport(action=Action.abort, back_window=parent_window))
    builder.adjust(1)
    return builder.as_markup()


def get_main_menu_kb() -> ReplyKeyboardMarkup:
    builder = ReplyKeyboardBuilder()
    builder.button(text="⚠️ Report")
    builder.button(text="📔 My alerts")
    builder.button(text="👋 Logout")
    builder.adjust(1)
    return builder.as_markup()
