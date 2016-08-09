package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.follow.Followable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.weapes.ntpaprseng.crawler.util.Helper.load;

/**
 * Created by lawrence on 16/8/7.
 */
public class BaseCrawlerImp implements Crawler {

    private static final int CREATOR_THREAD_NUM = 4;
    private static final int CONSUMER_THREAD_NUM = 4;
    private static final String CONF_FILE_PATH =
            "/Users/lawrence/Documents/practice/Java/NTPaprSEng/conf/allPapersFetch.json";

    private static final ExecutorService CREATOR =
            Executors.newScheduledThreadPool(CREATOR_THREAD_NUM);
    private static final ExecutorService CONSUMER =
            Executors.newScheduledThreadPool(CONSUMER_THREAD_NUM);

    @Override
    public void crawl() throws IOException, InterruptedException {

        List<? extends Followable> seeds = load(CONF_FILE_PATH);

        seeds.forEach(seed ->
                CREATOR.submit(new StorableFetcher(CREATOR, CONSUMER, seed)));

        boolean b = false;
        try {
            b = CREATOR.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
