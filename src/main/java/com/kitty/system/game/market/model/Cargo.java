package com.kitty.system.game.market.model;

public class Cargo {
  private String id;
  
  private int serial;
  
  private String price;
  
  private byte status;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPrice(String price) {
    this.price = price;
  }
  
  public void setStatus(byte status) {
    this.status = status;
  }
  
  public void setAmount(short amount) {
    this.amount = amount;
  }
  
  public void setSaleAmount(short saleAmount) {
    this.saleAmount = saleAmount;
  }
  
  public void setShowEndTime(int showEndTime) {
    this.showEndTime = showEndTime;
  }
  
  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }
  
  public void setExtra(String extra) {
    this.extra = extra;
  }
  
  public String getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getPrice() {
    return this.price;
  }
  
  public byte getStatus() {
    return this.status;
  }
  
  private short amount = 1;
  
  public short getAmount() {
    return this.amount;
  }
  
  private short saleAmount = 1;
  
  private int showEndTime;
  
  private int endTime;
  
  private String extra;
  
  public short getSaleAmount() {
    return this.saleAmount;
  }
  
  public int getShowEndTime() {
    return this.showEndTime;
  }
  
  public int getEndTime() {
    return this.endTime;
  }
  
  public String getExtra() {
    return this.extra;
  }
}
