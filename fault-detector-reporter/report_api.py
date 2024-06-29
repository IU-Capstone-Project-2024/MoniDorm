from typing import List

from aiogram import Bot


class Report:
    def __init__(self, report_identifier: str, message: str):
        self.__report_identifier = report_identifier
        self.__message = message

    def report_identifier(self) -> str:
        return self.__report_identifier

    def message(self) -> str:
        return self.__message


class ReportAPI:
    def __init__(self, bot_token: str):
        self.__bot = Bot(token=bot_token)

    async def report(self, reports: List[Report]):
        for report in reports:
            # TODO: send to all who subscribed
            await self.__bot.send_message(chat_id=0, text=report.message())