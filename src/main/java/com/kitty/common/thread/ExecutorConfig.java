package com.kitty.common.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ExecutorConfig {
  private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);
  
  @Bean
  public Executor asyncServiceExecutor() {
    logger.info("start asyncServiceExecutor");
    ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(99999);
    executor.setKeepAliveSeconds(60);
    executor.setThreadNamePrefix("async-springboot-thread-");
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();
    return (Executor)executor;
  }
}
