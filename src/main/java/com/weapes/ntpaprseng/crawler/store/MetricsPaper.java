package com.weapes.ntpaprseng.crawler.store;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.weapes.ntpaprseng.crawler.log.Log.getCrawlingSucceedNumbers;
import static com.weapes.ntpaprseng.crawler.log.Log.getLastLink;
import static com.weapes.ntpaprseng.crawler.log.Log.getUrlNumbers;
import static com.weapes.ntpaprseng.crawler.util.Helper.getLogger;

/**
 * Created by 不一样的天空 on 2016/8/18.
 */
public class MetricsPaper implements Storable{

    private static final Logger LOGGER =
            getLogger(Paper.class);

    final  String insert =
            "INSERT INTO REF_DATA(URL, Page_views, Web_of_Science, CrossRef, Scopus, News_outlets, " +
                    "reddit, Blog, Tweets, Facebook, Google, Pinterest, wikipedia, Mendeley, CiteUlink, Zotero, F1000, Video, " +
                    "linkedin, Q_A)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    @Override
    public boolean store() {
        System.out.println("Store begin: type=MetricsPaper.");
        final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();

        try (final Connection connection = mysqlDataSource.getConnection()){
            try (final PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                preparedStatement.setString(1,getUrl());
                preparedStatement.setInt(2,getPageViews());
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

                // 判断执行是否成功
                boolean succeed = preparedStatement.executeUpdate() != 0;
                if (succeed) {
                    LOGGER.info("第" + getCrawlingSucceedNumbers().incrementAndGet() + "篇论文相关指标爬取成功...\n"
                            + "链接为；" + getUrl());
                }
                if (getLastLink().equals(getUrl())) {
                    LOGGER.info("爬取完成，本次爬取论文总量：" + getUrlNumbers().get()
                            + " 成功数：" + getCrawlingSucceedNumbers().get()
                            + " 失败数：" + (getUrlNumbers().get() - getCrawlingSucceedNumbers().get()));
                }
                return succeed;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
