package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49000)
public class RespActivityOpenNotice extends Message {
  
  public void setType(short type) {
    this.type = type;
  }
  
  private short type = 1010;
  
  public short getType() {
    return this.type;
  }
}
