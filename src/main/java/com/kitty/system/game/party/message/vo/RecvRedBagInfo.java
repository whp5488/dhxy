package com.kitty.system.game.party.message.vo;

public class RecvRedBagInfo {
  private String name;
  
  private int coin;
  
  private int time;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setCoin(int coin) {
    this.coin = coin;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getCoin() {
    return this.coin;
  }
  
  public int getTime() {
    return this.time;
  }
}
