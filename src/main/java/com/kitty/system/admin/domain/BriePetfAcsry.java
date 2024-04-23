package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriePetfAcsry implements Serializable {
  private int id;
  
  private byte grade;
  
  private byte pos;
  
  private byte pj;
  
  private String base;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setGrade(byte grade) {
    this.grade = grade;
  }
  
  public void setPos(byte pos) {
    this.pos = pos;
  }
  
  public void setPj(byte pj) {
    this.pj = pj;
  }
  
  public void setBase(String base) {
    this.base = base;
  }
  
  public int getId() {
    return this.id;
  }
  
  public byte getGrade() {
    return this.grade;
  }
  
  public byte getPos() {
    return this.pos;
  }
  
  public byte getPj() {
    return this.pj;
  }
  
  public String getBase() {
    return this.base;
  }
}
