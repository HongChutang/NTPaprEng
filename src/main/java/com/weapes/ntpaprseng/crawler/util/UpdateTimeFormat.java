package com.weapes.ntpaprseng.crawler.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 不一样的天空 on 2016/11/4.
 */
public class UpdateTimeFormat {
    private  final String UPDATE_TIME_FORMAT =
            "yyyy年MM月dd日";
    private final String updateTime;
    private int year;
    private int month=0;
    private int day=0;
    public UpdateTimeFormat(String updateTime){
        this.updateTime=updateTime;
        int indexOfYear=updateTime.indexOf("年");
        int indexOfMonth=updateTime.indexOf("月");
        int indexOfDay=updateTime.indexOf("日");
        this.year=Integer.parseInt(updateTime.substring(0,indexOfYear));
        this.month=Integer.parseInt(updateTime.substring(indexOfYear+1,indexOfMonth));
        this.day=Integer.parseInt(updateTime.substring(indexOfMonth+1,indexOfDay));
    }
    public int getYear(){
      return year;
    }
    public int getMonth(){
        return month;
    }
    public int getDay(){
        return day;
    }
}
