from typing import Optional, Dict

from aiogram.types import InlineKeyboardMarkup, InlineKeyboardButton
from aiogram.filters.callback_data import CallbackData
from aiogram.utils.keyboard import InlineKeyboardBuilder


class EmailCallbackFactory(CallbackData, prefix="email"):
    pass


def get_email_kb():
    builder = InlineKeyboardBuilder()
    builder.button(text="📨 Change email", callback_data=EmailCallbackFactory())
    return builder.as_markup()