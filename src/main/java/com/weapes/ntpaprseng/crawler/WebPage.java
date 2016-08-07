package com.weapes.ntpaprseng.crawler;

/**
 * Created by lawrence on 16/8/7.
 */
public class WebPage {

    private String text;

    public WebPage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
