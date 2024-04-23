package com.kitty.system.game.party.model;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
public class PartyDeny {
  private int endTime;
  
  public PartyDeny() {}
  
  @ConstructorProperties({"endTime"})
  public PartyDeny(int endTime) {
    this.endTime = endTime;
  }

}
