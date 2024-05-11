
package com.kitty.listener.event.ranking;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BaseGameEvent;

public class RoleChangeEvent extends BaseGameEvent
{
    private Role role;
    
    public RoleChangeEvent(final EventType eventType, final Role role) {
        super(eventType);
        this.role = role;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public void setRole(final Role role) {
        this.role = role;
    }
}
