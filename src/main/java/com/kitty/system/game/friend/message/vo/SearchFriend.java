package com.kitty.system.game.friend.message.vo;

public class SearchFriend {
  private int id;
  
  private String name;
  
  private int serial;
  
  private byte rei;
  
  private short level;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public int getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public SearchFriend() {}
  
  public SearchFriend(int id, String name, int serial, byte rei, short level) {
    this.id = id;
    this.name = name;
    this.serial = serial;
    this.rei = rei;
    this.level = level;
  }
}
