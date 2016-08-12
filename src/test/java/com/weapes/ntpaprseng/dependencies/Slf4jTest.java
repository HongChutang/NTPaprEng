package com.weapes.ntpaprseng.dependencies;

import org.apache.log4j.spi.LoggerFactory;

import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.*;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Programmer on 2016/8/12.
 */
public class Slf4jTest {
    @Test
    @Ignore
    public void slf4jTest(){

        final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Slf4jTest.class);

        PropertyConfigurator.configure("E:\\javaproject\\git_project\\NTPaprSEng\\conf\\log4j.properties");

        logger.info("nice ------- Hello");
        logger.warn("warn ++++++ warn");

    }
}
