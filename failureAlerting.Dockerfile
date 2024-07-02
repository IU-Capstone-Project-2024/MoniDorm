###
### BUILD
###

FROM python:3.12-slim as builder

ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

RUN apt-get update && \
    apt-get install -y --no-install-recommends gcc libpq-dev

RUN pip install virtualenv && \
    pip install psycopg2-binary

RUN virtualenv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .
RUN pip install -r requirements.txt

###
### DEPLOY
###

FROM python:3.12-slim

COPY --from=builder /opt/venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY . ./app

EXPOSE 8443

WORKDIR /app/failure-alerting

RUN rm ./.env && \
    mv ./.env.prod ./.env

CMD ["python", "main.py"]
