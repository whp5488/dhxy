package com.kitty.system.game.house.message.vo;

import com.kitty.system.game.house.model.House;
import java.beans.ConstructorProperties;
import java.io.Serializable;

public class HouseEasy implements Serializable {
  private String id;
  
  private String name;
  
  private int motif;
  
  private byte state;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setMotif(int motif) {
    this.motif = motif;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public HouseEasy() {}
  
  @ConstructorProperties({"id", "name", "motif", "state"})
  public HouseEasy(String id, String name, int motif, byte state) {
    this.id = id;
    this.name = name;
    this.motif = motif;
    this.state = state;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getMotif() {
    return this.motif;
  }
  
  public byte getState() {
    return this.state;
  }
  
  public HouseEasy(House house) {
    this.id = String.valueOf(house.getId());
    this.name = house.getName();
    this.motif = house.getMotif();
    this.state = house.getState();
  }
}
