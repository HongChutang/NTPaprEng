package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.store.Storable;

/**
 * Created by lawrence on 16/8/8.
 */
class StorableHandler<T extends Storable> implements Runnable {

    private final T storable;

    StorableHandler(final T storable) {
        this.storable = storable;
    }

    @Override
    public void run() {
        storable.store();
    }
}
