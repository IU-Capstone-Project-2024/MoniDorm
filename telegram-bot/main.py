import asyncio
from aiogram import Bot, Dispatcher
from handling.handlers import router

from dotenv import load_dotenv
from os import getenv


async def main():
    bot = Bot(token=getenv("BOT_TOKEN"))
    dp = Dispatcher()

    dp.include_routers(router)
    await dp.start_polling(bot)


if __name__ == "__main__":
    load_dotenv()
    asyncio.run(main())