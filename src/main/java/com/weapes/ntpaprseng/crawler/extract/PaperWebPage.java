package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.store.Storable;

import java.util.List;

/**
 * Created by lawrence on 16/8/8.
 */
public class PaperWebPage extends WebPage {

    public PaperWebPage(final String text, final String url) {
        super(text, url);
    }

    @Override
    public Storable extract() {
        return null;
    }

    @Override
    public List<ExtractedObject> extractAll() {
        return null;
    }
}
