package com.kitty.system.game.email.domain;

import java.beans.ConstructorProperties;

public class StatusParam {
  private long key;
  
  private byte value;
  
  public void setKey(long key) {
    this.key = key;
  }
  
  public void setValue(byte value) {
    this.value = value;
  }
  
  public StatusParam() {}
  
  @ConstructorProperties({"key", "value"})
  public StatusParam(long key, byte value) {
    this.key = key;
    this.value = value;
  }
  
  public long getKey() {
    return this.key;
  }
  
  public byte getValue() {
    return this.value;
  }
}
