package com.weapes.ntpaprseng.crawler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.weapes.ntpaprseng.crawler.follow.AdvSearchLink;
import com.weapes.ntpaprseng.crawler.follow.PaperMetricsLink;
import com.weapes.ntpaprseng.crawler.store.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.forName;

/**
 * Created by lawrence on 16/8/7.
 */
public final class Helper {
    public static boolean isFirstCrawl = true;
    public static boolean isDesided = false;
    public static boolean isFirstPaperLink = true;
    public static String lastUrlForLastTime = null;
    private static final OkHttpClient OK_HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .build();
    private static final Pattern URL_CHECKER =
            Pattern.compile("\\w+://[\\w.]+/\\S*");
    private static final String BASE_URL =
            "http://www.nature.com/search";

    private static final String JSON_CFG_FILE_PATH =
            "conf" + File.separator + "filecfg.json";
    private static final Logger LOGGER =
            getLogger(Helper.class);

    private static final String DATE_FORMAT =
            "yyyy年MM月dd日 HH:mm";

    static {
        PropertyConfigurator.configure(
                getCfg().getString("log4j")
        );
    }


    private Helper() {

    }

    /**
     * 解析配置文件,获得原始种子
     *
     * @return seeds
     * @throws IOException dummyInfo
     */

    public static List<AdvSearchLink> loadSeeds()
            throws IOException {
        LOGGER.info("开始加载种子...");

        final JSONObject jsonObject =
                fileMapToJSONObject(getCfg().getString("allPapersFetch"));

        final List<String> urls =
                parseURLSWithJSONObject(jsonObject);

        LOGGER.info("种子加载完成...");

        return urls.stream()
                .map(AdvSearchLink::new)
                .collect(Collectors.toList());
    }

    public static JSONObject getCfg() {
        try {
            final JSONObject cfg = fileMapToJSONObject(JSON_CFG_FILE_PATH);
            assert cfg != null;
            return fileMapToJSONObject(JSON_CFG_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 将配置文件映射为JSON对象
    private static JSONObject fileMapToJSONObject(final String filePath)
            throws IOException {
        final byte[] bytes =
                Files.readAllBytes(new File(filePath).toPath());

        return JSON.parseObject(new String(bytes,
                forName("UTF-8")));
    }


    /**
     * 根据url下载网页。
     *
     * @param url 要下载的链接
     * @return 网页HTML
     * @throws IOException dummyInfo
     */
    public static String fetchWebPage(final String url)
            throws IOException {

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                .build();

        final Response executed = OK_HTTP_CLIENT.newCall(request)
                .execute();

        return executed.body().string();
    }

    // 是否为URL
    public static boolean isURL(final String url) {
        return URL_CHECKER
                .matcher(url)
                .matches();
    }

    public static Logger getLogger(Class cls) {
        return LoggerFactory.getLogger(cls);
    }


    // 将JSON对象映射为种子
    private static List<String> parseURLSWithJSONObject(final JSONObject object) {

        final JSONObject range = object.getJSONObject("first_range");
        final JSONArray journals = object.getJSONArray("journals");
        final String article_type = object.getString("article_type");
        final String order = object.getString("order");

        return journals.stream()
                .map(journal -> concatUrl(range, journal, article_type, order))
                .collect(Collectors.toList());
    }

    private static String concatUrl(final JSONObject range,
                                    final Object journal) {
        return concatUrl(range, journal, "research", "date_desc");
    }

    private static String concatUrl(final JSONObject range,
                                    final Object journal,
                                    final String article_type) {
        return concatUrl(range, journal, article_type, "date_desc");
    }

    // 生成种子链接URL
    private static String concatUrl(final JSONObject range,
                                    final Object journal,
                                    final String article_type,
                                    final String order) {
        // 根据是否是第一次爬取决定爬取的论文年份范围
        int begin, end;
        begin = range.getInteger("begin");
        end = range.getInteger("end");
        if (!Helper.isFirstCrawl) {
            Calendar nowDate = Calendar.getInstance();
            int year = nowDate.YEAR;
            int month = nowDate.MONTH;
            if (month >= 2) {
                begin = year;
                end = year;
            }
            else {
                begin = year - 1;
                end = year;
            }
        }
        // 如果只搜索特点年份,则URL的data_range参数应只有一个年份。
        String dateRange;
        if (begin == end)
            dateRange = "?date_range=" + begin;
        else
            dateRange = "?date_range=" + begin + "-" + end;

        return BASE_URL
                + dateRange
                + "&journal=" + journal.toString().toLowerCase()
                + "&article_type=" + article_type.toLowerCase()
                + "&order=" + order.toLowerCase();
    }


    public static List<PaperMetricsLink> loadMetricsLinks() {
        List<PaperMetricsLink> paperMetricsLinks = new ArrayList<>();
        final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
        //从第二张数据表中取出已有所有论文相关指标页面链接
        try (final Connection connection = mysqlDataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT URL FROM REF_DATA")) {
                try (ResultSet results = preparedStatement.executeQuery()) {
                    while (results.next()) {
                        final String url = results.getString("URL");
                        paperMetricsLinks.add(new PaperMetricsLink(url));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connected DB Failed");
        }
        return paperMetricsLinks;
    }

    //获取本次待更新相关指标论文数量方法
    public static int getRefDataNum() {
        int num = 0;
        final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
        try (final Connection connection = mysqlDataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM NT_PAPERS")) {
                try (ResultSet results = preparedStatement.executeQuery()) {
                    while (results.next())
                        num++;
                }
            }
        } catch (SQLException e) {
            System.out.println("Connected DB Failed");
        }
        return num;
    }

    //在意外终止爬虫后重启时获取上次爬取的最后一篇论文链接的备用方法
    public static String getLastUrlForLastTime() {
        String urlForLastTime = null;
        final HikariDataSource mysqlDataSource = DataSource.getMysqlDataSource();
        //从第二张数据表中取出已有所有论文相关指标页面链接
        try (final Connection connection = mysqlDataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT URL FROM NT_PAPERS LIMIT 1")) {
                try (ResultSet results = preparedStatement.executeQuery()) {
                    while (results.next()) {
                        urlForLastTime = results.getString("URL");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return urlForLastTime;
    }

    public static String getCrawlTime() {
        final Date now = new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(DATE_FORMAT);
        String time=simpleDateFormat.format(now);
        return time;
    }
    public static String getSeconds(long millis){
        float second= (float) (millis/1000.0);
        System.out.print(second+"秒");
        return second+"秒";
    }
    public static long getMillis(String time){
        SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
        try {
            Date date=format.parse(time);
            System.out.println(date.getTime());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }
    //获取第一部分爬虫间隔
    public static int getPaperCrawlerInterval() {
        String filePath = getCfg().getString("allPapersFetch");
        JSONObject jsonObject = null;
        try {
            jsonObject = fileMapToJSONObject(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject.getJSONObject("interval").getInteger("paper_crawler_interval_day");
    }
    //获取第二部分爬虫间隔
    public static int getDetailCrawlerInterval() {
        String filePath = getCfg().getString("allPapersFetch");
        JSONObject jsonObject = null;
        try {
            jsonObject = fileMapToJSONObject(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject.getJSONObject("interval").getInteger("detail_crawler_interval_day");
    }


}
