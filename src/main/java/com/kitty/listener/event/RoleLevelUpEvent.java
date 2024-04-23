package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;

public class RoleLevelUpEvent extends BasePlayerEvent {
  private Role role;
  
  private int oldLevel;
  
  private int level;
  
  public Role getRole() {
    return this.role;
  }
  
  public int getOldLevel() {
    return this.oldLevel;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public RoleLevelUpEvent(EventType evtType, Role role, int oldLevel, int level) {
    super(evtType, role);
    this.oldLevel = oldLevel;
    this.level = level;
    this.role = role;
  }
}
