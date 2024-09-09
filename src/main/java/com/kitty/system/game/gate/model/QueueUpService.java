package com.kitty.system.game.gate.model;

import com.kitty.mina.cache.DataCache;
import java.util.LinkedList;
import org.springframework.stereotype.Service;

@Service
public class QueueUpService
{
    private boolean needQueue;
    private int ONLINE_NUM;
    private static final int QUEUE_TIME = 3;
    private final LinkedList<String> QueueUpInfos;
    
    public QueueUpService() {
        this.needQueue = false;
        this.ONLINE_NUM = 1600;
        this.QueueUpInfos = new LinkedList();
    }
    
    public int getIndex(String sid) {
        return this.QueueUpInfos.indexOf(sid) + 1;
    }
    
    public int getQueueTime(String sid) {
        return this.getIndex(sid) * 3;
    }
    
    public int addQueue(String sid) {
        boolean success;
        do {
            success = this.QueueUpInfos.add(sid);
        } while (!success);
        return this.getIndex(sid);
    }
    
    public boolean peek(String sid) {
        return sid.equals(this.QueueUpInfos.peek()) && this.remove(sid);
    }
    
    public boolean remove(String sid) {
        return this.QueueUpInfos.remove(sid);
    }
    
    public boolean isNeedQueue() {
        int onlineNum = DataCache.ONLINE_ROLES.size();
        return onlineNum >= this.ONLINE_NUM && this.needQueue;
    }
    
    public void setNeedQueue(boolean needQueue) {
        if (!(this.needQueue = needQueue)) {
            this.QueueUpInfos.clear();
        }
    }
    
    public void setOnlineNum(int ONLINE_NUM) {
        this.ONLINE_NUM = ONLINE_NUM;
    }
}
