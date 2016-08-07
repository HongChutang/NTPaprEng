package com.weapes.ntpaprseng.crawler.util;

import com.weapes.ntpaprseng.crawler.WebPage;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by lawrence on 16/8/7.
 */
public final class Helper {

    private Helper() { }

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
<<<<<<< HEAD
=======
    private static final Pattern URL_CHECKER = Pattern.compile("\\w+://[\\w.]+/\\S*");
>>>>>>> develop

    public static WebPage fetchWebPage(final String url)
            throws IOException {

        final Request request = new Request.Builder()
                .url(url)
                .build();

        final Response executed = OK_HTTP_CLIENT.newCall(request)
                .execute();

        return new WebPage(executed.body().string());
    }

    public static void fetchWebPageWithCallback(final String url, final Callback callback) {

        final Request request = new Request.Builder()
                .url(url)
                .build();

<<<<<<< HEAD
        OK_HTTP_CLIENT.newCall(request).enqueue(callback);
=======
        OK_HTTP_CLIENT
                .newCall(request)
                .enqueue(callback);
>>>>>>> develop

    }

    public static boolean isURL(String url) {
        return URL_CHECKER
                .matcher(url)
                .matches();
    }
}
