package com.vampbear.domainyzer;

import java.util.*;
import java.util.concurrent.*;

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

        ExecutorService executor = Executors.newFixedThreadPool(sources.size());
        List<Future<SourceResult>> futuresList = new LinkedList<>();

        for (Source source : sources) {
            futuresList.add(executor.submit(() -> source.getSubdomains(domain)));
        }
        for (Future<SourceResult> future : futuresList) {
            try {
                SourceResult sourceResult = future.get();
                result.enrichWith(sourceResult);
            } catch (InterruptedException | ExecutionException ignore) {}
        }
        executor.shutdown();
        return result;
    }
}
