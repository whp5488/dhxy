package com.kitty.cross.core;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.cross.core.callback.CallbackTask;
import com.kitty.cross.core.client.C2SSessionPoolFactory;
import com.kitty.cross.core.client.CCSession;
import com.kitty.mina.message.Message;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrossTransportManager {
  private int defaultCoreSum = Runtime.getRuntime().availableProcessors();
  
  private ExecutorService[] services;
  
  private ExecutorService asynService;
  
  @Autowired
  private C2SSessionPoolFactory sessionFactory;
  
  @PostConstruct
  private void init() {
    this.services = new ExecutorService[this.defaultCoreSum];
    for (int i = 0; i < this.defaultCoreSum; i++)
      this.services[i] = Executors.newSingleThreadExecutor((ThreadFactory)new NamedThreadFactory("rpc-transport" + i)); 
    this.asynService = Executors.newFixedThreadPool(this.defaultCoreSum);
  }
  
  public void sendMessageSync(String ip, int port, Message message) {
    CCSession session = this.sessionFactory.borrowSession(ip, port);
    try {
      session.sendMessage(message);
    } finally {
      this.sessionFactory.returnSession(session);
    } 
  }
  
  public void sendMessageAsync(String ip, int port, Message message) {
    String key = (ip + port).toString();
    int index = key.hashCode() % this.defaultCoreSum;
    this.services[index].submit(() -> sendMessageSync(ip, port, message));
    CCSession session = this.sessionFactory.borrowSession(ip, port);
    sendMessageAsync(session, message);
  }
  
  public void sendMessageAsync(CCSession session, Message message) {
    try {
      session.sendMessage(message);
    } finally {
      this.sessionFactory.returnSession(session);
    } 
  }
  
  public Message callBack(CCSession session, Message message) throws ExecutionException, InterruptedException {
    CallbackTask task = CallbackTask.valueOf(session, message);
    return this.asynService.<Message>submit((Callable<Message>)task).get();
  }
}
