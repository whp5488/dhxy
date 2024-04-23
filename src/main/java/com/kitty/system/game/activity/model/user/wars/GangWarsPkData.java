package com.kitty.system.game.activity.model.user.wars;

import java.io.Serializable;

public class GangWarsPkData implements Serializable {
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setPosition(byte position) {
    this.position = position;
  }
  
  public void setRebirth(int rebirth) {
    this.rebirth = rebirth;
  }
  
  public String toString() {
    return "GangWarsPkData(state=" + getState() + ", position=" + getPosition() + ", rebirth=" + getRebirth() + ")";
  }
  
  private byte state = 0;
  
  public byte getState() {
    return this.state;
  }
  
  private byte position = 0;
  
  public byte getPosition() {
    return this.position;
  }
  
  private int rebirth = 0;
  
  public int getRebirth() {
    return this.rebirth;
  }
  
  public void updateState(byte state) {
    this.state = state;
  }
  
  public void updatePosition(byte position) {
    this.position = position;
  }
}
