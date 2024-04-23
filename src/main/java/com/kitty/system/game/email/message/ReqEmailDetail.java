package com.kitty.system.game.email.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4310)
public class ReqEmailDetail extends Message {
  private byte isSystem;
  
  private String emailId;
  
  public void setIsSystem(byte isSystem) {
    this.isSystem = isSystem;
  }
  
  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }
  
  public byte getIsSystem() {
    return this.isSystem;
  }
  
  public String getEmailId() {
    return this.emailId;
  }
}
