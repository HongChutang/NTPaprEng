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
    private static String startTime;
    private static long startMillisecond;
    /*
     * 生产者负责把Followable解析为Storable,
     * 消费者负责把Storable存储。
     */
    private static final ExecutorService CREATOR =
            Executors.newFixedThreadPool(CREATOR_THREAD_NUM);
    private static final ExecutorService CONSUMER =
            Executors.newFixedThreadPool(CONSUMER_THREAD_NUM);

    @Override
    public void crawl() {
        startTime=Helper.getCrawlTime();
        startMillisecond=System.currentTimeMillis();
        System.out.print("开始更新指标。系统时间： " + startTime + "\n");
        System.out.print("本次待更新指标的论文总量为： " + Helper.getRefDataNum() + "\n");
        Log.getUpdateTotalNumbers().set(Helper.getRefDataNum());

        Helper.loadMetricsLinks().forEach(paper ->
                CREATOR.submit(new StorableFetcher<>(CREATOR, CONSUMER, paper)));
    }

    public static String getUpdateTime(){
        return startTime;
    }
    public static long getUpdateMillisecond(){return startMillisecond;}
}

