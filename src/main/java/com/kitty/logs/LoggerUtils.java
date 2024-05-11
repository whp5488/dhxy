
package com.kitty.logs;

import org.slf4j.Logger;

public class LoggerUtils
{
    public static void error(final String errMsg, final Exception e) {
        final Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.error(errMsg, e);
    }
    
    public static void error(final String format, final Object... arguments) {
        final Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.error(format, arguments);
    }
    
    public static void info(final String errMsg, final Exception e) {
        final Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.error(errMsg, e);
    }
    
    public static void info(final String format, final Object... arguments) {
        final Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.info(format, arguments);
    }
}
