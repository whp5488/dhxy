package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53752)
public class ReqKickOffClient extends Message {
  private String reason;
  
  public String getReason() {
    return this.reason;
  }
  
  public void setReason(String reason) {
    this.reason = reason;
  }
}
