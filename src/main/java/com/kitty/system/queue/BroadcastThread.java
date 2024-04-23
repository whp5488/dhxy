package com.kitty.system.queue;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.game.chat.message.RespChat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class BroadcastThread {
  private static volatile BroadcastThread instance;
  
  public static BroadcastThread getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("broadcast-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<RespChat> queue = (BlockingQueue<RespChat>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(RespChat entity) {
    this.queue.add(entity);
  }
  
  public void add2Queue(String msg) {
    RespChat res = new RespChat();
    res.setMsg(msg);
    res.setType(19);
    this.queue.add(res);
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (BroadcastThread.this.run.get()) {
        try {
          RespChat take = BroadcastThread.this.queue.take();
          SenderUtils.pushChatMessage(take);
          Thread.sleep(10000L);
        } catch (Exception e) {
          LoggerUtils.error("系统播报异常", e);
        } 
      } 
    }
  }
}
