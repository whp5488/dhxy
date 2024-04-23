package com.kitty.system.game.fightnew.messages.vo;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.pet.domain.Pet;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class CombatAddObject implements Serializable {
  private int id;
  
  private int serial;
  
  private int hostId;
  
  private int leader;
  
  private int position;
  
  private int customIcon;
  
  private int weapon_icon;
  
  private List<Integer> effects;
  
  private List<Integer> suit_light_effects;
  
  private List<FiedValue> list;
  
  private List<Integer> skillIds;

  public void addSkillIds(Pet pet) {
    List<Integer> skillIds = FightOtherUtil.addSkillIds(pet);
    if (skillIds.size() > 0)
      this.skillIds = skillIds; 
  }
}
