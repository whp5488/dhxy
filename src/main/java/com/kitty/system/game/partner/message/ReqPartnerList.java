package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4203)
public class ReqPartnerList extends Message {
  private int roleId;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setIsFight(byte isFight) {
    this.isFight = isFight;
  }

  public int getRoleId() {
    return this.roleId;
  }
  
  private byte isFight = 0;
  
  public byte getIsFight() {
    return this.isFight;
  }
}
