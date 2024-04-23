package com.kitty.system.game.party.message.vo;

public class RedBagInfo {
  private String redBgGid;
  
  private String senderName;
  
  private String msg;
  
  private int time;
  
  private short count;
  
  private byte state;
  
  private byte isSender;
  
  private byte isRecv;
  
  public void setRedBgGid(String redBgGid) {
    this.redBgGid = redBgGid;
  }
  
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setCount(short count) {
    this.count = count;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setIsSender(byte isSender) {
    this.isSender = isSender;
  }
  
  public void setIsRecv(byte isRecv) {
    this.isRecv = isRecv;
  }
  
  public String getRedBgGid() {
    return this.redBgGid;
  }
  
  public String getSenderName() {
    return this.senderName;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public short getCount() {
    return this.count;
  }
  
  public byte getState() {
    return this.state;
  }
  
  public byte getIsSender() {
    return this.isSender;
  }
  
  public byte getIsRecv() {
    return this.isRecv;
  }
}
