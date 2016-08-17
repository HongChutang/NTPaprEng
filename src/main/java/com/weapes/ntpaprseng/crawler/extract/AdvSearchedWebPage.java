package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.follow.AdvSearchedLink;
import com.weapes.ntpaprseng.crawler.follow.Link;
import com.weapes.ntpaprseng.crawler.follow.PaperLink;
import com.weapes.ntpaprseng.crawler.log.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.weapes.ntpaprseng.crawler.util.Helper.isURL;

/**
 * Created by lawrence on 16/8/8.
 */
public class AdvSearchedWebPage extends WebPage {

    // 每页论文数量
    private static final int NUM_OF_PAPERS_PER_PAGE = 25;

    // 抽取多个论文链接的CSS选择器
    private static final String PAPER_LINK_CSS_SELECTOR =
            "h2.h3.extra-tight-line-height a";

    // 抽取网页论文总数的CSS选择器
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

        // 所有链接集合
        final List<Link> allLinks = new ArrayList<>();


        // 得到目前页面论文链接
        final List<? extends Link> paperLinks =
                getPaperLinks(parsePaperLinks(dom));

        // 加入所有链接集合,如果与下面的if语句互换位置则为广度优先遍历。
        allLinks.addAll(paperLinks);

        if (isFirstPage()) {
            allLinks.addAll(getSiblingLinks(dom));
        }

        System.out.println("Links parsed: url=" + getUrl()
                + " linksSize=" + allLinks.size()
                + " type=" + "AdvSearched");

        return allLinks;
    }

    // 得到其他AdvSearched链接
    private List<Link> getSiblingLinks(final Document dom) {
        List<Link> siblingLinks = new ArrayList<>();
        for (int i = 2; i <= parsePageNum(dom); i++) {
            siblingLinks.add(new AdvSearchedLink(buildURLWithPageOrder(i)));
        }
        return siblingLinks;
    }

    // 得到论文链接
    private List<? extends Link> getPaperLinks(final Elements paperLinks) {
        return paperLinks.stream()
                .map(link -> new PaperLink(link.attr("href")))
                .filter(paper -> isURL(paper.getUrl()))
                .collect(Collectors.toList());

    }

    // 通过URL判断是否是第一个AdvSearched
    private boolean isFirstPage() {
        return !getUrl().contains("page");
    }

    // 得到AdvSearched链接总页数
    private int parsePageNum(final Document dom) {
        final int totalNum =
                Integer.parseInt(parseTotalNumSpan(dom).text().trim());

        //单次爬取论文总数量
        Log.getUrlNumbers().set(totalNum);

        return (totalNum % NUM_OF_PAPERS_PER_PAGE) == 0
                ? totalNum / NUM_OF_PAPERS_PER_PAGE
                : (totalNum / NUM_OF_PAPERS_PER_PAGE) + 1;

    }

    // 构建其他AdvSearched链接。
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
