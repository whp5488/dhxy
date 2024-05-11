
package com.kitty.system.game.activity.facade;

import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.message.ReqCaInfo;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.message.ReqWaterInfo;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.listener.event.TeamChangeLeaderEvent;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.listener.event.TeamLeaveMomentEvent;
import com.kitty.listener.event.TeamLeaveEvent;
import com.kitty.listener.event.RoleExpireEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.LogoutEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.activity.service.ActivityService;
import org.springframework.stereotype.Controller;

@Controller
public class TaskActivityController
{
    @Autowired
    ActivityService activityService;
    
    @EventHandler({ EventType.LOGOUT })
    public void handleLogoutEvent(final LogoutEvent LogoutEvent) {
        final Role role = LogoutEvent.getRole();
        this.activityService.handleLogout(role);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        this.activityService.handleLogin(role);
    }
    
    @EventHandler({ EventType.ROLE_EXPIRE })
    public void handleRoleExpireEvent(final RoleExpireEvent roleExpireEvent) {
        final Role role = roleExpireEvent.getRole();
        this.activityService.handleRoleExpire(role);
    }
    
    @EventHandler({ EventType.TEAM_LEAVE })
    public void handleTeamLeaveEvent(final TeamLeaveEvent teamLeaveEvent) {
        final Role role = teamLeaveEvent.getRole();
        this.activityService.handleLeaveTeam(role, teamLeaveEvent.isLeader(), teamLeaveEvent.getTeam());
    }
    
    @EventHandler({ EventType.TEAM_LEAVE_MOMENT })
    public void handleTeamLeaveMonmentEvent(final TeamLeaveMomentEvent teamLeaveMomentEvent) {
        final Role role = teamLeaveMomentEvent.getRole();
        this.activityService.handleTeamLeaveMoment(role);
    }
    
    @EventHandler({ EventType.TEAM_JOIN })
    public void handleTeamJoinEvent(final TeamJoinEvent joinEvent) {
        final Role role = joinEvent.getRole();
        this.activityService.handleTeamJoinEvent(role);
    }
    
    @EventHandler({ EventType.TEAM_CHANGE_LEADER })
    public void handleTeamChangeLeaderEvent(final TeamChangeLeaderEvent teamChangeLeaderEvent) {
        final Role role = teamChangeLeaderEvent.getRole();
        this.activityService.handleTeamChangeLeader(role, teamChangeLeaderEvent.getMemberList());
    }
    
    @EventHandler({ EventType.HEART_BEAT })
    public void handleHeartBeat(final HeartBeatEvent heartBeatEvent) {
        final Role role = heartBeatEvent.getRole();
        if (role == null) {
            return;
        }
        this.activityService.handleHeartBeat(role, heartBeatEvent.getPrevHeartTime());
    }
    
    @EventHandler({ EventType.FIGHT_END })
    public void handleFightEndEvent(final FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> SpringUtils.getBean(TaskService.class).doFightEnd(fightEndEvent.getRole(), fightEndEvent));
        this.activityService.handleFightEnd(fightEndEvent);
    }
    
    @RequestMapping
    public void waterInfo(final Role role, final ReqWaterInfo reqWaterInfo) {
        SpringUtils.getBean(WaterHandler.class).waterInfo(role);
    }
    
    @RequestMapping
    public void caInfo(final Role role, final ReqCaInfo reqCaInfo) {
        SpringUtils.getBean(CaHandler.class).caInfo(role);
    }
}
