from typing import List

from aiogram import Bot
from motor.motor_asyncio import AsyncIOMotorClient

from formatters.types import Alert

INF = 1000000


class AlertingService:
    def __init__(self, bot: Bot, mongo_client: AsyncIOMotorClient):
        self.__bot = bot
        self.__bot_collection = mongo_client['aiogram_fsm'].get_collection("states_and_data")

    async def report(self, reports: List[Alert]):
        for report in reports:
            subscribers = await self.__find_subscribed_users(report)
            for subscriber_chat in subscribers:
                await self.__bot.send_message(chat_id=subscriber_chat, text=report.message())

    async def __find_subscribed_users(self, report: Alert) -> List[int]:
        ids_with_alerts = await self.__bot_collection.find(
            {},
            {"_id": 1, "data.alerts": 1}
        ).to_list(length=INF)

        subscribers = list()
        for id_with_alerts in ids_with_alerts:
            if any(report.failure_id().startswith(x) for x in id_with_alerts['data']['alerts']):
                chat_id = int(id_with_alerts['_id'].split(':')[-1])
                subscribers.append(chat_id)
        return subscribers
