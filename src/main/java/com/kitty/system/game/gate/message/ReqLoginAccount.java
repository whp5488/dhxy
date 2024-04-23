package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 9040)
public class ReqLoginAccount extends Message {
  private String type;
  
  private String accountId;
  
  private String password;
  
  private String gameV;
  
  private String engV;
  
  public void setType(String type) {
    this.type = type;
  }
  
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setGameV(String gameV) {
    this.gameV = gameV;
  }
  
  public void setEngV(String engV) {
    this.engV = engV;
  }
  
  public String getType() {
    return this.type;
  }
  
  public String getAccountId() {
    return this.accountId;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public String getGameV() {
    return this.gameV;
  }
  
  public String getEngV() {
    return this.engV;
  }
}
