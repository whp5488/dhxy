package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41009)
public class RespReplyServerTime extends Message {
  private String serverTime;
  
  public String getServerTime() {
    return this.serverTime;
  }
  
  public void setServerTime(String serverTime) {
    this.serverTime = serverTime;
  }
}
