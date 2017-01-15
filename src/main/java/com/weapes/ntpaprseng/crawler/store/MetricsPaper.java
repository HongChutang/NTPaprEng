package com.weapes.ntpaprseng.crawler.store;

import com.weapes.ntpaprseng.crawler.crawler.DetailCrawler;
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

import static com.weapes.ntpaprseng.crawler.log.Log.*;
import static com.weapes.ntpaprseng.crawler.util.Helper.getLogger;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by 不一样的天空 on 2016/8/18.
 * 每次以主键指标页面URL为选择条件更新其它字段
 */
public class MetricsPaper implements Storable{

    private static final Logger LOGGER =
            getLogger(Paper.class);
    //更新REF_DATA表的sql语句
    private String REF_UPDATE_SQL = SQLHelper.getRefUpdateSQL();

    private final String url;
    private final int pageViews;
    private final int webOfScience;
    private final int crossRef;
    private final int scopus;
    private final int newsOutlets;
    private final int reddit;
    private final int blog;
    private final int tweets;
    private final int facebook;
    private final int google;
    private final int pinterest;
    private final int wikipedia;
    private final int mendeley;
    private final int citeUlink;
    private final int zotero;
    private final int f1000;
    private final int video;
    private final int linkedin;
    private final int q_a;
    public  MetricsPaper(final String url,
                         final int pageViews,
                         final int webOfScience,
                         final int crossRef,
                         final int scopus,
                         final int newsOutlets,
                         final int reddit,
                         final int blog,
                         final int tweets,
                         final int facebook,
                         final int google,
                         final int pinterest,
                         final int wikipedia,
                         final int mendeley,
                         final int citeUlink,
                         final int zotero,
                         final int f1000,
                         final int video,
                         final int linkedin,
                         final int q_a){
        this.url=url;
        this.pageViews=pageViews;
        this.webOfScience=webOfScience;
        this.crossRef=crossRef;
        this.scopus=scopus;
        this.newsOutlets=newsOutlets;
        this.reddit=reddit;
        this.blog=blog;
        this.tweets=tweets;
        this.facebook=facebook;
        this.google=google;
        this.pinterest=pinterest;
        this.wikipedia=wikipedia;
        this.mendeley=mendeley;
        this.citeUlink=citeUlink;
        this.zotero=zotero;
        this.f1000=f1000;
        this.video=video;
        this.linkedin=linkedin;
        this.q_a=q_a;
    }
    private String getUrl() {
        return url;
    }
    private int getPageViews() {
        return pageViews;
    }

    private int getWebOfScience() {
        return webOfScience;
    }

    private int getCrossRef() {
        return crossRef;
    }

    private int getScopus() {
        return scopus;
    }

    private int getNewsOutlets() {
        return newsOutlets;
    }

    private int getReddit() {
        return reddit;
    }

    private int getBlog() {
        return blog;
    }

    private int getTweets() {
        return tweets;
    }

    private int getFacebook() {
        return facebook;
    }

    private int getGoogle() {
        return google;
    }

    private int getPinterest() {
        return pinterest;
    }

    private int getWikipedia() {
        return wikipedia;
    }

    private int getMendeley() {
        return mendeley;
    }

    private int getCiteUlink() {
        return citeUlink;
    }

    private int getZotero() {
        return zotero;
    }

    private int getF1000() {
        return f1000;
    }

    private int getVideo() {
        return video;
    }

    private int getLinkedin() {
        return linkedin;
    }

    private int getQ_a() {
        return q_a;
    }
    private int getFinalIndex() {
        return 0;
    }

