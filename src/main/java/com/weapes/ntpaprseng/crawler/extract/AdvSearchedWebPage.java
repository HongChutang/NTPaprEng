package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.follow.AdvSearchedLink;
import com.weapes.ntpaprseng.crawler.follow.Link;
import com.weapes.ntpaprseng.crawler.follow.PaperLink;
import com.weapes.ntpaprseng.crawler.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lawrence on 16/8/8.
 */
public class AdvSearchedWebPage extends WebPage {

    private static final int NUM_OF_PAPERS_PER_PAGE = 25;
    private static final String LINK_CSS_SELECTOR =
            "section > ol > li > div > h2 > a";
    private static final String PAPERS_TOTAL_NUM_SELECTOR =
            "div.filter-menu > div.pin-left > p > span:last-child";

    public AdvSearchedWebPage(final String text, final String url) {
        super(text, url, true);
    }

    @Override
    public ExtractedObject extract() {
        return null;
    }

    @Override
    public List<? extends Link> extractAll() {
        final Document dom = Jsoup.parse(getText());
        final Elements links = dom.select(LINK_CSS_SELECTOR);

        final List<Link> extractedObjects = new ArrayList<>();

        for (Element link : links) {
            final String href = link.attr("href");

            if (Helper.isURL(href)) {
                extractedObjects.add(new PaperLink(href));
            }
        }

        if (getUrl().contains("page")) {

            final Elements allNumSpan = dom
                    .select(PAPERS_TOTAL_NUM_SELECTOR);
            final String allPaperNum = allNumSpan.text();
            final int totalNum = Integer.parseInt(allPaperNum);

            final int pageNum;
            if ((totalNum % NUM_OF_PAPERS_PER_PAGE) == 0) {
                pageNum = totalNum / NUM_OF_PAPERS_PER_PAGE;
            } else {
                pageNum = (totalNum / NUM_OF_PAPERS_PER_PAGE) + 1;
            }

            for (int i = 2; i <= pageNum; i++) {
                final String anotherAdvPageURL = getUrl() + "&page=" + i;
                extractedObjects.add(new AdvSearchedLink(anotherAdvPageURL));
            }

        }

        return extractedObjects;

    }
}
