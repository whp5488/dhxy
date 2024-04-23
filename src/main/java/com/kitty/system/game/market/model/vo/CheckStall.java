package com.kitty.system.game.market.model.vo;

import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.tianfu.domain.RoleTianFu;

public class CheckStall {
  private Pet pet;
  
  private BagEquip equip;
  
  private RoleTianFu rune;
  
  public void setPet(Pet pet) {
    this.pet = pet;
  }
  
  public void setEquip(BagEquip equip) {
    this.equip = equip;
  }
  
  public void setRune(RoleTianFu rune) {
    this.rune = rune;
  }
  
  public Pet getPet() {
    return this.pet;
  }
  
  public BagEquip getEquip() {
    return this.equip;
  }
  
  public RoleTianFu getRune() {
    return this.rune;
  }
  
  public CheckStall() {}
  
  public CheckStall(Pet pet) {
    this.pet = pet;
  }
  
  public CheckStall(BagEquip equip) {
    this.equip = equip;
  }
  
  public CheckStall(RoleTianFu rune) {
    this.rune = rune;
  }
}
