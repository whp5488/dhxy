package com.kitty.system.game.email.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.email.domain.EmailSystemAccessory;
import java.util.List;

@MessageMeta(module = 4320)
public class RespEmailDetail extends Message {
  private String emailId;
  
  private String emailTitle;
  
  private String senderName;
  
  private String content;
  
  List<EmailSystemAccessory> accessorys;
  
  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }
  
  public void setEmailTitle(String emailTitle) {
    this.emailTitle = emailTitle;
  }
  
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public void setAccessorys(List<EmailSystemAccessory> accessorys) {
    this.accessorys = accessorys;
  }
  
  public String getEmailId() {
    return this.emailId;
  }
  
  public String getEmailTitle() {
    return this.emailTitle;
  }
  
  public String getSenderName() {
    return this.senderName;
  }
  
  public String getContent() {
    return this.content;
  }
  
  public List<EmailSystemAccessory> getAccessorys() {
    return this.accessorys;
  }
}
