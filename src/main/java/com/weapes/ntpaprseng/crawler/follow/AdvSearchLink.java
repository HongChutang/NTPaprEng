package com.weapes.ntpaprseng.crawler.follow;

import com.weapes.ntpaprseng.crawler.extract.AdvSearchedWebPage;
import com.weapes.ntpaprseng.crawler.util.Helper;

import java.io.IOException;

/**
 * Created by lawrence on 16/8/8.
 */
public class AdvSearchLink extends Link {
    public AdvSearchLink(final String urlParsed) {
        super(urlParsed);
    }

    @Override
    public AdvSearchedWebPage follow() throws IOException {
        final String webPage = Helper.fetchWebPage(getUrl());
        System.out.println("i download the advsearched");
        return new AdvSearchedWebPage(webPage, getUrl());
    }
}
