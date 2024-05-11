
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class RoleLevelUpEvent extends BasePlayerEvent
{
    private Role role;
    private int oldLevel;
    private int level;
    
    public RoleLevelUpEvent(final EventType evtType, final Role role, final int oldLevel, final int level) {
        super(evtType, role);
        this.oldLevel = oldLevel;
        this.level = level;
        this.role = role;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public int getOldLevel() {
        return this.oldLevel;
    }
    
    public int getLevel() {
        return this.level;
    }
}
