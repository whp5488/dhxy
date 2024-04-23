package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;

@MessageMeta(module = 41000)
public class RespVerifyInfo extends Message {
  private String id;
  
  private String name;
  
  private byte rei;
  
  private short level;
  
  private int serial;
  
  private List<FiedValue> rests;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRests(List<FiedValue> rests) {
    this.rests = rests;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public List<FiedValue> getRests() {
    return this.rests;
  }
  
  public RespVerifyInfo() {}
  
  public RespVerifyInfo(String id, String name, byte rei, short level, int serial) {
    this.id = id;
    this.name = name;
    this.rei = rei;
    this.level = level;
    this.serial = serial;
  }
  
  public RespVerifyInfo(String id, String name, byte rei, short level, int serial, List<FiedValue> rests) {
    this.id = id;
    this.name = name;
    this.rei = rei;
    this.level = level;
    this.serial = serial;
    this.rests = rests;
  }
}
