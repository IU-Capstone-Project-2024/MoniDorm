from abc import ABC, abstractmethod
from typing import Iterable, List

from algorithms.types import Failure


class Alert:
    def __init__(self, failure_id: str, message: str):
        self.__failure_id = failure_id
        self.__message = message

    def failure_id(self) -> str:
        return self.__failure_id

    def message(self) -> str:
        return self.__message


class AlertFormatter(ABC):
    @abstractmethod
    def __call__(self, failures: Iterable[Failure]) -> List[Alert]:
        pass
