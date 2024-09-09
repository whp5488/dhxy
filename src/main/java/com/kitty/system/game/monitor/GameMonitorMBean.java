package com.kitty.system.game.monitor;

public interface GameMonitorMBean
{
    public abstract int getOnlinePlayerSum();
    
    public abstract int getCachePlayerSum();
    
    public abstract int getFightRoomSum();
    
    public abstract int getPlayerDbQueueSum();
    
    public abstract String getMessageStatistics();
    
    public abstract String getTaskContextInfo();
    
    public abstract String execGroovyScript(String groovyCode);
}
