package com.kitty.system.game.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
public class UnifiedValue {
  private String type;
  
  private int value;
  
  public UnifiedValue() {}
  
  @ConstructorProperties({"type", "value"})
  public UnifiedValue(String type, int value) {
    this.type = type;
    this.value = value;
  }

}
