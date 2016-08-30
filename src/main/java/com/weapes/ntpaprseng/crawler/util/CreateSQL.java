package com.weapes.ntpaprseng.crawler.util;

/**
 * Created by  不一样的天空  on 2016/8/30.
 */
public class CreateSQL {
    // 第一次更新REF_DATA表的sql语句
    private static final String FIRST_REF_UPDATE_SQL =
            "UPDATE REF_DATA SET "+"Page_views = ?, " + "Web_of_Science = ?, " +
                    "CrossRef = ?, " + "Scopus = ?, " + "News_outlets = ?, " +
                    "reddit = ?, " + "Blog = ?, " + "Tweets = ?, " + "Facebook = ?, " +
                    "Google = ?, " + "Pinterest = ?, " + "wikipedia = ?, " + "Mendeley = ?, " +
                    "CiteUlink = ?, " + "Zotero = ?, " + "F1000 = ?, " +
                    "Video = ?, " + "linkedin = ?, " + "Q_A = ? " + "WHERE URL = ";

    private static final String NT_PAPERS_INSERT_SQL =
            "INSERT INTO " + "NT_PAPERS(" +
                    "Title ," + "Authors, " + "SourceTitle, " + "ISSN, " +
                    "EISSN, " + "DOI, " + "Volum, " + "Issue, " + "PageBegin, " +
                    "PageEnd, " + "URL, " + "AFFILIATION, " + "CRAWL_TIME, " +
                    "PUBLISH_TIME) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // 第一次向REF_DATA表插入数据的sql语句
    private static final  String FIRST_REF_INSERT_SQL =
            "INSERT INTO REF_DATA(URL, Page_views, Web_of_Science, CrossRef, Scopus, News_outlets, " +
                    "reddit, Blog, Tweets, Facebook, Google, Pinterest, wikipedia, Mendeley, CiteUlink, Zotero, F1000, Video, " +
                    "linkedin, Q_A)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String columns[]={"Page_views_","Web_of_Science_",
    "CrossRef_","Scopus_","News_outlets_","reddit_","Blog_",
    "Tweets_","Facebook_","Google_","Pinterest_","wikipedia_",
    "Mendeley_","CiteUlink_","Zotero_","F1000_","Video_","linkein_","Q_A_"};

    private static String ADD_COLUMNS_SQL="ALTER TABLE REF_DATA ADD(";//添加REF_DATA表里的字段的SQL

    private static String REF_UPDATE_SQL="UPDATE REF_DATA SET ";//从第二次爬取开始更新REF_DATA表的SQL

    private static String DEFAULT_ATTRIBUTE=" INT(11) NULL DEFAULT 0,";//向REF_DATA表添加字段的默认属性

    private static String UPDATE_TIME_SQL="SELECT　* FROM TIME";
    private static String TIME="2";//从数据库中获取的第几次爬取的值
    public static String getAddColumnsSQL(){
        for (String s:columns) {
            ADD_COLUMNS_SQL+=s+TIME+DEFAULT_ATTRIBUTE;
        }
        return ADD_COLUMNS_SQL.substring(0,ADD_COLUMNS_SQL.length()-1)+")";
    }

    public static  String getRefUpdateSQL(){
        if (Integer.valueOf(TIME)>=2){
            for (String s:columns) {
                REF_UPDATE_SQL+=s+TIME+" = ?, ";
            }
            return REF_UPDATE_SQL+"WHERE URL = ";
        }else {
            return FIRST_REF_UPDATE_SQL;
        }
    }

    public static String getNtPapersInsertSQL(){
        return NT_PAPERS_INSERT_SQL;
    }

    public static String getRefInsertSQL(){
            return FIRST_REF_INSERT_SQL;
    }
    public static String getUpdateTimeSQL(){
        return UPDATE_TIME_SQL;
    }

}
