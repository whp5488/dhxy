package com.kitty.system.game.team.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.message.ReqAccept;
import com.kitty.system.game.team.message.ReqChangeTeamLeader;
import com.kitty.system.game.team.message.ReqCheckPlayer;
import com.kitty.system.game.team.message.ReqFoundJoin;
import com.kitty.system.game.team.message.ReqGetTeamById;
import com.kitty.system.game.team.message.ReqJoinTeamList;
import com.kitty.system.game.team.message.ReqKickOutTeam;
import com.kitty.system.game.team.message.ReqLeaveTempTeam;
import com.kitty.system.game.team.message.ReqOperTeleportItem;
import com.kitty.system.game.team.message.ReqQuitTeam;
import com.kitty.system.game.team.message.ReqRefreshRequestInfo;
import com.kitty.system.game.team.message.ReqReject;
import com.kitty.system.game.team.message.ReqRequestJoin;
import com.kitty.system.game.team.message.ReqReturnTeam;
import com.kitty.system.game.team.message.ReqTeamList;
import com.kitty.system.game.team.message.ReqUpdateTeamTarget;
import com.kitty.system.game.team.service.TeamService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TeamController {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  TeamService teamService;
  
  @RequestMapping
  public void refreshRequestInfo(IoSession session, ReqRefreshRequestInfo reqRefreshRequestInfo) {
    this.teamService.refreshRequestInfo(session, reqRefreshRequestInfo);
  }
  
  @RequestMapping
  public void transLeader(IoSession session, ReqKickOutTeam reqKickOutTeam) {
    this.teamService.kickOutTeam(session, reqKickOutTeam);
  }
  
  @RequestMapping
  public void leaveTempTeam(IoSession session, ReqLeaveTempTeam reqLeaveTempTeam) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.teamService.leaveTemp(role, true);
  }
  
  @RequestMapping
  public void returnTeam(IoSession session, ReqReturnTeam reqguiduiTeam) {
    Role role = SessionUtils.getRoleBySession(session);
    this.teamService.returnTeam(role, true);
  }
  
  @RequestMapping
  public void operTeleportItem(IoSession session, ReqOperTeleportItem reqOperTeleportItem) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.teamService.operTeleport(role, reqOperTeleportItem);
  }
  
  @RequestMapping
  public void changeTeamLeader(IoSession session, ReqChangeTeamLeader reqChangeTeamLeader) {
    this.teamService.changeLeader(session, reqChangeTeamLeader);
  }
  
  @RequestMapping
  public void check(IoSession session, ReqCheckPlayer reqCheckPlayer) {
    this.teamService.checkRoleInfo(session, reqCheckPlayer);
  }
  
  @RequestMapping
  public void leave(IoSession session, ReqQuitTeam reqQuitTeam) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.teamService.leave(role, false);
  }
  
  @RequestMapping
  public void accept(IoSession session, ReqAccept reqAccept) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.teamService.accept(role, reqAccept);
  }
  
  @RequestMapping
  public void reject(IoSession session, ReqReject reqReject) {
    this.teamService.reject(session, reqReject);
  }
  
  @RequestMapping
  public void foundJoin(IoSession session, ReqFoundJoin foundJoin) {
    this.teamService.foundJoin(session, foundJoin);
  }
  
  @RequestMapping
  public void requestJoin(IoSession session, ReqRequestJoin requestJoin) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.teamService.requestJoin(role, requestJoin);
  }
  
  @RequestMapping
  public void getTeamList(IoSession session, ReqTeamList reqTeamList) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    String activityType = reqTeamList.getActivityType();
    if (activityType == null || "".equals(activityType) || activityType.equals(ActivityType.NO_TARGET.getActivityId()))
      activityType = ActivityType.NO_TARGET.getActivityId(); 
    this.teamService.getTeamList(role, activityType);
  }
  
  @RequestMapping
  public void updateTeamTarget(IoSession session, ReqUpdateTeamTarget reqUpdateTeamTarget) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    int roleId = reqUpdateTeamTarget.getRoleId();
    if (roleId < 0)
      return; 
    String activityType = reqUpdateTeamTarget.getActivityType();
    if (activityType == null || "".equals(activityType) || activityType.equals(ActivityType.NO_TARGET.getActivityId()))
      activityType = ActivityType.NO_TARGET.getActivityId(); 
    this.teamService.updateTeamTarget(role, roleId, activityType);
  }
  
  @RequestMapping
  public void joinTeams(IoSession session, ReqJoinTeamList reqJoinTeamList) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.teamService.joinTeams(role, reqJoinTeamList.getActivityType());
  }
  
  @RequestMapping
  public void getTeamById(IoSession session, ReqGetTeamById reqGetTeamById) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.teamService.getTeamById(role, reqGetTeamById.getRoleId());
  }
}
