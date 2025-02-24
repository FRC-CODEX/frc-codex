from support.actions.base_action import BaseAction


class GetChStreamingStatsAction(BaseAction):

    def _run(self, options, cursor) -> tuple[bool, str, list | int | dict]:
        query = "SELECT " \
                "COUNT(*) AS unprocessed_count, " \
                "MIN(created_date) AS earliest_created_date, " \
                "MIN(timepoint) AS earliest_timepoint, " \
                "MAX(created_date) AS latest_created_date, " \
                "MAX(timepoint) AS latest_timepoint " \
                "FROM stream_events"
        cursor.execute(query)
        stats = BaseAction.collect_stats(cursor)
        message = "Companies House streaming statistics retrieved."
        return True, message, stats
