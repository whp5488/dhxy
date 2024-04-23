package com.kitty.system.game.pet.domain.vo;

import java.io.Serializable;
import java.util.List;

public class SuitParam implements Serializable {
  private Integer serial;
  
  private String name;
  
  private List<Integer> petIds;
  
  public Integer getSerial() {
    return this.serial;
  }
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public List<Integer> getPetIds() {
    return this.petIds;
  }
  
  public void setPetIds(List<Integer> petIds) {
    this.petIds = petIds;
  }
  
  public int hashCode() {
    int result = 1;
    int prime = 31;
    result = this.serial.intValue() * prime;
    return result;
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof SuitParam) {
      SuitParam suitParam = (SuitParam)obj;
      if (this.serial.equals(suitParam.getSerial()))
        return true; 
      return false;
    } 
    return false;
  }
}
