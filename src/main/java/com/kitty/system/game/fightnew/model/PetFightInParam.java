package com.kitty.system.game.fightnew.model;

import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.pet.domain.Pet;
import java.io.Serializable;
import java.util.List;

public class PetFightInParam implements Serializable {
  private int petId;
  
  private List<Integer> skillIds;
  
  public void setPetId(int petId) {
    this.petId = petId;
  }
  
  public void setSkillIds(List<Integer> skillIds) {
    this.skillIds = skillIds;
  }
  
  public int getPetId() {
    return this.petId;
  }
  
  public List<Integer> getSkillIds() {
    return this.skillIds;
  }
  
  public void addSkillIds(Pet pet) {
    List<Integer> skillIds = FightOtherUtil.addSkillIds(pet);
    if (skillIds.size() > 0)
      this.skillIds = skillIds; 
  }
}
