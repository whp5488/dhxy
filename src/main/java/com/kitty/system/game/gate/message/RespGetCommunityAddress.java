package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45697)
public class RespGetCommunityAddress extends Message {
  private String addr;
  
  public String getAddr() {
    return this.addr;
  }
  
  public void setAddr(String addr) {
    this.addr = addr;
  }
}
