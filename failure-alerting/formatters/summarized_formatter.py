from typing import Iterable, List

from algorithms.types import Failure
from formatters.simple_formatter import SimpleFormatter
from formatters.types import Alert

from mistralai.client import MistralClient
from mistralai.models.chat_completion import ChatMessage


class SummarizedFormatter(SimpleFormatter):
    def __init__(self, path_to_schemas: str, mistral_token: str):
        super().__init__(path_to_schemas)

        self.__mistral_client = MistralClient(api_key=mistral_token)

    def __call__(self, failures: Iterable[Failure]) -> List[Alert]:
        alerts = list()
        for failure in failures:
            alerts.append(self.__format_single_alert(failure))
        return alerts

    def __format_single_alert(self, failure: Failure):
        location_human, failure_human = super()._get_human_readable_failure_and_location(failure)
        message = f'🔔 Reports are coming in about possible problems with {failure_human}' \
                  f' at {', '.join(location_human)}. There are {failure.reports_count()} of them at this moment.'

        if len(failure.description()) != 0:
            response = self.__mistral_client.chat(
                model="mistral-large-latest",
                messages=[
                    ChatMessage(
                        role="user",
                        content=f'Summarize following text.'
                                f'Do not provide any additional info except summarization '
                                f'of following sentences. {failure.description()}'
                    )
                ]
            )
            message += '\n\n' + response.choices[0].message.content

        return Alert(
            f'{failure.location()}.{failure.failure()}',
            message
        )
