package com.kitty.system.init.domain.vo;

public class Prop {
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setOnset(int onset) {
    this.onset = onset;
  }
  
  public void setOver(int over) {
    this.over = over;
  }
  
  public void setProp(double prop) {
    this.prop = prop;
  }
  
  private int serial = 0;
  
  public int getSerial() {
    return this.serial;
  }
  
  private int onset = 0;
  
  public int getOnset() {
    return this.onset;
  }
  
  private int over = 0;
  
  public int getOver() {
    return this.over;
  }
  
  private double prop = 0.0D;
  
  public double getProp() {
    return this.prop;
  }
}
