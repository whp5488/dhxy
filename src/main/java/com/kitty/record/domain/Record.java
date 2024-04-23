package com.kitty.record.domain;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class Record implements Serializable {
  private long uid;
  
  private int roleId;
  
  private short type;
  
  private byte classify;
  
  private String string;
  
  private long time;
  
  private String marking;

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

}
