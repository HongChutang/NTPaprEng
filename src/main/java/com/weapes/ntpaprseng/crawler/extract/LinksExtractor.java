package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.ExtractedObject;
import com.weapes.ntpaprseng.crawler.Link;
import com.weapes.ntpaprseng.crawler.WebPage;
import com.weapes.ntpaprseng.crawler.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by lawrence on 16/8/7.
 */
public class LinksExtractor implements Extractor {

    private final String cssSelector = "section > ol > li > div > h2 > a";

    @Override
    public ExtractedObject extract(Input input) {

        if (input.getType() == Input.Type.FILE) {
            return null;
        }

        final WebPage webPage = (WebPage) input.getValue();
        final String html = webPage.getText();

        final Document dom = Jsoup.parse(html);
        final Elements links = dom.select(cssSelector);

        LinkArray linkArray = new LinkArray();

        for (Element link : links) {
            final String url = link.attr("href");
            if (Helper.isURL(url)) {
                linkArray.add(new Link(url, true));
            }
        }

        return linkArray;

    }
}
