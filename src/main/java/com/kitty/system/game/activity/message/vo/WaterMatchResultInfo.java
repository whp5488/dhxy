package com.kitty.system.game.activity.message.vo;

import java.io.Serializable;
import java.util.List;

public class WaterMatchResultInfo implements Serializable {
  private List<Integer> list;
  
  public void setList(List<Integer> list) {
    this.list = list;
  }
  
  public void setState(byte state) {
    this.state = state;
  }

  public List<Integer> getList() {
    return this.list;
  }
  
  private byte state = 0;
  
  public byte getState() {
    return this.state;
  }
  
  public WaterMatchResultInfo(List<Integer> list, byte state) {
    this.list = list;
    this.state = state;
  }
  
  public WaterMatchResultInfo() {}
}
