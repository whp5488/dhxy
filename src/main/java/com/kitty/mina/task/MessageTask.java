package com.kitty.mina.task;

import com.kitty.mina.message.Message;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageTask extends AbstractDistributeTask {
  private static Logger logger = LoggerFactory.getLogger(MessageTask.class);
  
  private long playerId;
  
  private Message message;
  
  private Object handler;
  
  private Method method;
  
  private Object[] params;
  
  public static MessageTask valueOf(int distributeKey, Object handler, Method method, Object[] params, Message message) {
    MessageTask msgTask = new MessageTask();
    msgTask.distributeKey = distributeKey;
    msgTask.handler = handler;
    msgTask.method = method;
    msgTask.params = params;
    msgTask.message = message;
    return msgTask;
  }
  
  public void action() {
    try {
      this.method.invoke(this.handler, this.params);
    } catch (Throwable e) {
      e.printStackTrace();
      logger.error("message task execute failed=协议=={}，异常={}", Integer.valueOf(this.message.getModule()), this.message);
    } 
  }
  
  public long getPlayerId() {
    return this.playerId;
  }
  
  public Message getMessage() {
    return this.message;
  }
  
  public Object getHandler() {
    return this.handler;
  }
  
  public Method getMethod() {
    return this.method;
  }
  
  public Object[] getParams() {
    return this.params;
  }
  
  public String toString() {
    return getName() + "[" + this.handler.getClass().getName() + "@" + this.method.getName() + "]";
  }
}
