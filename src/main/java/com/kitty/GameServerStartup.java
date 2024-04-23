package com.kitty;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.mysql.DynamicDataSourceRegister;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.task.TaskHandlerContext;
import com.kitty.system.game.player.PlayerService;
import com.kitty.system.init.product.ProductDataPool;
import com.kitty.system.init.rank.ActingRank;
import org.apache.commons.lang3.time.StopWatch;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = {"com.kitty"})
@MapperScan({"com.kitty.common.dao"})
@EnableTransactionManagement
@EnableScheduling
@EnableCaching
@Import({DynamicDataSourceRegister.class})
@ServletComponentScan
public class GameServerStartup extends SpringBootServletInitializer {
  private static final Logger logger = LoggerFactory.getLogger(GameServerStartup.class);
  
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(GameServerStartup.class);
  }
  
  public static void main(String[] args) {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(new Object[] { GameServerStartup.class });
      applicationBuilder.bannerMode(Banner.Mode.OFF);
      applicationBuilder.run(args);
      SpringUtils.getIdentityService().init();
      ((PlayerService)SpringUtils.getBean(PlayerService.class)).loadAllPlayerProfiles();
      ProductDataPool.INSTANCE.load();
      ActingRank.initRank();
    } catch (Exception e) {
      LoggerUtils.error("", e);
      System.exit(-1);
    } 
    stopWatch.stop();
    logger.error("启动成功，耗時{}毫秒", Long.valueOf(stopWatch.getTime()));
    Runtime.getRuntime().addShutdownHook(new Thread() {
          public void run() {
            GameServerStartup.onServerShutDown();
          }
        });
  }
  
  @Bean(name = {"idWorker"})
  public IdWorker idWorkker() {
    return new IdWorker();
  }
  
  private static void onServerShutDown() {
    logger.error("服务准备关闭");
    try {
      SessionManager.INSTANCE.killAllPlayers();
    } catch (Exception e) {
      LoggerUtils.error("", e);
    } 
    TaskHandlerContext.INSTANCE.shutDown();
    SchedulerManager.getInstance().shutDown();
    logger.error("服务准备关闭结束");
  }
}
