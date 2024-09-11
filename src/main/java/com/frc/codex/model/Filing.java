package com.frc.codex.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Filing {
	private final UUID filingId;
	private final Timestamp discoveredDate;
	private final String status;
	private final String registryCode;
	private final String downloadUrl;
	private final String companyName;
	private final String companyNumber;
	private final String filename;
	private final String filingType;
	private final LocalDateTime filingDate;
	private final LocalDateTime documentDate;
	private final Long streamTimepoint;
	private final String error;
	private final String logs;
	private final String stubViewerUrl;
	private final String oimCsvUrl;
	private final String oimJsonUrl;

	public Filing(Builder b) {
		this.filingId = b.filingId;
		this.discoveredDate = b.discoveredDate;
		this.status = b.status;
		this.registryCode = b.registryCode;
		this.downloadUrl = b.downloadUrl;
		this.companyName = b.companyName;
		this.companyNumber = b.companyNumber;
		this.filename = b.filename;
		this.filingType = b.filingType;
		this.filingDate = b.filingDate;
		this.documentDate = b.documentDate;
		this.streamTimepoint = b.streamTimepoint;
		this.error = b.error;
		this.logs = b.logs;
		this.stubViewerUrl = b.stubViewerUrl;
		this.oimCsvUrl = b.oimCsvUrl;
		this.oimJsonUrl = b.oimJsonUrl;
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

	public String getOimCsvUrl() {
		return oimCsvUrl;
	}

	public String getOimJsonUrl() {
		return oimJsonUrl;
	}

	public String getViewerLink() {
		return "view/" + filingId.toString() + "/" + stubViewerUrl;
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
		private String filename;
		private String filingType;
		private LocalDateTime filingDate;
		private LocalDateTime documentDate;
		private Long streamTimepoint;
		private String error;
		private String logs;
		private String stubViewerUrl;
		private String oimCsvUrl;
		private String oimJsonUrl;

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

		public Builder oimCsvUrl(String oimCsvUrl) {
			this.oimCsvUrl = oimCsvUrl;
			return this;
		}

		public Builder oimJsonUrl(String oimJsonUrl) {
			this.oimJsonUrl = oimJsonUrl;
			return this;
		}
	}
}
