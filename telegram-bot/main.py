import asyncio
from aiogram import Bot, Dispatcher
from handling.handlers import router

from dotenv import load_dotenv
from os import getenv

from reporting.callbacks import ReportCallbackProvider
from mail.client import Client


async def main():
    bot = Bot(token=getenv("BOT_TOKEN"))
    dp = Dispatcher()

    dp.include_routers(router)

    report_callback_provider = ReportCallbackProvider(
        path_to_schemas='../common/generated/failures-schemas.json'
    )
    mail_client = Client(
        user=getenv("EMAIL_ADDRESS"),
        password=getenv("EMAIL_PASSWORD"),
        hostname=getenv("EMAIL_SMTP_HOST"),
        port=465,
        path_to_templates="../common",
        message_template="confirmation-email-template.html.j2"
    )

    await dp.start_polling(
        bot,
        report_provider=report_callback_provider,
        mail_client=mail_client
    )


if __name__ == "__main__":
    load_dotenv()
    asyncio.run(main())
