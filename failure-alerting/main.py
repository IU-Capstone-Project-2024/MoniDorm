import logging
import os
import time

import schedule
from aiogram import Bot
from dotenv import load_dotenv
from motor.motor_asyncio import AsyncIOMotorClient

import pgclient
from alerting_service import AlertingService
from algorithms.threshold_alert_retention import ThresholdWithRetention
from detector import Detector
from formatters.summarized_formatter import SummarizedFormatter


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
    algo = ThresholdWithRetention(
        shift,
        interval,
        int(os.getenv("THRESHOLD")),
        int(os.getenv("FAILURE_RETENTION_MINUTES"))
    )
    formatter = SummarizedFormatter(
        '../common/generated/failures-schemas.json',
        os.getenv("MISTRAL_TOKEN")
    )
    detector = Detector(postgres_client, algo, formatter, report_api)

    schedule.every(int(os.getenv("CHECKOUT_INTERVAL_MINUTES"))).minutes.do(detector.detect)

    while True:
        schedule.run_pending()
        time.sleep(1)
