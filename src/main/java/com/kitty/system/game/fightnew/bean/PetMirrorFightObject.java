package com.kitty.system.game.fightnew.bean;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.mirror.model.PetMirror;
import com.kitty.system.game.pet.domain.Pet;

public class PetMirrorFightObject extends FightObject {
  private Pet pet;
  
  public Pet getPet() {
    return this.pet;
  }
  
  public PetMirrorFightObject(PetMirror petMirror, int id, FightObject master, int pos, boolean isASide) {
    Pet pet = new Pet();
    pet.setPetId(Integer.valueOf(id));
    pet.setPetName(petMirror.getName());
    this.pet = pet;
    setPos(pos);
    setName(petMirror.getName());
    setLevel(petMirror.getLevel());
    setId(Integer.valueOf(id));
    setSpeed(petMirror.getSpeed());
    setPhyPower(petMirror.getPhyPower());
    setMaxLife(petMirror.getMaxLife());
    setMaxMana(petMirror.getMaxMana());
    setCurrLife(petMirror.getMaxLife());
    setIcon(petMirror.getIcon());
    setSkillIds(SkillFunc.initPetSkill(petMirror.getSkillIds()));
    setHostId(master.getId().intValue());
    setASide(isASide);
  }
  
  public String toString() {
    return "PetMirrorFightObject{id=" + this.id + ", name='" + this.name + '\'' + '}';
  }
}
