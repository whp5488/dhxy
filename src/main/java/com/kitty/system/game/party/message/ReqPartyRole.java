package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8368)
public class ReqPartyRole extends Message {
  private int roleId;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
}
