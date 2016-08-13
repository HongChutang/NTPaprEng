package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.follow.Followable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.weapes.ntpaprseng.crawler.util.Helper.loadSeeds;

/**
 * Created by lawrence on 16/8/7.
 */
class BaseCrawlerImp implements Crawler {

    //生产者消费者线程数,可以根据环境进行调整
    private static final int CREATOR_THREAD_NUM = 1;
    private static final int CONSUMER_THREAD_NUM = 1;

    /*
     * 生产者负责把Followable解析为Storable,
     * 消费者负责把Storable存储。
     */
    private static final ExecutorService CREATOR =
            Executors.newScheduledThreadPool(CREATOR_THREAD_NUM);
    private static final ExecutorService CONSUMER =
            Executors.newScheduledThreadPool(CONSUMER_THREAD_NUM);

    @Override
    public void crawl() throws IOException, InterruptedException {

        // 种子解析为followable
        List<? extends Followable> seeds = loadSeeds();

        // 对每个种子,交给生产者处理为Storable。
        seeds.forEach(seed ->
                CREATOR.submit(new StorableFetcher(CREATOR, CONSUMER, seed))); //调整参数可调整线程策略

        // 1天内爬取失败者自动终止,可调整
        try {
            CREATOR.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
