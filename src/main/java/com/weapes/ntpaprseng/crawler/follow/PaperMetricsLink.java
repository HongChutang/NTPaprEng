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
        int index1 = getUrl().indexOf("/full");
        String subString = getUrl().substring(0,index1);
        String subString1 = getUrl().substring(index1);
        int index2 = subString1.indexOf(".html");
        String subString2 = subString1.substring(5,index2);
        return subString + subString2 + "/metrics";
    }
}
