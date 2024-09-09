package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class TeamJoinEvent extends BasePlayerEvent
{
    private Role role;
    private String type;
    
    public TeamJoinEvent(EventType evtType, Role role, String type) {
        super(evtType, role);
        this.role = role;
        this.type = type;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public String getType() {
        return this.type;
    }
}
