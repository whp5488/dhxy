package com.kitty.system.game.fightnew.bean;

import java.util.HashMap;

public class AstrictSkill {
  private boolean isASide;
  
  private int round;
  
  public void setASide(boolean isASide) {
    this.isASide = isASide;
  }
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setSkillMap(HashMap<Integer, Integer> skillMap) {
    this.skillMap = skillMap;
  }

  public boolean isASide() {
    return this.isASide;
  }
  
  public int getRound() {
    return this.round;
  }
  
  private int time = 1;
  
  public int getTime() {
    return this.time;
  }
  
  private HashMap<Integer, Integer> skillMap = new HashMap<>();
  
  public HashMap<Integer, Integer> getSkillMap() {
    return this.skillMap;
  }
}
