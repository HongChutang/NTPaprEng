package com.weapes.ntpaprseng.crawler;

/**
 * Created by lawrence on 16/8/7.
 */
public class Link implements Followable {

    private String url;
    private boolean isFollow;

    public Link(String url, boolean isFollow) {
        this.url = url;
        this.isFollow = isFollow;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean isFollow() {
        return isFollow;
    }
}
