
package com.kitty.common.thread;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAsync
public class ExecutorConfig
{
    private static final Logger logger;
    
    @Bean
    public Executor asyncServiceExecutor() {
        ExecutorConfig.logger.info("start asyncServiceExecutor");
        final ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(99999);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("async-springboot-thread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
    
    static {
        logger = LoggerFactory.getLogger(ExecutorConfig.class);
    }
}
