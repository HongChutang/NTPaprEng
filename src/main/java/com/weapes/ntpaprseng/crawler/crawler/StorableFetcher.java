package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.extract.Extractable;
import com.weapes.ntpaprseng.crawler.extract.ExtractedObject;
import com.weapes.ntpaprseng.crawler.follow.Followable;
import com.weapes.ntpaprseng.crawler.store.Storable;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * Created by lawrence on 16/8/8.
 */
class StorableFetcher implements Runnable {

    private final ExecutorService creator;
    private final ExecutorService consumer;
    private final Followable seed;

    StorableFetcher(final ExecutorService creator,
                    final ExecutorService consumer,
                    final Followable seed) {
        this.creator = creator;
        this.consumer = consumer;
        this.seed = seed;
    }

    @Override
    public void run() {
        try {
            final Extractable extractable = seed.follow();
            if (!extractable.isMulti()) {
                dispatch(extractable.extract());
            } else {
                extractable.extractAll().forEach(this::dispatch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(final ExtractedObject extractedObject) {
        if (extractedObject instanceof Followable) {
            creator.submit(new StorableFetcher(creator, consumer,
                    (Followable) extractedObject));
        } else {
            consumer.submit(new StorableHandler((Storable) extractedObject));
        }
    }

}
