package com.weapes.ntpaprseng.crawler.log;

import com.weapes.ntpaprseng.crawler.store.DataSource;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 不一样的天空 on 2016/9/8.
 */
public class SaveLog {

    private static final String INSERTLOG =
            "INSERT INTO LOG(" +
                    "URL," + "ArticlePosition," + "TotalNumber," + "CrawTime)" +
                    "VALUES(?, ?, ?, ?)";
    private static final String UPDATELOG =
            "UPDATE LOG SET " + "IsSuccessful = ?," + "SuccessfulNumber = ?," +
                    "FailedNumber = ?" + " WHERE URL = ";
    private static final String UPDATELOGTIME="UPDATE LOG SET " + "AverageTime = ?"+
            " WHERE TotalNumber = ";
    public static void executeInsertLogSQL(String url, int currentPosition, int totalNumber, String crawTime) {
        final HikariDataSource mysqlDataSource =
                DataSource.getMysqlDataSource();
        // 从DB连接池得到连接
        try (final Connection connection = mysqlDataSource.getConnection()) {
            try {
                 final PreparedStatement preparedStatement =
                        connection.prepareStatement(INSERTLOG);
                preparedStatement.setString(1, url);
                preparedStatement.setInt(2, currentPosition);
                preparedStatement.setInt(3, totalNumber);
                preparedStatement.setString(4, crawTime);
                boolean isSuccessful = preparedStatement.executeUpdate() != 0;
                if (isSuccessful) {
                    System.out.println("保存部分日志成功");
                } else {
                    System.out.println("保存部分日志失败");
                }

            } catch (SQLException e) {
            }
        } catch (SQLException e) {
        }

    }

    public static void executeUpdateLogSQL(int isSuccessful, int successfulNumber, int failedNumber, String url) {

        final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
        try(final Connection connection = mysqlDataSource.getConnection()){
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(UPDATELOG + "'" + url + "'");
            preparedStatement.setInt(1, isSuccessful);
            preparedStatement.setInt(2, successfulNumber);
            preparedStatement.setInt(3, failedNumber);
            boolean Successful = preparedStatement.executeUpdate() != 0;
            if (Successful) {
                System.out.println("更新部分日志成功");
            } else {
                System.out.println("更新部分日志失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void executeUpdateAverageTimeSQL(String averageTime,int totalNumber){
        final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
        try(final Connection connection = mysqlDataSource.getConnection()){
            try {
                final PreparedStatement preparedStatement = connection.prepareStatement(UPDATELOGTIME + totalNumber);
                preparedStatement.setString(1, averageTime);
                boolean Successful = preparedStatement.executeUpdate() != 0;
                if (Successful) {
                    System.out.println("更新日志平均时间成功");
                } else {
                    System.out.println("更新日志平均时间失败");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
