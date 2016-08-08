package com.weapes.ntpaprseng.crawler.extract;

import java.util.List;

/**
 * Created by lawrence on 16/8/7.
 */
public interface Extractable {
    boolean isMulti();
    ExtractedObject extract();
    List<? extends ExtractedObject> extractAll();
}
