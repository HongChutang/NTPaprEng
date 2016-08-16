package com.weapes.ntpaprseng.crawler.follow;

import com.weapes.ntpaprseng.crawler.extract.Extractable;
import com.weapes.ntpaprseng.crawler.extract.ExtractedObject;

import java.io.IOException;

/**
 * Created by lawrence on 16/8/7.
 */
public interface Followable extends ExtractedObject {
    Extractable follow() throws IOException;
}
