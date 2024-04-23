package com.kitty.system.game.fightnew.imbue.bean;

import java.beans.ConstructorProperties;

public class ImbueParam {
  private int sr;
  
  private double value;
  
  private String others;
  
  public void setSr(int sr) {
    this.sr = sr;
  }
  
  public void setValue(double value) {
    this.value = value;
  }
  
  public void setOthers(String others) {
    this.others = others;
  }
  
  @ConstructorProperties({"sr", "value", "others"})
  public ImbueParam(int sr, double value, String others) {
    this.sr = sr;
    this.value = value;
    this.others = others;
  }
  
  public ImbueParam() {}
  
  public int getSr() {
    return this.sr;
  }
  
  public double getValue() {
    return this.value;
  }
  
  public String getOthers() {
    return this.others;
  }
  
  public ImbueParam(int sr, double value) {
    this.sr = sr;
    this.value = value;
  }
}
