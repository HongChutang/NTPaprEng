package com.weapes.ntpaprseng.crawler.log;

import com.weapes.ntpaprseng.crawler.util.Helper;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Programmer on 2016/8/14.
 */
public class Log {
    //日志变量
    public static final Logger LOGGER = LoggerFactory.getLogger(Log.class);
    //单次爬取论文总量
    public static AtomicInteger URL_NUMBERS = new AtomicInteger();
    //目前正在爬取的第几篇论文
    public static AtomicInteger CRAWING_NUMBERS = new AtomicInteger();
    //单次爬取成功数量
    public static AtomicInteger CRAWING_SUCCEED_NUMBERS = new AtomicInteger();
    public static String LAST_LINK;

    public Log(){
        PropertyConfigurator.configure(Helper.getCfg().getString("log4j"));
    }
}
