package com.kitty.system.game.equip.helper.vo;

import com.kitty.system.game.bag.domain.BagEquip;

public class EquipInfo {
  private BagEquip equip;
  
  private String tips;
  
  public void setEquip(BagEquip equip) {
    this.equip = equip;
  }
  
  public void setTips(String tips) {
    this.tips = tips;
  }
  
  public BagEquip getEquip() {
    return this.equip;
  }
  
  public String getTips() {
    return this.tips;
  }
}
