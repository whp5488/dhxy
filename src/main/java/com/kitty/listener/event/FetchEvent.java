package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class FetchEvent extends BasePlayerEvent
{
    public static final byte FETCH_PET = 1;
    private Role role;
    private byte fetchType;
    
    public FetchEvent(EventType evtType, Role role, byte fetchType) {
        super(evtType, role);
        this.role = role;
        this.fetchType = fetchType;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public byte getFetchType() {
        return this.fetchType;
    }
}
