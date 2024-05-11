
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class TalkEndEvent extends BasePlayerEvent
{
    private Role role;
    private int talkId;
    
    public TalkEndEvent(final EventType evtType, final Role role, final int talkId) {
        super(evtType, role);
        this.role = role;
        this.talkId = talkId;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public int getTalkId() {
        return this.talkId;
    }
}
