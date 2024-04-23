package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;

public class LeaveMapEvent extends BasePlayerEvent {
  private int mapId;
  
  private Role role;
  
  public int getMapId() {
    return this.mapId;
  }
  
  public Role getRole() {
    return this.role;
  }
  
  public LeaveMapEvent(EventType evtType, Role role, int mapId) {
    super(evtType, role);
    this.mapId = mapId;
    this.role = role;
  }
}
