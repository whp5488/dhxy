package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45096)
public class ReqMarketSearchItem extends Message {
  private int serial;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public int getSerial() {
    return this.serial;
  }
}
