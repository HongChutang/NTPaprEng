package com.weapes.ntpaprseng.dependencies;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lawrence on 16/8/8.
 */
public class HikariTest {

    @Test
    @Ignore
    public void testAccessibility() throws SQLException {
        final HikariConfig mysql =
                new HikariConfig("E:\\javaproject\\git_project\\NTPaprSEng\\conf\\hikari.properties");
        final HikariDataSource hikariDataSource =
                new HikariDataSource(mysql);
        final Connection connection =
                hikariDataSource.getConnection();
        final PreparedStatement preparedStatement =
                connection.prepareStatement("SHOW TABLES;");
        final ResultSet resultSet =
                preparedStatement.executeQuery();
        Assert.assertNotNull(resultSet);
    }
}
