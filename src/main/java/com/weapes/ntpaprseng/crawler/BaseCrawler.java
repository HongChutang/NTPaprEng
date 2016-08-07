package com.weapes.ntpaprseng.crawler;

/**
 * Created by lawrence on 16/8/7.
 */

public interface BaseCrawler extends Crawler {

    public ExtractedObject extract(WebPage webPage);

    public boolean store(ExtractedObject extractedObject);

}
