package com.weapes.ntpaprseng.dependencies;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by lawrence on 16/8/7.
 */
public class OkHttpTest {

    @Test
    public void testOkHttp() throws IOException {

        final String htmlMsg = run("http://www.nature.com/index.html");
        assertNotNull(htmlMsg);

    }

    private String run(final String url) throws IOException {

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        final Response response = client.newCall(request).execute();

        return response.body().string();

    }
}
