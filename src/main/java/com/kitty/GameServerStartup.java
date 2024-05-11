
package com.kitty;

import org.slf4j.LoggerFactory;
import com.kitty.common.core.SchedulerManager;
import com.kitty.mina.task.TaskHandlerContext;
import com.kitty.mina.session.SessionManager;
import org.springframework.context.annotation.Bean;
import com.kitty.common.thread.IdWorker;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.init.rank.ActingRank;
import com.kitty.system.init.product.ProductDataPool;
import com.kitty.system.game.player.PlayerService;
import com.kitty.common.utils.spring.SpringUtils;
import org.springframework.boot.Banner;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.slf4j.Logger;
import org.springframework.boot.web.servlet.ServletComponentScan;
import com.kitty.common.mysql.DynamicDataSourceRegister;
import org.springframework.context.annotation.Import;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@EntityScan(basePackages = { "com.kitty" })
@MapperScan({ "com.kitty.common.dao" })
@EnableTransactionManagement
@EnableScheduling
@EnableCaching
@Import({ DynamicDataSourceRegister.class })
@ServletComponentScan
public class GameServerStartup extends SpringBootServletInitializer
{
    private static Logger logger;
    
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(GameServerStartup.class);
    }
    
    public static void main(final String[] args) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(GameServerStartup.class);
            applicationBuilder.bannerMode(Banner.Mode.OFF);
            applicationBuilder.run(args);
            SpringUtils.getIdentityService().init();
            SpringUtils.getBean(PlayerService.class).loadAllPlayerProfiles();
            ProductDataPool.INSTANCE.load();
            ActingRank.initRank();
        }
        catch (final Exception e) {
            LoggerUtils.error("", e);
            System.exit(-1);
        }
        stopWatch.stop();
        GameServerStartup.logger.error("启动成功，耗時{}毫秒", stopWatch.getTime());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                onServerShutDown();
            }
        });
    }
    
    @Bean(name = { "idWorker" })
    public IdWorker idWorkker() {
        return new IdWorker();
    }
    
    private static void onServerShutDown() {
        GameServerStartup.logger.error("服务准备关闭");
        try {
            SessionManager.INSTANCE.killAllPlayers();
        }
        catch (final Exception e) {
            LoggerUtils.error("", e);
        }
        TaskHandlerContext.INSTANCE.shutDown();
        SchedulerManager.getInstance().shutDown();
        GameServerStartup.logger.error("服务准备关闭结束");
    }
    
    static {
        GameServerStartup.logger = LoggerFactory.getLogger(GameServerStartup.class);
    }
}
