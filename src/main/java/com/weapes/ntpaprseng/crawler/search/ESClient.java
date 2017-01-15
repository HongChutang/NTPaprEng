package com.weapes.ntpaprseng.crawler.search;

//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by 不一样的天空 on 2016/11/2.
 */
public class ESClient {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 9300;
    private static final int NT_PAPER_SIZE=14;
    private static final int REF_DATA_SIZE=21;
    private static final String INDEX = "ntpaprseng";
    private static final String NT_PAPER_TYPE = "NT_PAPERS";
    private static final String REF_DATA_TYPE = "REF_DATA";
    private static TransportClient client = null;
    private static ESClient singleton = null;
    private ESClient() {
    }

    public static ESClient getInstance() {
        if (singleton == null) {
            synchronized (ESClient.class) {
                if (singleton == null) {
                    singleton = new ESClient();
                    initClient();
                }
            }
        }
        return singleton;
    }

    private static void initClient() {
        try {
            client = TransportClient.builder().settings(Settings.EMPTY).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public   boolean putPaperIntoES(String id,XContentBuilder json) {
        IndexResponse response = client.prepareIndex(INDEX,NT_PAPER_TYPE,id)
                .setSource(json)
                .get();
        return response.isCreated();
    }
    public   boolean putMetricsPaperIntoES(XContentBuilder json) {

        IndexResponse response = client.prepareIndex(INDEX,REF_DATA_TYPE)
                .setSource(json)
                .get();
        return response.isCreated();
    }

}
