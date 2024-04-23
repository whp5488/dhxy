package com.kitty.common.utils.vo;

import java.beans.ConstructorProperties;

public class Approximate {
  private int id;
  
  private int num;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setNum(int num) {
    this.num = num;
  }
  
  public Approximate() {}
  
  @ConstructorProperties({"id", "num"})
  public Approximate(int id, int num) {
    this.id = id;
    this.num = num;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getNum() {
    return this.num;
  }
}
