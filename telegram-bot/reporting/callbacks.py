import json
from abc import ABC
from typing import List

from aiogram.filters.callback_data import CallbackData
from aiogram.types import InlineKeyboardMarkup
from aiogram.utils.keyboard import InlineKeyboardBuilder


class ReportingKbCallback(CallbackData, prefix="report"):
    window_id: int


class ReportCallback(ABC):
    pass


class CategoryCallback(ReportCallback):
    def __init__(self, placements: List[str], category: str, parent_id: int):
        self.__placements = placements
        self.__category = category
        self.__parent_id = parent_id

    def placements(self) -> List[str]:
        return self.__placements

    def category(self) -> str:
        return self.__category

    def parent_id(self) -> int:
        return self.__parent_id


class TransitionCallback(ReportCallback):
    def __init__(self, keyboard: InlineKeyboardMarkup):
        self.__keyboard = keyboard

    def keyboard(self) -> InlineKeyboardMarkup:
        return self.__keyboard


class CancelCallback(ReportCallback):
    def __init__(self):
        pass


class ReportCallbackProvider:
    def __init__(self, path_to_schemas: str):
        with open(path_to_schemas, 'r') as f:
            schemas = json.load(f)

        window_id = 1
        self.__callbacks = {
            0: CancelCallback()
        }

        def dfs(node: dict, parent_id: int, categories: List[str]):
            nonlocal window_id

            node_id = window_id
            window_id += 1

            builder = InlineKeyboardBuilder()

            if node["type"] == "failure":
                self.__callbacks[node_id] = CategoryCallback(
                    categories,
                    node['id'],
                    parent_id
                )
                return node_id

            for child in node["items"]:
                child_id = dfs(child, node_id, categories + [node['id']])
                if child["type"] == "failure":
                    icon = '🔔'
                else:
                    icon = '📁'
                builder.button(
                    text=f'{icon} {child["name"]["en"]}',
                    callback_data=ReportingKbCallback(window_id=child_id)
                )

            if parent_id == 0:
                return_button = '❌ Cancel'
            else:
                return_button = '👈 Back'
            builder.button(
                text=return_button,
                callback_data=ReportingKbCallback(window_id=parent_id)
            )

            builder.adjust(3)
            self.__callbacks[node_id] = TransitionCallback(builder.as_markup())

            return node_id

        dfs(schemas, 0, list())

    def get_callback(self, callback_id):
        return self.__callbacks[callback_id]
