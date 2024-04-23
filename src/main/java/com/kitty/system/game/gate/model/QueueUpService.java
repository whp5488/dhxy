package com.kitty.system.game.gate.model;

import com.kitty.mina.cache.DataCache;
import java.util.LinkedList;
import org.springframework.stereotype.Service;

@Service
public class QueueUpService {
  private boolean needQueue = false;
  
  private int ONLINE_NUM = 1600;
  
  private static final int QUEUE_TIME = 3;
  
  private final LinkedList<String> QueueUpInfos = new LinkedList<>();
  
  public int getIndex(String sid) {
    return this.QueueUpInfos.indexOf(sid) + 1;
  }
  
  public int getQueueTime(String sid) {
    return getIndex(sid) * 3;
  }
  
  public int addQueue(String sid) {
    while (true) {
      boolean success = this.QueueUpInfos.add(sid);
      if (success)
        return getIndex(sid); 
    } 
  }
  
  public boolean peek(String sid) {
    if (sid.equals(this.QueueUpInfos.peek()))
      return remove(sid); 
    return false;
  }
  
  public boolean remove(String sid) {
    return this.QueueUpInfos.remove(sid);
  }
  
  public boolean isNeedQueue() {
    int onlineNum = DataCache.ONLINE_ROLES.size();
    return (onlineNum >= this.ONLINE_NUM && this.needQueue);
  }
  
  public void setNeedQueue(boolean needQueue) {
    this.needQueue = needQueue;
    if (!needQueue)
      this.QueueUpInfos.clear(); 
  }
  
  public void setOnlineNum(int ONLINE_NUM) {
    this.ONLINE_NUM = ONLINE_NUM;
  }
}
