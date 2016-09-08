package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.log.Log;
import com.weapes.ntpaprseng.crawler.util.Helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



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
        try {
            System.out.print("开始更新指标。系统时间： " + Helper.getCrawlTime() + "\n");
            System.out.print("本次待更新指标的论文总量为： " + Helper.getRefDataNum() + "\n");
            Log.getTotalUpdateNumbers().set(Helper.getRefDataNum());
            //更新指标时间间隔
            final int interval_day = Helper.getDetailCrawlerInterval();
            //启动后延迟时间为0，间隔为interval_day，时间单位为minute，调整参数可以改变线程执行策略
            Helper.loadMetricsLinks().forEach(paper ->
                    CREATOR.scheduleAtFixedRate(new StorableFetcher<>(CREATOR, CONSUMER, paper),
                            0, interval_day, TimeUnit.MINUTES));
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            CREATOR.awaitTermination(3, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
