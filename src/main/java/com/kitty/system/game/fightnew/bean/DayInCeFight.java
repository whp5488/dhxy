package com.kitty.system.game.fightnew.bean;

import java.util.ArrayList;
import java.util.List;

public class DayInCeFight {
  private short level;
  
  List<TianFuObject> tianFuList = new ArrayList<>();
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setTianFuList(List<TianFuObject> tianFuList) {
    this.tianFuList = tianFuList;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public List<TianFuObject> getTianFuList() {
    return this.tianFuList;
  }
}
