package com.kitty.system.init.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class MonsterPower implements Serializable {
  private double a;
  
  private double b;
  
  private double c;
  
  @ConstructorProperties({"a", "b", "c"})
  public MonsterPower(double a, double b, double c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public MonsterPower() {}

}
