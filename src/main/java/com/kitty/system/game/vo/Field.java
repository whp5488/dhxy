package com.kitty.system.game.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class Field implements Serializable {
  private Integer pos;
  
  private Integer min;
  
  private Integer max;
  
  public Field() {}
  
  @ConstructorProperties({"pos", "min", "max"})
  public Field(Integer pos, Integer min, Integer max) {
    this.pos = pos;
    this.min = min;
    this.max = max;
  }

}
