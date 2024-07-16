import psycopg2
from typing import List


class PostgresClient:
    def __init__(self, host: str, port: str, database: str, user: str, password: str):
        self.__conn = psycopg2.connect(
            host=host,
            port=port,
            database=database,
            user=user,
            password=password
        )
        self.__cur = self.__conn.cursor()

    def get_grouped_recent_reports(self, tz_shift: str, time_interval: str):
        self.__cur.execute(f"""
            select
                category,
                placement,
                min(failure_date),
                count(*) as reports_count,
                string_agg(description, ',') as full_desctiption
            from
                report
            where
                failure_date - interval '{tz_shift}' >= now() - interval '{time_interval}'
            group by
            category, placement;
        """)

        rows = self.__cur.fetchall()
        self.__conn.rollback()
        return rows

    def insert_recent_alerted_failures(self, unwrapped_failures: List):
        for f in unwrapped_failures:
            self.__cur.execute('''
                insert into failure
                (category, placement, failure_date, report_count, aggregated_report_messages, summarization)
                values (%s, %s, %s, %s, %s, %s)
            ''', f
        )
        self.__cur.connection.commit()

    def __del__(self):
        self.__cur.close()
        self.__conn.close()
