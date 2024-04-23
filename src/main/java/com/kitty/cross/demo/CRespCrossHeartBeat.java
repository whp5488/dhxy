package com.kitty.cross.demo;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = -2)
public class CRespCrossHeartBeat extends Message {
  private long time = System.currentTimeMillis();
  
  public long getTime() {
    return this.time;
  }
  
  public void setTime(long time) {
    this.time = time;
  }
}
