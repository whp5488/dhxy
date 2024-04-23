package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45214)
public class ReqAlterFriend extends Message {
  private String id;
  
  private String remark;
  
  private int pal;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  public void setPal(int pal) {
    this.pal = pal;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getRemark() {
    return this.remark;
  }
  
  public int getPal() {
    return this.pal;
  }
}
