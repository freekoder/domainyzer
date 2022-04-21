package com.vampbear.domainyzer.sources;

import java.util.List;
import org.json.JSONArray;
import java.util.LinkedList;

public class AnubisSource extends AbstractSource {

    @Override
    public String getName() {
        return "anubis";
    }

    @Override
    protected String getSourceUrlTemplate() {
        return "https://jonlu.ca/anubis/subdomains/%s";
    }

    @Override
    protected List<String> extractSubdomains(String content) {
        List<String> subdomains = new LinkedList<>();
        JSONArray jsonResponse = new JSONArray(content);
        for (Object item : jsonResponse) {
            if (item instanceof String) {
                subdomains.add((String) item);
            }
        }
        return subdomains;
    }
}
