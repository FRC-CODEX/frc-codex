package com.frc.codex.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Filing {
	private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final UUID filingId;
	private final Timestamp discoveredDate;
	private final String status;
	private final String registryCode;
	private final String downloadUrl;
	private final String companyName;
	private final String companyNumber;
	private final String externalFilingId;
	private final String externalViewUrl;
	private final String filename;
	private final String filingType;
	private final LocalDateTime filingDate;
	private final LocalDateTime documentDate;
	private final Long streamTimepoint;
	private final String error;
	private final String logs;
	private final String stubViewerUrl;
	private final String oimDirectory;
	private final GenerationVersioning generationVersioning;

	public Filing(Builder b) {
		this.filingId = b.filingId;
		this.discoveredDate = b.discoveredDate;
		this.status = b.status;
		this.registryCode = b.registryCode;
		this.downloadUrl = b.downloadUrl;
		this.companyName = b.companyName;
		this.companyNumber = b.companyNumber;
		this.externalFilingId = b.externalFilingId;
		this.externalViewUrl = b.externalViewUrl;
		this.filename = b.filename;
		this.filingType = b.filingType;
		this.filingDate = b.filingDate;
		this.documentDate = b.documentDate;
		this.streamTimepoint = b.streamTimepoint;
		this.error = b.error;
		this.logs = b.logs;
		this.stubViewerUrl = b.stubViewerUrl;
		this.oimDirectory = b.oimDirectory;
		this.generationVersioning = b.generationVersioning;
	}

	public String displayDocumentDate() {
		if (documentDate == null) {
			return "Available in viewer.";
		}
		return DISPLAY_DATE_FORMAT.format(documentDate);
	}

	public String displayFilingDate() {
		if (filingDate == null) {
			return "";
		}
		return DISPLAY_DATE_FORMAT.format(filingDate);
	}

	public UUID getFilingId() {
		return filingId;
	}

	public Timestamp getDiscoveredDate() {
		return discoveredDate;
	}

	public String getStatus() {
		return status;
	}

	public boolean isCompleted() {
		return Objects.equals(status, FilingStatus.COMPLETED.toString());
	}

	public String getRegistryCode() {
		return registryCode;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	public String getFilename() {
		return filename;
	}

	public String getFilenameStem() {
		if (filename == null) {
			return null;
		}
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex > 0) {
			return filename.substring(0, dotIndex);
		}
		return filename;
	}

	public String getFilingType() {
		return filingType;
	}

	public LocalDateTime getFilingDate() {
		return filingDate;
	}

	public LocalDateTime getDocumentDate() {
		return documentDate;
	}

	public Long getStreamTimepoint() {
		return streamTimepoint;
	}

	public String getError() {
		return error;
	}

	public String getLogs() {
		return logs;
	}

	public String getStubViewerUrl() {
		return stubViewerUrl;
	}

	public String getOimDirectory() {
		return oimDirectory;
	}

	public String getXbrlCsvUrl() {
		return "download/" + filingId.toString() + "/csv";
	}

	public String getXbrlJsonUrl() {
		return "download/" + filingId.toString() + "/json";
	}

	public String getViewerLink() {
		String path = isCompleted() ? stubViewerUrl : "viewer";
		return "view/" + filingId.toString() + "/" + path;
	}

	public String getExternalFilingId() {
		return externalFilingId;
	}

	public String getExternalViewUrl() {
		if (getRegistryCode().equals(RegistryCode.FCA.getCode()) ){
			return externalViewUrl;
		}
		else {
			return "https://find-and-update.company-information.service.gov.uk/company/"
					+ companyNumber + "/filing-history/" + externalFilingId
					+ "/document?format=xhtml&download=0";
		}
	}

	public GenerationVersioning getGenerationVersioning() {
		return generationVersioning;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private UUID filingId;
		private Timestamp discoveredDate;
		private String status;
		private String registryCode;
		private String downloadUrl;
		private String companyName;
		private String companyNumber;
		private String externalFilingId;
		private String externalViewUrl;
		private String filename;
		private String filingType;
		private LocalDateTime filingDate;
		private LocalDateTime documentDate;
		private Long streamTimepoint;
		private String error;
		private String logs;
		private String stubViewerUrl;
		private String oimDirectory;
		private GenerationVersioning generationVersioning;

		public Filing build() {
			return new Filing(this);
		}

		public Builder filingId(String filingId) {
			this.filingId = UUID.fromString(filingId);
			return this;
		}

		public Builder discoveredDate(Timestamp discoveredDate) {
			this.discoveredDate = discoveredDate;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Builder registryCode(String registryCode) {
			this.registryCode = registryCode;
			return this;
		}

		public Builder downloadUrl(String downloadUrl) {
			this.downloadUrl = downloadUrl;
			return this;
		}

		public Builder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}

		public Builder companyNumber(String companyNumber) {
			this.companyNumber = companyNumber;
			return this;
		}

		public Builder externalFilingId(String externalFilingId) {
			this.externalFilingId = externalFilingId;
			return this;
		}

		public Builder externalViewUrl(String externalViewUrl) {
			this.externalViewUrl = externalViewUrl;
			return this;
		}


		public Builder filename(String filename) {
			this.filename = filename;
			return this;
		}

		public Builder filingType(String filingType) {
			this.filingType = filingType;
			return this;
		}

		public Builder filingDate(LocalDateTime filingDate) {
			this.filingDate = filingDate;
			return this;
		}

		public Builder documentDate(LocalDateTime documentDate) {
			this.documentDate = documentDate;
			return this;
		}

		public Builder streamTimepoint(Long streamTimepoint) {
			this.streamTimepoint = streamTimepoint;
			return this;
		}

		public Builder error(String error) {
			this.error = error;
			return this;
		}

		public Builder logs(String logs) {
			this.logs = logs;
			return this;
		}

		public Builder stubViewerUrl(String stubViewerUrl) {
			this.stubViewerUrl = stubViewerUrl;
			return this;
		}

		public Builder oimDirectory(String oimDirectory) {
			this.oimDirectory = oimDirectory;
			return this;
		}

		public Builder generationVersioning(GenerationVersioning generationVersioning) {
			this.generationVersioning = generationVersioning;
			return this;
		}
	}
}
