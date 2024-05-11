
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class TeamLeaveEvent extends BasePlayerEvent
{
    private Role role;
    private boolean isLeader;
    private Team team;
    
    public TeamLeaveEvent(final EventType evtType, final Role role, final boolean isLeader, final Team team) {
        super(evtType, role);
        this.role = role;
        this.isLeader = isLeader;
        this.team = team;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public boolean isLeader() {
        return this.isLeader;
    }
    
    public Team getTeam() {
        return this.team;
    }
}
