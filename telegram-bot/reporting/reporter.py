"""
Description of classes for reporting issues to backend
"""

import datetime
import logging
from typing import Optional, List

import requests

# class representing a failure report
class Report:
    def __init__(
            self,
            placement: List[str],
            category: str,
            owner_email: str,
            description: Optional[str],
            date_time: datetime.datetime
    ):
        self.__placement = placement
        self.__category = category
        self.__owner_email = owner_email
        self.__description = description
        self.__date_time = date_time

    def to_post(self):
        """
        Converts the report to a dict used for sending to backend
        :return: dict to be sent
        """
        return {
            'category': self.__category,
            'placement': ','.join(self.__placement),
            'date_time': self.__date_time,
            'owner_email': self.__owner_email,
            'description': self.__description
        }


# Builder for a report class
class ReportBuilder:
    def __init__(self):
        self.__placement = None
        self.__category = None
        self.__owner_email = None
        self.__description = ''
        self.__date_time = None

    def add_placement(self, placements: List[str]):
        self.__placement = placements
        return self

    def add_category(self, category: str):
        self.__category = category
        return self

    def add_email(self, owner_email: str):
        self.__owner_email = owner_email
        return self

    def add_description(self, description: str):
        self.__description = description
        return self

    def stamp_datetime(self):
        self.__date_time = datetime.datetime.now(datetime.UTC).isoformat()
        return self

    def build(self):
        return Report(
            self.__placement,
            self.__category,
            self.__owner_email,
            self.__description,
            self.__date_time
        )


# class to send reports to API
class Reporter:
    def __init__(self, url: str, token: str):
        self.__component = 'reporter'
        self.__url = url
        self.__headers = {
            'Token': token,
            'Content-Type': 'application/json'
        }

    def report(self, report: Report):
        response = requests.post(f'{self.__url}/api/report',
                                 headers=self.__headers,
                                 json=report.to_post())
        logging.info(f'{self.__component}: report is sent — {response}')
