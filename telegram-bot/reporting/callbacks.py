import json
from abc import ABC
from copy import deepcopy
from typing import List

from aiogram.filters.callback_data import CallbackData
from aiogram.utils.keyboard import InlineKeyboardBuilder


class ReportingKbCallback(CallbackData, prefix="report"):
    window_id: int


class AlertsStatusCallback(CallbackData, prefix="subscription"):
    window_id: int
    path: str
    enable: bool


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

    def get_path(self) -> str:
        return f"{'.'.join(self.__placements)}.{self.__category}"


class TransitionCallback(ReportCallback):
    def __init__(
            self,
            window_id: int,
            placements: List[str],
            kb_builder: InlineKeyboardBuilder
    ):
        self.__window_id = window_id
        self.__kb_builder = kb_builder
        self.__placements = placements

    def __report_kb_builder(self) -> InlineKeyboardBuilder:
        return self.__kb_builder

    def keyboard(self, alerts_enabled: bool):
        builder = deepcopy(self.__report_kb_builder())
        if alerts_enabled:
            builder.button(text='🔔️ Alerts enabled',
                           callback_data=AlertsStatusCallback(
                               window_id=self.__window_id,
                               path=self.get_path(),
                               enable=False
                           ))
        else:
            builder.button(text='🔕 No alerts',
                           callback_data=AlertsStatusCallback(
                               window_id=self.__window_id,
                               path=self.get_path(),
                               enable=True
                           ))
        builder.adjust(3, repeat=True)
        return builder.as_markup()

    def get_path(self) -> str:
        return '.'.join(self.__placements)


class CancelCallback(ReportCallback):
    def __init__(self):
        pass


class AlertUnsubscribeCallback(CallbackData, prefix="alert_unsub"):
    back: bool
    alert: str


class ReportCallbackProvider:
    def __init__(self, path_to_schemas: str):
        with open(path_to_schemas, 'r') as f:
            self.__schemas = json.load(f)

        window_id = 1
        self.__callbacks = {
            0: CancelCallback()
        }

        def __dfs(node: dict, parent_id: int, categories: List[str]):
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
                child_id = __dfs(child, node_id, categories + [node['id']])
                if child["type"] == "failure":
                    icon = '⚠️'
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

            self.__callbacks[node_id] = TransitionCallback(
                node_id,
                categories + [node["id"]],
                builder
            )

            return node_id

        __dfs(self.__schemas, 0, list())

    def get_report_callback(self, callback_id):
        return self.__callbacks[callback_id]

    def get_human_readable_path_en(self, path: str) -> List[str]:
        next_id = path.split('.')[1:]
        human_readable = [self.__schemas["name"]["en"]]

        node = self.__schemas
        for item_id in next_id:
            for item in node["items"]:
                if item["id"] == item_id:
                    node = item
                    human_readable.append(node["name"]["en"])
                    break
        return human_readable

    def get_alert_callback(self, enabled_alerts: List[str]) -> InlineKeyboardBuilder:
        builder = InlineKeyboardBuilder()
        for alert in enabled_alerts:
            builder.button(
                text=f"❌ {', '.join(self.get_human_readable_path_en(alert))}",
                callback_data=AlertUnsubscribeCallback(
                    back=False,
                    alert=alert
                )
            )
        builder.button(
            text="👈 Back",
            callback_data=AlertUnsubscribeCallback(
                back=True,
                alert=""
            )
        )
        builder.adjust(1)
        return builder
