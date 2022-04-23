package com.vampbear.domainyzer.sources;

import com.vampbear.domainyzer.SourceResult;

public interface Source {
    String getName();
    SourceResult getSubdomains(String domain);
}
