import logging
import os

from processor.main.main_download_manager import MainDownloadManager
from processor.main.main_upload_manager import MainUploadManager
from processor.main.main_worker_factory import MainWorkerFactory
from processor.processor import Processor
from processor.processor_options import ProcessorOptions

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO, format='{%(processName)s} [%(levelname)s] %(message)s')


def lambda_handler(event, context):
    secrets_filepath = os.getenv('SECRETS_FILEPATH')
    processor_options = ProcessorOptions(secrets_filepath)
    processor = Processor(
        download_manager=MainDownloadManager(processor_options),
        upload_manager=MainUploadManager(processor_options),
        worker_factory=MainWorkerFactory(processor_options),
    )
    worker_result = processor.run_from_lambda(event, context)
    result = {
        'CompanyName': worker_result.company_name,
        'CompanyNumber': worker_result.company_number,
        'Error': worker_result.error,
        'FilingId': worker_result.filing_id,
        'Logs': worker_result.logs,
        'Success': worker_result.success,
        'ViewerEntrypoint': worker_result.viewer_entrypoint,
    }
    if worker_result.document_date is not None:
        result['DocumentDate'] = worker_result.document_date.strftime('%Y-%m-%d')
    return result