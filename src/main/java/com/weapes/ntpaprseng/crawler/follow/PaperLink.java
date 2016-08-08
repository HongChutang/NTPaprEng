package com.weapes.ntpaprseng.crawler.follow;

import com.weapes.ntpaprseng.crawler.extract.Extractable;
import com.weapes.ntpaprseng.crawler.extract.PaperWebPage;
import com.weapes.ntpaprseng.crawler.util.Helper;

import java.io.IOException;

/**
 * Created by lawrence on 16/8/8.
 */
public class PaperLink extends Link {

    public PaperLink(final String url) {
        super(url);
    }

    @Override
    public Extractable follow() throws IOException {
        final String paperWebPage = Helper.fetchWebPage(getUrl());
        return new PaperWebPage(paperWebPage, getUrl());
    }
}
