package com.kitty.system.game.general.message.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class DomesticateInfo implements Serializable {
  private String id;
  
  private int time;
  
  private int num;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setNum(int num) {
    this.num = num;
  }
  
  public void setResidue(short residue) {
    this.residue = residue;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  @ConstructorProperties({"id", "time", "num", "residue", "type"})
  public DomesticateInfo(String id, int time, int num, short residue, byte type) {
    this.id = id;
    this.time = time;
    this.num = num;
    this.residue = residue;
    this.type = type;
  }
  
  public String getId() {
    return this.id;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public int getNum() {
    return this.num;
  }
  
  private short residue = -1;
  
  private byte type;
  
  public short getResidue() {
    return this.residue;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public DomesticateInfo() {}
}
