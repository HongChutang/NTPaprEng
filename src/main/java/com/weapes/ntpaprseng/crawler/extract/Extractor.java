package com.weapes.ntpaprseng.crawler.extract;

import com.weapes.ntpaprseng.crawler.ExtractedObject;

/**
 * Created by lawrence on 16/8/7.
 */
public interface Extractor {
    public ExtractedObject extract(Input input);
}
