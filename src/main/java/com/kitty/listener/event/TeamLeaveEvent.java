package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;

public class TeamLeaveEvent extends BasePlayerEvent {
  private Role role;
  
  private boolean isLeader;
  
  private Team team;
  
  public Role getRole() {
    return this.role;
  }
  
  public boolean isLeader() {
    return this.isLeader;
  }
  
  public Team getTeam() {
    return this.team;
  }
  
  public TeamLeaveEvent(EventType evtType, Role role, boolean isLeader, Team team) {
    super(evtType, role);
    this.role = role;
    this.isLeader = isLeader;
    this.team = team;
  }
}
