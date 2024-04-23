package com.kitty.system.game.activity.facade;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.event.LogoutEvent;
import com.kitty.listener.event.RoleExpireEvent;
import com.kitty.listener.event.TeamChangeLeaderEvent;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.listener.event.TeamLeaveEvent;
import com.kitty.listener.event.TeamLeaveMomentEvent;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.message.ReqCaInfo;
import com.kitty.system.game.activity.message.ReqWaterInfo;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TaskActivityController {
  @Autowired
  ActivityService activityService;
  
  @EventHandler({EventType.LOGOUT})
  public void handleLogoutEvent(LogoutEvent logoutEvent) {
    Role role = logoutEvent.getRole();
    this.activityService.handleLogout(role);
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    this.activityService.handleLogin(role);
  }
  
  @EventHandler({EventType.ROLE_EXPIRE})
  public void handleRoleExpireEvent(RoleExpireEvent roleExpireEvent) {
    Role role = roleExpireEvent.getRole();
    this.activityService.handleRoleExpire(role);
  }
  
  @EventHandler({EventType.TEAM_LEAVE})
  public void handleTeamLeaveEvent(TeamLeaveEvent teamLeaveEvent) {
    Role role = teamLeaveEvent.getRole();
    this.activityService.handleLeaveTeam(role, teamLeaveEvent.isLeader(), teamLeaveEvent.getTeam());
  }
  
  @EventHandler({EventType.TEAM_LEAVE_MOMENT})
  public void handleTeamLeaveMonmentEvent(TeamLeaveMomentEvent teamLeaveMomentEvent) {
    Role role = teamLeaveMomentEvent.getRole();
    this.activityService.handleTeamLeaveMoment(role);
  }
  
  @EventHandler({EventType.TEAM_JOIN})
  public void handleTeamJoinEvent(TeamJoinEvent joinEvent) {
    Role role = joinEvent.getRole();
    this.activityService.handleTeamJoinEvent(role);
  }
  
  @EventHandler({EventType.TEAM_CHANGE_LEADER})
  public void handleTeamChangeLeaderEvent(TeamChangeLeaderEvent teamChangeLeaderEvent) {
    Role role = teamChangeLeaderEvent.getRole();
    this.activityService.handleTeamChangeLeader(role, teamChangeLeaderEvent.getMemberList());
  }
  
  @EventHandler({EventType.HEART_BEAT})
  public void handleHeartBeat(HeartBeatEvent heartBeatEvent) {
    Role role = heartBeatEvent.getRole();
    if (role == null)
      return; 
    this.activityService.handleHeartBeat(role, heartBeatEvent.getPrevHeartTime());
  }
  
  @EventHandler({EventType.FIGHT_END})
  public void handleFightEndEvent(FightEndEvent fightEndEvent) {
    ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> ((TaskService)SpringUtils.getBean(TaskService.class)).doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    this.activityService.handleFightEnd(fightEndEvent);
  }
  
  @RequestMapping
  public void waterInfo(Role role, ReqWaterInfo reqWaterInfo) {
    ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).waterInfo(role);
  }
  
  @RequestMapping
  public void caInfo(Role role, ReqCaInfo reqCaInfo) {
    ((CaHandler)SpringUtils.getBean(CaHandler.class)).caInfo(role);
  }
}
