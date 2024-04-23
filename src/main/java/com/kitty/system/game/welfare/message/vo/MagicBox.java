package com.kitty.system.game.welfare.message.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class MagicBox implements Serializable {
  private int serial;
  
  private short num;
  
  private int bd;
  
  @ConstructorProperties({"serial", "num", "bd"})
  public MagicBox(int serial, short num, int bd) {
    this.serial = serial;
    this.num = num;
    this.bd = bd;
  }
  
  public MagicBox() {}

}
