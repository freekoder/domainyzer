package com.vampbear.domainyzer.sources;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;

public class AlienVaultSource extends AbstractSource {

    @Override
    public String getName() {
        return "alienvault";
    }

    @Override
    protected String getSourceUrlTemplate() {
        return "https://otx.alienvault.com/api/v1/indicators/domain/%s/passive_dns";
    }

    @Override
    protected List<String> extractSubdomains(String content) {
        List<String> subdomains = new LinkedList<>();
        JSONObject jsonResponse = new JSONObject(content);
        JSONArray passiveDNS = jsonResponse.getJSONArray("passive_dns");
        for (Object item : passiveDNS) {
            if (item instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) item;
                if (jsonObject.has("hostname")) {
                    subdomains.add(jsonObject.getString("hostname"));
                }
            }
        }
        return subdomains;
    }
}
