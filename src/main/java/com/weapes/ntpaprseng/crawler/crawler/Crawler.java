package com.weapes.ntpaprseng.crawler.crawler;

import java.io.IOException;

/**
 * Created by lawrence on 16/8/7.
 */
interface Crawler {
    void crawl() throws IOException, InterruptedException;
}
