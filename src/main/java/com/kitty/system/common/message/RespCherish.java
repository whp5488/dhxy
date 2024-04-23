package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 6666)
public class RespCherish extends Message {
  private int serial;
  
  private short num;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setNum(short num) {
    this.num = num;
  }
  
  public RespCherish() {
    this.serial = 0;
    this.num = 0;
  }
  
  @ConstructorProperties({"serial", "num"})
  public RespCherish(int serial, short num) {
    this.serial = 0;
    this.num = 0;
    this.serial = serial;
    this.num = num;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public short getNum() {
    return this.num;
  }
}
