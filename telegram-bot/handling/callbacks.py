from aiogram.filters.callback_data import CallbackData
from aiogram.utils.keyboard import InlineKeyboardBuilder


class EmailCallback(CallbackData, prefix="email"):
    pass


def get_email_kb():
    builder = InlineKeyboardBuilder()
    builder.button(text="📨 Change email", callback_data=EmailCallback())
    return builder.as_markup()


class DetailedReportCallback(CallbackData, prefix="report_details"):
    pass


def get_detailed_report_kb():
    builder = InlineKeyboardBuilder()
    builder.button(text="🛎 Report as is", callback_data=DetailedReportCallback())
    return builder.as_markup()
