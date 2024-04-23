package com.kitty.system.game.enter;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16431)
public class RespPositionMove extends Message {
  public void setX(int x) {
    this.x = x;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  private int x = 0;
  
  public int getX() {
    return this.x;
  }
  
  private int y = 0;
  
  private int roleId;
  
  public int getY() {
    return this.y;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
}
