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

    static final int TIME_2_DELAY = 100000;
    private static final Pattern urlChecker = Pattern.compile("\\w+://[\\w.]+/\\S*");

    @Test
    @Ignore
    public void jsoupOnlineExtract() throws IOException {


        final List<String> urls = new ArrayList<>();

        urls.add("http://www.nature.com/search?date_range=2012-2016&journal=nature");
        for (String url : urls) {

            final Document dom = Jsoup.parse(new URL(url), TIME_2_DELAY);
            final Elements links = dom.select("section > ol > li > div > h2 > a");

            for (Element link : links) {

                final boolean isURL = urlChecker
                        .matcher(link.attr("href"))
                        .matches();

                System.out.println(isURL);
                Assert.assertTrue(isURL);

            }

        }
    }
}


