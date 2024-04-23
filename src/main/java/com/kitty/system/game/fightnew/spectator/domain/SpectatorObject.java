package com.kitty.system.game.fightnew.spectator.domain;

import com.kitty.system.game.role.domain.Role;

public class SpectatorObject {
  private long id;
  
  private int roleId;
  
  private boolean isA;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setA(boolean isA) {
    this.isA = isA;
  }
  
  public long getId() {
    return this.id;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public boolean isA() {
    return this.isA;
  }
  
  public SpectatorObject() {}
  
  public SpectatorObject(Role role, boolean isA) {
    this.id = role.getId().longValue();
    this.roleId = role.getRoleId().intValue();
    this.isA = isA;
  }
}
