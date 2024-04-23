package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;

public class LeaveTeamConfirm extends RoleConfirm {
  private int type = -1;
  
  public ConfirmType getConfirmType() {
    return ConfirmType.LEAVE_TEAM;
  }
  
  public LeaveTeamConfirm(int type) {
    this.type = type;
  }
  
  public int getType() {
    return this.type;
  }
  
  public void setType(int type) {
    this.type = type;
  }
}
