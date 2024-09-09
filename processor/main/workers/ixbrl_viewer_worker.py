import datetime
import logging
import zipfile
from dataclasses import dataclass
from pathlib import Path
from typing import cast

from arelle.RuntimeOptions import RuntimeOptions  # type: ignore
from arelle.api.Session import Session  # type: ignore

from processor.base.queue_manager import JobMessage
from processor.base.worker import Worker, WorkerResult

VIEWER_HTML_FILENAME = 'ixbrlviewer.html'


logger = logging.getLogger(__name__)


@dataclass(frozen=True)
class IxbrlViewerResult:
    success: bool
    logs: str
    company_name: str | None
    company_number: str | None
    document_date: datetime.datetime | None


class IxbrlViewerWorker(Worker):

    def work(self, job_message: JobMessage, target_path: Path, viewer_directory: Path) -> WorkerResult:
        packages = []
        for parent in target_path.parents:
            if zipfile.is_zipfile(parent):
                packages.append(parent)
                break
        result = self._generate_viewer(target_path, viewer_directory, packages)
        if not result.success:
            return WorkerResult(
                error='Viewer generation failed within Arelle. Check the logs for details.',
                logs=result.logs
            )
        viewer_path = viewer_directory / VIEWER_HTML_FILENAME
        if not viewer_path.exists():
            return WorkerResult(
                error='Arelle reported success but viewer was not found. Check the logs for details.',
                logs=result.logs
            )
        return WorkerResult(
            success=True,
            viewer_entrypoint=VIEWER_HTML_FILENAME,
            logs=result.logs,
            company_name=result.company_name,
            company_number=result.company_number,
            document_date=result.document_date,
        )

    def _get_value_by_local_name(self, model_xbrl, local_name: str) -> str | None:
        facts = model_xbrl.factsByLocalName.get(local_name, [])
        if facts:
            return next(iter(facts)).xValue
        return None

    def _generate_viewer(self, target_path: Path, viewer_directory: Path, packages: list[Path]) -> IxbrlViewerResult:
        runtime_options = RuntimeOptions(
            cacheDirectory='./_HTTP_CACHE',
            disablePersistentConfig=True,
            entrypointFile=str(target_path),
            # TODO: Enable this when we have taxonomy packages provided
            # internetConnectivity='offline',
            keepOpen=True,
            logFormat="[%(messageCode)s] %(message)s - %(file)s",
            logFile='logToBuffer',
            pluginOptions={
                'saveViewerDest': str(viewer_directory),
                'useStubViewer': True,
                'viewerNoCopyScript': True,
                'viewerURL': '/ixbrlviewer.js',
            },
            plugins='ixbrl-viewer',
            strictOptions=False,
            packages=[str(package) for package in packages],
        )
        with Session() as session:
            success = session.run(runtime_options)
            company_name: str | None = None
            company_number: str | None = None
            document_date: datetime.datetime | None = None
            model_xbrls = session.get_models()
            if model_xbrls:
                model_xbrl = model_xbrls[0]
                company_name = self._get_value_by_local_name(model_xbrl, 'EntityCurrentLegalOrRegisteredName')
                company_number = self._get_value_by_local_name(model_xbrl, 'UKCompaniesHouseRegisteredNumber')
                document_date = cast(datetime.datetime, self._get_value_by_local_name(model_xbrl, 'BalanceSheetDate'))
            logs = session.get_logs('text', clear_logs=True)
            return IxbrlViewerResult(
                success=success,
                logs=logs,
                company_name=company_name,
                company_number=company_number,
                document_date=document_date,
            )