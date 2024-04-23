package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 24112)
public class ReqLoginPreview extends Message {
  private String sid;
  
  public String getSid() {
    return this.sid;
  }
  
  public void setSid(String sid) {
    this.sid = sid;
  }
}
