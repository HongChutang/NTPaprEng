package com.weapes.ntpaprseng.dependencies;


import com.weapes.ntpaprseng.crawler.util.Helper;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * Created by Programmer on 2016/8/12.
 */
public class Slf4jTest {
    @Test
    @Ignore
    public void slf4jTest() {

        //日志变量
        org.slf4j.Logger logger = LoggerFactory.getLogger(Slf4jTest.class);

        //加载log4j配置文件
        PropertyConfigurator.configure(Helper.getCfg().getString("log4j"));

        //打印日志
        logger.info("nice ------ Hello");
        logger.warn("warn ++++++ warn");
    }
}
