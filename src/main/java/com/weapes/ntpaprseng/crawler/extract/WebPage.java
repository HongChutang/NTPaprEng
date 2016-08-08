package com.weapes.ntpaprseng.crawler.extract;

/**
 * Created by lawrence on 16/8/7.
 */
public abstract class WebPage implements Extractable {

    private final String text;
    private final String url;
    private final boolean isMulti;

    WebPage(final String text,
            final String url,
            final boolean isMulti) {
        this.text = text;
        this.url = url;
        this.isMulti = isMulti;
    }

    WebPage(final String text, final String url) {
        this(text, url, false);
    }

    String getText() {
        return text;
    }

    String getUrl() {
        return url;
    }

    public boolean isMulti() {
        return isMulti;
    }

}
