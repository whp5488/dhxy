package com.kitty.system.game.welfare.message.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
public class LuckDrawInfo {
  private int serial;
  
  private int num;
  
  public LuckDrawInfo() {}
  
  @ConstructorProperties({"serial", "num"})
  public LuckDrawInfo(int serial, int num) {
    this.serial = serial;
    this.num = num;
  }

}
