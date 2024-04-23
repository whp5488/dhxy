package com.kitty.system.game.npc.domain;

import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.Npc;

public class NpcContentParam {
  private Role role;
  
  private Npc npc;
  
  public Role getRole() {
    return this.role;
  }
  
  public Npc getNpc() {
    return this.npc;
  }
  
  public NpcContentParam(Role role, Npc npc) {
    this.role = role;
    this.npc = npc;
  }
}
