package com.vampbear.domainyzer;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

public class ReconResult {

    private final Map<String, String> sourceErrorMap = new HashMap<>();
    private final Map<String, List<String>> subdomainSourcesMap = new HashMap<>();

    public void enrichWith(SourceResult sourceResult) {
        String sourceName = sourceResult.getSourceName();
        if (sourceResult.isSuccess()) {
            for (String subdomain : sourceResult.getSubdomains()) {
                subdomainSourcesMap.putIfAbsent(subdomain, new LinkedList<>());
                subdomainSourcesMap.get(subdomain).add(sourceName);
            }
        } else {
            sourceErrorMap.putIfAbsent(sourceName, sourceResult.getMessage());
        }

    }

    public List<Subdomain> getSubdomains() {
        List<Subdomain> subdomains = new LinkedList<>();
        for (String key : subdomainSourcesMap.keySet()) {
            subdomains.add(new Subdomain(key, subdomainSourcesMap.get(key)));
        }
        return subdomains;
    }

    public List<SourceError> getErrors() {
        List<SourceError> errors = new LinkedList<>();
        for (Map.Entry<String, String> item : sourceErrorMap.entrySet()) {
            errors.add(new SourceError(item.getKey(), item.getValue()));
        }
        return errors;
    }
}
