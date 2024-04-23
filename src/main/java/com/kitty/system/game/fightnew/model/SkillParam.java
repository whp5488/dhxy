package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class SkillParam implements Serializable {
  private int skillId;
  
  private int proficient;
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public void setProficient(int proficient) {
    this.proficient = proficient;
  }
  
  @ConstructorProperties({"skillId", "proficient"})
  public SkillParam(int skillId, int proficient) {
    this.skillId = skillId;
    this.proficient = proficient;
  }
  
  public SkillParam() {}
  
  public int getSkillId() {
    return this.skillId;
  }
  
  public int getProficient() {
    return this.proficient;
  }
}
