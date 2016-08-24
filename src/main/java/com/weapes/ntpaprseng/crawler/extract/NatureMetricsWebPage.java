package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.store.MetricsPaper;
import com.weapes.ntpaprseng.crawler.store.Storable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.management.snmp.jvmmib.EnumJvmRTBootClassPathSupport;

import java.util.*;

/**
 * Created by lawrence on 16/8/17.
 */
public class NatureMetricsWebPage extends WebPage {

    //存储抽取后的字段和数值
    private Map<String, Integer> hashMap= new HashMap<>();

    //选择器路径
    private static String TotalCitations = "#am-container > div.am-cols.cleared > div > div.am-module.citations-container " +
            "> div > section > div > ul > li";

    private static String OnlineAttention = "#altmetric-metrics > div.cleared > div.altmetric-key > ul > li.altmetric";

    private static String PageViews = "#am-container > div.am-module.pageview-metrics-container.page-views " +
            "> article > div > div.page-view-header > h2>span.total";

    public NatureMetricsWebPage(String html, String metricsUrl) {
        super(html, metricsUrl);
    }

    @Override
    // TODO 抽取Metrics信息
    public Storable extract() {
        Document doc = Jsoup.parse(getText());
        String number, referenceUnit;
        //抽取Total citations信息
        Elements citation= doc.select(TotalCitations);
        for (Element element : citation) {
            if (!element.text().contains("Data not available")) {
                number = element.select("a > div").text();
                referenceUnit = element.select("span > a").text();
                hashMap.put(referenceUnit, Integer.parseInt(number));
//                System.out.print(referenceUnit+": " + number + "!!!!!!!\n");
            }
        }

        //抽取Online attention信息
        Elements onlineAttention= doc.select(OnlineAttention);
        for (Element element : onlineAttention) {
            referenceUnit = null;
            referenceUnit = element.select("div").text();
            number = element.select("div > b").text();
            if (referenceUnit != null) {
                hashMap.put(referenceUnit, Integer.parseInt(number));
//                System.out.print(referenceUnit+": " + number + "!!!!!!!\n");
            }
        }

        final Set<Map.Entry<String,Integer>> entries = hashMap.entrySet();
        return new MetricsPaper(
                getUrl(),
                parsePageViews(doc),
                parseWebOfScience(entries),
                parseCrossRef(entries),
                parseScopus(entries),
                parseNewsOutlets(entries),
                parseReddit(entries),
                parseBlog(entries),
                parseTweet(entries),
                parseFacebook(entries),
                parseGoogle(entries),
                parsePinterest(entries),
                parseWikipedia(entries),
                parseMendeley(entries),
                parseCiteUlink(entries),
                parseZotero(entries),
                parseF1000(entries),
                parseVideo(entries),
                parseLinkedin(entries),
                parseQ_A(entries)
        );
    }

    private int parsePageViews(final Document doc){
        return Integer.parseInt(doc.select(PageViews).text().replace(",",""));
    }
    private int parseWebOfScience(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("web of science")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseCrossRef(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("crossref")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseScopus(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("scopus")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseNewsOutlets(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("news outlets")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseReddit(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("reddit")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseBlog(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("blogged")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseTweet(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("tweeted")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseFacebook(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("facebook")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseGoogle(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("google")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parsePinterest(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("pinterest")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseWikipedia(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("wikipedia")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseMendeley(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("mendeley")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseCiteUlink(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("citeulink")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseZotero(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("zetero")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseF1000(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("f1000")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseVideo(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("video")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseLinkedin(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("linkedin")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    private int parseQ_A(final Set<Map.Entry<String,Integer>> entries){
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            if (key.toLowerCase().contains("q&a")) {
                return entry.getValue();
            }
        }
        return 0;
    }
    @Override
    public List<? extends ExtractedObject> extractAll() {
        return null;
    }

}
