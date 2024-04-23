package com.kitty.cross.core.callback;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cross.core.client.C2SSessionPoolFactory;
import com.kitty.cross.core.client.CCSession;
import com.kitty.mina.message.Message;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CallbackTask implements Callable<Message> {
  private CCSession session;
  
  private Message request;
  
  public static CallbackTask valueOf(CCSession session, Message message) {
    CallbackTask task = new CallbackTask();
    task.request = message;
    task.session = session;
    return task;
  }
  
  public Message call() throws Exception {
    try {
      CReqCallBack reqCallBack = (CReqCallBack)this.request;
      int index = CallBack.nextMsgId();
      reqCallBack.setIndex(index);
      reqCallBack.serialize();
      this.session.sendMessage(reqCallBack);
      ReentrantLock lock = new ReentrantLock();
      Condition condition = lock.newCondition();
      long maxTimeOut = 5000L;
      long startTime = System.currentTimeMillis();
      CallBackService callBackService = SpringUtils.getCallBackService();
      while (System.currentTimeMillis() - startTime <= maxTimeOut) {
        CallBack c = callBackService.removeCallBack(index);
        if (c != null)
          return c.getData(); 
        lock.lockInterruptibly();
        try {
          condition.await(10L, TimeUnit.MILLISECONDS);
        } catch (Exception exception) {
        
        } finally {
          lock.unlock();
        } 
      } 
    } finally {
      ((C2SSessionPoolFactory)SpringUtils.getBean(C2SSessionPoolFactory.class)).returnSession(this.session);
    } 
    return null;
  }
}
