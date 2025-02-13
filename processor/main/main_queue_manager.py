import json
import logging
from collections.abc import Iterable

import boto3
from botocore.exceptions import ClientError

from processor.base.job_message import JobMessage
from processor.base.queue_manager import QueueManager
from processor.base.worker import WorkerResult
from processor.processor_options import ProcessorOptions

logger = logging.getLogger(__name__)


class MainQueueManager(QueueManager):

    def __init__(self, processor_options: ProcessorOptions):
        self._jobs_queue = MainQueueManager._get_queue(processor_options.sqs_jobs_queue_name, processor_options)
        self._results_queue = MainQueueManager._get_queue(processor_options.sqs_results_queue_name, processor_options)
        self._max_number = processor_options.sqs_max_messages
        self._wait_time = processor_options.sqs_wait_time
        self._arelle_version = processor_options.arelle_version
        self._ixbrl_viewer_version = processor_options.ixbrl_viewer_version
        self._service_version = processor_options.service_version

    def complete_job(self, job_message: JobMessage) -> None:
        self._jobs_queue.delete_messages(
            Entries=[{
                'Id': job_message.message_id,
                'ReceiptHandle': job_message.receipt_handle
            }]
        )

    def get_jobs(self) -> Iterable[JobMessage]:
        """
        Receive a batch of job messages in a single request from an SQS queue.
        :return: The list of Message objects received. These each contain the body
                 of the message and metadata and custom attributes.
        """
        try:
            logger.debug("Waiting for messages...")
            messages = self._jobs_queue.receive_messages(
                MessageAttributeNames=["All"],
                MaxNumberOfMessages=self._max_number,
                WaitTimeSeconds=self._wait_time,
            )
            logger.debug("Received %s message(s).", len(messages))
            for message in messages:
                attributes_names = [key for key in message.message_attributes] if message.message_attributes else []
                logger.info(
                    "Processing started for message with attributes %s: (%s) %s",
                    attributes_names, message.message_id, message.body
                )
                yield JobMessage(
                    download_url=message.message_attributes['DownloadUrl']['StringValue'],
                    filing_id=message.body,
                    message_id=message.message_id,
                    receipt_handle=message.receipt_handle,
                    registry_code=message.message_attributes['RegistryCode']['StringValue'],
                )
                logger.info("Processing completed for message: (%s) %s", message.message_id, message.body)
        except ClientError as error:
            logger.exception("Couldn't receive messages from queue: %s", self._jobs_queue)
            raise error

    def publish_result(self, worker_result: WorkerResult) -> None:
        message_attributes = {
            'FilingId': {
                'StringValue': worker_result.filing_id,
                'DataType': 'String'
            },
            'Success': {
                'StringValue': 'true' if worker_result.success else 'false',
                'DataType': 'String'
            },
        }
        if self._arelle_version:
            message_attributes['ArelleVersion'] = {
                'StringValue': self._arelle_version,
                'DataType': 'String'
            }
        if self._ixbrl_viewer_version:
            message_attributes['ArelleViewerVersion'] = {
                'StringValue': self._ixbrl_viewer_version,
                'DataType': 'String'
            }
        if self._service_version:
            message_attributes['ServiceVersion'] = {
                'StringValue': self._service_version,
                'DataType': 'String'
            }
        if worker_result.company_name:
            message_attributes['CompanyName'] = {
                'StringValue': worker_result.company_name,
                'DataType': 'String'
            }
        if worker_result.company_number:
            message_attributes['CompanyNumber'] = {
                'StringValue': worker_result.company_number,
                'DataType': 'String'
            }
        if worker_result.document_date:
            message_attributes['DocumentDate'] = {
                'StringValue': worker_result.document_date.strftime('%Y-%m-%d'),
                'DataType': 'String'
            }
        if worker_result.error:
            message_attributes['Error'] = {
                'StringValue': worker_result.error,
                'DataType': 'String'
            }
        message_attributes['Analytics'] = {
            'StringValue': json.dumps({
                'download_time': worker_result.download_time,
                'total_processing_time': worker_result.total_processing_time,
                'total_uploaded_bytes': worker_result.total_uploaded_bytes,
                'upload_time': worker_result.upload_time,
                'worker_time': worker_result.worker_time,
            }),
            'DataType': 'String'
        }
        if worker_result.filename:
            message_attributes['Filename'] = {
                'StringValue': worker_result.filename,
                'DataType': 'String'
            }
        if worker_result.viewer_entrypoint:
            message_attributes['ViewerEntrypoint'] = {
                'StringValue': worker_result.viewer_entrypoint,
                'DataType': 'String'
            }
        if worker_result.oim_directory:
            message_attributes['OimDirectory'] = {
                'StringValue': worker_result.oim_directory,
                'DataType': 'String'
            }
        self._results_queue.send_message(
            MessageBody=worker_result.logs,
            MessageAttributes=message_attributes
        )

    @staticmethod
    def _get_queue(queue_name: str, processor_options: ProcessorOptions):
        session = boto3.session.Session()
        client = session.resource(
            'sqs',
            region_name=processor_options.sqs_region_name,
        )
        queue = client.get_queue_by_name(QueueName=queue_name)
        return queue
