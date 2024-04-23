package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4301)
public class ReqGoodInfo extends Message {
  private short serial;
  
  private String id;
  
  public void setSerial(short serial) {
    this.serial = serial;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public short getSerial() {
    return this.serial;
  }
  
  public String getId() {
    return this.id;
  }
}
