package com.kitty.record.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Record implements Serializable {
  private long uid;
  
  private int roleId;
  
  private short type;
  
  private byte classify;
  
  private String string;
  
  private long time;
  
  private String marking;
  
  public void setUid(long uid) {
    this.uid = uid;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setType(short type) {
    this.type = type;
  }
  
  public void setClassify(byte classify) {
    this.classify = classify;
  }
  
  public void setString(String string) {
    this.string = string;
  }
  
  public void setTime(long time) {
    this.time = time;
  }
  
  public void setMarking(String marking) {
    this.marking = marking;
  }

  @ConstructorProperties({"uid", "roleId", "type", "classify", "string", "time", "marking"})
  public Record(long uid, int roleId, short type, byte classify, String string, long time, String marking) {
    this.uid = uid;
    this.roleId = roleId;
    this.type = type;
    this.classify = classify;
    this.string = string;
    this.time = time;
    this.marking = marking;
  }
  
  public Record() {}
  
  public long getUid() {
    return this.uid;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public short getType() {
    return this.type;
  }
  
  public byte getClassify() {
    return this.classify;
  }
  
  public String getString() {
    return this.string;
  }
  
  public long getTime() {
    return this.time;
  }
  
  public String getMarking() {
    return this.marking;
  }
}
