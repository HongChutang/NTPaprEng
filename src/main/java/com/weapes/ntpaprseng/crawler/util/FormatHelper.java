package com.weapes.ntpaprseng.crawler.util;

/**
 * Created by 不一样的天空 on 2017/1/14.
 */
public class FormatHelper {

    class PublishTimeFormatter implements TimeFormatter{
        private String[] args=new String[3];
        public PublishTimeFormatter(String publishTime){
           args=publishTime.split(" ");
        }
        @Override
        public int getYear() {
            return Integer.parseInt(args[2]);
        }

        @Override
        public int getMonth() {
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

        @Override
        public int getDay() {
            return  Integer.parseInt(args[0]);
        }
    }
    class UpdateTimeFormatter implements TimeFormatter{
        private  int year=0;
        private  int month=0;
        private  int day=0;
        public UpdateTimeFormatter(String updateTime){
            int indexOfYear=updateTime.indexOf("年");
            int indexOfMonth=updateTime.indexOf("月");
            int indexOfDay=updateTime.indexOf("日");
            year=Integer.parseInt(updateTime.substring(0,indexOfYear));
            month=Integer.parseInt(updateTime.substring(indexOfYear+1,indexOfMonth));
            day=Integer.parseInt(updateTime.substring(indexOfMonth+1,indexOfDay));
        }
        @Override
        public int getYear() {
            return year;
        }

        @Override
        public int getMonth() {
            return month;
        }

        @Override
        public int getDay() {
            return day;
        }
    }
    public  TimeFormatter formatUpdateTime(String updateTime){
        return new UpdateTimeFormatter(updateTime);
    }
    public  TimeFormatter formatPublishTime(String publishTime){
        return new PublishTimeFormatter(publishTime);
    }
}
