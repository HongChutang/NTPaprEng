package com.weapes.ntpaprseng.crawler.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weapes.ntpaprseng.crawler.follow.AdvSearchLink;
import com.weapes.ntpaprseng.crawler.follow.Followable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lawrence on 16/8/7.
 */
final class SeedLoader {
    private SeedLoader() { }

    private static final String BASE_URL = "http://nature.com/search";

    static List<? extends Followable> load(final String filePath)
            throws IOException {

        final File file = new File(filePath);

        final byte[] bytes = Files.readAllBytes(file.toPath());

        final JSONObject parse = JSON.parseObject(new String(bytes,
                Charset.forName("UTF-8")));

        final JSONObject range = parse.getJSONObject("range");
        final int begin = range.getInteger("begin");
        final int end = range.getInteger("end");

        final JSONArray categories =
                parse.getJSONArray("categories");

        List<Followable> followableList = new ArrayList<>();

        for (Object obj : categories) {
            final String urlParsed = BASE_URL
                    + "?"
                    + "data_range=" + begin + "-" + end
                    + "journal=" + obj;

            followableList.add(new AdvSearchLink(urlParsed));

        }

        return followableList;
    }
}
