
package com.kitty.listener;

import com.kitty.system.game.role.domain.Role;

public abstract class BasePlayerEvent extends BaseGameEvent
{
    private final long playerUid;
    
    public BasePlayerEvent(final EventType evtType, final Role role) {
        super(evtType);
        this.playerUid = role.getId();
    }
    
    public long getPlayerUid() {
        return this.playerUid;
    }
}
