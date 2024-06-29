import asyncio


import algorithms.template
from report_api import ReportAPI
from pgclient import PostgresClient


class Detector:
    def __init__(
            self,
            pgclient: PostgresClient,
            algorithm: algorithms.template.FailureDetectionAlgorithm,
            report_api: ReportAPI
    ):
        self.__pgclient = pgclient
        self.__algorithm = algorithm
        self.__report_api = report_api

    def detect(self):
        reports = self.__algorithm(self.__pgclient)
        loop = asyncio.get_event_loop()
        if loop.is_closed():
            loop = asyncio.new_event_loop()
            asyncio.set_event_loop(loop)
        loop.run_until_complete(self.__report_api.report(reports))
