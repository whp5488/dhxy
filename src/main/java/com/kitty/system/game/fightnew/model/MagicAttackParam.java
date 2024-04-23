package com.kitty.system.game.fightnew.model;

import com.kitty.system.game.fightnew.FightObject;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

public class MagicAttackParam implements Serializable {
  private boolean isDodge;
  
  private List<Integer> positiveStatus;
  
  private FightObject target;
  
  private int hurt;
  
  private List<Integer> clearStatusList;
  
  private int targetReduceMp;
  
  private FightObject sideTarget;
  
  public void setDodge(boolean isDodge) {
    this.isDodge = isDodge;
  }
  
  public void setPositiveStatus(List<Integer> positiveStatus) {
    this.positiveStatus = positiveStatus;
  }
  
  public void setTarget(FightObject target) {
    this.target = target;
  }
  
  public void setHurt(int hurt) {
    this.hurt = hurt;
  }
  
  public void setClearStatusList(List<Integer> clearStatusList) {
    this.clearStatusList = clearStatusList;
  }
  
  public void setTargetReduceMp(int targetReduceMp) {
    this.targetReduceMp = targetReduceMp;
  }
  
  public void setSideTarget(FightObject sideTarget) {
    this.sideTarget = sideTarget;
  }
  
  @ConstructorProperties({"isDodge", "positiveStatus", "target", "hurt", "clearStatusList", "targetReduceMp", "sideTarget"})
  public MagicAttackParam(boolean isDodge, List<Integer> positiveStatus, FightObject target, int hurt, List<Integer> clearStatusList, int targetReduceMp, FightObject sideTarget) {
    this.isDodge = isDodge;
    this.positiveStatus = positiveStatus;
    this.target = target;
    this.hurt = hurt;
    this.clearStatusList = clearStatusList;
    this.targetReduceMp = targetReduceMp;
    this.sideTarget = sideTarget;
  }
  
  public MagicAttackParam() {}
  
  public String toString() {
    return "MagicAttackParam(isDodge=" + isDodge() + ", positiveStatus=" + getPositiveStatus() + ", target=" + getTarget() + ", hurt=" + getHurt() + ", clearStatusList=" + getClearStatusList() + ", targetReduceMp=" + getTargetReduceMp() + ", sideTarget=" + getSideTarget() + ")";
  }
  
  public boolean isDodge() {
    return this.isDodge;
  }
  
  public List<Integer> getPositiveStatus() {
    return this.positiveStatus;
  }
  
  public FightObject getTarget() {
    return this.target;
  }
  
  public int getHurt() {
    return this.hurt;
  }
  
  public List<Integer> getClearStatusList() {
    return this.clearStatusList;
  }
  
  public int getTargetReduceMp() {
    return this.targetReduceMp;
  }
  
  public FightObject getSideTarget() {
    return this.sideTarget;
  }
}
