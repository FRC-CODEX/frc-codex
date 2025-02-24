import os
from abc import ABC, abstractmethod
from typing import Any

import psycopg2


class BaseAction(ABC):

    @abstractmethod
    def _run(self, options, connection):
        pass

    def run(self, options) -> tuple[bool, str, list | int | dict]:
        connection = psycopg2.connect(
            dbname=os.getenv('DB_DATABASE'),
            user=os.getenv('DB_USERNAME'),
            password=os.getenv('DB_PASSWORD'),
            host=os.getenv('DB_HOST'),
            port=os.getenv('DB_PORT')
        )
        try:
            cursor = connection.cursor()
            try:
                return self._run(options, cursor)
            finally:
                connection.commit()
                cursor.close()
        finally:
            connection.close()

    @staticmethod
    def collect_results(cursor) -> list[dict[str, Any]]:
        rows = cursor.fetchall()
        column_names = [desc[0] for desc in cursor.description]
        results = []
        for row in rows:
            row_str = [str(r) for r in row]
            results.append(dict(zip(column_names, row_str)))
        return results

    @staticmethod
    def collect_stats(cursor) -> dict[str, Any]:
        column_names = [desc[0] for desc in cursor.description]
        first_row = cursor.fetchone()
        stats = dict(zip(column_names, first_row))
        return {
            k: str(v) for k, v in stats.items()
        }
