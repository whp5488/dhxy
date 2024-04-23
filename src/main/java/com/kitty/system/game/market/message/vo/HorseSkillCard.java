package com.kitty.system.game.market.message.vo;

import java.io.Serializable;

public class HorseSkillCard implements Serializable {
  private int growUp;
  
  private short level;
  
  private String skill;
  
  public void setGrowUp(int growUp) {
    this.growUp = growUp;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setSkill(String skill) {
    this.skill = skill;
  }
  
  public int getGrowUp() {
    return this.growUp;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public String getSkill() {
    return this.skill;
  }
  
  public HorseSkillCard() {}
  
  public HorseSkillCard(int growUp, short level, String skill) {
    this.growUp = growUp;
    this.level = level;
    this.skill = skill;
  }
}
