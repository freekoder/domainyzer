package com.vampbear.domainyzer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Domainyzer {

    private static final String DOMAIN_REGEXP =
            "^((?!-)[A-Za-z0-9-]"
                    + "{1,63}(?<!-)\\.)"
                    + "+[A-Za-z]{2,6}";

    private static final Pattern DOMAIN_PATTERN = Pattern.compile(DOMAIN_REGEXP);

    public List<String> fromDomain(String domain) {
        Set<String> subdomains = new HashSet<>();
        subdomains.addAll(getSubdomainsFromCRTSH(domain));
        subdomains.addAll(getSubdomainsFromAlienVault(domain));
        subdomains.addAll(getSubdomainsFromFullHunt(domain));
        subdomains.addAll(getSubdomainsFromRiddler(domain));
        subdomains.addAll(getSubdomainsFromAnubis(domain));
        List<String> list = new LinkedList<>(subdomains);
        return list.stream().sorted().collect(Collectors.toList());
    }

    private Collection<String> getSubdomainsFromAnubis(String domain) {
        Set<String> subdomains = new HashSet<>();
        HttpClient client = new HttpClient();
        try {
            String requestUrl = String.format("https://jonlu.ca/anubis/subdomains/%s", domain);
            PageResponse response = client.getPageByUrl(requestUrl);
            JSONArray jsonResponse = new JSONArray(response.getOrigContent());
            for (Object item : jsonResponse) {
                if (item instanceof String) {
                    String name = (String) item;
                    if (name.endsWith(domain) && isDomainName(name)) {
                        subdomains.add(name);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subdomains;
    }

    private Collection<String> getSubdomainsFromRiddler(String domain) {
        Set<String> subdomains = new HashSet<>();
        HttpClient client = new HttpClient();
        try {
            String requestUrl = String.format("https://riddler.io/search?q=pld:%s&view_type=data_table", domain);
            PageResponse response = client.getPageByUrl(requestUrl);
            Document document = response.getDocument();
            Elements elements = document.select("body > div.container-main > div > div > div:nth-child(2) > table > tbody > tr > td.col-lg-5.col-md-5.col-sm-5 > a:nth-child(2)");
            for (Element element : elements) {
                String elementContent = element.text();
                if (elementContent.endsWith(domain) && isDomainName(elementContent)) {
                    subdomains.add(elementContent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subdomains;
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
                    if (hostname.endsWith(domain) && isDomainName(hostname)) {
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
        return matcher.matches();
    }
}
