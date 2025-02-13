-- Capture dependency versions used to generate viewer and OIM artifacts for potential future resets.
ALTER TABLE filings
    ADD COLUMN generation_arelle_version VARCHAR(20),
    ADD COLUMN generation_ixbrl_viewer_version VARCHAR(20),
    ADD COLUMN generation_service_version VARCHAR(20);

CREATE INDEX generation_arelle_version_idx ON filings (generation_arelle_version);
CREATE INDEX generation_ixbrl_viewer_version_idx ON filings (generation_ixbrl_viewer_version);
CREATE INDEX generation_service_version_idx ON filings (generation_service_version);
