package com.vampbear.domainyzer;

import java.util.*;

import com.vampbear.domainyzer.sources.*;

public class Domainyzer {

    private final List<Source> sources = new LinkedList<>();

    public Domainyzer() {
        sources.add(new CRTSHSource());
        sources.add(new AlienVaultSource());
        sources.add(new FullhuntSource());
        sources.add(new RiddlerSource());
        sources.add(new AnubisSource());
    }

    public ReconResult fromDomain(String domain) {
        ReconResult result = new ReconResult();
        for (Source source : sources) {
            SourceResult sourceResult = source.getSubdomains(domain);
            result.enrichWith(sourceResult);
        }
        return result;
    }
}
