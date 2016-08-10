package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.follow.Link;
import com.weapes.ntpaprseng.crawler.util.Helper;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by lawrence on 16/8/9.
 */
public class AdvSearchedWebPageTest {
    @Test
    public void extractAll() throws Exception {
        final String url = "http://www.nature.com/search?article_type=research&journal=nature&order=date_desc";
        final String html = Helper.fetchWebPage(url);
        final AdvSearchedWebPage advSearchedWebPage = new AdvSearchedWebPage(html, url);
        final List<? extends Link> links = advSearchedWebPage.extractAll();

        for (Link link : links) {
            System.out.println(link.getUrl());
        }

        links.forEach(Assert::assertNotNull);
    }

}