package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefMagical implements Serializable {
  private int id;
  
  private int serial;
  
  private String base;
  
  private String refining;
  
  private int gemCnt;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setBase(String base) {
    this.base = base;
  }
  
  public void setRefining(String refining) {
    this.refining = refining;
  }
  
  public void setGemCnt(int gemCnt) {
    this.gemCnt = gemCnt;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getBase() {
    return this.base;
  }
  
  public String getRefining() {
    return this.refining;
  }
  
  public int getGemCnt() {
    return this.gemCnt;
  }
}
