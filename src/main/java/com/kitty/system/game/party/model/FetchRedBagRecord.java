package com.kitty.system.game.party.model;

public class FetchRedBagRecord {
  private long roleUid;
  
  private short coin;
  
  private int time;
  
  public void setRoleUid(long roleUid) {
    this.roleUid = roleUid;
  }
  
  public void setCoin(short coin) {
    this.coin = coin;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public long getRoleUid() {
    return this.roleUid;
  }
  
  public short getCoin() {
    return this.coin;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public FetchRedBagRecord() {}
  
  public FetchRedBagRecord(long roleUid, short coin) {
    this.roleUid = roleUid;
    this.coin = coin;
    this.time = (int)(System.currentTimeMillis() / 1000L);
  }
}
