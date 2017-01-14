package com.weapes.ntpaprseng.crawler.crawler;

import com.weapes.ntpaprseng.crawler.util.Helper;


import java.util.concurrent.TimeUnit;

/**
 * Created by Evan Hung on 2017/1/14.
 */
public class Launcher {
    public static void main(String args[]) {
        System.out.print("系统开始运行。\n");
        while (true) {
            Thread paperCrawlerTask = new Thread(new Task(new PaperCrawler()));
            Thread detailCrawlerTask = new Thread(new Task(new DetailCrawler()));
            paperCrawlerTask.start();
            try {
                paperCrawlerTask.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            detailCrawlerTask.start();
            try {
                detailCrawlerTask.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                long pause = Helper.getTaskPeriod();
                System.out.print(pause + "!!!!!!!!!!!\n");
                TimeUnit.MINUTES.sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Task implements Runnable{

    Crawler crawler;

    Task(Crawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public synchronized void run() {
        crawler.crawl();
    }
}
