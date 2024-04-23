package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class CreateDungeonMwkFrontConfirm extends RoleConfirm {
  private Map<Integer, Short> ballotResults;
  
  private ScheduledFuture<?> schedule;
  
  private String type;
  
  public Map<Integer, Short> getBallotResults() {
    return this.ballotResults;
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public String getType() {
    return this.type;
  }
  
  public CreateDungeonMwkFrontConfirm(Map<Integer, Short> ballotResults, ScheduledFuture<?> schedule, String type) {
    this.type = type;
    this.schedule = schedule;
    this.ballotResults = ballotResults;
  }
  
  public ConfirmType getConfirmType() {
    return ConfirmType.CREATE_DUNGEON_MWK_F;
  }
}
