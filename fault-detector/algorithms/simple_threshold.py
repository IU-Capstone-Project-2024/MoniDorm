import json

from pgclient import PostgresClient
from algorithms.template import FailureDetectionAlgorithm


class SimpleThreshold(FailureDetectionAlgorithm):
    def __init__(
            self,
            tz_shift: str,
            time_interval: str,
            path_to_schemas: str,
            threshold: int
    ):
        self.__tz_shift = tz_shift
        self.__time_interval = time_interval
        with open(path_to_schemas, 'r') as file:
            self.__schemas = json.load(file)
        self.__threshold = threshold

    def __call__(self, client: PostgresClient):
        reports_received = client.get_grouped_recent_reports(
            self.__tz_shift, self.__time_interval
        )
        fault_descriptions = list()
        for fault, location, reports_cnt, commentary in reports_received:
            if reports_cnt < self.__threshold:
                continue
            simple_description = self.__human_readable_fault_description(fault, location)
            fault_descriptions.append(simple_description)

        return fault_descriptions

    def __human_readable_fault_description(self, fault: str, location: str):
        locations = location.split(',')[1:]

        location_desc = [self.__schemas["name"]["en"]]

        current_node = self.__schemas
        for location in locations:
            for item in current_node["items"]:
                if item["id"] == location:
                    current_node = item
                    location_desc.append(current_node["name"]["en"])
                    break

        failure_desc = None
        for item in current_node["items"]:
            if item["id"] == fault:
                failure_desc = item["name"]["en"]

        return (f'🔔 Reports are coming in about possible problems with **{failure_desc}**'
                f' at **{', '.join(location_desc)}**. Be informed about it!')
