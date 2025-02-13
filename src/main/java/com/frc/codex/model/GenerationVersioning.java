package com.frc.codex.model;

public class GenerationVersioning {
    private final String arelleVersion;
    private final String viewerVersion;
    private final String serviceVersion;

    public GenerationVersioning(Builder b) {
        this.arelleVersion = b.arelleVersion;
        this.viewerVersion = b.viewerVersion;
        this.serviceVersion = b.serviceVersion;
    }

    public String getArelleVersion() {
        return arelleVersion;
    }

    public String getViewerVersion() {
        return viewerVersion;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String arelleVersion;
        private String viewerVersion;
        private String serviceVersion;

        public GenerationVersioning build() {
            return new GenerationVersioning(this);
        }

        public Builder arelleVersion(String arelleVersion) {
            this.arelleVersion = arelleVersion;
            return this;
        }

        public Builder viewerVersion(String viewerVersion) {
            this.viewerVersion = viewerVersion;
            return this;
        }

        public Builder serviceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }
    }
}
