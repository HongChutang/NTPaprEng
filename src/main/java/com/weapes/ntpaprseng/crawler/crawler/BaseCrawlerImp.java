package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.follow.Followable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lawrence on 16/8/7.
 */
public class BaseCrawlerImp implements Crawler {

    private static final int CREATOR_THREAD_NUM = 4;
    private static final int CONSUMER_THREAD_NUM = 4;

    private static final ExecutorService CREATOR =
            Executors.newScheduledThreadPool(CREATOR_THREAD_NUM);
    private static final ExecutorService CONSUMER =
            Executors.newScheduledThreadPool(CONSUMER_THREAD_NUM);

    @Override
    public void crawl() throws IOException {

        List<? extends Followable> seeds = SeedLoader.load("");

        for (final Followable seed : seeds) {
            CREATOR.submit(new StorableFetcher(CREATOR, CONSUMER, seed));
        }

    }

}
