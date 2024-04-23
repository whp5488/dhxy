package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20701)
public class ReqChatObjectDetail extends Message {
  private String id;
  
  private byte type;
  
  private int ownerRoleId;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setOwnerRoleId(int ownerRoleId) {
    this.ownerRoleId = ownerRoleId;
  }
  
  public String getId() {
    return this.id;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public int getOwnerRoleId() {
    return this.ownerRoleId;
  }
}
