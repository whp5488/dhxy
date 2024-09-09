package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class TeamLeaveMomentEvent extends BasePlayerEvent
{
    private Role role;
    
    public TeamLeaveMomentEvent(EventType evtType, Role role) {
        super(evtType, role);
        this.role = role;
    }
    
    public Role getRole() {
        return this.role;
    }
}
