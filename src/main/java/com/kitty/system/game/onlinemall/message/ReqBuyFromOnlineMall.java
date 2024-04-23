package com.kitty.system.game.onlinemall.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8410)
public class ReqBuyFromOnlineMall extends Message {
  private int serial;
  
  private int amount;
  
  private int type;
  
  private String pwd;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setAmount(int amount) {
    this.amount = amount;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setPwd(String pwd) {
    this.pwd = pwd;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public int getAmount() {
    return this.amount;
  }
  
  public int getType() {
    return this.type;
  }
  
  public String getPwd() {
    return this.pwd;
  }
}
