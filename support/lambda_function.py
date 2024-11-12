import json
import logging

from support.actions.list_errors_action import ListErrorsAction
from support.actions.reset_filings_action import ResetFilingsAction

logger = logging.getLogger(__name__)

ACTIONS_MAP = {
    'list_errors': ListErrorsAction,
    'reset_filings': ResetFilingsAction,
}


def _error(message):
    return {
        'success': False,
        'message': message,
    }


def lambda_handler(event, _):
    body = event
    if 'body' in body:
        body = json.loads(body['body'])
    if 'action' not in body:
        return _error("No action specified in request.")
    action_name = body['action']
    if action_name not in ACTIONS_MAP:
        return _error(f"Unknown action: '{action_name}'. Available actions: {sorted(ACTIONS_MAP.keys())}")
    action = ACTIONS_MAP[action_name]()

    logger.info("Performing support action '%s'.", action_name)
    success, message, results = action.run(body)
    if not success:
        logger.error("Failed to complete support action '%s': %s", action_name, message)
        return _error(message)
    logger.info("Successfully completed support action '%s': %s", action_name, message)
    return {
        'success': True,
        'message': message,
        'results': results
    }
