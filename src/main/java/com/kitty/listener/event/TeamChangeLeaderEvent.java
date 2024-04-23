package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Member;
import java.util.List;

public class TeamChangeLeaderEvent extends BasePlayerEvent {
  private Role role;
  
  private List<Member> memberList;
  
  public Role getRole() {
    return this.role;
  }
  
  public List<Member> getMemberList() {
    return this.memberList;
  }
  
  public TeamChangeLeaderEvent(EventType evtType, Role role, List<Member> memberList) {
    super(evtType, role);
    this.memberList = memberList;
    this.role = role;
  }
}
