package com.kitty.system.game.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FiedEquip {
  private Integer type;
  
  private Integer value;

  private double rate = 0.0D;

  public FiedEquip(Integer type, Integer value) {
    this.type = type;
    this.value = value;
  }
  
  public FiedEquip(Integer type, Integer value, double rate) {
    this.type = type;
    this.value = value;
    this.rate = rate;
  }
  
  public FiedEquip() {}
}
