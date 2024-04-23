package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;

public class EnterMapEvent extends BasePlayerEvent {
  private Role role;
  
  public Role getRole() {
    return this.role;
  }
  
  public EnterMapEvent(EventType evtType, Role role) {
    super(evtType, role);
    this.role = role;
  }
}
