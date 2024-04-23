package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 45388)
public class RespHmlNotify extends Message {
  private short serial;
  
  private byte state;
  
  public void setSerial(short serial) {
    this.serial = serial;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  @ConstructorProperties({"serial", "state"})
  public RespHmlNotify(short serial, byte state) {
    this.serial = serial;
    this.state = state;
  }
  
  public RespHmlNotify() {}
  
  public short getSerial() {
    return this.serial;
  }
  
  public byte getState() {
    return this.state;
  }
}
