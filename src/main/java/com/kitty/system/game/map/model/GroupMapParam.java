package com.kitty.system.game.map.model;

import java.io.Serializable;
import com.kitty.system.game.role.domain.Role;

public class GroupMapParam
{
    private Role role;
    private Serializable groupKey;
    
    public GroupMapParam(Role role) {
        this.role = role;
    }
    
    public GroupMapParam(Serializable groupKey) {
        this.groupKey = groupKey;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public Serializable getGroupKey() {
        return this.groupKey;
    }
}
