
package com.kitty.system.game.monitor;

import javax.script.ScriptEngine;
import com.kitty.logs.LoggerUtils;
import javax.script.ScriptEngineManager;
import com.kitty.mina.task.TaskHandlerContext;
import com.kitty.mina.MessageStatistics;
import com.kitty.common.db.Db4PlayerService;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import org.springframework.cache.guava.GuavaCache;
import com.kitty.common.utils.spring.SpringUtils;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.jmx.export.annotation.ManagedOperation;
import com.kitty.mina.cache.DataCache;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "GameMXBean:name=gameMonitor")
public class GameMonitor implements GameMonitorMBean
{
    @ManagedOperation
    @Override
    public int getOnlinePlayerSum() {
        return DataCache.ONLINE_ROLES.size();
    }
    
    @ManagedOperation
    @Override
    public int getCachePlayerSum() {
        final GuavaCacheManager cacheManager = SpringUtils.getBean(GuavaCacheManager.class);
        final GuavaCache guavaCache = (GuavaCache)cacheManager.getCache("player");
        return (int)guavaCache.getNativeCache().size();
    }
    
    @ManagedOperation
    @Override
    public int getFightRoomSum() {
        return FightExecutorNewService.FIGHT_ROOMS.size();
    }
    
    @ManagedOperation
    @Override
    public int getPlayerDbQueueSum() {
        return Db4PlayerService.getInstance().size();
    }
    
    @ManagedOperation
    @Override
    public String getMessageStatistics() {
        return MessageStatistics.getInstance().toString();
    }
    
    @ManagedOperation
    @Override
    public String getTaskContextInfo() {
        return TaskHandlerContext.INSTANCE.statistics();
    }
    
    @ManagedOperation(description = "执行groovy脚本")
    @Override
    public String execGroovyScript(final String groovyCode) {
        try {
            final ScriptEngineManager engineManager = new ScriptEngineManager();
            final ScriptEngine scriptEngine = engineManager.getEngineByName("groovy");
            return scriptEngine.eval(groovyCode).toString();
        }
        catch (final Exception e) {
            LoggerUtils.error("", e);
            final String msg = e.getMessage();
            return msg;
        }
    }
}
