package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class RoleExpChangeEvent extends BasePlayerEvent
{
    private Role role;
    private int change;
    
    public RoleExpChangeEvent(EventType evtType, Role role, int change) {
        super(evtType, role);
        this.role = role;
        this.change = change;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public int getChange() {
        return this.change;
    }
}
