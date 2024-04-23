package com.kitty.system.game.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class TitleInfo implements Serializable {
  private int serial;
  
  private int time;
  
  public TitleInfo() {}

  public TitleInfo(int serial, int time) {
    this.serial = serial;
    this.time = time;
  }
}
