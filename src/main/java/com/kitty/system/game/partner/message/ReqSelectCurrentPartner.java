package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4201)
public class ReqSelectCurrentPartner extends Message {
  private int partnerId;
  
  private byte isFight;
  
  public void setPartnerId(int partnerId) {
    this.partnerId = partnerId;
  }
  
  public void setIsFight(byte isFight) {
    this.isFight = isFight;
  }
  
  public void setReplacePartnerId(int replacePartnerId) {
    this.replacePartnerId = replacePartnerId;
  }
  
  public int getPartnerId() {
    return this.partnerId;
  }
  
  public byte getIsFight() {
    return this.isFight;
  }
  
  private int replacePartnerId = 0;
  
  public int getReplacePartnerId() {
    return this.replacePartnerId;
  }
}
