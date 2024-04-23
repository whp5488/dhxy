package com.kitty.system.game.fightnew.book.acsrypet.pojo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class AcsryPetSkillParam implements Serializable {
  private int serial;
  
  private double pval;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPval(double pval) {
    this.pval = pval;
  }
  
  @ConstructorProperties({"serial", "pval"})
  public AcsryPetSkillParam(int serial, double pval) {
    this.serial = serial;
    this.pval = pval;
  }
  
  public AcsryPetSkillParam() {}
  
  public int getSerial() {
    return this.serial;
  }
  
  public double getPval() {
    return this.pval;
  }
}
