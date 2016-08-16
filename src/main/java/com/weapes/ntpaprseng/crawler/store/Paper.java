package com.weapes.ntpaprseng.crawler.store;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 论文Model
 * Created by lawrence on 16/8/9.
 */
public class Paper implements Storable {

    private List<String> authors;

    private String url;

    private String title;
    private String sourceTitle;
    private String ISSN;
    private String eISSN;
    private String DOI;

    private int volum;
    private int issue;
    private int pageBegin;
    private int pageEnd;

    public Paper(final  String url,
                 final List<String> authors,
                 final String title,
                 final String sourceTitle,
                 final String ISSN,
                 final String eISSN,
                 final String DOI,
                 final int volum,
                 final int issue,
                 final int pageBegin,
                 final int pageEnd) {
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
    }

    public String getUrl() {
        return url;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public String getISSN() {
        return ISSN;
    }

    public String geteISSN() {
        return eISSN;
    }

    public String getDOI() {
        return DOI;
    }

    public int getVolum() {
        return volum;
    }

    public int getIssue() {
        return issue;
    }

    public int getPageBegin() {
        return pageBegin;
    }

    public int getPageEnd() {
        return pageEnd;
    }

    @Override
    public boolean store() {

        System.out.println("Store begin: type=Paper");
        final HikariDataSource mysqlDataSource =
                DataSource.getMysqlDataSource();

        // 从DB连接池得到连接
        try (final Connection connection = mysqlDataSource.getConnection()) {

            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO NT_PAPERS(Title ,Authors, SourceTitle, ISSN, EISSN, DOI, Volum, Issue, PageBegin, PageEnd, URL) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

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
            preparedStatement.setString(11,getUrl());

            System.out.println("sql exeing");

            // 判断执行是否成功
            return preparedStatement.executeUpdate() != 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;


    }
}
