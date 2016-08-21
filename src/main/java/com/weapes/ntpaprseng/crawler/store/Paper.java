package com.weapes.ntpaprseng.crawler.store;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.weapes.ntpaprseng.crawler.log.Log.*;
import static com.weapes.ntpaprseng.crawler.util.Helper.getLogger;

/**
 * 论文Model
 * Created by lawrence on 16/8/9.
 */
public class Paper implements Storable {

    private static final Logger LOGGER =
            getLogger(Paper.class);

    private static final String INSERT_SQL =
            "INSERT INTO " +
                    "NT_PAPERS(" +
                    "Title ," +
                    "Authors, " +
                    "SourceTitle, " +
                    "ISSN, " +
                    "EISSN, " +
                    "DOI, " +
                    "Volum, " +
                    "Issue, " +
                    "PageBegin, " +
                    "PageEnd, " +
                    "URL, " +
                    "AFFILIATION, " +
                    "CRAWL_TIME, " +
                    "PUBLISH_TIME) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final List<String> authors;

    private final String url;

    private final String title;
    private final String sourceTitle;
    private final String ISSN;
    private final String eISSN;
    private final String DOI;

    private final int volum;
    private final int issue;
    private final int pageBegin;
    private final int pageEnd;

    private final String affiliation;
    private final String publishTime;
    private final String crawlTime;

    public Paper(final String url,
                 final List<String> authors,
                 final String title,
                 final String sourceTitle,
                 final String ISSN,
                 final String eISSN,
                 final String DOI,
                 final int volum,
                 final int issue,
                 final int pageBegin,
                 final int pageEnd,
                 final String affiliation,
                 final String publishTime,
                 final String crawlTime) {
        this.url = url;
        this.authors = authors;
        this.title = title;
        this.sourceTitle = sourceTitle;
        this.ISSN = ISSN;
        this.eISSN = eISSN;
        this.DOI = DOI;
        this.volum = volum;
        this.issue = issue;
        this.pageBegin = pageBegin;
        this.pageEnd = pageEnd;
        this.affiliation = affiliation;
        this.publishTime = publishTime;
        this.crawlTime = crawlTime;
    }

    private String getUrl() {
        return url;
    }

    private List<String> getAuthors() {
        return authors;
    }

    private String getTitle() {
        return title;
    }

    private String getSourceTitle() {
        return sourceTitle;
    }

    private String getISSN() {
        return ISSN;
    }

    private String geteISSN() {
        return eISSN;
    }

    private String getDOI() {
        return DOI;
    }

    private int getVolum() {
        return volum;
    }

    private int getIssue() {
        return issue;
    }

    private int getPageBegin() {
        return pageBegin;
    }

    private int getPageEnd() {
        return pageEnd;
    }

    private String getAffiliation() {
        return affiliation;
    }

    private String getPublishTime() {
        return publishTime;
    }

    private String getCrawlTime() {
        return crawlTime;
    }

    @Override
    public boolean store() {
        System.out.println("Store begin: type=Paper");

        final HikariDataSource mysqlDataSource =
                DataSource.getMysqlDataSource();

        // 从DB连接池得到连接
        try (final Connection connection = mysqlDataSource.getConnection()) {

            try (final PreparedStatement preparedStatement =
                         connection.prepareStatement(INSERT_SQL)) {
                bindSQL(preparedStatement);
                System.out.println("sql exeing");

                // 判断执行是否成功
                boolean succeed = preparedStatement.executeUpdate() != 0;

                if (succeed) {
                    LOGGER.info("第" + getCrawlingSucceedNumbers().incrementAndGet() + "篇爬取成功...\n"
                            + "链接为；" + getUrl());
                }

                if (getLastLink().equals(getUrl())) {
                    LOGGER.info("爬取完成，本次爬取论文总量：" + getUrlNumbers().get()
                            + " 成功数：" + getCrawlingSucceedNumbers().get()
                            + " 失败数：" + (getUrlNumbers().get() - getCrawlingSucceedNumbers().get()));
                }

                return succeed;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void bindSQL(final PreparedStatement preparedStatement)
            throws SQLException {
        // 填坑
        preparedStatement.setString(1, getTitle());
        preparedStatement.setString(2, String.join(",", getAuthors()));
        preparedStatement.setString(3, getSourceTitle());
        preparedStatement.setString(4, getISSN());
        preparedStatement.setString(5, geteISSN());
        preparedStatement.setString(6, getDOI());
        preparedStatement.setInt(7, getVolum());
        preparedStatement.setInt(8, getIssue());
        preparedStatement.setInt(9, getPageBegin());
        preparedStatement.setInt(10, getPageEnd());
        preparedStatement.setString(11, getUrl());
        preparedStatement.setString(12, getAffiliation());
        preparedStatement.setString(13, getCrawlTime());
        preparedStatement.setString(14, getPublishTime());
    }
}