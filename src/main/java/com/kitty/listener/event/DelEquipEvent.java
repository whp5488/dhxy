package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class DelEquipEvent extends BasePlayerEvent
{
    private Role role;
    private long id;
    
    public DelEquipEvent(EventType evtType, Role role, long id) {
        super(evtType, role);
        this.role = role;
        this.id = id;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public long getId() {
        return this.id;
    }
}
