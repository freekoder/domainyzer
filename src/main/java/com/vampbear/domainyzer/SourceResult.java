package com.vampbear.domainyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SourceResult {

    private final boolean success;
    private final String sourceName;
    private final Set<String> subdomains;
    private final String message;

    private SourceResult(boolean success, String sourceName, List<String> subdomains, String message) {
        this.success = success;
        this.sourceName = sourceName;
        this.subdomains = new HashSet<>(subdomains);
        this.message = message;
    }

    public static SourceResult error(String sourceName, String message) {
        return new SourceResult(false, sourceName, Collections.emptyList(), message);
    }

    public static SourceResult success(String sourceName, List<String> subdomains) {
        return new SourceResult(true, sourceName, subdomains, "");
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Set<String> getSubdomains() {
        return subdomains;
    }

    public String getMessage() {
        return message;
    }
}
