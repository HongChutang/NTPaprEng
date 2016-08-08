package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.store.Storable;

/**
 * Created by lawrence on 16/8/8.
 */
class StorableHandler implements Runnable {

    private final Storable storable;

    StorableHandler(final Storable storable) {
        this.storable = storable;
    }

    @Override
    public void run() {
        storable.store();
    }
}
