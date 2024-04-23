package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4288)
public class ReqChangeTitle extends Message {
  private int serial;
  
  private byte type;
  
  public int getSerial() {
    return this.serial;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
}
