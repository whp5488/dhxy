package com.kitty.system.game.market.model;

public class StallItem {
  private String id;
  
  private int serial;
  
  private String price;
  
  private byte status;
  
  private int startTime;
  
  private int endTime;
  
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
  
  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }
  
  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }
  
  public void setAmount(short amount) {
    this.amount = amount;
  }
  
  public void setNum(int num) {
    this.num = num;
  }
  
  public void setFocus(int focus) {
    this.focus = focus;
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
  
  public int getStartTime() {
    return this.startTime;
  }
  
  public int getEndTime() {
    return this.endTime;
  }
  
  private short amount = 1;
  
  public short getAmount() {
    return this.amount;
  }
  
  private int num = 0;
  
  public int getNum() {
    return this.num;
  }
  
  private int focus = 0;
  
  private String extra;
  
  public int getFocus() {
    return this.focus;
  }
  
  public String getExtra() {
    return this.extra;
  }
}
