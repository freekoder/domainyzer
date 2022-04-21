package com.vampbear.domainyzer.sources;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.stream.Collectors;

public class RiddlerSource extends AbstractSource {

    @Override
    public String getName() {
        return "riddler";
    }

    @Override
    protected String getSourceUrlTemplate() {
        return "https://riddler.io/search?q=pld:%s&view_type=data_table";
    }

    @Override
    protected List<String> extractSubdomains(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.select("body > div.container-main > div > div > div:nth-child(2) > table > tbody > tr > td.col-lg-5.col-md-5.col-sm-5 > a:nth-child(2)");
        return elements.stream().map(Element::text).collect(Collectors.toList());
    }
}
