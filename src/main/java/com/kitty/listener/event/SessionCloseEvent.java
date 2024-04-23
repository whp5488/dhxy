package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;

public class SessionCloseEvent extends BasePlayerEvent {
  private Role role;
  
  private IoSession session;
  
  public SessionCloseEvent(EventType evtType, Role role, IoSession session) {
    super(evtType, role);
    this.role = role;
    this.session = session;
  }
  
  public Role getRole() {
    return this.role;
  }
  
  public IoSession getSession() {
    return this.session;
  }
}
