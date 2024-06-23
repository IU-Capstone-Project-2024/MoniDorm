###
### BUILD
###

FROM python:3.10-slim as builder

ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

RUN apt-get update && \
    apt-get install -y --no-install-recommends gcc

RUN pip install virtualenv

RUN virtualenv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .
RUN pip install -r requirements.txt

###
### DEPLOY
###

FROM python:3.10-slim

COPY --from=builder /opt/venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY . ./app

EXPOSE 8443

WORKDIR /app/telegram-bot

RUN rm ./.env && \
    mv ./.env.prod ./.env

CMD ["python", "main.py"]
