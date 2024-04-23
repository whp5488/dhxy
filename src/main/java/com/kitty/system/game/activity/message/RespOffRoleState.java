package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 49999)
public class RespOffRoleState extends Message {
  private String type;
  
  private byte state;
  
  public void setType(String type) {
    this.type = type;
  }
  
  public void setState(byte state) {
    this.state = state;
  }

  @ConstructorProperties({"type", "state"})
  public RespOffRoleState(String type, byte state) {
    this.type = type;
    this.state = state;
  }
  
  public RespOffRoleState() {}
  
  public String getType() {
    return this.type;
  }
  
  public byte getState() {
    return this.state;
  }
}
