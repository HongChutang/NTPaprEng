package com.weapes.ntpaprseng.crawler.util;

import com.weapes.ntpaprseng.crawler.store.DataSource;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by  不一样的天空  on 2016/8/30.
 */
public class SQLHelper {
    // 通过URL来更新REF_DATA表
    private static final String REF_UPDATE_SQL =
            "UPDATE REF_DATA SET "+" UpdateTime = ?, "+"PageViews = ?, " + "WebOfScience = ?, " +
                    "CrossRef = ?, " + "Scopus = ?, " + "NewsOutlets = ?, " +
                    "Reddit = ?, " + "Blog = ?, " + "Tweets = ?, " + "Facebook = ?, " +
                    "Google = ?, " + "Pinterest = ?, " + "wikipedia = ?, " + "Mendeley = ?, " +
                    "CiteUlink = ?, " + "Zotero = ?, " + "F1000 = ?, " + "Video = ?, " +
                    "Linkedin = ?, " + "Q_A = ?," + "FinalIndex = ? "+ "WHERE URL = ";

    private static final String NT_PAPERS_INSERT_SQL =
            "INSERT INTO " + "NT_PAPERS(" +
                    "Title ," + "Authors, " + "SourceTitle, " + "ISSN, " +
                    "EISSN, " + "DOI, " + "Volume, " + "Issue, " + "PageBegin, " +
                    "PageEnd, " + "URL, " + "Affiliation, " + "CrawlTime, " +
                    "PublishTime) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // 更新NT_PAPERS的同时向REF_DATA添加URL和UpdateTime这两个字段
    private static final  String REF_INSERT_SQL =
            "INSERT INTO REF_DATA(URL, UpdateTime,PageViews, WebOfScience, CrossRef, Scopus, NewsOutlets, " +
                    "Reddit, Blog, Tweets, Facebook, Google, Pinterest, Wikipedia, Mendeley, CiteUlink, Zotero, F1000, Video, " +
                    "Linkedin, Q_A, FinalIndex)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static String UPDATE_TIME_SQL="SELECT times FROM Time";

    private static String CHANGE_UPDATE_TIME_SQL="INSERT INTO Time(times,date)"+"VALUES(?, ?)";
    private static String TIME=getUpdateTime();//从数据库中获取的第几次爬取的值
    public static  String getRefUpdateSQL(){
            return REF_UPDATE_SQL;
    }
    public static String getNtPapersInsertSQL(){
        return NT_PAPERS_INSERT_SQL;
    }

    public static String getRefInsertSQL(){
            return REF_INSERT_SQL;
    }

    private static String getChangeUpdateTime(){
        return  String.valueOf(Integer.valueOf(TIME)+1);
    }
    private static String getUpdateTime(){
        try {
            String time="";
            final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
            final Connection connection = mysqlDataSource.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TIME_SQL);
            ResultSet resultSet=preparedStatement.executeQuery();
             while (resultSet.next()){
                 time=resultSet.getString(1);
             }
            System.out.println("当前是第"+time+"次爬取");
            return time;
        } catch (SQLException e) {
            e.printStackTrace();
            return "2";
        }
    }

    public static boolean executeChangeUpdateTimeSQL(){
        try {
            final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
            final Connection connection = mysqlDataSource.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_UPDATE_TIME_SQL);
            preparedStatement.setString(1,getChangeUpdateTime());
            preparedStatement.setString(2,Helper.getCrawlTime());
            boolean isSuccessful=preparedStatement.executeUpdate()!=0;
           if (isSuccessful){
                System.out.println("成功更新爬取次数");
           }
            return isSuccessful;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("更新爬取次数失败");
            return false;
        }

    }
}
