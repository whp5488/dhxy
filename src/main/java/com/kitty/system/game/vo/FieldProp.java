package com.kitty.system.game.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldProp {
  private Integer type;
  
  private Integer value;

  public FieldProp() {}
  
  public FieldProp(Integer type, Integer value) {
    this.type = type;
    this.value = value;
  }
}
