import json

from aiogram.filters.callback_data import CallbackData
from aiogram.utils.keyboard import InlineKeyboardBuilder
from typing import List


class ReportCallback(CallbackData, prefix="report"):
    window_id: int


class ReportCallbackProvider:
    def __init__(self, path_to_schemas: str):
        with open(path_to_schemas, 'r') as f:
            schemas = json.load(f)

        window_id = 1
        self.__actions = {
            0: {
                "action": "cancel"
            }
        }

        def dfs(node: dict, parent_id: int, categories: List[str]):
            nonlocal window_id

            node_id = window_id
            window_id += 1

            builder = InlineKeyboardBuilder()

            if node["type"] == "failure":
                self.__actions[node_id] = {
                    "action": "report",
                    "meta": {
                        "categories": categories,
                        "failure_id": node["id"]
                    }
                }
                return node_id

            for child in node["items"]:
                child_id = dfs(child, node_id, categories + [node['id']])
                if child["type"] == "failure":
                    icon = '🔔'
                else:
                    icon = '📁'
                builder.button(
                    text=f'{icon} {child['name']['en']}',
                    callback_data=ReportCallback(window_id=child_id)
                )

            if parent_id == 0:
                return_button = '❌ Cancel'
            else:
                return_button = '👈 Back'
            builder.button(
                text=return_button,
                callback_data=ReportCallback(window_id=parent_id)
            )

            builder.adjust(3)
            self.__actions[node_id] = {
                "action": "transfer",
                "keyboard": builder.as_markup()
            }

            return node_id

        dfs(schemas, 0, list())

    def get_callback(self, callback_id):
        return self.__actions[callback_id]


