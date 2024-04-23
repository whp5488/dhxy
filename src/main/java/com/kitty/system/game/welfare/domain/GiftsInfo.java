package com.kitty.system.game.welfare.domain;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
public class GiftsInfo {
  private short serial;
  
  private byte state;

  @ConstructorProperties({"serial", "state"})
  public GiftsInfo(short serial, byte state) {
    this.serial = serial;
    this.state = state;
  }
  
  public GiftsInfo() {}

}
