FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8443

ENV ENV=production

CMD ["python", "telegram-bot/main.py"]
