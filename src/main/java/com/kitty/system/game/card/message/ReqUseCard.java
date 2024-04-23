package com.kitty.system.game.card.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 250)
public class ReqUseCard extends Message {
  private String id;
  
  private int serial;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public String getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
}
