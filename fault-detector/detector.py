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
        self.__report_api.report(reports)
