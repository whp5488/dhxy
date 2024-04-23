package com.kitty.system.game.email.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class EmailSystemAccessory implements Serializable {
  private byte type;
  
  private int serial;
  
  private String num;
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setNum(String num) {
    this.num = num;
  }
  
  public EmailSystemAccessory() {}
  
  @ConstructorProperties({"type", "serial", "num"})
  public EmailSystemAccessory(byte type, int serial, String num) {
    this.type = type;
    this.serial = serial;
    this.num = num;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getNum() {
    return this.num;
  }
}
