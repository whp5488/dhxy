package com.kitty.system.game.email.message.vo;

import com.kitty.system.enums.email.Sender;
import com.kitty.system.game.email.domain.Email;
import com.kitty.system.game.email.domain.EmailSystem;
import java.io.Serializable;

public class EmailParam implements Serializable {
  private String id;
  
  private String sender;
  
  private String emialTitle;
  
  private String senderName;
  
  private byte read;
  
  private byte accessory;
  
  private String sendTime;
  
  private byte isSystem;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSender(String sender) {
    this.sender = sender;
  }
  
  public void setEmialTitle(String emialTitle) {
    this.emialTitle = emialTitle;
  }
  
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }
  
  public void setRead(byte read) {
    this.read = read;
  }
  
  public void setAccessory(byte accessory) {
    this.accessory = accessory;
  }
  
  public void setSendTime(String sendTime) {
    this.sendTime = sendTime;
  }
  
  public void setIsSystem(byte isSystem) {
    this.isSystem = isSystem;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getSender() {
    return this.sender;
  }
  
  public String getEmialTitle() {
    return this.emialTitle;
  }
  
  public String getSenderName() {
    return this.senderName;
  }
  
  public byte getRead() {
    return this.read;
  }
  
  public byte getAccessory() {
    return this.accessory;
  }
  
  public String getSendTime() {
    return this.sendTime;
  }
  
  public byte getIsSystem() {
    return this.isSystem;
  }
  
  public EmailParam(Email email) {
    this.id = String.valueOf(email.getId());
    this.emialTitle = email.getEmailTitle();
    this.sender = String.valueOf(email.getSender());
    this.senderName = Sender.getName((int)email.getSender());
    this.read = email.getRead();
    this.accessory = email.getAccessory();
    this.sendTime = String.valueOf(email.getCreateTime());
    this.isSystem = 0;
  }
  
  public EmailParam(EmailSystem emailSystem, byte accessory) {
    this.id = String.valueOf(emailSystem.getId());
    this.emialTitle = emailSystem.getEmailTitle();
    this.sender = String.valueOf(emailSystem.getSender());
    this.senderName = Sender.getName(emailSystem.getSender());
    this.read = 0;
    this.accessory = accessory;
    this.sendTime = String.valueOf(emailSystem.getCreateTime());
    this.isSystem = 1;
  }
}
