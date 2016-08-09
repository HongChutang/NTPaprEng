package com.weapes.ntpaprseng.crawler.store;

import com.weapes.ntpaprseng.crawler.extract.ExtractedObject;

import java.sql.SQLException;

/**
 * Created by lawrence on 16/8/7.
 */
public interface Storable extends ExtractedObject {
    boolean store() throws SQLException;
}
