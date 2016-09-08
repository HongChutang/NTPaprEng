package com.weapes.ntpaprseng.crawler.follow;

import com.weapes.ntpaprseng.crawler.extract.Extractable;
import com.weapes.ntpaprseng.crawler.extract.PaperWebPage;
import com.weapes.ntpaprseng.crawler.log.Log;
import com.weapes.ntpaprseng.crawler.log.SaveLog;
import com.weapes.ntpaprseng.crawler.util.Helper;
import org.slf4j.Logger;

import java.io.IOException;

import static com.weapes.ntpaprseng.crawler.log.Log.getCrawlingNumbers;
import static com.weapes.ntpaprseng.crawler.log.Log.getUrlNumbers;

/**
 * 论文链接
 * Created by lawrence on 16/8/8.
 */
public class PaperLink extends Link {

    private static final Logger LOGGER =
            Helper.getLogger(PaperLink.class);
    private static long startTime;

    public PaperLink(final String url) {
        super(url);
    }

    public static long getStartTime() {
        return startTime;
    }

    @Override
    public Extractable follow() throws IOException {

        LOGGER.info("本次爬取论文" + getUrlNumbers().get() + "篇，"
                + "正在爬取第" + Log.getCrawlingNumbers().incrementAndGet() + "篇\n"
                + "链接为：" + getUrl());
        if (getCrawlingNumbers().get()==1){
            startTime=System.currentTimeMillis();
        }
        SaveLog.executeInsertLogSQL(getUrl(),getCrawlingNumbers().get(),getUrlNumbers().get(),Helper.getCrawlTime());
        if (Log.getUrlNumbers().get() == Log.getCrawlingNumbers().get()) {
            Log.setLastLink(getUrl());
        }

        final String paperWebPage = Helper.fetchWebPage(getUrl());
        return new PaperWebPage(paperWebPage, getUrl());
    }
}
