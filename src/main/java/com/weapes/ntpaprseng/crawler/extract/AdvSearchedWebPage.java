package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.follow.AdvSearchedLink;
import com.weapes.ntpaprseng.crawler.follow.Link;
import com.weapes.ntpaprseng.crawler.follow.PaperLink;
import com.weapes.ntpaprseng.crawler.log.Log;
import com.weapes.ntpaprseng.crawler.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.weapes.ntpaprseng.crawler.util.Helper.isDesided;
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

        if (isFirstPage()) {
            allLinks.addAll(getSiblingLinks(dom));
        }

        // 得到目前页面论文链接
        final List<? extends Link> paperLinks =
                getPaperLinks(parsePaperLinks(dom));

        allLinks.addAll(paperLinks);

        return allLinks;
    }
    private final boolean isPaperLinksToBeCrawled(final String url) {
        // 利用Helper中的静态变量或数据库中上一次爬取的最后一条论文详细页面url（备用）
        // 来确定更新的数量，检查范围是本次爬取的所论文，当匹配到上次爬取的末位置就检查完毕
        String lastUrlForLastTime = Helper.lastUrlForLastTime;
        if (lastUrlForLastTime == null) {//使用备用方法
            lastUrlForLastTime = Helper.getLastUrlForLastTime();
        }
        if (url.equals(lastUrlForLastTime)) {// 匹配到
            isDesided = true; //检查完毕，flag置位
            return false;
        } else if (!isDesided) { // 未匹配到且没有检查完毕
            if (Helper.isFirstPaperLink) {
                Helper.lastUrlForLastTime = url;
                Helper.isFirstPaperLink = false;
            }
            Log.getUrlNumbers().addAndGet(1);
            return true;
        }
        return false;
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
                .filter(paper -> isURL(paper.getUrl())).filter(paper -> isPaperLinksToBeCrawled(paper.getUrl()))
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
