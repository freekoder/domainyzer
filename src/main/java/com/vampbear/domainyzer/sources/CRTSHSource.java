package com.vampbear.domainyzer.sources;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;

public class CRTSHSource extends AbstractSource {

    @Override
    public String getName() {
        return "crtsh";
    }

    @Override
    protected String getSourceUrlTemplate() {
        return "https://crt.sh/?q=%%25.%s&output=json";
    }

    @Override
    protected List<String> extractSubdomains(String content) {
        List<String> subdomains = new LinkedList<>();
        JSONArray jsonResponse = new JSONArray(content);
        for (Object item : jsonResponse) {
            if (item instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) item;
                if (jsonObject.has("common_name")) {
                    subdomains.add(jsonObject.getString("common_name"));
                }
            }
        }
        return subdomains;
    }

}
