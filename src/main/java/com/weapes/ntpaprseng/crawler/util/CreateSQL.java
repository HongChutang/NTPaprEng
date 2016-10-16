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
public class CreateSQL {
    // 第一次更新REF_DATA表的sql语句
    private static final String FIRST_REF_UPDATE_SQL =
            "UPDATE REF_DATA SET "+"UpdateTime = ?, "+"Page_views = ?, " + "Web_of_Science = ?, " +
                    "CrossRef = ?, " + "Scopus = ?, " + "News_outlets = ?, " +
                    "reddit = ?, " + "Blog = ?, " + "Tweets = ?, " + "Facebook = ?, " +
                    "Google = ?, " + "Pinterest = ?, " + "wikipedia = ?, " + "Mendeley = ?, " +
                    "CiteUlink = ?, " + "Zotero = ?, " + "F1000 = ?, " +
                    "Video = ?, " + "linkedin = ?, " + "Q_A = ? " + "WHERE URL = ";

    private static final String NT_PAPERS_INSERT_SQL =
            "INSERT INTO " + "NT_PAPERS(" +
                    "Title ," + "Authors, " + "SourceTitle, " + "ISSN, " +
                    "EISSN, " + "DOI, " + "Volum, " + "Issue, " + "PageBegin, " +
                    "PageEnd, " + "URL, " + "Affiliation, " + "CrawlTime, " +
                    "PublishTime) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // 第一次向REF_DATA表插入数据的sql语句
    private static final  String REF_INSERT_SQL =
            "INSERT INTO REF_DATA(URL, UpdateTime,Page_views, Web_of_Science, CrossRef, Scopus, News_outlets, " +
                    "reddit, Blog, Tweets, Facebook, Google, Pinterest, wikipedia, Mendeley, CiteUlink, Zotero, F1000, Video, " +
                    "linkedin, Q_A)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static String UPDATE_TIME_SQL="SELECT time FROM TIME";

    private static String CHANGE_UPDATE_TIME_SQL="INSERT INTO TIME(time,date)"+"VALUES(?, ?)";
    private static String TIME=getUpdateTime();//从数据库中获取的第几次爬取的值
    public static  String getRefUpdateSQL(){
            return FIRST_REF_UPDATE_SQL;
    }

    public static String getNtPapersInsertSQL(){
        return NT_PAPERS_INSERT_SQL;
    }

    public static String getRefInsertSQL(){
            return REF_INSERT_SQL;
    }
    public static String getUpdateTimeSQL(){
        return UPDATE_TIME_SQL;
    }
    private static String getChangeUpdateTime(){
        return  String.valueOf(Integer.valueOf(TIME)+1);
    }
    private static String getUpdateTime(){
        try {
            String time="";
            final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
            final Connection connection = mysqlDataSource.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(getUpdateTimeSQL());
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
    public static void executeAllSQL(){
        executeChangeUpdateTimeSQL();//更新数据库的爬取次数
        TIME=String.valueOf(Integer.valueOf(TIME)+1);

        System.out.println("executeAllSQL成功");
    }
}
