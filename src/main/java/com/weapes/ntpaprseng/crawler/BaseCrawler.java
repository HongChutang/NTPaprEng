package com.weapes.ntpaprseng.crawler;

/**
 * Created by lawrence on 16/8/7.
 */

public interface BaseCrawler extends Crawler {

    ExtractedObject extract(final WebPage webPage);

    boolean store(final ExtractedObject extractedObject);

}
