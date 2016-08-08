package com.weapes.ntpaprseng.crawler.extract;

import com.alibaba.fastjson.JSON;
import com.weapes.ntpaprseng.crawler.util.Helper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by lawrence on 16/8/9.
 */
public class PaperWebPageTest {
    @Test
    public void extract() throws Exception {
        final String url = "http://www.nature.com/nature/journal/v517/n7532/full/nature14010.html";
        final String html = Helper.fetchWebPage(url);
        final PaperWebPage paperWebPage =
                new PaperWebPage(html, url);
        final String jsonString = JSON.toJSONString(paperWebPage.extract());
        Assert.assertNotNull(jsonString);
    }

}