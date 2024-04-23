package com.kitty.system.game.onlinemall.message.vo;

import com.kitty.system.game.activity.domain.WeekTreasure;
import java.io.Serializable;

public class WeekTreasureInfo implements Serializable {
  private String id;
  
  private String rId;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setRId(String rId) {
    this.rId = rId;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setShare(short share) {
    this.share = share;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getRId() {
    return this.rId;
  }
  
  private int serial = 0;
  
  public int getSerial() {
    return this.serial;
  }
  
  private short share = 0;
  
  public short getShare() {
    return this.share;
  }
  
  public WeekTreasureInfo(WeekTreasure obj) {
    this.id = String.valueOf(obj.getId());
    this.rId = String.valueOf(obj.getRId());
    this.serial = obj.getSerial();
    this.share = obj.getShare();
  }
  
  public WeekTreasureInfo() {}
}
