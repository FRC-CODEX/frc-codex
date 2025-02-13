package com.frc.codex.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

public class FilingResultRequest {
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String companyName;
	private final String companyNumber;
	private final LocalDateTime documentDate;
	private final Double downloadTime;
	private final String error;
	private final UUID filingId;
	private final String logs;
	private final String filename;
	private final String oimDirectory;
	private final String stubViewerUrl;
	private final boolean success;
	private final Double totalProcessingTime;
	private final Long totalUploadedBytes;
	private final Double uploadTime;
	private final Double workerTime;
	private final GenerationVersioning generationVersioning;

	private FilingResultRequest(Builder builder) {
		this.companyName = builder.companyName;
		this.companyNumber = builder.companyNumber;
		this.documentDate = builder.documentDate;
		this.downloadTime = builder.downloadTime;
		this.error = builder.error;
		this.filingId = builder.filingId;
		this.logs = builder.logs;
		this.filename = builder.filename;
		this.oimDirectory = builder.oimDirectory;
		this.stubViewerUrl = builder.stubViewerUrl;
		this.success = builder.success;
		this.totalProcessingTime = builder.totalProcessingTime;
		this.totalUploadedBytes = builder.totalUploadedBytes;
		this.uploadTime = builder.uploadTime;
		this.workerTime = builder.workerTime;
		this.generationVersioning = builder.generationVersioning;
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	public LocalDateTime getDocumentDate() {
		return documentDate;
	}

	public Double getDownloadTime() {
		return downloadTime;
	}

	public String getError() {
		return error;
	}

	public UUID getFilingId() {
		return filingId;
	}

	public String getLogs() {
		return logs;
	}

	public String getFilename() {
		return filename;
	}

	public String getOimDirectory() {
		return oimDirectory;
	}

	public String getStubViewerUrl() {
		return stubViewerUrl;
	}

	public FilingStatus getStatus() {
		return success ? FilingStatus.COMPLETED : FilingStatus.FAILED;
	}

	public Double getTotalProcessingTime() {
		return totalProcessingTime;
	}

	public Long getTotalUploadedBytes() {
		return totalUploadedBytes;
	}

	public Double getUploadTime() {
		return uploadTime;
	}

	public Double getWorkerTime() {
		return workerTime;
	}

	public GenerationVersioning getGenerationVersioning() {
		return generationVersioning;
	}

	public boolean isSuccess() {
		return success;
	}

	public static class Builder {
		private String companyName;
		private String companyNumber;
		private LocalDateTime documentDate;
		private Double downloadTime;
		private String error;
		private UUID filingId;
		private String logs;
		private String filename;
		private String oimDirectory;
		private String stubViewerUrl;
		private boolean success;
		private Double totalProcessingTime;
		private Long totalUploadedBytes;
		private Double uploadTime;
		private Double workerTime;
		private GenerationVersioning generationVersioning;

		public Builder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}

		public Builder companyNumber(String companyNumber) {
			this.companyNumber = companyNumber;
			return this;
		}

		public Builder documentDate(LocalDateTime documentDate) {
			this.documentDate = documentDate;
			return this;
		}

		public Builder documentDate(String documentDate) {
			if (documentDate == null) {
				this.documentDate = null;
			} else {
				this.documentDate = LocalDate.parse(documentDate, DATE_FORMAT).atStartOfDay();
			}
			return this;
		}

		public Builder error(String error) {
			this.error = error;
			return this;
		}

		public Builder filingId(UUID filingId) {
			this.filingId = filingId;
			return this;
		}

		private Long getLong(JsonNode jsonNode, String key) {
			return jsonNode.has(key) ? jsonNode.get(key).asLong() : null;
		}

		private Double getDouble(JsonNode jsonNode, String key) {
			return jsonNode.has(key) ? jsonNode.get(key).asDouble() : null;
		}

		public Builder json(JsonNode jsonNode) {
			boolean success = Objects.equals(jsonNode.get("Success").asText(), "true");
			String error = null;
			String filename = null;
			String oimDirectory = null;
			String viewerEntrypoint = null;
			GenerationVersioning generationVersioning = null;
			if (!success) {
				error = jsonNode.get("Error").asText();
			} else {
				filename = jsonNode.get("Filename").asText();
				oimDirectory = jsonNode.get("OimDirectory").asText(null);
				viewerEntrypoint = jsonNode.get("ViewerEntrypoint").asText();
				generationVersioning = GenerationVersioning.builder()
						.arelleVersion(jsonNode.get("ArelleVersion").asText(null))
						.viewerVersion(jsonNode.get("ArelleViewerVersion").asText(null))
						.serviceVersion(jsonNode.get("ServiceVersion").asText(null))
						.build();
			}
			String companyName = jsonNode.get("CompanyName").asText();
			companyName = companyName == null ? null : companyName.toUpperCase();
			String companyNumber = jsonNode.get("CompanyNumber").asText();
			String documentDate = null;
			if (jsonNode.has("DocumentDate")) {
				documentDate = jsonNode.get("DocumentDate").asText();
			}
			UUID filingId = UUID.fromString(Objects.requireNonNull(jsonNode.get("FilingId").asText()));
			String logs = jsonNode.get("Logs").asText();
			downloadTime = getDouble(jsonNode, "DownloadTime");
			totalProcessingTime = getDouble(jsonNode, "TotalProcessingTime");
			totalUploadedBytes = getLong(jsonNode, "TotalUploadedBytes");
			uploadTime = getDouble(jsonNode, "UploadTime");
			workerTime = getDouble(jsonNode, "WorkerTime");

			return this
					.companyName(companyName)
					.companyNumber(companyNumber)
					.documentDate(documentDate)
					.error(error)
					.filingId(filingId)
					.logs(logs)
					.filename(filename)
					.oimDirectory(oimDirectory)
					.stubViewerUrl(viewerEntrypoint)
					.generationVersioning(generationVersioning)
					.success(success);
		}

		public Builder logs(String logs) {
			this.logs = logs;
			return this;
		}

		public Builder filename(String filename) {
			this.filename = filename;
			return this;
		}

		public Builder oimDirectory(String oimDirectory) {
			this.oimDirectory = oimDirectory;
			return this;
		}

		public Builder stubViewerUrl(String stubViewerUrl) {
			this.stubViewerUrl = stubViewerUrl;
			return this;
		}

		public Builder generationVersioning(GenerationVersioning generationVersioning) {
			this.generationVersioning = generationVersioning;
			return this;
		}

		public Builder success(boolean success) {
			this.success = success;
			return this;
		}

		public FilingResultRequest build() {
			return new FilingResultRequest(this);
		}
	}
}
