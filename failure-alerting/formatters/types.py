import datetime
from abc import ABC, abstractmethod
from typing import Iterable, List

from algorithms.types import Failure


class Alert:
    def __init__(
            self,
            failure: Failure,
            message: str
    ):
        self.__failure = failure
        self.__message = message

    def failure_id(self) -> str:
        return f'{self.__failure.location()}.{self.__failure.failure()}'

    def message(self) -> str:
        return self.__message

    def failure(self) -> Failure:
        return self.__failure


class AlertFormatter(ABC):
    @abstractmethod
    def __call__(self, failures: Iterable[Failure]) -> List[Alert]:
        pass
