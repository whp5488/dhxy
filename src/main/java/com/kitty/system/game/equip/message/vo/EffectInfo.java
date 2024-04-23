package com.kitty.system.game.equip.message.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class EffectInfo implements Serializable {
  private int serial;
  
  private byte state;
  
  private int time;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  @ConstructorProperties({"serial", "state", "time"})
  public EffectInfo(int serial, byte state, int time) {
    this.serial = serial;
    this.state = state;
    this.time = time;
  }
  
  public EffectInfo() {}
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getState() {
    return this.state;
  }
  
  public int getTime() {
    return this.time;
  }
}
