
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class LeaveMapEvent extends BasePlayerEvent
{
    private int mapId;
    private Role role;
    
    public LeaveMapEvent(final EventType evtType, final Role role, final int mapId) {
        super(evtType, role);
        this.mapId = mapId;
        this.role = role;
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public Role getRole() {
        return this.role;
    }
}
