package com.weapes.ntpaprseng.crawler.follow;

import com.weapes.ntpaprseng.crawler.extract.AdvSearchedWebPage;
import com.weapes.ntpaprseng.crawler.util.Helper;

import java.io.IOException;

/**
 * 种子链接,高级搜索配置好后Submit创建的链接
 * Created by lawrence on 16/8/8.
 */
public class AdvSearchLink extends Link {
    public AdvSearchLink(final String urlParsed) {
        super(urlParsed);
    }

    @Override
    public AdvSearchedWebPage follow() throws IOException {
        final String webPage = Helper.fetchWebPage(getUrl());
        return new AdvSearchedWebPage(webPage, getUrl());
    }
}
