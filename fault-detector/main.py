import os

from dotenv import load_dotenv

import pgclient


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
    for category, location, reports_count, description in \
            client.get_grouped_recent_reports(interval, shift):
        print(category, location, reports_count, description)
