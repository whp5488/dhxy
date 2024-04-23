package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4278)
public class ReqModifyNotice extends Message {
  private String msg;
  
  private byte type;
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public byte getType() {
    return this.type;
  }
}
