package com.weapes.ntpaprseng.crawler.store;

import com.weapes.ntpaprseng.crawler.extract.ExtractedObject;

/**
 * Created by lawrence on 16/8/7.
 */
public interface Storable extends ExtractedObject {
    boolean store();
}
