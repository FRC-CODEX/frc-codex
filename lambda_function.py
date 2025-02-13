import logging

from processor.main.main_download_manager import MainDownloadManager
from processor.main.main_upload_manager import MainUploadManager
from processor.main.main_worker_factory import MainWorkerFactory
from processor.processor import Processor
from processor.processor_options import ProcessorOptions

logger = logging.getLogger(__name__)


def lambda_handler(event, context):
    processor_options = ProcessorOptions()
    processor = Processor(
        download_manager=MainDownloadManager(processor_options),
        upload_manager=MainUploadManager(processor_options),
        worker_factory=MainWorkerFactory(processor_options),
    )
    worker_result = processor.run_from_lambda(event, context)
    result = {
        'ArelleVersion': processor_options.arelle_version,
        'ArelleViewerVersion': processor_options.ixbrl_viewer_version,
        'ServiceVersion': processor_options.service_version,
        'CompanyName': worker_result.company_name,
        'CompanyNumber': worker_result.company_number,
        'Error': worker_result.error,
        'FilingId': worker_result.filing_id,
        'Logs': worker_result.logs,
        'Success': worker_result.success,
        "Filename": worker_result.filename,
        'ViewerEntrypoint': worker_result.viewer_entrypoint,
        'OimDirectory': worker_result.oim_directory,
        # Analytics
        'DownloadTime': worker_result.download_time,
        'TotalProcessingTime': worker_result.total_processing_time,
        'TotalUploadedBytes': worker_result.total_uploaded_bytes,
        'UploadTime': worker_result.upload_time,
        'WorkerTime': worker_result.worker_time,
    }
    if worker_result.document_date is not None:
        result['DocumentDate'] = worker_result.document_date.strftime('%Y-%m-%d')
    return result
