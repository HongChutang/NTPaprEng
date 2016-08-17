package com.weapes.ntpaprseng.crawler.extract;

import java.util.List;

/**
 * Created by lawrence on 16/8/17.
 */
public class NatureMetricsWebPage extends WebPage {
    public NatureMetricsWebPage(String html, String metricsUrl) {
        super(html, metricsUrl);
    }

    @Override
    // TODO 抽取Metrics信息
    public ExtractedObject extract() {

        return null;
    }

    @Override
    public List<? extends ExtractedObject> extractAll() {
        return null;
    }
}
