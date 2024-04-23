package com.kitty.system.game.market.message.vo;

import java.io.Serializable;

public class PetEquipCard implements Serializable {
  private String id;
  
  private int serial;
  
  private byte pj;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPj(byte pj) {
    this.pj = pj;
  }
  
  public String getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getPj() {
    return this.pj;
  }
  
  public PetEquipCard() {}
  
  public PetEquipCard(long id, int serial, byte pj) {
    this.id = String.valueOf(id);
    this.serial = serial;
    this.pj = pj;
  }
}
