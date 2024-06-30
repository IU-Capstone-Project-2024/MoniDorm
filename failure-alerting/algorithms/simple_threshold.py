from typing import List

from pgclient import PostgresClient
from algorithms.types import FailureDetectionAlgorithm, Failure


class SimpleThreshold(FailureDetectionAlgorithm):
    def __init__(
            self,
            tz_shift: str,
            time_interval: str,
            threshold: int
    ):
        self.__tz_shift = tz_shift
        self.__time_interval = time_interval
        self.__threshold = threshold

    def __call__(self, client: PostgresClient) -> List[Failure]:
        reports_received = client.get_grouped_recent_reports(
            self.__tz_shift, self.__time_interval
        )
        failures = list()
        for failure, location, reports_cnt, commentary in reports_received:
            if reports_cnt < self.__threshold:
                continue
            failures.append(
                Failure(
                    failure,
                    location,
                    reports_cnt,
                    commentary
                )
            )
        return failures
