package com.weapes.ntpaprseng.crawler.util;

/**
 * Created by 不一样的天空 on 2016/11/4.
 */
public class PublishTimeFormat {
    private final String[] args;
    public PublishTimeFormat(String publishTime){
        args=publishTime.split(" ");
    }
    public  int getYear(){
        return Integer.parseInt(args[2]);
    }
    public int getMonth(){
        int month=1;
        switch (args[1]){
            case "January": month=1;
                break;
            case "February": month=2;
                break;
            case "March": month=3;
                break;
            case "April": month=4;
                break;
            case "May": month=5;
                break;
            case "June": month=6;
                break;
            case "July": month=7;
                break;
            case "August": month=8;
                break;
            case "September": month=9;
                break;
            case "October": month=10;
                break;
            case "November": month=11;
                break;
            case "December": month=12;
                break;
        }
        return month;
    }
    public  int getDay(){
        return Integer.parseInt(args[0]);
    }
}
