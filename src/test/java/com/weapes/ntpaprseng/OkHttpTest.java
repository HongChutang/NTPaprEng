package com.weapes.ntpaprseng;

import junit.framework.TestCase;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by lawrence on 16/8/7.
 */
public class OkHttpTest extends TestCase {
    public void testOkHttp() throws IOException {
        assertNotNull(run("http://www.nature.com/index.html"));
    }

    private String run(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }
}
