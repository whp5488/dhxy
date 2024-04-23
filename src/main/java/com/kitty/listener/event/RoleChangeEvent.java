package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import lombok.Getter;

@Getter
public class RoleChangeEvent extends BasePlayerEvent {
  public static int REI = 0;
  
  public static int RACE = 1;
  
  public static int NAME = 2;
  
  private Role role;
  
  private String prevName;
  
  private String currName;
  
  private int type;

  public RoleChangeEvent(EventType evtType, Role role, String prevName, String currName, int type) {
    super(evtType, role);
    this.role = role;
    this.prevName = prevName;
    this.currName = currName;
    this.type = type;
  }
}
