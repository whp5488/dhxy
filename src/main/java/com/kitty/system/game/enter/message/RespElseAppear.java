package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65530)
public class RespElseAppear extends Message {
  private int id;
  
  private short x;
  
  private short y;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setX(short x) {
    this.x = x;
  }
  
  public void setY(short y) {
    this.y = y;
  }
  
  public void setS(short s) {
    this.s = s;
  }
  
  public void setN(String n) {
    this.n = n;
  }
  
  public void setT(String t) {
    this.t = t;
  }
  
  public void setC(byte c) {
    this.c = c;
  }
  
  public int getId() {
    return this.id;
  }
  
  public short getX() {
    return this.x;
  }
  
  public short getY() {
    return this.y;
  }
  
  private short s = 1;
  
  private String n;
  
  private String t;
  
  public short getS() {
    return this.s;
  }
  
  public String getN() {
    return this.n;
  }
  
  public String getT() {
    return this.t;
  }
  
  private byte c = 0;
  
  public byte getC() {
    return this.c;
  }
}
