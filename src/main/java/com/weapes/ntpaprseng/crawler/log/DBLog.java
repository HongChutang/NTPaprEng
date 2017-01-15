package com.weapes.ntpaprseng.crawler.log;

import com.weapes.ntpaprseng.crawler.store.DataSource;
import com.weapes.ntpaprseng.crawler.store.Paper;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.weapes.ntpaprseng.crawler.util.Helper.getLogger;

/**
 * Created by 不一样的天空 on 2016/11/23.
 */
public class DBLog {
    private static final Logger LOGGER =
            getLogger(Paper.class);
    private static final String CRAWL_DETAIL_LOG =
            "INSERT INTO CrawlDetailLog(" +
                    "URL," + "ArticlePosition," + "TotalNumber," +"IsSuccessful,"+"CrawlTime)" +
                    "VALUES(?, ?, ?, ?, ?)";
    private static final String CRAWL_LOG =
            "INSERT INTO CrawlLog(" +
                    "CrawlTime,"+"SuccessfulNumber,"+"FailedNumber,"+ "TotalNumber,"+ "AverageTime)"+
                  "VALUES(?, ?, ?, ?,?)";
    private static final String UPDATE_DETAIL_LOG =
            "INSERT INTO UpdateDetailLog(" + "URL," + "ArticlePosition," + "TotalNumber," +"IsSuccessful,"+
                    "UpdateTime)" + "VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_LOG =
            "INSERT INTO UpdateLog(" +
                    "CrawlTime,"+"SuccessfulNumber,"+"FailedNumber"+ "TotalNumber)"+
                    "VALUES(?, ?, ?, ?)";
    private static final String CRAWL_AVERAGE_TIME_LOG="UPDATE CrawlLog SET " +
            "AverageTime = ?"+ " WHERE TotalNumber = ";
    private static final String UPDATE_AVERAGE_TIME_LOG="UPDATE UpdateLog SET " +
            "AverageTime = ?"+ " WHERE TotalNumber = ";

    public static void saveCrawlDetailLog(String url, int currentPosition, int totalNumber, boolean isSuccessful,String crawlTime) {
        final HikariDataSource mysqlDataSource =
                DataSource.getMysqlDataSource();
        // 从DB连接池得到连接
        try (final Connection connection = mysqlDataSource.getConnection()) {
            try {
                final PreparedStatement preparedStatement =
                        connection.prepareStatement(CRAWL_DETAIL_LOG);
                preparedStatement.setString(1, url);
                preparedStatement.setInt(2, currentPosition);
                preparedStatement.setInt(3, totalNumber);
                preparedStatement.setBoolean(4, isSuccessful);
                preparedStatement.setString(5, crawlTime);
                if (preparedStatement.executeUpdate() != 0) {
                    LOGGER.info("爬取过程的具体日志保存成功");
                } else {
                    LOGGER.info("爬取过程的具体日志保存失败");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveFinalCrawlLog(String crawlTime,int successfulNumber,int failedNumber, int totalNumber,String average) {
        final HikariDataSource mysqlDataSource =
                DataSource.getMysqlDataSource();
        // 从DB连接池得到连接
        try (final Connection connection = mysqlDataSource.getConnection()) {
            try {
                final PreparedStatement preparedStatement =
                        connection.prepareStatement(CRAWL_LOG);
                preparedStatement.setString(1, crawlTime);
                preparedStatement.setInt(2, successfulNumber);
                preparedStatement.setInt(3, failedNumber);
                preparedStatement.setInt(4, totalNumber);
                preparedStatement.setString(5, average);
                if (preparedStatement.executeUpdate() != 0) {
                    LOGGER.info("爬取过程的总体情况日志保存成功");
                } else {
                    LOGGER.info("爬取过程的总体情况日志保存失败");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveUpdateDetailLog(String url, int currentPosition, int totalNumber, boolean isSuccessful,String updateTime) {
        final HikariDataSource mysqlDataSource =
                DataSource.getMysqlDataSource();
        // 从DB连接池得到连接
        try (final Connection connection = mysqlDataSource.getConnection()) {
            try {
                final PreparedStatement preparedStatement =
                        connection.prepareStatement(UPDATE_DETAIL_LOG);
                preparedStatement.setString(1, url);
                preparedStatement.setInt(2, currentPosition);
                preparedStatement.setInt(3, totalNumber);
                preparedStatement.setBoolean(4, isSuccessful);
                preparedStatement.setString(5, updateTime);
                if (preparedStatement.executeUpdate() != 0) {
                    LOGGER.info("更新过程的具体日志保存成功");
                } else {
                    LOGGER.info("更新过程的具体日志保存失败");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveFinalUpdateLog(String updateTime,int successfulNumber,int failedNumber, int totalNumber) {
        final HikariDataSource mysqlDataSource =
                DataSource.getMysqlDataSource();
        // 从DB连接池得到连接
        try (final Connection connection = mysqlDataSource.getConnection()) {
            try {
                final PreparedStatement preparedStatement =
                        connection.prepareStatement(UPDATE_LOG);
                preparedStatement.setString(1, updateTime);
                preparedStatement.setInt(2, successfulNumber);
                preparedStatement.setInt(3, failedNumber);
                preparedStatement.setInt(4, totalNumber);
                if (preparedStatement.executeUpdate() != 0) {
                    LOGGER.info("更新过程的总体情况日志保存成功");
                } else {
                    LOGGER.info("更新过程的总体情况日志保存失败");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
