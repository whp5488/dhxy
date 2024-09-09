package com.kitty.system.game.fightnew.spectator.domain;

import com.kitty.system.game.role.domain.Role;

public class SpectatorObject
{
    private long id;
    private int roleId;
    private boolean isA;
    
    public SpectatorObject() {
    }
    
    public SpectatorObject(Role role, boolean isA) {
        this.id = (long)role.getId();
        this.roleId = (int)role.getRoleId();
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
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setA(final boolean isA) {
        this.isA = isA;
    }
}
