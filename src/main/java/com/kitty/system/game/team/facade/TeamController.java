
package com.kitty.system.game.team.facade;

import com.kitty.system.game.team.message.ReqGetTeamById;
import com.kitty.system.game.team.message.ReqJoinTeamList;
import com.kitty.system.game.team.message.ReqUpdateTeamTarget;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.team.message.ReqTeamList;
import com.kitty.system.game.team.message.ReqRequestJoin;
import com.kitty.system.game.team.message.ReqFoundJoin;
import com.kitty.system.game.team.message.ReqReject;
import com.kitty.system.game.team.message.ReqAccept;
import com.kitty.system.game.team.message.ReqQuitTeam;
import com.kitty.system.game.team.message.ReqCheckPlayer;
import com.kitty.system.game.team.message.ReqChangeTeamLeader;
import com.kitty.system.game.team.message.ReqOperTeleportItem;
import com.kitty.system.game.team.message.ReqReturnTeam;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.team.message.ReqLeaveTempTeam;
import com.kitty.system.game.team.message.ReqKickOutTeam;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.team.message.ReqRefreshRequestInfo;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.team.service.TeamService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class TeamController
{
    Logger logger;
    @Autowired
    TeamService teamService;
    
    public TeamController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @RequestMapping
    public void refreshRequestInfo(final IoSession session, final ReqRefreshRequestInfo reqRefreshRequestInfo) {
        this.teamService.refreshRequestInfo(session, reqRefreshRequestInfo);
    }
    
    @RequestMapping
    public void transLeader(final IoSession session, final ReqKickOutTeam reqKickOutTeam) {
        this.teamService.kickOutTeam(session, reqKickOutTeam);
    }
    
    @RequestMapping
    public void leaveTempTeam(final IoSession session, final ReqLeaveTempTeam reqLeaveTempTeam) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.teamService.leaveTemp(role, true);
    }
    
    @RequestMapping
    public void returnTeam(final IoSession session, final ReqReturnTeam reqguiduiTeam) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.teamService.returnTeam(role, true);
    }
    
    @RequestMapping
    public void operTeleportItem(final IoSession session, final ReqOperTeleportItem reqOperTeleportItem) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.teamService.operTeleport(role, reqOperTeleportItem);
    }
    
    @RequestMapping
    public void changeTeamLeader(final IoSession session, final ReqChangeTeamLeader reqChangeTeamLeader) {
        this.teamService.changeLeader(session, reqChangeTeamLeader);
    }
    
    @RequestMapping
    public void check(final IoSession session, final ReqCheckPlayer reqCheckPlayer) {
        this.teamService.checkRoleInfo(session, reqCheckPlayer);
    }
    
    @RequestMapping
    public void leave(final IoSession session, final ReqQuitTeam reqQuitTeam) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.teamService.leave(role, false);
    }
    
    @RequestMapping
    public void accept(final IoSession session, final ReqAccept reqAccept) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.teamService.accept(role, reqAccept);
    }
    
    @RequestMapping
    public void reject(final IoSession session, final ReqReject reqReject) {
        this.teamService.reject(session, reqReject);
    }
    
    @RequestMapping
    public void foundJoin(final IoSession session, final ReqFoundJoin foundJoin) {
        this.teamService.foundJoin(session, foundJoin);
    }
    
    @RequestMapping
    public void requestJoin(final IoSession session, final ReqRequestJoin requestJoin) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.teamService.requestJoin(role, requestJoin);
    }
    
    @RequestMapping
    public void getTeamList(final IoSession session, final ReqTeamList reqTeamList) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        String activityType = reqTeamList.getActivityType();
        if (activityType == null || "".equals(activityType) || activityType.equals(ActivityType.NO_TARGET.getActivityId())) {
            activityType = ActivityType.NO_TARGET.getActivityId();
        }
        this.teamService.getTeamList(role, activityType);
    }
    
    @RequestMapping
    public void updateTeamTarget(final IoSession session, final ReqUpdateTeamTarget reqUpdateTeamTarget) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final int roleId = reqUpdateTeamTarget.getRoleId();
        if (roleId < 0) {
            return;
        }
        String activityType = reqUpdateTeamTarget.getActivityType();
        if (activityType == null || "".equals(activityType) || activityType.equals(ActivityType.NO_TARGET.getActivityId())) {
            activityType = ActivityType.NO_TARGET.getActivityId();
        }
        this.teamService.updateTeamTarget(role, roleId, activityType);
    }
    
    @RequestMapping
    public void joinTeams(final IoSession session, final ReqJoinTeamList reqJoinTeamList) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.teamService.joinTeams(role, reqJoinTeamList.getActivityType());
    }
    
    @RequestMapping
    public void getTeamById(final IoSession session, final ReqGetTeamById reqGetTeamById) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.teamService.getTeamById(role, reqGetTeamById.getRoleId());
    }
}
