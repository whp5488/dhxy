package com.kitty.system.game.enter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Position implements Serializable {
  private Integer x;
  
  private Integer y;
  
  public void setX(Integer x) {
    this.x = x;
  }
  
  public void setY(Integer y) {
    this.y = y;
  }
  
  public Position() {}
  
  @ConstructorProperties({"x", "y"})
  public Position(Integer x, Integer y) {
    this.x = x;
    this.y = y;
  }
  
  public Integer getX() {
    return this.x;
  }
  
  public Integer getY() {
    return this.y;
  }
}
