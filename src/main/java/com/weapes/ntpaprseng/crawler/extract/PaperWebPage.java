package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.store.Paper;
import com.weapes.ntpaprseng.crawler.store.Storable;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lawrence on 16/8/8.
 */
public class PaperWebPage extends WebPage {

    private static final String AUTHOR_CSS_SELECTOR =
            "ul.authors > li > a > span";
    private static final String TITLE_CSS_SELECTOR =
            "article > header > h1";
    private static final String SOURCE_LINK_CSS_SELECTOR =
            "article > header > div:first-child > p > span:first-child";
    private static final String ISSN_CSS_SELECTOR =
            "#footer > div > dl > dd.issn";
    private static final String EISSN_CSS_SELECTOR =
            "#footer > div > dl > dd.eissn";
    private static final String DOI_CSS_SELECTOR =
            "article > header > dl > dd.doi";
    private static final String VOLUM_CSS_SELECTOR =
            "#sub-navigation > li.parent.parent-2 > a";
    private static final String ISSUE_CSS_SELECTOR =
            "#sub-navigation > li.parent.parent-3 > a";
    private static final String PAGE_CSS_SELECTOR =
            "article > header > dl > dd.page";

    private static final int ISSN_TEXT_OFFSET = 6;
    private static final int EISSN_TEXT_OFFSET = 7;
    private static final int DOI_TEXT_OFFSET = 4;
    private static final int VOLUM_TEXT_OFFSET = 7;
    private static final int ISSUE_TEXT_OFFSET = 6;

    public PaperWebPage(final String text, final String url) {
        super(text, url);
    }

    @Override
    public Storable extract() {
        final Document dom = Jsoup.parse(getText());
        return new Paper(parseAuthors(dom),
                parseTitle(dom),
                parseSourceLink(dom),
                parseISSN(dom),
                parseEISSN(dom),
                parseDOI(dom),
                parseVolum(dom),
                parseIssue(dom),
                parsePageBegin(dom),
                parsePageEnd(dom));
    }

    @Override
    public List<ExtractedObject> extractAll() {
        return null;
    }

    private List<String> parseAuthors(final Document dom) {
        final Elements authorsDOM = dom.select(AUTHOR_CSS_SELECTOR);
        return authorsDOM
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());
    }

    private String parseTitle(final Document dom) {
        return dom
                .select(TITLE_CSS_SELECTOR)
                .text();
    }

    private String parseSourceLink(final Document dom) {
        return dom
                .select(SOURCE_LINK_CSS_SELECTOR)
                .text();
    }

    @NotNull
    private String parseISSN(final Document dom) {
        return dom
                .select(ISSN_CSS_SELECTOR)
                .text()
                .trim()
                .substring(ISSN_TEXT_OFFSET);
    }

    @NotNull
    private String parseEISSN(final Document dom) {
        return dom
                .select(EISSN_CSS_SELECTOR)
                .text()
                .trim()
                .substring(EISSN_TEXT_OFFSET);
    }

    @NotNull
    private String parseDOI(final Document dom) {
        return dom.select(DOI_CSS_SELECTOR)
                .text()
                .substring(DOI_TEXT_OFFSET);
    }

    private int parseVolum(final Document dom) {
        return Integer.parseInt(dom.select(VOLUM_CSS_SELECTOR)
                                    .text()
                                    .substring(VOLUM_TEXT_OFFSET));
    }

    private int parseIssue(final Document dom) {
        return Integer.parseInt(dom.select(ISSUE_CSS_SELECTOR)
                .text()
                .substring(ISSUE_TEXT_OFFSET));
    }

    private int parsePageBegin(final Document dom) {
        final String pageRange = dom.select(PAGE_CSS_SELECTOR).text();
        return Integer.parseInt(pageRange.substring(0, pageRange.indexOf("–")));
    }

    private int parsePageEnd(final Document dom) {
        final String pageRange = dom.select(PAGE_CSS_SELECTOR).text();
        return Integer.parseInt(pageRange.substring(pageRange.indexOf("–") + 1));
    }
}
