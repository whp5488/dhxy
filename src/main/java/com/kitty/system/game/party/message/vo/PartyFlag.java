package com.kitty.system.game.party.message.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class PartyFlag implements Serializable {
  private short index;
  
  private byte state;
  
  public void setIndex(short index) {
    this.index = index;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public PartyFlag() {}
  
  @ConstructorProperties({"index", "state"})
  public PartyFlag(short index, byte state) {
    this.index = index;
    this.state = state;
  }
  
  public short getIndex() {
    return this.index;
  }
  
  public byte getState() {
    return this.state;
  }
}
