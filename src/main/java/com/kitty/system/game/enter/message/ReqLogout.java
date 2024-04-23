package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4)
public class ReqLogout extends Message {
  private Integer reason;
  
  public void setReason(Integer reason) {
    this.reason = reason;
  }
  
  public Integer getReason() {
    return this.reason;
  }
}
