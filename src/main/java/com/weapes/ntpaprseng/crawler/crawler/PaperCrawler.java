package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.util.Helper;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Created by lawrence on 16/8/7.
 */
class PaperCrawler implements Crawler {

    //生产者消费者线程数,可以根据环境进行调整
    private static final int CREATOR_THREAD_NUM = 1;
    private static final int CONSUMER_THREAD_NUM = 1;
    /*
     * 生产者负责把Followable解析为Storable,
     * 消费者负责把Storable存储。
     */
    private static final ExecutorService CREATOR =
            Executors.newFixedThreadPool(CREATOR_THREAD_NUM);
    private static final ExecutorService CONSUMER =
            Executors.newFixedThreadPool(CONSUMER_THREAD_NUM);

    private static final Logger LOGGER =
            Helper.getLogger(PaperCrawler.class);


    @Override
    public void crawl() {
        System.out.print("爬虫运行。系统时间：" + Helper.getCrawlTime() + "\n");
        // 种子解析为followable
        // 对每个种子,交给生产者处理为Storable

        Helper.loadSeeds().forEach(seed ->
                CREATOR.submit(new StorableFetcher<>(CREATOR, CONSUMER, seed)));

        LOGGER.info("种子分发完成...");

    }
}
