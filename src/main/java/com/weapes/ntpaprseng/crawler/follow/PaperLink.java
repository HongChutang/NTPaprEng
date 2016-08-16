package com.weapes.ntpaprseng.crawler.follow;

import com.weapes.ntpaprseng.crawler.extract.Extractable;
import com.weapes.ntpaprseng.crawler.extract.PaperWebPage;
import com.weapes.ntpaprseng.crawler.log.Log;
import com.weapes.ntpaprseng.crawler.util.Helper;

import java.io.IOException;

/**
 * 论文链接
 * Created by lawrence on 16/8/8.
 */
public class PaperLink extends Link {

    public PaperLink(final String url) {
        super(url);
    }

    @Override
    public Extractable follow() throws IOException {

        Log.LOGGER.info("本次爬取论文"+Log.URL_NUMBERS.get()+"篇，"
                       +"正在爬取第"+Log.CRAWING_NUMBERS.incrementAndGet()+"篇\n"
                       +"链接为："+getUrl());

        if (Log.URL_NUMBERS.get() == Log.CRAWING_NUMBERS.get()) {
            Log.LAST_LINK = getUrl();
        }

        final String paperWebPage = Helper.fetchWebPage(getUrl());
        return new PaperWebPage(paperWebPage, getUrl());
    }
}
