from abc import ABC, abstractmethod
from pgclient import PostgresClient


class FailureDetectionAlgorithm(ABC):
    @abstractmethod
    def __call__(self, client: PostgresClient):
        pass
