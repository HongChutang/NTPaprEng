package com.weapes.ntpaprseng.dependencies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lawrence on 16/8/7.
 */
public class JsoupTest {

    public static Pattern pattern = Pattern.compile("\\w+://[\\w.]+/\\S*");

    @Test
    @Ignore
    public void JsoupOnlineExtract() throws IOException {

        final int time2Delay = 100000;

        final List<String> urls = new ArrayList<>();

        urls.add("http://www.nature.com/search?date_range=2012-2016&journal=nature");

        for (String url : urls) {

            final Document dom = Jsoup.parse(new URL(url), time2Delay);
            final Elements links = dom.select("section > ol > li > div > h2 > a");

            for (Element link : links) {

                final boolean isURL = pattern
                        .matcher(link.attr("href"))
                        .matches();

                Assert.assertTrue(isURL);

            }

        }
    }
}


