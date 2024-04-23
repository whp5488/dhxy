package com.kitty.system.game.house.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20586)
public class ReqHouseGoHome extends Message {
  
  public void setType(byte type) {
    this.type = type;
  }
  
  private byte type = 0;
  
  public byte getType() {
    return this.type;
  }
}
