from pgclient import PostgresClient
from algorithms.template import FailureDetectionAlgorithm


class SimpleThreshold(FailureDetectionAlgorithm):
    def __init__(self, tz_shift: str, time_interval: str):
        self.__tz_shift = tz_shift
        self.__time_interval = time_interval

    def __call__(self, client: PostgresClient):
        reports = client.get_grouped_recent_reports(
            self.__tz_shift, self.__time_interval
        )

        return reports
