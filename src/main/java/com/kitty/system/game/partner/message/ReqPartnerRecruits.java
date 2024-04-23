package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4202)
public class ReqPartnerRecruits extends Message {
  public void setPartnerId(int partnerId) {
    this.partnerId = partnerId;
  }
  
  public void setIsRecruits(byte isRecruits) {
    this.isRecruits = isRecruits;
  }
  
  private int partnerId = 0;
  
  public int getPartnerId() {
    return this.partnerId;
  }
  
  private byte isRecruits = 1;
  
  public byte getIsRecruits() {
    return this.isRecruits;
  }
}
