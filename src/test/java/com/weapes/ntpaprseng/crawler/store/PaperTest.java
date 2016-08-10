package com.weapes.ntpaprseng.crawler.store;

import com.weapes.ntpaprseng.crawler.extract.PaperWebPage;
import com.weapes.ntpaprseng.crawler.util.Helper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by lawrence on 16/8/9.
 */
public class PaperTest {
    @Test
    @Ignore
    public void store() throws Exception {
        final String url = "http://www.nature.com/nature/journal/vaap/ncurrent/full/nature19339.html";
        final String html = Helper.fetchWebPage(url);
        final PaperWebPage paperWebPage =
                new PaperWebPage(html, url);
        final boolean store =
                paperWebPage
                        .extract()
                        .store();
        Assert.assertTrue(store);
    }

}