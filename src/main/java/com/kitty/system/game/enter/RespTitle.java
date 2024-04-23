package com.kitty.system.game.enter;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61671)
public class RespTitle extends Message {
  private int roleId;
  
  private byte param;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setParam(byte param) {
    this.param = param;
  }

  public int getRoleId() {
    return this.roleId;
  }
  
  public byte getParam() {
    return this.param;
  }
}
