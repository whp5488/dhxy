package com.kitty.system.game.activity.model.user.lu;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class WaterEventSet implements Serializable {
  public void setRound(byte round) {
    this.round = round;
  }
  
  public void setMax(byte max) {
    this.max = max;
  }
  
  public void setSeason(int season) {
    this.season = season;
  }
  
  @ConstructorProperties({"round", "max", "season"})
  public WaterEventSet(byte round, byte max, int season) {
    this.round = round;
    this.max = max;
    this.season = season;
  }
  
  private byte round = 1;
  
  public byte getRound() {
    return this.round;
  }
  
  private byte max = 3;
  
  public byte getMax() {
    return this.max;
  }
  
  private int season = 1;
  
  public int getSeason() {
    return this.season;
  }
  
  public WaterEventSet() {}
}
