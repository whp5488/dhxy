package com.kitty.system.game.bag.message.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class PotholingVo implements Serializable {
  private Integer serial;
  
  private Integer num;
  
  @ConstructorProperties({"serial", "num"})
  public PotholingVo(Integer serial, Integer num) {
    this.serial = serial;
    this.num = num;
  }
  
  public PotholingVo() {}

}
