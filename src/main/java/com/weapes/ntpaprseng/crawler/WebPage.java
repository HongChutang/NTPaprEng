package com.weapes.ntpaprseng.crawler;

/**
 * Created by lawrence on 16/8/7.
 */
public class WebPage {

    private String text;

    public WebPage(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

}
