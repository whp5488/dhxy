package com.kitty.listener.event.ranking;

import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;

public class PetChangeEvent extends BaseGameEvent {
  private Pet pet;
  
  private List<PetEquip> equips;
  
  public PetChangeEvent(EventType evtType, Pet pet, List<PetEquip> equips) {
    super(evtType);
    this.pet = pet;
    this.equips = equips;
  }
  
  public Pet getPet() {
    return this.pet;
  }
  
  public List<PetEquip> getEquips() {
    return this.equips;
  }
}
