package com.kitty.system.game.fightnew.book.tianfu.pojo;

import java.io.Serializable;

public class TianFuAddition implements Serializable {
  private short dayInCeLevel;
  
  private byte tianFuLevel;
  
  public void setDayInCeLevel(short dayInCeLevel) {
    this.dayInCeLevel = dayInCeLevel;
  }
  
  public void setTianFuLevel(byte tianFuLevel) {
    this.tianFuLevel = tianFuLevel;
  }
  
  public short getDayInCeLevel() {
    return this.dayInCeLevel;
  }
  
  public byte getTianFuLevel() {
    return this.tianFuLevel;
  }
  
  public TianFuAddition(short dayInCeLevel, byte tianFuLevel) {
    this.dayInCeLevel = dayInCeLevel;
    this.tianFuLevel = tianFuLevel;
  }
}
