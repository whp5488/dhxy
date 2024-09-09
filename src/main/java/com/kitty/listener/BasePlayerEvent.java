package com.kitty.listener;

import com.kitty.system.game.role.domain.Role;

public abstract class BasePlayerEvent extends BaseGameEvent
{
    private final long playerUid;
    
    public BasePlayerEvent(EventType evtType, Role role) {
        super(evtType);
        this.playerUid = (long)role.getId();
    }
    
    public long getPlayerUid() {
        return this.playerUid;
    }
}
