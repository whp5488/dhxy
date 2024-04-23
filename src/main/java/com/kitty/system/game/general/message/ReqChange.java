package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45061)
public class ReqChange extends Message {
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  private int serial = 0;
  
  public int getSerial() {
    return this.serial;
  }
  
  private byte type = 0;
  
  public byte getType() {
    return this.type;
  }
}
