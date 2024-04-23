package com.kitty.system.game.vo.init;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class Monster implements Serializable {
  private Integer pos;
  
  private Integer serial;
  
  public Monster() {}
  
  @ConstructorProperties({"pos", "serial"})
  public Monster(Integer pos, Integer serial) {
    this.pos = pos;
    this.serial = serial;
  }

}
