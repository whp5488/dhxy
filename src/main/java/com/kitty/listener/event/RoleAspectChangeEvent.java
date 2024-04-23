package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;

public class RoleAspectChangeEvent extends BasePlayerEvent {
  private Role role;
  
  public Role getRole() {
    return this.role;
  }
  
  public RoleAspectChangeEvent(EventType evtType, Role role) {
    super(evtType, role);
    this.role = role;
  }
}
