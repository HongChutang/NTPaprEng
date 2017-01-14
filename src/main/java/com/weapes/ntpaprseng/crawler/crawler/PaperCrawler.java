package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.util.Helper;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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
    private static final ScheduledExecutorService CREATOR =
            Executors.newScheduledThreadPool(CREATOR_THREAD_NUM);
    private static final ExecutorService CONSUMER =
            Executors.newScheduledThreadPool(CONSUMER_THREAD_NUM);

    private static final Logger LOGGER =
            Helper.getLogger(PaperCrawler.class);


    @Override
    public void crawl() {
        try {
            // 种子解析为followable
            // 对每个种子,交给生产者处理为Storable.

            System.out.print("爬虫运行。系统时间：" + Helper.getCrawlTime() + "\n");
            //定时爬取间隔
            final int interval_day = Helper.getPaperCrawlerInterval();
            //启动后延迟时间为0，间隔为interval_day，时间单位为minute，调整参数可以改变线程执行策略
            Helper.loadSeeds().forEach(seed ->
                    CREATOR.scheduleAtFixedRate(new StorableFetcher<>(CREATOR, CONSUMER, seed), 0, interval_day, TimeUnit.MINUTES ));

            LOGGER.info("种子分发完成...");

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            CREATOR.awaitTermination(3, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
