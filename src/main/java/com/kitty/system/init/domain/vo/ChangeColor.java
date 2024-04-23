package com.kitty.system.init.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.Arrays;

@Setter
@Getter
public class ChangeColor {
  private int serial;
  
  private String name;
  
  private int[] goodsNum;
  
  public ChangeColor() {}
  
  @ConstructorProperties({"serial", "name", "goodsNum"})
  public ChangeColor(int serial, String name, int[] goodsNum) {
    this.serial = serial;
    this.name = name;
    this.goodsNum = goodsNum;
  }

}
