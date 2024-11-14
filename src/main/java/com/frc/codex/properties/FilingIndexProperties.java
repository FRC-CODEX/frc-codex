package com.frc.codex.properties;

import com.zaxxer.hikari.HikariConfig;

public interface FilingIndexProperties {
	String adminCookieName();
	boolean adminEnabled();
	String adminKey();
	String awsLambdaFunctionName();
	long awsLambdaTimeoutSeconds();
	String companiesHouseDocumentApiBaseUrl();
	String companiesHouseInformationApiBaseUrl();
	int companiesHouseRapidRateLimit();
	int companiesHouseRapidRateWindow();
	String companiesHouseRestApiKey();
	String companiesHouseStreamApiBaseUrl();
	String companiesHouseStreamApiKey();
	String dbSeedScriptPath();
	String fcaDataApiBaseUrl();
	int fcaPastDays();
	String fcaSearchApiUrl();
	boolean enablePreprocessing();
	int filingLimitCompaniesHouse();
	int filingLimitFca();
	HikariConfig getDatabaseConfig(String poolName);
	boolean isAws();
	boolean isDbMigrateAsync();
	int lambdaPreprocessingConcurrency();
	long maximumSearchResults();
	String s3ResultsBucketName();
	long searchPageSize();
	String sqsJobsQueueName();
	String sqsResultsQueueName();
	String supportEmail();

	/**
	 * @return The number of unprocessed `companies` records that can be queued before the indexer stops
	 *   downloading Companies House archives to discover new companies.
	 */
	int unprocessedCompaniesLimit();
}