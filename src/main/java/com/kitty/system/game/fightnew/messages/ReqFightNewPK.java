package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 4113)
public class ReqFightNewPK extends Message {
  private int roleId;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  @ConstructorProperties({"roleId"})
  public ReqFightNewPK(int roleId) {
    this.roleId = roleId;
  }
  
  public ReqFightNewPK() {}
  
  public int getRoleId() {
    return this.roleId;
  }
}
