package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.follow.AdvSearchedLink;
import com.weapes.ntpaprseng.crawler.follow.Link;
import com.weapes.ntpaprseng.crawler.follow.PaperLink;
import com.weapes.ntpaprseng.crawler.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lawrence on 16/8/8.
 */
public class AdvSearchedWebPage extends WebPage {

    private static final int NUM_OF_PAPERS_PER_PAGE = 25;
    private static final String PAPER_LINK_CSS_SELECTOR =
            "h2.h3.extra-tight-line-height a";
    private static final String PAPERS_TOTAL_NUM_SELECTOR =
            "p.text13.tiny-space-below.mb0.pt4 > span:last-child";

    public AdvSearchedWebPage(final String text, final String url) {
        super(text, url, true);
    }

    @Override
    public ExtractedObject extract() {
        return null;
    }

    @Override
    public List<? extends Link> extractAll() {
        System.out.println("Links parsing: url=" + getUrl() + " type=AdvSearched");
        final Document dom = Jsoup.parse(getText());

        final List<Link> allLinks = new ArrayList<>();

        final List<Link> paperLinks =
                getPaperLinks(parsePaperLinks(dom));

        allLinks.addAll(paperLinks);

        if (isFirstPage()) {
            allLinks.addAll(getSiblingLinks(dom));
        }
        System.out.println("Links parsed: url=" + getUrl()
                + " linksSize=" + allLinks.size()
                + " type=" + "AdvSearched");

        return allLinks;
    }

    private List<Link> getSiblingLinks(final Document dom) {
        List<Link> siblingLinks = new ArrayList<>();
        for (int i = 2; i <= parsePageNum(dom); i++) {
            siblingLinks.add(new AdvSearchedLink(buildURLWithPageOrder(i)));
        }
        return siblingLinks;
    }

    private List<Link> getPaperLinks(final Elements paperLinks) {
        return paperLinks.stream()
                .map(link -> new PaperLink(link.attr("href")))
                .filter(paper -> Helper.isURL(paper.getUrl()))
                .collect(Collectors.toList());

    }

    private boolean isFirstPage() {
        return !getUrl().contains("page");
    }

    private int parsePageNum(final Document dom) {
        final int totalNum =
                Integer.parseInt(parseTotalNumSpan(dom).text().trim());

        return (totalNum % NUM_OF_PAPERS_PER_PAGE) == 0
                ? totalNum / NUM_OF_PAPERS_PER_PAGE
                : (totalNum / NUM_OF_PAPERS_PER_PAGE) + 1;

    }

    private String buildURLWithPageOrder(final int index) {
        return getUrl() + "&page=" + index;
    }

    private Elements parsePaperLinks(final Document dom) {
        return dom.select(PAPER_LINK_CSS_SELECTOR);
    }

    private Elements parseTotalNumSpan(Document dom) {
        return dom.select(PAPERS_TOTAL_NUM_SELECTOR);
    }
}
