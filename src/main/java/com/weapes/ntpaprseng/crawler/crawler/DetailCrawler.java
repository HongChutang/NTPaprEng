package com.weapes.ntpaprseng.crawler.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.weapes.ntpaprseng.crawler.util.Helper.loadMetricsLinks;
import static java.util.concurrent.TimeUnit.DAYS;

/**
 * Created by lawrence on 16/8/16.
 */
public class DetailCrawler implements Crawler {

    //生产者消费者线程数,可以根据环境进行调整
    private static final int CREATOR_THREAD_NUM = 1;
    private static final int CONSUMER_THREAD_NUM = 1;

    /*
     * 生产者负责把Followable解析为Storable,
     * 消费者负责把Storable存储。
     */
    private static final ScheduledExecutorService CREATOR =
            Executors.newScheduledThreadPool(CREATOR_THREAD_NUM);
    private static final ExecutorService CONSUMER =
            Executors.newScheduledThreadPool(CONSUMER_THREAD_NUM);


    @Override
    public void crawl() {
        loadMetricsLinks().forEach(paper ->
            CREATOR.submit(new StorableFetcher<>(CREATOR, CONSUMER, paper)));

        try {
            CREATOR.awaitTermination(1, DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
