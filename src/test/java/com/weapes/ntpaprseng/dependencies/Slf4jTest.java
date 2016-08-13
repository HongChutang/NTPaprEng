package com.weapes.ntpaprseng.dependencies;


import java.beans.PropertyEditor;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.*;

/**
 * Created by Programmer on 2016/8/12.
 */
public class Slf4jTest {
    @Test
    @Ignore
    public void slf4jTest(){

        //日志变量
        org.slf4j.Logger logger =   LoggerFactory.getLogger(Slf4jTest.class);

        //加载log4j配置文件
        PropertyConfigurator.configure("E:\\javaproject\\git_project\\NTPaprSEng\\conf\\log4j.properties");

        //打印日志
        logger.info("nice ------ Hello");
        logger.warn("warn ++++++ warn");

    }
}
