package com.kitty.common.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Lottery {
  private Integer serial;
  
  private String name;
  
  private Double bili;

  private int num = 0;

  public Lottery(Integer serial, String name, Double bili, int num) {
    this.serial = serial;
    this.name = name;
    this.bili = bili;
    this.num = num;
  }
  
  public Lottery(Integer serial, String name, Double bili) {
    this.serial = serial;
    this.name = name;
    this.bili = bili;
  }
  
  public Lottery(Integer serial) {
    this.serial = serial;
  }
}