    @Override
    public boolean store() {
        System.out.println("Store begin: type = MetricsPaper.");
        LOGGER.info("本次更新论文" + Log.getUpdateTotalNumbers().get() + "篇，"
                + "正在更新第" + Log.getCurrentUpdateNumbers().incrementAndGet() + "篇\n"
                + "链接为：" + getUrl());
        final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
        // 加上选择条件 URL
        REF_UPDATE_SQL = REF_UPDATE_SQL + "'" + getUrl() + "'";
        try (final Connection connection = mysqlDataSource.getConnection()){
            try (final PreparedStatement preparedStatement = connection.prepareStatement(REF_UPDATE_SQL)) {
                bindUpdateSql(preparedStatement);
                // 判断执行是否成功
                boolean succeed = preparedStatement.executeUpdate() != 0;
                if (succeed) {
                    LOGGER.info("当前共有" + getUpdateSucceedNumbers().incrementAndGet() + "篇论文相关指标更新成功...\n"
                            + "链接为；" + getUrl());
                }else {
                    LOGGER.info("当前共有" + getUpdateFailedNumbers().incrementAndGet() + "篇论文相关指标更新失败...\n"
                            + "链接为；" + getUrl());
                }
                boolean isSuccess= updateRefDataIntoES();//更新论文指标到ElasticSearch中的REF_DATA
                if (!isSuccess){
                    LOGGER.info("更新论文指标到ElasticSearch中的REF_DATA失败");
                }else {
                    LOGGER.info("更新论文指标到ElasticSearch中的REF_DATA成功");
                }
                //保存更新的具体数据到数据库中
                DBLog.saveUpdateDetailLog(getUrl(),getCurrentUpdateNumbers().get(),getUpdateTotalNumbers().get(),succeed,
                        DetailCrawler.getUpdateTime());

                if (getCurrentUpdateNumbers().get() == getUpdateTotalNumbers().get()) {
                    LOGGER.info("更新完成，本次更新相关指标论文总量：" +getUpdateTotalNumbers().get()
                            + " 成功数：" + getUpdateSucceedNumbers().get()
                            + " 失败数：" + getUpdateFailedNumbers());
                    long startTime= DetailCrawler.getUpdateMillisecond();//开始更新的时间
                    long endTime=System.currentTimeMillis();//结束更新的时间
                    long total=endTime-startTime;
                    String averageTime=Helper.getSeconds(total/getUrlNumbers().get());
                    DBLog.saveUpdateAverageTimeLog(averageTime,getUrlNumbers().get());//保存更新的平均时间
                    //保存更新完成后的总体情况数据到数据库中
                    DBLog.saveFinalUpdateLog(DetailCrawler.getUpdateTime(),getUpdateSucceedNumbers().get(),
                            getUpdateFailedNumbers().get(),getUpdateTotalNumbers().get());
                }
                return succeed;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private void bindUpdateSql(final PreparedStatement preparedStatement) throws SQLException{
        preparedStatement.setString(1, Helper.getUpdateTime());
        preparedStatement.setInt(2, getPageViews());
        preparedStatement.setInt(3,getWebOfScience());
        preparedStatement.setInt(4,getCrossRef());
        preparedStatement.setInt(5,getScopus());
        preparedStatement.setInt(6,getNewsOutlets());
        preparedStatement.setInt(7,getReddit());
        preparedStatement.setInt(8,getBlog());
        preparedStatement.setInt(9,getTweets());
        preparedStatement.setInt(10,getFacebook());
        preparedStatement.setInt(11,getGoogle());
        preparedStatement.setInt(12,getPinterest());
        preparedStatement.setInt(13,getWikipedia());
        preparedStatement.setInt(14,getMendeley());
        preparedStatement.setInt(15,getCiteUlink());
        preparedStatement.setInt(16,getZotero());
        preparedStatement.setInt(17,getF1000());
        preparedStatement.setInt(18,getVideo());
        preparedStatement.setInt(19,getLinkedin());
        preparedStatement.setInt(20,getQ_a());
        preparedStatement.setInt(21,getFinalIndex());
    }


    public boolean updateRefDataIntoES(){
        XContentBuilder json=null;
        FormatHelper formatHelper=new FormatHelper();
        TimeFormatter formatter=formatHelper.formatUpdateTime(DetailCrawler.getUpdateTime());
        try {
            json=jsonBuilder().startObject()
                    .field("URL",getUrl())
                    .field("UpdateTime", DetailCrawler.getUpdateTime())
                    .field("Year", formatter.getYear())
                    .field("Month", formatter.getMonth())
                    .field("Day", formatter.getDay())
                    .field("Page_views",getPageViews())
                    .field("Web_Of_Science",getWebOfScience())
                    .field("CrossRef",getCrossRef())
                    .field("Scopus",getScopus())
                    .field("News_outlets",getNewsOutlets())
                    .field("Reddit",getReddit())
                    .field("Blog",getBlog())
                    .field("Tweets",getTweets())
                    .field("FaceBook",getFacebook())
                    .field("Google",getGoogle())
                    .field("Pinterest",getPinterest())
                    .field("Wikipedia",getWikipedia())
                    .field("Mendeley",getMendeley())
                    .field("CiteUlink",getCiteUlink())
                    .field("Zotero",getZotero())
                    .field("F10000",getF1000())
                    .field("Video",getVideo())
                    .field("Linkedin",getLinkedin())
                    .field("Q_A",getQ_a())
                    .field("FinalIndex",0).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return   ESClient.getInstance().putMetricsPaperIntoES(json);
    }
}
