package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class AutoFightSkillParam implements Serializable {
  private int objId;
  
  private int action;
  
  private int skillId;
  
  private int born;
  
  public void setObjId(int objId) {
    this.objId = objId;
  }
  
  public void setAction(int action) {
    this.action = action;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public void setBorn(int born) {
    this.born = born;
  }
  
  @ConstructorProperties({"objId", "action", "skillId", "born"})
  public AutoFightSkillParam(int objId, int action, int skillId, int born) {
    this.skillId = -1;
    this.born = 0;
    this.objId = objId;
    this.action = action;
    this.skillId = skillId;
    this.born = born;
  }
  
  public AutoFightSkillParam() {
    this.skillId = -1;
    this.born = 0;
  }
  
  public int getObjId() {
    return this.objId;
  }
  
  public int getAction() {
    return this.action;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
  
  public int getBorn() {
    return this.born;
  }
}
