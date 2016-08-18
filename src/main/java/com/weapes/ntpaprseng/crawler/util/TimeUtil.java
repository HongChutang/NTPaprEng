package com.weapes.ntpaprseng.crawler.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 不一样的天空 on 2016/8/17.
 */
public class TimeUtil {
    public static String getCrawlTime() {

        final long seconds = System.currentTimeMillis();
        final Date date = new Date(seconds);
        final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return format.format(date);

    }
}
