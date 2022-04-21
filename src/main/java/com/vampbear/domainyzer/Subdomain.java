package com.vampbear.domainyzer;

import java.util.List;
import java.util.ArrayList;

public class Subdomain {

    private final String subdomain;
    private final List<String> sources;

    public Subdomain(String subdomain, List<String> sources) {
        this.subdomain = subdomain;
        this.sources = new ArrayList<>(sources);
    }

    public String getSubdomain() {
        return subdomain;
    }

    public List<String> getSources() {
        return sources;
    }
}
