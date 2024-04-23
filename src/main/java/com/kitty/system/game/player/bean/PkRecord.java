package com.kitty.system.game.player.bean;

public class PkRecord {
  private long id;
  
  private int time;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public long getId() {
    return this.id;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public PkRecord() {}
  
  public PkRecord(long id, int time) {
    this.id = id;
    this.time = time;
  }
}
