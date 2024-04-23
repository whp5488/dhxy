package com.kitty.logs;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.PropertyContainer;
import ch.qos.logback.core.util.OptionHelper;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.LoggerFactory;

public class LoggerBuilder {
  private static final Map<String, Logger> container = new HashMap<>();
  
  private static final String LOG_PATH = "log/";
  
  public static Logger getLogger(String name) {
    Logger logger = container.get(name);
    if (logger != null)
      return logger; 
    synchronized (LoggerBuilder.class) {
      logger = container.get(name);
      if (logger != null)
        return logger; 
      logger = build(name);
      container.put(name, logger);
    } 
    return logger;
  }
  
  public static void main(String[] args) {
    LoggerFunction.FIGHT.getLogger().error("heelo");
  }
  
  private static Logger build(String name) {
    DateFormat format = DateFormat.getDateInstance(2, Locale.SIMPLIFIED_CHINESE);
    LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
    Logger logger = context.getLogger(name);
    RollingFileAppender appender = new RollingFileAppender();
    appender.setContext((Context)context);
    appender.setName(name);
    appender.setAppend(true);
    appender.setPrudent(false);
    TimeBasedRollingPolicy policy = new TimeBasedRollingPolicy();
    String fp = OptionHelper.substVars("log/" + name + "/" + name + ".log.%d{yyyy-MM-dd}", (PropertyContainer)context);
    policy.setFileNamePattern(fp);
    policy.setMaxHistory(15);
    policy.setParent((FileAppender)appender);
    policy.setContext((Context)context);
    policy.start();
    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setContext((Context)context);
    encoder.setPattern("%d %p (%file:%line\\)- %m%n");
    encoder.start();
    PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
    encoder1.setContext((Context)context);
    encoder1.setPattern("%d %p [%t] %m%n");
    encoder1.start();
    appender.setRollingPolicy((RollingPolicy)policy);
    appender.setEncoder((Encoder)encoder);
    appender.start();
    ConsoleAppender consoleAppender = new ConsoleAppender();
    consoleAppender.setContext((Context)context);
    consoleAppender.setEncoder((Encoder)encoder1);
    consoleAppender.start();
    logger.addAppender((Appender)consoleAppender);
    logger.setLevel(Level.INFO);
    logger.addAppender((Appender)appender);
    return logger;
  }
}
