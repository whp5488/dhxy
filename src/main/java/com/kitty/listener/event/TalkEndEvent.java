package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;

public class TalkEndEvent extends BasePlayerEvent {
  private Role role;
  
  private int talkId;
  
  public Role getRole() {
    return this.role;
  }
  
  public int getTalkId() {
    return this.talkId;
  }
  
  public TalkEndEvent(EventType evtType, Role role, int talkId) {
    super(evtType, role);
    this.role = role;
    this.talkId = talkId;
  }
}
