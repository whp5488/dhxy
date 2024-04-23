package com.kitty.system.game.role.message.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class Instruct implements Serializable {
  private short serial;
  
  private byte index;
  
  private String str;
  
  @ConstructorProperties({"serial", "index", "str"})
  public Instruct(short serial, byte index, String str) {
    this.serial = serial;
    this.index = index;
    this.str = str;
  }
  
  public Instruct() {}

}
