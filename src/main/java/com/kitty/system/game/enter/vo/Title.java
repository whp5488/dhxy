package com.kitty.system.game.enter.vo;

import com.kitty.system.game.vo.TitleInfo;

public class Title {
  private int serial;
  
  private int time;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public Title() {}
  
  public Title(int serial, int time) {
    this.serial = serial;
    this.time = time;
  }
  
  public Title(TitleInfo titleInfo) {
    this.serial = titleInfo.getSerial();
    this.time = titleInfo.getTime();
  }
}
