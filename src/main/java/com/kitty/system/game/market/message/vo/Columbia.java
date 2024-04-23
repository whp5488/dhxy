package com.kitty.system.game.market.message.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Columbia implements Serializable {
  private String id;
  
  private int serial;
  
  private String price;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPrice(String price) {
    this.price = price;
  }
  
  @ConstructorProperties({"id", "serial", "price"})
  public Columbia(String id, int serial, String price) {
    this.id = id;
    this.serial = serial;
    this.price = price;
  }
  
  public Columbia() {}
  
  public String getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getPrice() {
    return this.price;
  }
}
