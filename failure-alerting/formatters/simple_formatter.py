import json
from typing import Iterable, List

from algorithms.types import Failure
from formatters.types import AlertFormatter, Alert


class SimpleFormatter(AlertFormatter):
    def __init__(
            self,
            path_to_schemas: str
    ):
        with open(path_to_schemas, 'r') as file:
            self.__schemas = json.load(file)

    def __call__(self, failures: Iterable[Failure]) -> List[Alert]:
        alerts = list()
        for failure in failures:
            alerts.append(self.__format_single_alert(failure))
        return alerts

    def __format_single_alert(self, failure: Failure):
        next_locations = failure.location().split('.')[1:]
        # human readable failure location description
        location_human = [self.__schemas["name"]["en"]]

        node = self.__schemas
        for location in next_locations:
            for item in node["items"]:
                if item["id"] == location:
                    node = item
                    location_human.append(node["name"]["en"])
                    break

        # human readable failure description
        failure_human = None
        for item in node["items"]:
            if item["id"] == failure.failure():
                failure_human = item["name"]["en"]
                break

        message = f'🔔 Reports are coming in about possible problems with {failure_human}' \
                  f' at {', '.join(location_human)}. There are {failure.reports_count()} of them at this moment.'
        return Alert(
            f'{failure.location()}.{failure.failure()}',
            message
        )
