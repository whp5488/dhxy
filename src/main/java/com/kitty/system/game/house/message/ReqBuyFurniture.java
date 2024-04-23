package com.kitty.system.game.house.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20577)
public class ReqBuyFurniture extends Message {
  private int serial;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public int getSerial() {
    return this.serial;
  }
}
