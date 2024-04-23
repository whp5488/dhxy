package com.kitty.system.game.friend.message.vo;

import com.kitty.system.game.friend.domain.PalFriendGroup;

public class FriendGroup {
  private String serial;
  
  private String groupName;
  
  private short sort;
  
  public void setSerial(String serial) {
    this.serial = serial;
  }
  
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }
  
  public void setSort(short sort) {
    this.sort = sort;
  }
  
  public String getSerial() {
    return this.serial;
  }
  
  public String getGroupName() {
    return this.groupName;
  }
  
  public short getSort() {
    return this.sort;
  }
  
  public FriendGroup() {}
  
  public FriendGroup(PalFriendGroup friendGroup) {
    this.serial = String.valueOf(friendGroup.getId());
    this.groupName = friendGroup.getGroupName();
    this.sort = friendGroup.getSort();
  }
  
  public FriendGroup(String serial, String groupName, short sort) {
    this.serial = serial;
    this.groupName = groupName;
    this.sort = sort;
  }
}
