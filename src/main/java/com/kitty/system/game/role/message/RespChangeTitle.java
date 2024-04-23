package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4289)
public class RespChangeTitle extends Message {
  private int serial;
  
  public int getSerial() {
    return this.serial;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public RespChangeTitle() {}
  
  public RespChangeTitle(int serial) {
    this.serial = serial;
  }
}
