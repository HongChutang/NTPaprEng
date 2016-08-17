package com.weapes.ntpaprseng.crawler.store;

import com.weapes.ntpaprseng.crawler.extract.ExtractedObject;

/**
 * 可存储的对象,如果后期使用Ebean做ORM后,这些应该是Models
 * Created by lawrence on 16/8/7.
 */
public interface Storable extends ExtractedObject {
    boolean store();
}
