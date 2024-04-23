package com.kitty.system.game.fightnew.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BeforeActionSkill {
  private boolean isASide;
  
  private boolean passiveAction;
  
  private int round;
  
  public void setASide(boolean isASide) {
    this.isASide = isASide;
  }
  
  public void setPassiveAction(boolean passiveAction) {
    this.passiveAction = passiveAction;
  }
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setRoleSkillMap(Map<Integer, List<Integer>> roleSkillMap) {
    this.roleSkillMap = roleSkillMap;
  }
  
  public void setSkills(List<Integer> skills) {
    this.skills = skills;
  }
  
  public boolean isASide() {
    return this.isASide;
  }
  
  public boolean isPassiveAction() {
    return this.passiveAction;
  }
  
  public int getRound() {
    return this.round;
  }
  
  private Map<Integer, List<Integer>> roleSkillMap = new LinkedHashMap<>();
  
  public Map<Integer, List<Integer>> getRoleSkillMap() {
    return this.roleSkillMap;
  }
  
  private List<Integer> skills = new ArrayList<>();
  
  public List<Integer> getSkills() {
    return this.skills;
  }
}
