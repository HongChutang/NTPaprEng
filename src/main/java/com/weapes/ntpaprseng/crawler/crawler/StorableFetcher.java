package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.extract.Extractable;
import com.weapes.ntpaprseng.crawler.extract.ExtractedObject;
import com.weapes.ntpaprseng.crawler.follow.Followable;
import com.weapes.ntpaprseng.crawler.follow.Link;
import com.weapes.ntpaprseng.crawler.store.Storable;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * Created by lawrence on 16/8/8.
 */
class StorableFetcher<F extends Followable> implements Runnable {

    private final ExecutorService creator;
    private final ExecutorService consumer;
    private final F seed;

    StorableFetcher(final ExecutorService creator,
                    final ExecutorService consumer,
                    final F seed) {
        this.creator = creator;
        this.consumer = consumer;
        this.seed = seed;
    }

    /**
     * 爬虫运行框架,如果链接follow后得到Extractable。
     * 根据其能抽取成一个或多个followable/storable来做再分发。
     */
    @Override
    public void run() {
        try {
            final Extractable extractable = seed.follow();
            if (!extractable.isMulti()) {
                dispatch(extractable.extract());
            } else {
                extractable
                        .extractAll()
                        .forEach(this::dispatch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 分别根据参数的类型做分发。
     * 如果是Followable则继续交给生产者,
     * 如果是Storable交给消费者分发。
     *
     * @param extractedObject 被抽取后的Obj,可以为链接或可存储的对象
     */
    private void dispatch(final ExtractedObject extractedObject) {
        if (extractedObject instanceof Followable) {
            creator.submit(new StorableFetcher<>(creator, consumer, (Link) extractedObject));
        } else {
            consumer.submit(new StorableHandler<>((Storable) extractedObject));
        }
    }

}
