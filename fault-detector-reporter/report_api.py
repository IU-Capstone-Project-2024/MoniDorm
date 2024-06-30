from typing import List

from aiogram import Bot
from motor.motor_asyncio import AsyncIOMotorClient


INF = 1000000

class Report:
    def __init__(self, report_identifier: str, message: str):
        self.__report_identifier = report_identifier
        self.__message = message

    def report_identifier(self) -> str:
        return self.__report_identifier

    def message(self) -> str:
        return self.__message


class ReportAPI:
    def __init__(self, bot_token: str, mongo_client: AsyncIOMotorClient):
        self.__bot = Bot(token=bot_token)
        self.__mongo_client = mongo_client

    async def report(self, reports: List[Report]):
        collection = self.__mongo_client['aiogram_fsm'].get_collection("states_and_data")
        ids_with_alerts = await collection.find({}, {"_id": 1, "data.alerts": 1}).to_list(length=INF)
        for report in reports:
            for id_with_alerts in ids_with_alerts:
                if any(report.report_identifier().startswith(x) for x in id_with_alerts['data']['alerts']):
                    chat_id = int(id_with_alerts['_id'].split(':')[-1])
                    await self.__bot.send_message(chat_id=chat_id, text=report.message())