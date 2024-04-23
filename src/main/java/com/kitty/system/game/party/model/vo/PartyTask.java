package com.kitty.system.game.party.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
public class PartyTask {
  private int time;
  
  private int count;
  
  @ConstructorProperties({"time", "count"})
  public PartyTask(int time, int count) {
    this.time = time;
    this.count = count;
  }
  
  public PartyTask() {}

}
