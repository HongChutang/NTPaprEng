package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.store.Paper;
import com.weapes.ntpaprseng.crawler.store.Storable;
import com.weapes.ntpaprseng.crawler.util.TimeUtil;
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

    // 论文信息抽取CSS选择器
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
    private static final String AFFILIATION_CSS_SELECTOR =
            "#a1 > h3";
    private static final String PUBLISHTIME_CSS_SELECTOR =
            "#content > article > header > dl.citation.dates > dd:nth-child(6) > time";


    // 有些抽取后的信息需要经过subString方法剪切,这些是所需偏移值。
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

        System.out.println("StoreObj parsing: type=PaperWebPage");

        final Document dom = Jsoup.parse(getText());

        return new Paper(
                getUrl(),
                parseAuthors(dom),
                parseTitle(dom),
                parseSourceLink(dom),
                parseISSN(dom),
                parseEISSN(dom),
                parseDOI(dom),
                parseVolum(dom),
                parseIssue(dom),
                parsePageBegin(dom),
                parsePageEnd(dom),
                parseAffiliation(dom),
                parsePublishTime(dom),
                TimeUtil.getCrawlTime()
        );
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

    private String parseISSN(final Document dom) {
        return dom
                .select(ISSN_CSS_SELECTOR)
                .text()
                .trim()
                .substring(ISSN_TEXT_OFFSET);
    }

    private String parseEISSN(final Document dom) {
        return dom
                .select(EISSN_CSS_SELECTOR)
                .text()
                .trim()
                .substring(EISSN_TEXT_OFFSET);
    }

    private String parseDOI(final Document dom) {
        return dom.select(DOI_CSS_SELECTOR)
                .text()
                .substring(DOI_TEXT_OFFSET);
    }

    private int parseVolum(final Document dom) {

        final Elements volum = dom.select(VOLUM_CSS_SELECTOR);

        try {
            return Integer.parseInt(volum.text().substring(VOLUM_TEXT_OFFSET));
        } catch (Exception e) {
            return 0;
        }
    }

    private int parseIssue(final Document dom) {
        final Elements issue = dom.select(ISSUE_CSS_SELECTOR);

        try {
            return Integer.parseInt(issue.text().substring(ISSUE_TEXT_OFFSET));
        } catch (Exception e) {
            return 0;
        }

    }

    // PageRange抽取后格式="begin-end",需要根据-位置进行再处理
    private int parsePageBegin(final Document dom) {
        final String pageRange = dom.select(PAGE_CSS_SELECTOR).text();
        try {
            return Integer.parseInt(pageRange.substring(0, pageRange.indexOf("–")));
        } catch (Exception e) {
            return 0;
        }
    }

    private int parsePageEnd(final Document dom) {
        final String pageRange = dom.select(PAGE_CSS_SELECTOR).text();
        try {
            return Integer.parseInt(pageRange.substring(pageRange.indexOf("–") + 1));
        } catch (Exception e) {
            return 0;
        }
    }

    private String parseAffiliation(final Document dom) {

        try {
            return dom
                    .select(AFFILIATION_CSS_SELECTOR)
                    .text();
        } catch (Exception e) {
            return null;
        }
    }

    private String parsePublishTime(final Document dom) {

        try {
            return dom
                    .select(PUBLISHTIME_CSS_SELECTOR)
                    .text();
        } catch (Exception e) {
            return null;
        }
    }
}
