package com.weapes.ntpaprseng.dependencies;

import jdk.nashorn.internal.ir.annotations.Ignore;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Programmer on 2016/8/17.
 */

public class SelectTest {

    private final Map<String, Integer> hashMap = new HashMap<>();

    @Test
    @Ignore
    public void test() throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://www.nature.com/nature/journal/v490/n7421/nature11558/metrics")
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("error");
        }


        Document element = Jsoup.parse(new String(response.body().bytes()));

        String numbers, numbersString;

        //Total citations
        String totalCitations = "#am-container > div.am-cols.cleared > div > div.am-module.citations-container " +
                "> div > section > div > ul > li";
        Elements elements1 = element.select(totalCitations);
        for (Element element2 : elements1) {

            numbersString = element2.select("span").text();
            numbers = element2.select("a > div").text();

            if (numbers != null) {
                if (!numbers.equals(""))
                    hashMap.put(numbersString, Integer.parseInt(numbers));
            }
        }

        //Online attention
        String onlineAttention = "#altmetric-metrics > div.cleared > div.altmetric-key > ul > li";
        Elements elements2 = element.select(onlineAttention);
        for (Element element2 : elements2) {

            numbersString = element2.select("div").text();
            numbers = element2.select("div > b").text();

            if (numbers != null) {
                if (!numbers.equals(""))
                    hashMap.put(numbersString, Integer.parseInt(numbers));
            }

        }

        //Page views
        String pageViews = "#am-container > div.am-module.pageview-metrics-container.page-views " +
                "> article > div > div.page-view-header > h2";
        Elements elements3 = element.select(pageViews);
        for (Element element2 : elements3) {

            numbersString = element2.select("span.type").text();
            numbers = element2.select("span.total").text();
            numbers = numbers.replaceAll(",", "");

            hashMap.put(numbersString, Integer.parseInt(numbers));
        }

        //输出，用于查看调试
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.println("获取的键值对为：" + entry);
        }


    }
}
