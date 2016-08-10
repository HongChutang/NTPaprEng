package com.weapes.ntpaprseng.crawler.extract;

import java.util.List;

/**
 * Created by lawrence on 16/8/7.
 */
public interface Extractable {
    boolean isMulti(); // 是否抽取出多个Etd,2中情况属于互斥事件。

    ExtractedObject extract(); // 抽取单个Etd

    List<? extends ExtractedObject> extractAll(); // 抽取多个Etd
}
