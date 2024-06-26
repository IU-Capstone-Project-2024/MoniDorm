import os
import time

import schedule
from dotenv import load_dotenv

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
    load_dotenv()
    client = init_client()
    interval = os.getenv('PULL_INTERVAL')
    shift = os.getenv('TIMEZONE_SHIFT')

    report_api = ReportAPI()
    algo = SimpleThreshold('3 hours', '1 minutes')
    detector = Detector(client, algo, report_api)

    schedule.every(1).minutes.do(detector.detect)

    while True:
        schedule.run_pending()
        time.sleep(1)
