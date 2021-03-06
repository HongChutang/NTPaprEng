package com.weapes.ntpaprseng.crawler.store;

import com.weapes.ntpaprseng.crawler.follow.PaperLink;
import com.weapes.ntpaprseng.crawler.log.DBLog;
import com.weapes.ntpaprseng.crawler.log.Log;
import com.weapes.ntpaprseng.crawler.search.ESClient;
import com.weapes.ntpaprseng.crawler.util.SQLHelper;
import com.weapes.ntpaprseng.crawler.util.FormatHelper;
import com.weapes.ntpaprseng.crawler.util.Helper;
import com.weapes.ntpaprseng.crawler.util.TimeFormatter;
import com.zaxxer.hikari.HikariDataSource;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.weapes.ntpaprseng.crawler.log.Log.*;
import static com.weapes.ntpaprseng.crawler.util.Helper.*;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * 论文Model
 * Created by lawrence on 16/8/9.
 * 存储论文信息到第一张表，并存储论文相关指标页面url到第二张表
 */
public class Paper implements Storable {

    private static final Logger LOGGER =
            getLogger(Paper.class);

    private static final String NT_PAPERS_INSERT_SQL = SQLHelper.getNtPapersInsertSQL();


    final String REF_INSERT_SQL = SQLHelper.getRefInsertSQL();


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
            boolean isSucceed = true;

            try {
                final PreparedStatement paperPreparedStatement =
                        connection.prepareStatement(NT_PAPERS_INSERT_SQL);
                bindPaperSQL(paperPreparedStatement);
                System.out.println("sql exeing");
                // 判断爬取论文信息操作是否成功
                isSucceed = paperPreparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                System.out.print(e.getSQLState() + "  " + e.getMessage());
                isSucceed = false;
            }
            boolean isSuccess = putNTPaperIntoES();//保存论文信息到ES中
            if (!isSuccess) {
                System.out.println("保存论文信息到ElasticSearch中的NT_PAPER失败");
            }
            try {
                final PreparedStatement refPreparedStatement =
                        connection.prepareStatement(REF_INSERT_SQL);
                bindRefSQL(refPreparedStatement);
                System.out.println("store metrix url to  REF_DATA");
                refPreparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
          if (isSucceed) {
                LOGGER.info("当前共有" + getCrawlingSucceedNumbers().incrementAndGet() + "篇爬取成功...\n"
                        + "链接为；" + getUrl());
            } else {
                LOGGER.info("当前共有" + getCrawlingFailedNumber().incrementAndGet() + "篇爬取失败...\n"
                        + "链接为；" + getUrl());
            }
            DBLog.saveCrawlDetailLog(getUrl(), Log.getCrawlingNumbers().get(),Log.getUrlNumbers().get(),isSucceed,Helper.getCrawlTime());
            if (getLastLink().equals(getUrl())) {
                LOGGER.info("爬取完成，本次爬取论文总量：" + getUrlNumbers().get()
                        + " 成功数：" + getCrawlingSucceedNumbers().get()
                        + " 失败数：" + getCrawlingFailedNumber().get());
                long startTime = PaperLink.getStartMillisecond();//开始爬取的时间
                long endTime = System.currentTimeMillis();//结束爬取的时间
                long total = endTime - startTime;
                String averageTime = Helper.getSeconds(total / getUrlNumbers().get());
                //保存爬取完成的总体情况数据到数据库中
                DBLog.saveFinalCrawlLog(PaperLink.getStartTime(),getCrawlingSucceedNumbers().get(),
                        getCrawlingFailedNumber().get(),getUrlNumbers().get(),averageTime);
            }
            //更新爬取检查状态参数
            Helper.isFirstCrawl = false;
            isDesided = false;
            isFirstPaperLink = true;
            return isSucceed;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void bindPaperSQL(final PreparedStatement preparedStatement)
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

    private boolean putNTPaperIntoES() {
        XContentBuilder json = null;
        FormatHelper formatHelper=new FormatHelper();
        TimeFormatter formatter=formatHelper.formatPublishTime(getPublishTime());
        try {
            json = jsonBuilder().startObject()
                    .field("URL", getUrl())
                    .field("Title", getTitle())
                    .field("Authors", getAuthors())
                    .field("SourceTitle", getSourceTitle())
                    .field("ISSN", geteISSN())
                    .field("EISSN", geteISSN())
                    .field("DOI", getDOI())
                    .field("Volum", getVolum())
                    .field("Issue", getIssue())
                    .field("PageBegin", getPageBegin())
                    .field("PageEnd", getPageEnd())
                    .field("Affiliation", getAffiliation())
                    .field("CrawlTime", getCrawlTime())
                    .field("Year", formatter.getYear())
                    .field("Month", formatter.getMonth())
                    .field("Day", formatter.getDay())
                    .field("PublishTime", getPublishTime()).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ESClient.getInstance().putPaperIntoES(getUrl(), json);
    }

    public void bindRefSQL(final PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, getMetricsUrl());
        preparedStatement.setString(2, Helper.getUpdateTime());
        preparedStatement.setInt(3, 0);
        preparedStatement.setInt(4, 0);
        preparedStatement.setInt(5, 0);
        preparedStatement.setInt(6, 0);
        preparedStatement.setInt(7, 0);
        preparedStatement.setInt(8, 0);
        preparedStatement.setInt(9, 0);
        preparedStatement.setInt(10, 0);
        preparedStatement.setInt(11, 0);
        preparedStatement.setInt(12, 0);
        preparedStatement.setInt(13, 0);
        preparedStatement.setInt(14, 0);
        preparedStatement.setInt(15, 0);
        preparedStatement.setInt(16, 0);
        preparedStatement.setInt(17, 0);
        preparedStatement.setInt(18, 0);
        preparedStatement.setInt(19, 0);
        preparedStatement.setInt(20, 0);
        preparedStatement.setInt(21, 0);
        preparedStatement.setInt(22, 0);
    }

    private boolean putRefDataIntoES() {
        XContentBuilder json = null;
        FormatHelper formatHelper=new FormatHelper();
        TimeFormatter formatter=formatHelper.formatUpdateTime(Helper.getUpdateTime());
        try {
            json = jsonBuilder().startObject()
                    .field("URL", getMetricsUrl())
                    .field("UpdateTime", Helper.getUpdateTime())
                    .field("Year", formatter.getYear())
                    .field("Month", formatter.getMonth())
                    .field("Day", formatter.getDay())
                    .field("Page_views", 0)
                    .field("Web_Of_Science", 0)
                    .field("CrossRef", 0)
                    .field("Scopus", 0)
                    .field("News_outlets", 0)
                    .field("Reddit", 0)
                    .field("Blog", 0)
                    .field("Tweets", 0)
                    .field("FaceBook", 0)
                    .field("Google", 0)
                    .field("Pinterest", 0)
                    .field("Wikipedia", 0)
                    .field("Mendeley", 0)
                    .field("CiteUlink", 0)
                    .field("Zotero", 0)
                    .field("F10000", 0)
                    .field("Video", 0)
                    .field("Linkedin", 0)
                    .field("Q_A", 0)
                    .field("FinalIndex", 0)
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ESClient.getInstance().putMetricsPaperIntoES(json);
    }

    // 将原来的论文详细页面url进行字符串处理，转化为metric相关指标页面url
    private String getMetricsUrl() {
        int index1 = getUrl().indexOf("/full");
        String subString = getUrl().substring(0, index1);
        String subString1 = getUrl().substring(index1);
        int index2 = subString1.indexOf(".html");
        String subString2 = subString1.substring(5, index2);
        return subString + subString2 + "/metrics";
    }
}