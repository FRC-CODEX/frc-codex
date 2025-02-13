import os
from functools import cached_property
from importlib.metadata import PackageNotFoundError, version
from pathlib import Path


def _get_library_version(library_name: str) -> str | None:
    try:
        return version(library_name)
    except PackageNotFoundError:
        return None


class ProcessorOptions:

    @cached_property
    def arelle_version(self) -> str | None:
        return _get_library_version('arelle-release')

    @cached_property
    def aws_endpoint_url(self):
        return os.getenv('AWS_ENDPOINT_URL')

    @cached_property
    def http_cache_directory(self) -> Path:
        return Path(os.getenv('HTTP_CACHE_DIRECTORY', '/tmp/_HTTP_CACHE'))

    @cached_property
    def ixbrl_viewer_version(self) -> str | None:
        return _get_library_version('ixbrl-viewer')

    @cached_property
    def maximum_processors(self):
        return int(os.getenv('MAXIMUM_PROCESSORS', 0))

    @cached_property
    def s3_http_cache_bucket_name(self):
        return os.getenv('S3_HTTP_CACHE_BUCKET_NAME')

    @cached_property
    def s3_results_bucket_name(self):
        return os.getenv('S3_RESULTS_BUCKET_NAME')

    @cached_property
    def s3_taxonomy_packages_bucket_name(self):
        return os.getenv('S3_TAXONOMY_PACKAGES_BUCKET_NAME')

    @cached_property
    def s3_region_name(self):
        return os.getenv('S3_REGION_NAME')

    @cached_property
    def service_version(self) -> str | None:
        return os.getenv('SERVICE_VERSION')

    @cached_property
    def sqs_jobs_queue_name(self):
        return os.getenv('SQS_JOBS_QUEUE_NAME')

    @cached_property
    def sqs_results_queue_name(self):
        return os.getenv('SQS_RESULTS_QUEUE_NAME')

    @cached_property
    def sqs_region_name(self):
        return os.getenv('SQS_REGION_NAME')

    @cached_property
    def sqs_max_messages(self):
        return os.getenv('SQS_MAX_MESSAGES', 1)

    @cached_property
    def sqs_visibility_timeout(self):
        return os.getenv('SQS_VISIBILITY_TIMEOUT', 60)

    @cached_property
    def sqs_wait_time(self):
        return os.getenv('SQS_WAIT_TIME', 20)

    @cached_property
    def sync_interval_seconds(self):
        return os.getenv('SYNC_INTERVAL_SECONDS', 0)
