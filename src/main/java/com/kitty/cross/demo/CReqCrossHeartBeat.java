package com.kitty.cross.demo;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = -1)
public class CReqCrossHeartBeat extends Message {
  private long time = System.currentTimeMillis();
  
  public long getTime() {
    return this.time;
  }
  
  public void setTime(long time) {
    this.time = time;
  }
}
