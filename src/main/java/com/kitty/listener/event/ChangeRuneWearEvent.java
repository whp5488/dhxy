package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class ChangeRuneWearEvent extends BasePlayerEvent
{
    private Role role;
    
    public ChangeRuneWearEvent(EventType evtType, Role role) {
        super(evtType, role);
        this.role = role;
    }
    
    public Role getRole() {
        return this.role;
    }
}
