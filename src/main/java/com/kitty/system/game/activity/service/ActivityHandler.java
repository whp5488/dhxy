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
    
    public abstract void handleFightEnd(FightEndEvent fightEndEvent);
    
    public void handleTeamLeave(Role role, boolean leader, Team team) {
    }
    
    public void handleTeamLeaveMoment(Role role) {
    }
    
    public void handleTeamJoinEvent(Role role) {
    }
    
    public void handleTeamChangeLeader(Role role, List<Member> memberList) {
    }
    
    public void handleLogout(Role role) {
    }
    
    public void handleLogin(Role role) {
    }
    
    public void handleHeartBeat(Role role, long prevHeartTime) {
    }
    
    public void handleRoleExpire(Role role) {
    }
}
