package com.kitty.system.game.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class FieldByte implements Serializable {
  private Integer k;
  
  private Byte v;

  public FieldByte() {}
  
  public FieldByte(Integer k, Byte v) {
    this.k = k;
    this.v = v;
  }
}
