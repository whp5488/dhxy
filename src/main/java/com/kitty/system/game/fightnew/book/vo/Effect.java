package com.kitty.system.game.fightnew.book.vo;

import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import java.util.List;

public class Effect {
  public void setType(int type) {
    this.type = type;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setHurt(int hurt) {
    this.hurt = hurt;
  }
  
  public void setHit(double hit) {
    this.hit = hit;
  }
  
  public void setTargetHit(boolean isTargetHit) {
    this.isTargetHit = isTargetHit;
  }
  
  public void setList(List<FiedValue> list) {
    this.list = list;
  }
  
  public void setOtherType(String otherType) {
    this.otherType = otherType;
  }
  
  public void setSkillId(Integer SkillId) {
    this.SkillId = SkillId;
  }
  
  private int type = 0;
  
  public int getType() {
    return this.type;
  }
  
  private int time = 0;
  
  public int getTime() {
    return this.time;
  }
  
  private int hurt = 0;
  
  public int getHurt() {
    return this.hurt;
  }
  
  private double hit = -1.0D;
  
  public double getHit() {
    return this.hit;
  }
  
  private boolean isTargetHit = true;
  
  private List<FiedValue> list;
  
  private String otherType;
  
  private Integer SkillId;
  
  public boolean isTargetHit() {
    return this.isTargetHit;
  }
  
  public List<FiedValue> getList() {
    return this.list;
  }
  
  public String getOtherType() {
    return this.otherType;
  }
  
  public Integer getSkillId() {
    return this.SkillId;
  }
  
  public Effect(int type, int hurt) {
    this.type = type;
    this.hurt = hurt;
  }
  
  public Effect(int type, int time, int hurt, double hit, List<FiedValue> list) {
    this.type = type;
    this.time = time;
    this.hurt = hurt;
    this.hit = hit;
    this.list = list;
  }
  
  public List<FiedValue> getOrCreateList() {
    if (this.list == null)
      this.list = new ArrayList<>(); 
    return this.list;
  }
  
  public Effect() {}
}
