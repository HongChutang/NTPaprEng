package com.weapes.ntpaprseng.crawler.follow;

import com.weapes.ntpaprseng.crawler.extract.Extractable;
import com.weapes.ntpaprseng.crawler.extract.NatureMetricsWebPage;
import com.weapes.ntpaprseng.crawler.util.Helper;

import java.io.IOException;

/**
 * Created by lawrence on 16/8/17.
 */
public class PaperMetricsLink extends Link {
    public PaperMetricsLink(String url) {
        super(url);
    }

    @Override
    public Extractable follow() throws IOException {
        final String html = Helper.fetchWebPage(getMetricsUrl());
        return new NatureMetricsWebPage(html, getMetricsUrl());
    }

    private String getMetricsUrl() {
        return getUrl().substring(0, getUrl().length() - 1 - 5)
                + "/metrics";
    }
}
