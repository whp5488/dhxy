
package com.kitty.logs;

import java.util.HashMap;
import ch.qos.logback.classic.Level;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.spi.PropertyContainer;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.rolling.RollingFileAppender;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import java.text.DateFormat;
import java.util.Locale;
import ch.qos.logback.classic.Logger;
import java.util.Map;

public class LoggerBuilder
{
    private static final Map<String, Logger> container;
    private static final String LOG_PATH = "log/";
    
    public static Logger getLogger(final String name) {
        Logger logger = LoggerBuilder.container.get(name);
        if (logger != null) {
            return logger;
        }
        synchronized (LoggerBuilder.class) {
            logger = LoggerBuilder.container.get(name);
            if (logger != null) {
                return logger;
            }
            logger = build(name);
            LoggerBuilder.container.put(name, logger);
        }
        return logger;
    }
    
    public static void main(final String[] args) {
        LoggerFunction.FIGHT.getLogger().error("heelo");
    }
    
    private static Logger build(final String name) {
        final DateFormat format = DateFormat.getDateInstance(2, Locale.SIMPLIFIED_CHINESE);
        final LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
        final Logger logger = context.getLogger(name);
        final RollingFileAppender appender = new RollingFileAppender();
        appender.setContext((Context)context);
        appender.setName(name);
        appender.setAppend(true);
        appender.setPrudent(false);
        final TimeBasedRollingPolicy policy = new TimeBasedRollingPolicy();
        final String fp = OptionHelper.substVars("log/" + name + "/" + name + ".log.%d{yyyy-MM-dd}", (PropertyContainer)context);
        policy.setFileNamePattern(fp);
        policy.setMaxHistory(15);
        policy.setParent((FileAppender)appender);
        policy.setContext((Context)context);
        policy.start();
        final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext((Context)context);
        encoder.setPattern("%d %p (%file:%line\\)- %m%n");
        encoder.start();
        final PatternLayoutEncoder encoder2 = new PatternLayoutEncoder();
        encoder2.setContext((Context)context);
        encoder2.setPattern("%d %p [%t] %m%n");
        encoder2.start();
        appender.setRollingPolicy((RollingPolicy)policy);
        appender.setEncoder((Encoder)encoder);
        appender.start();
        final ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setContext((Context)context);
        consoleAppender.setEncoder((Encoder)encoder2);
        consoleAppender.start();
        logger.addAppender((Appender)consoleAppender);
        logger.setLevel(Level.INFO);
        logger.addAppender((Appender)appender);
        return logger;
    }
    
    static {
        container = new HashMap<String, Logger>();
    }
}
