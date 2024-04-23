package com.kitty.system.game.bag.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class Temporary implements Serializable {
  private String id;
  
  private Integer serial;
  
  private Integer type;
  
  private Integer num;
  
  private Integer rests;
  
  @ConstructorProperties({"id", "serial", "type", "num", "rests"})
  public Temporary(String id, Integer serial, Integer type, Integer num, Integer rests) {
    this.id = id;
    this.serial = serial;
    this.type = type;
    this.num = num;
    this.rests = rests;
  }
  
  public Temporary() {}

}
