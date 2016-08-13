package com.weapes.ntpaprseng.dependencies;


import java.beans.PropertyEditor;
import java.util.logging.Logger;

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

        org.slf4j.Logger logger =   LoggerFactory.getLogger(Slf4jTest.class);

        logger.info("nice ------ Hello");
        logger.warn("warn ++++++ warn");

    }
}
