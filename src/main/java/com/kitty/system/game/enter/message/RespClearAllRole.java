package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45157)
public class RespClearAllRole extends Message {
  private int roleId;
  
  private int mapId;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setMapId(int mapId) {
    this.mapId = mapId;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public int getMapId() {
    return this.mapId;
  }
}
