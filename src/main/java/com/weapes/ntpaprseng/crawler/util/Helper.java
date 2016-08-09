package com.weapes.ntpaprseng.crawler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weapes.ntpaprseng.crawler.follow.AdvSearchLink;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.forName;

/**
 * Created by lawrence on 16/8/7.
 */
public final class Helper {

    private Helper() { }

    private static final OkHttpClient OK_HTTP_CLIENT =
            new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

    private static final Pattern URL_CHECKER =
            Pattern.compile("\\w+://[\\w.]+/\\S*");

    private static final String BASE_URL =
            "http://nature.com/search";

    public static List<AdvSearchLink> loadSeeds(final String filePath)
            throws IOException {

        final JSONObject jsonObject =
                fileMapToJSONObject(filePath);

        final List<String> urls =
                parseURLSWithJSONObject(jsonObject);

        return urls.stream()
                .map(AdvSearchLink::new)
                .collect(Collectors.toList());
    }

    private static JSONObject fileMapToJSONObject(final String file)
            throws IOException {
        final byte[] bytes =
                Files.readAllBytes(new File(file).toPath());

        return JSON.parseObject(new String(bytes,
                forName("UTF-8")));
    }


    public static String fetchWebPage(final String url)
            throws IOException {

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                .build();

        System.out.println("Downloading: " + url);

        final Response executed = OK_HTTP_CLIENT.newCall(request)
                .execute();

        System.out.println("Downloaded:" + url);

        return executed.body().string();
    }

    public static boolean isURL(final String url) {
        return URL_CHECKER
                .matcher(url)
                .matches();
    }

    private static List<String> parseURLSWithJSONObject(final JSONObject object) {

        final JSONObject range = object.getJSONObject("range");
        final JSONArray journals = object.getJSONArray("journals");
        final String article_type = object.getString("article_type");
        final String order = object.getString("order");

        return journals.stream()
                    .map(journal -> concatUrl(range ,journal, article_type, order))
                    .collect(Collectors.toList());
    }

    private static String concatUrl(final JSONObject range, final Object journal) {
        return concatUrl(range, journal, "research", "date_desc");
    }

    private static String concatUrl(final JSONObject range, final Object journal, final String article_type) {
        return concatUrl(range, journal, article_type, "date_desc");
    }

    private static String concatUrl(final JSONObject range, final Object journal, final String article_type, final String order) {
        final int begin = range.getInteger("begin");
        final int end = range.getInteger("end");

        return BASE_URL
                + "?data_range=" + begin + "-" + end
                + "&journal=" + journal.toString().toLowerCase()
                + "&article_type=" + article_type.toLowerCase()
                + "&order=" + order.toLowerCase();
    }
}
