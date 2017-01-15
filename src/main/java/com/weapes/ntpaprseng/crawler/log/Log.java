package com.weapes.ntpaprseng.crawler.log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Programmer on 2016/8/14.
 */
public class Log {
    //单次爬取论文总量
    private static AtomicInteger URL_NUMBERS = new AtomicInteger();
    //目前正在爬取的第几篇论文
    private static final AtomicInteger CRAWLING_NUMBERS = new AtomicInteger();
    //单次当前爬取成功数量
    private static final AtomicInteger CRAWLING_SUCCEED_NUMBERS = new AtomicInteger();
    //单次当前爬取失败数量
    private static final AtomicInteger CRAWLING_FAILED_NUMBER = new AtomicInteger();

    //单次更新指标论文总量
    private static AtomicInteger UPDATE_TOTAL_NUMBERS = new AtomicInteger();
    //目前正在更新的第几篇论文
    private static final AtomicInteger  CURRENT_UPDATE_NUMBERS= new AtomicInteger();
    //单次当前更新成功数量
    private static final AtomicInteger UPDATE_SUCCEED_NUMBERS = new AtomicInteger();
    //单次当前更新失败数量
    private static final AtomicInteger UPDATE_FAILED_NUMBERS = new AtomicInteger();

    private static String LAST_LINK;

    public static String getLastLink() {
        return LAST_LINK;
    }

    public static void setLastLink(String lastLink) {
        LAST_LINK = lastLink;
    }

    public static AtomicInteger getUrlNumbers() {
        return URL_NUMBERS;
    }

    public static AtomicInteger getCrawlingNumbers() {
        return CRAWLING_NUMBERS;
    }

    public static AtomicInteger getCrawlingSucceedNumbers() {
        return CRAWLING_SUCCEED_NUMBERS;
    }

    public static AtomicInteger getCrawlingFailedNumber() {
        return CRAWLING_FAILED_NUMBER;
    }

    public static AtomicInteger getUpdateTotalNumbers() {
        return UPDATE_TOTAL_NUMBERS;
    }

    public static AtomicInteger getCurrentUpdateNumbers() {
        return CURRENT_UPDATE_NUMBERS;
    }

    public static AtomicInteger getUpdateSucceedNumbers() {
        return UPDATE_SUCCEED_NUMBERS;
    }
    public static AtomicInteger getUpdateFailedNumbers() {
        return UPDATE_FAILED_NUMBERS;
    }

}
