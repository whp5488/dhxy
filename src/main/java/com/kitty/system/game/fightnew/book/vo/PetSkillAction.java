package com.kitty.system.game.fightnew.book.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class PetSkillAction implements Serializable {
  private int petId;
  
  private int skillId;
  
  private int targetId;
  
  public void setPetId(int petId) {
    this.petId = petId;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }
  
  @ConstructorProperties({"petId", "skillId", "targetId"})
  public PetSkillAction(int petId, int skillId, int targetId) {
    this.petId = petId;
    this.skillId = skillId;
    this.targetId = targetId;
  }
  
  public int getPetId() {
    return this.petId;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
  
  public int getTargetId() {
    return this.targetId;
  }
}
