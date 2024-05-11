
package com.kitty.system.game.activity.service;

import com.kitty.system.game.team.domain.Member;
import java.util.List;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.logs.LoggerFunction;
import org.slf4j.Logger;

public abstract class ActivityHandler
{
    protected Logger logger;
    
    public ActivityHandler() {
        this.logger = LoggerFunction.ACTIVITY.getLogger();
    }
    
    public abstract void handleFightEnd(final FightEndEvent p0);
    
    public void handleTeamLeave(final Role role, final boolean leader, final Team team) {
    }
    
    public void handleTeamLeaveMoment(final Role role) {
    }
    
    public void handleTeamJoinEvent(final Role role) {
    }
    
    public void handleTeamChangeLeader(final Role role, final List<Member> memberList) {
    }
    
    public void handleLogout(final Role role) {
    }
    
    public void handleLogin(final Role role) {
    }
    
    public void handleHeartBeat(final Role role, final long prevHeartTime) {
    }
    
    public void handleRoleExpire(final Role role) {
    }
}
