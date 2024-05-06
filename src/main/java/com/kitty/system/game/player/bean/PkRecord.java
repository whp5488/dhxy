package com.kitty.system.game.player.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PkRecord {
  private long id;
  
  private int time;

  public PkRecord() {}
  
  public PkRecord(long id, int time) {
    this.id = id;
    this.time = time;
  }
}
