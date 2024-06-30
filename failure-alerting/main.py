import os
import time
import logging

import schedule
from aiogram import Bot
from dotenv import load_dotenv
from motor.motor_asyncio import AsyncIOMotorClient

from algorithms.simple_threshold import SimpleThreshold
from detector import Detector
import pgclient
from formatters.simple_formatter import SimpleFormatter
from alerting_service import AlertingService


def init_pgclient() -> pgclient.PostgresClient:
    return pgclient.PostgresClient(
        host=os.getenv("PG_HOST"),
        port=os.getenv("PG_PORT"),
        user=os.getenv("PG_USER"),
        password=os.getenv("PG_PASS"),
        database=os.getenv("PG_DB")
    )


if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)

    load_dotenv()

    postgres_client = init_pgclient()
    logging.info('Connection with PostgreSQL database established')

    mongo_client = AsyncIOMotorClient(os.getenv("BOT_STORAGE_MONGO_URI"))
    logging.info("Connection with MongoDB established")

    bot = Bot(os.getenv("BOT_TOKEN"))
    logging.info("Connection with Telegram bot established")

    interval = os.getenv('PULL_INTERVAL')
    shift = os.getenv('TIMEZONE_SHIFT')

    report_api = AlertingService(
        bot,
        mongo_client
    )
    algo = SimpleThreshold(
        shift,
        interval,
        int(os.getenv("THRESHOLD"))
    )
    formatter = SimpleFormatter(
        '../common/generated/failures-schemas.json'
    )
    detector = Detector(postgres_client, algo, formatter, report_api)

    # TODO: set configurable interval
    schedule.every(15).seconds.do(detector.detect)

    while True:
        schedule.run_pending()
        time.sleep(1)
