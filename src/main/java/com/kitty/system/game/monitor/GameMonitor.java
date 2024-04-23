package com.kitty.system.game.monitor;

import com.kitty.common.db.Db4PlayerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.MessageStatistics;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.task.TaskHandlerContext;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "GameMXBean:name=gameMonitor")
public class GameMonitor implements GameMonitorMBean {
  @ManagedOperation
  public int getOnlinePlayerSum() {
    return DataCache.ONLINE_ROLES.size();
  }
  
  @ManagedOperation
  public int getCachePlayerSum() {
    GuavaCacheManager cacheManager = (GuavaCacheManager)SpringUtils.getBean(GuavaCacheManager.class);
    GuavaCache guavaCache = (GuavaCache)cacheManager.getCache("player");
    return (int)guavaCache.getNativeCache().size();
  }
  
  @ManagedOperation
  public int getFightRoomSum() {
    return FightExecutorNewService.FIGHT_ROOMS.size();
  }
  
  @ManagedOperation
  public int getPlayerDbQueueSum() {
    return Db4PlayerService.getInstance().size();
  }
  
  @ManagedOperation
  public String getMessageStatistics() {
    return MessageStatistics.getInstance().toString();
  }
  
  @ManagedOperation
  public String getTaskContextInfo() {
    return TaskHandlerContext.INSTANCE.statistics();
  }
  
  @ManagedOperation(description = "执行groovy脚本")
  public String execGroovyScript(String groovyCode) {
    try {
      ScriptEngineManager engineManager = new ScriptEngineManager();
      ScriptEngine scriptEngine = engineManager.getEngineByName("groovy");
      return scriptEngine.eval(groovyCode).toString();
    } catch (Exception e) {
      LoggerUtils.error("", e);
      String msg = e.getMessage();
      return msg;
    } 
  }
}
