package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4204)
public class ReqFeedPartner extends Message {
  private int partnerId;
  
  private int param;
  
  private String type;
  
  private int num;
  
  public void setPartnerId(int partnerId) {
    this.partnerId = partnerId;
  }
  
  public void setParam(int param) {
    this.param = param;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public void setNum(int num) {
    this.num = num;
  }
  
  public int getPartnerId() {
    return this.partnerId;
  }
  
  public int getParam() {
    return this.param;
  }
  
  public String getType() {
    return this.type;
  }
  
  public int getNum() {
    return this.num;
  }
}
