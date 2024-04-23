package com.kitty.system.game.card.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65524)
public class RespCardFlyInfo extends Message {
  private String id;
  
  private int serial;
  
  private String rests;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRests(String rests) {
    this.rests = rests;
  }

  public String getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getRests() {
    return this.rests;
  }
  
  public RespCardFlyInfo() {}
  
  public RespCardFlyInfo(String id, int serial, String rests) {
    this.id = id;
    this.serial = serial;
    this.rests = rests;
  }
}
