package com.vampbear.domainyzer.sources;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;

public class FullhuntSource extends AbstractSource {

    @Override
    public String getName() {
        return "fullhunt";
    }

    @Override
    protected String getSourceUrlTemplate() {
        return "https://fullhunt.io/api/v1/domain/%s/subdomains";
    }

    @Override
    protected List<String> extractSubdomains(String content) {
        List<String> subdomains = new LinkedList<>();
        JSONObject jsonResponse = new JSONObject(content);
        JSONArray hosts = jsonResponse.getJSONArray("hosts");
        for (Object item : hosts) {
            if (item instanceof String) {
                subdomains.add((String) item);
            }
        }
        return subdomains;
    }
}
