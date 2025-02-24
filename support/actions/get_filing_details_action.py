from typing import Any

from support.actions.base_action import BaseAction


class GetFilingDetailsAction(BaseAction):

    def _run(self, options, cursor) -> tuple[bool, str, Any]:
        if 'filing_id' not in options:
            return False, "Must provide 'filing_id'.", 0
        filing_id = options['filing_id']
        query = "SELECT * FROM filings WHERE filing_id = %s LIMIT 1;"
        cursor.execute(query, (filing_id,))
        results = BaseAction.collect_results(cursor)
        if not results:
            return False, f"Could not find filing with ID '{filing_id}'.", 0
        message = f"Found filing with ID '{filing_id}'."
        return True, message, results
