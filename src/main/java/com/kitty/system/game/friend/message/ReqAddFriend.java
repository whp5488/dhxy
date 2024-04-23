package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8294)
public class ReqAddFriend extends Message {
  private int roleId;
  
  private String groupId;
  
  private String remark;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }
  
  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public String getGroupId() {
    return this.groupId;
  }
  
  public String getRemark() {
    return this.remark;
  }
}
