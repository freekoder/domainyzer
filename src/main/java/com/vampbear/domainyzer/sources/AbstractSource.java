package com.vampbear.domainyzer.sources;

import com.vampbear.domainyzer.HttpClient;
import com.vampbear.domainyzer.PageResponse;
import com.vampbear.domainyzer.SourceResult;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractSource implements Source {

    private static final String DOMAIN_REGEXP = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}";

    private static final Pattern DOMAIN_PATTERN = Pattern.compile(DOMAIN_REGEXP);

    protected boolean isDomainName(String name) {
        Matcher matcher = DOMAIN_PATTERN.matcher(name);
        return matcher.matches();
    }

    public SourceResult getSubdomains(String domain) {
        List<String> subdomains = new LinkedList<>();
        try {
            String sourceContent = getContentFromSource(getSourceUrlTemplate(), domain);
            List<String> candidates = extractSubdomains(sourceContent);
            for (String candidate : candidates) {
                candidate = candidate.toLowerCase();
                if (isDomainName(candidate) && candidate.endsWith(domain)) {
                    subdomains.add(candidate);
                }
            }
            return SourceResult.success(getName(), subdomains);
        } catch (Exception e) {
            return SourceResult.error(getName(), e.getMessage());
        }
    }

    protected String getContentFromSource(String url, String domain) throws IOException {
        HttpClient client = new HttpClient();
        String requestUrl = String.format(url, domain);
        PageResponse response = client.getPageByUrl(requestUrl);
        return response.getOrigContent();
    }

    protected abstract String getSourceUrlTemplate();
    protected abstract List<String> extractSubdomains(String content);
}
