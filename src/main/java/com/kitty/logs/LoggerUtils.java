package com.kitty.logs;

import org.slf4j.Logger;

public class LoggerUtils
{
    public LoggerUtils() {
    }
    
    public static void error(String errMsg, Exception e) {
        Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.error(errMsg, e);
    }
    
    public static void error(String format, Object... arguments) {
        Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.error(format, arguments);
    }
    
    public static void info(String errMsg, Exception e) {
        Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.error(errMsg, e);
    }
    
    public static void info(String format, Object... arguments) {
        Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.info(format, arguments);
    }
}
