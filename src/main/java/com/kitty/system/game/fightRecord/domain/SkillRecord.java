package com.kitty.system.game.fightRecord.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class SkillRecord implements Serializable {
  private int o;
  
  private int r;
  
  private int p;
  
  private long a;
  
  private long t;
  
  private int s;
  
  public void setO(int o) {
    this.o = o;
  }
  
  public void setR(int r) {
    this.r = r;
  }
  
  public void setP(int p) {
    this.p = p;
  }
  
  public void setA(long a) {
    this.a = a;
  }
  
  public void setT(long t) {
    this.t = t;
  }
  
  public void setS(int s) {
    this.s = s;
  }
  
  public SkillRecord() {}
  
  @ConstructorProperties({"o", "r", "p", "a", "t", "s"})
  public SkillRecord(int o, int r, int p, long a, long t, int s) {
    this.o = o;
    this.r = r;
    this.p = p;
    this.a = a;
    this.t = t;
    this.s = s;
  }
  
  public int getO() {
    return this.o;
  }
  
  public int getR() {
    return this.r;
  }
  
  public int getP() {
    return this.p;
  }
  
  public long getA() {
    return this.a;
  }
  
  public long getT() {
    return this.t;
  }
  
  public int getS() {
    return this.s;
  }
}
