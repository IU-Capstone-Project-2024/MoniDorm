import os
import time
import logging

import schedule
from dotenv import load_dotenv
from motor.motor_asyncio import AsyncIOMotorClient

from algorithms.simple_threshold import SimpleThreshold
from detector import Detector
import pgclient
from report_api import ReportAPI


def init_client() -> pgclient.PostgresClient:
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

    postgres_client = init_client()
    mongo_client = AsyncIOMotorClient(os.getenv("BOT_STORAGE_MONGO_URI"))
    logging.info("Connection with MongoDB established")

    interval = os.getenv('PULL_INTERVAL')
    shift = os.getenv('TIMEZONE_SHIFT')

    report_api = ReportAPI(
        os.getenv("BOT_TOKEN"),
        mongo_client
    )
    algo = SimpleThreshold(
        shift,
        interval,
        '../common/generated/failures-schemas.json',
        int(os.getenv("THRESHOLD"))
    )
    detector = Detector(postgres_client, algo, report_api)

    # TODO: set configurable interval
    schedule.every(15).seconds.do(detector.detect)

    while True:
        schedule.run_pending()
        time.sleep(1)
