
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class HeartBeatEvent extends BasePlayerEvent
{
    private Role role;
    private long prevHeartTime;
    private long curveHeartTime;
    
    public HeartBeatEvent(final EventType evtType, final Role role, final long prevHeartTime) {
        super(evtType, role);
        this.role = role;
        this.prevHeartTime = prevHeartTime;
        this.curveHeartTime = role.getPrevHeartTime();
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public long getPrevHeartTime() {
        return this.prevHeartTime;
    }
    
    public long getCurveHeartTime() {
        return this.curveHeartTime;
    }
}
