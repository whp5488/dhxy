
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class RoleAspectChangeEvent extends BasePlayerEvent
{
    private Role role;
    
    public RoleAspectChangeEvent(final EventType evtType, final Role role) {
        super(evtType, role);
        this.role = role;
    }
    
    public Role getRole() {
        return this.role;
    }
}
