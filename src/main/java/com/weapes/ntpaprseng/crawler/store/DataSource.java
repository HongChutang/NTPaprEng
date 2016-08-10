package com.weapes.ntpaprseng.crawler.store;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Created by lawrence on 16/8/9.
 */
public final class DataSource {
    private static final HikariConfig mysql =
            new HikariConfig("/Users/lawrence/Documents/practice/Java/NTPaprSEng/conf/hikari.properties");

    private static final HikariDataSource mysqlDataSource =
            new HikariDataSource(mysql);

    private DataSource() {
    }

    static HikariDataSource getMysqlDataSource() {
        return mysqlDataSource;
    }
}
