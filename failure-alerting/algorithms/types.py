from abc import ABC, abstractmethod
from datetime import datetime
from typing import Iterable

from pgclient import PostgresClient


class Failure:
    def __init__(
            self,
            failure: str,
            location: str,
            reports_count: int,
            description: str,
            failure_datetime: datetime
    ):
        self.__failure = failure
        self.__location = location
        self.__reports_count = reports_count
        self.__description = description
        self.__failure_datetime = failure_datetime

    def failure(self):
        return self.__failure

    def location(self):
        return self.__location

    def reports_count(self):
        return self.__reports_count

    def description(self):
        return self.__description

    def datetime(self):
        return self.__failure_datetime


class FailureDetectionAlgorithm(ABC):
    @abstractmethod
    def __call__(self, client: PostgresClient) -> Iterable[Failure]:
        pass
