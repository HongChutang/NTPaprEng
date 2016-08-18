package com.weapes.ntpaprseng.crawler.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 不一样的天空 on 2016/8/17.
 */
public class TimeUtil {
        public static String getCrawlTime(){
            long seconds=System.currentTimeMillis();
            Date date=new Date(seconds);
            SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            String crawlTime=format.format(date);
            return crawlTime;
        }
}
