package com.vampbear.domainyzer.sources;

import com.vampbear.domainyzer.SourceResult;

import java.util.Set;

public interface Source {
    String getName();
    SourceResult getSubdomains(String domain);
}
