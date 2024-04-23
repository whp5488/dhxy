package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;

public class LifeManaParam {
  private String saveLife;
  
  private String saveMana;
  
  private int maxLife;
  
  private int maxMana;
  
  private int currLife;
  
  private int currMana;
  
  public void setSaveLife(String saveLife) {
    this.saveLife = saveLife;
  }
  
  public void setSaveMana(String saveMana) {
    this.saveMana = saveMana;
  }
  
  public void setMaxLife(int maxLife) {
    this.maxLife = maxLife;
  }
  
  public void setMaxMana(int maxMana) {
    this.maxMana = maxMana;
  }
  
  public void setCurrLife(int currLife) {
    this.currLife = currLife;
  }
  
  public void setCurrMana(int currMana) {
    this.currMana = currMana;
  }
  
  public LifeManaParam() {}
  
  @ConstructorProperties({"saveLife", "saveMana", "maxLife", "maxMana", "currLife", "currMana"})
  public LifeManaParam(String saveLife, String saveMana, int maxLife, int maxMana, int currLife, int currMana) {
    this.saveLife = saveLife;
    this.saveMana = saveMana;
    this.maxLife = maxLife;
    this.maxMana = maxMana;
    this.currLife = currLife;
    this.currMana = currMana;
  }
  
  public String getSaveLife() {
    return this.saveLife;
  }
  
  public String getSaveMana() {
    return this.saveMana;
  }
  
  public int getMaxLife() {
    return this.maxLife;
  }
  
  public int getMaxMana() {
    return this.maxMana;
  }
  
  public int getCurrLife() {
    return this.currLife;
  }
  
  public int getCurrMana() {
    return this.currMana;
  }
}
