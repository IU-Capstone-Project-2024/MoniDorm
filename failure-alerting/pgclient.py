import psycopg2


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
                count(*) as reports_count,
                string_agg(description, ',') as full_desctiption
            from
                report
            where
                failure_date - interval '{tz_shift}' >= now() - interval '{time_interval}'
            group by
            category, placement;
        """)
        return self.__cur.fetchall()

    def __del__(self):
        self.__cur.close()
        self.__conn.close()
