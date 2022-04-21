package com.vampbear.domainyzer;

public class SourceError {

    private final String sourceName;
    private final String errorMessage;

    public SourceError(String sourceName, String errorMessage) {
        this.sourceName = sourceName;
        this.errorMessage = errorMessage;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
