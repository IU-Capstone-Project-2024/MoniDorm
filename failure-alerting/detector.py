import asyncio

import algorithms.types
import formatters.types
from alerting_service import AlertingService
from pgclient import PostgresClient


class Detector:
    def __init__(
            self,
            pgclient: PostgresClient,
            detection_algorithm: algorithms.types.FailureDetectionAlgorithm,
            report_formatter: formatters.types.AlertFormatter,
            report_api: AlertingService
    ):
        self.__pgclient = pgclient
        self.__algorithm = detection_algorithm
        self.__report_api = report_api
        self.__formatter = report_formatter

    def detect(self):
        failures = self.__algorithm(self.__pgclient)
        alerts = self.__formatter(failures)
        unwrapped_alerts = list(
            map(
                lambda f: (f.failure().failure(),
                           f.failure().location(),
                           f.failure().datetime(),
                           f.failure().reports_count(),
                           f.failure().description(),
                           f.message()),
                alerts
            )
        )
        self.__pgclient.insert_recent_alerted_failures(unwrapped_alerts)
        loop = asyncio.get_event_loop()
        if loop.is_closed():
            loop = asyncio.new_event_loop()
            asyncio.set_event_loop(loop)
        loop.run_until_complete(self.__report_api.report(alerts))
