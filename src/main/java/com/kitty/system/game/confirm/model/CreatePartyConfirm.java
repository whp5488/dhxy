package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;

public class CreatePartyConfirm extends RoleConfirm {
  private String partyName;
  
  public String getPartyName() {
    return this.partyName;
  }
  
  public ConfirmType getConfirmType() {
    return ConfirmType.CREATE_PARTY;
  }
  
  public CreatePartyConfirm(String partyName) {
    this.partyName = partyName;
  }
}
