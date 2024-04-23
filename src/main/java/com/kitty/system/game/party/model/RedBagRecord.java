package com.kitty.system.game.party.model;

public class RedBagRecord {
  private long redBagId;
  
  private String senderName;
  
  private int coin;
  
  private int time;
  
  public void setRedBagId(long redBagId) {
    this.redBagId = redBagId;
  }
  
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }
  
  public void setCoin(int coin) {
    this.coin = coin;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public long getRedBagId() {
    return this.redBagId;
  }
  
  public String getSenderName() {
    return this.senderName;
  }
  
  public int getCoin() {
    return this.coin;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public RedBagRecord() {}
  
  public RedBagRecord(long redBagId, String senderName, int coin) {
    this.redBagId = redBagId;
    this.senderName = senderName;
    this.coin = coin;
    this.time = (int)(System.currentTimeMillis() / 1000L);
  }
}
