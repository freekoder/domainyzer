package com.vampbear.domainyzer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Domainyzer {

    private static final Pattern DOMAIN_PATTERN = Pattern.compile("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\\\.)+[A-Za-z]{2,6}$");

    public List<String> fromDomain(String domain) {
        Set<String> subdomains = getSubdomainsFromCRTSH(domain);
        subdomains.addAll(getSubdomainsFromAlienVault(domain));
        subdomains.addAll(getSubdomainsFromFullHunt(domain));
        List<String> list = new LinkedList<>(subdomains);
        return list.stream().sorted().collect(Collectors.toList());
    }

    private Collection<String> getSubdomainsFromFullHunt(String domain) {
        Set<String> subdomains = new HashSet<>();
        HttpClient client = new HttpClient();
        try {
            String requestUrl = String.format("https://fullhunt.io/api/v1/domain/%s/subdomains", domain);
            PageResponse response = client.getPageByUrl(requestUrl);
            JSONObject jsonResponse = new JSONObject(response.getOrigContent());
            JSONArray hosts = jsonResponse.getJSONArray("hosts");
            for (Object item : hosts) {
                if (item instanceof String) {
                    String hostname = (String) item;
                    if (hostname.endsWith(domain) && isDomainName(domain)) {
                        subdomains.add(hostname);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subdomains;
    }

    private Collection<String> getSubdomainsFromAlienVault(String domain) {
        Set<String> subdomains = new HashSet<>();
        HttpClient client = new HttpClient();
        try {
            String requestUrl = String.format("https://otx.alienvault.com/api/v1/indicators/domain/%s/passive_dns", domain);
            PageResponse response = client.getPageByUrl(requestUrl);
            JSONObject jsonResponse = new JSONObject(response.getOrigContent());
            JSONArray passiveDNS = jsonResponse.getJSONArray("passive_dns");
            for (Object item : passiveDNS) {
                if (item instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) item;
                    if (jsonObject.has("hostname")) {
                        String hostname = jsonObject.getString("hostname");
                        if (hostname.endsWith(domain) && isDomainName(hostname)) {
                            subdomains.add(hostname);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subdomains;
    }

    private Set<String> getSubdomainsFromCRTSH(String domain) {
        Set<String> subdomains = new HashSet<>();
        HttpClient client = new HttpClient();
        try {
            String requestUrl = String.format("https://crt.sh/?q=%%25.%s&output=json", domain);
            PageResponse response = client.getPageByUrl(requestUrl);
            JSONArray jsonResponse = new JSONArray(response.getOrigContent());
            for (Object item : jsonResponse) {
                if (item instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) item;
                    if (jsonObject.has("common_name")) {
                        String commonName = jsonObject.getString("common_name");
                        if (commonName.endsWith(domain) && isDomainName(commonName)) {
                            subdomains.add(commonName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subdomains;
    }

    private boolean isDomainName(String name) {
        Matcher matcher = DOMAIN_PATTERN.matcher(name);
        return matcher.find();
    }
}
