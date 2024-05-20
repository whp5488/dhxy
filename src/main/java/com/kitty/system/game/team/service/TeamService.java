
package com.kitty.system.game.team.service;

import java.util.Optional;

import com.kitty.system.game.task.service.handler.dungeon.DungeonCmHandler;
import com.kitty.system.game.team.message.RespSmartTeamMemerList;
import com.kitty.system.game.team.message.RespTeamList;
import com.kitty.system.game.team.domain.vo.SmartTeamMember;
import com.kitty.system.game.team.domain.vo.SmartTeam;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ConcurrentModificationException;
import com.kitty.mina.task.ThreadLocalUtil;
import java.util.function.Consumer;
import com.kitty.system.game.team.message.RespLookPlayerInfo;
import com.kitty.system.game.team.message.ReqCheckPlayer;
import com.kitty.system.game.team.message.RespFriendAddRole;
import com.kitty.system.game.team.message.ReqChangeTeamLeader;
import com.kitty.listener.event.TeamChangeLeaderEvent;
import com.kitty.system.game.partner.service.PartnerService;
import java.util.Collections;
import java.util.Date;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.player.PlayerProfile;
import java.util.concurrent.TimeUnit;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.confirm.model.RequestTeamLeaderConfirm;
import com.kitty.system.game.team.message.RespAroundInfo;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.game.team.message.ReqRequestJoin;
import com.kitty.system.game.team.message.ReqFoundJoin;
import com.kitty.system.game.activity.handler.timing.hg.HgFightHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import com.kitty.system.game.task.service.handler.dungeon.DungeonXmHandler;
import com.kitty.system.game.task.service.handler.LookForTaskHandler;
import com.kitty.system.game.task.service.handler.TowerTaskHandler;
import org.nutz.lang.util.NutMap;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.system.game.team.domain.RespClearnAllRequest;
import com.kitty.system.common.utils.SessionDataUtils;
import com.kitty.system.game.team.message.ReqAccept;
import com.kitty.system.game.team.message.RespClearnRequest;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.team.message.ReqReject;
import com.kitty.system.game.map.service.MapService;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.system.game.team.message.ReqKickOutTeam;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.confirm.model.ReturnTeamConfirm;
import com.kitty.system.game.team.message.RespConfirm;
import com.kitty.system.game.team.message.ReqOperTeleportItem;
import com.kitty.listener.event.TeamLeaveMomentEvent;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.team.message.RespLobbyInfo;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.team.message.ReqRefreshRequestInfo;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.TeamLeaveEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.friend.RoleView;
import java.util.function.Predicate;
import com.kitty.mina.cache.SessionCache;
import java.util.Collection;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.team.domain.TeamMember;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.io.Serializable;
import com.kitty.system.game.team.domain.vo.TeamRequest;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.message.RespUpdateTeamList;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.LogoutSelfEvent;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.cache.RedisService;
import com.kitty.system.game.equip.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class TeamService
{
    private static AtomicInteger atomicInteger;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EquipService equipService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BroadcastService broadcastService;
    @Autowired
    private FightNewService fightNewService;
    
    @EventHandler({ EventType.LOGOUT_SELF })
    public void handleLogoutSelfEvent(final LogoutSelfEvent logoutSelfEvent) {
        final Role role = logoutSelfEvent.getRole();
        final Team team = this.getTeam(role.getRoleId());
        if (team != null) {
            this.leave(role, false);
        }
    }
    
    public void updateTeamList(final Role role) {
        final Team team = this.getTeam(role.getRoleId());
        if (team != null) {
            final RespUpdateTeamList respUpdateTeamList = new RespUpdateTeamList();
            respUpdateTeamList.setTarget(team.getTarget());
            respUpdateTeamList.setList(this.getTeamMemberList(team));
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                if (member != null) {
                    if (!member.isPartner()) {
                        final Role memberRole = this.roleService.getPlayer(member.getId());
                        this.broadcastService.updateAppearance(role);
                        MessagePusher.pushMessage(memberRole, respUpdateTeamList);
                    }
                }
            }
        }
    }
    
    public void liveLoadTeamList(final Role role) {
        final Team team = this.getTeam(role.getRoleId());
        if (team != null && team.getLeaderRoleId() == role.getRoleId()) {
            final TeamRequest request = this.redisService.get(team.getLeaderRoleId() + "request_join", TeamRequest.class);
            this.liveLoadTeamList(role, team, request, "request_join");
            final TeamRequest invite = this.redisService.get(team.getLeaderRoleId() + "invite_join", TeamRequest.class);
            this.liveLoadTeamList(role, team, invite, "invite_join");
        }
    }
    
    public void liveLoadTeamList(final Role role, final Team team, final TeamRequest request, final String type) {
        if (request != null && request.getMap() != null) {
            final LinkedHashMap<Integer, String> map = request.getMap();
            final int size = map.size();
            int index = 0;
            for (final Integer key : map.keySet()) {
                if (index >= size - 5) {
                    final Role player = this.roleService.getPlayerBy(key);
                    if (player != null) {
                        MessagePusher.pushMessage(role, this.loadTeamRequest(team, player, type, map.get(key)));
                    }
                }
                ++index;
            }
        }
    }
    
    public List<TeamMember> getTeamMemberList(final Team team) {
        final List<TeamMember> list = new ArrayList<TeamMember>();
        int index = 0;
        Member captainMember = null;
        boolean isHavePartner = false;
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            if (member != null) {
                if (member.isPartner()) {
                    isHavePartner = true;
                }
                final TeamMember teamMember = new TeamMember();
                if (!member.isInTeam()) {
                    teamMember.setStatus(2);
                }
                if (member.getObjectId() == null || "".equals(member.getObjectId())) {
                    teamMember.setRoleId(member.getRoleId());
                }
                else {
                    teamMember.setRoleId(member.getObjectId());
                }
                if (!member.isPartner()) {
                    final Role role = this.roleService.getPlayer(member.getId());
                    this.getMember(role, member);
                    teamMember.setLight_effect(this.equipService.getWearEffect(role));
                }
                teamMember.setWeaponIcon(member.getWeaponIcon());
                teamMember.setMasquerade(member.getMasquerade());
                teamMember.setList(this.getMemberField(member));
                teamMember.setPet(member.getPet());
                teamMember.setGem((byte)member.getGem());
                list.add(teamMember);
                if (index == 0) {
                    captainMember = member;
                }
                ++index;
            }
        }
        if (captainMember != null && list.size() < 5 && team.getPartner().size() > 0 && !isHavePartner) {
            for (final Member partner : team.getPartner()) {
                final TeamMember teamMember2 = new TeamMember();
                teamMember2.setRoleId(partner.getObjectId());
                teamMember2.setList(this.getMemberField(partner));
                list.add(teamMember2);
            }
        }
        return list;
    }
    
    private List<FiedValue> getMemberField(final Member member) {
        final List<FiedValue> fieldValues = new ArrayList<FiedValue>();
        fieldValues.add(new FiedValue("level", (byte)3, member.getLevel()));
        fieldValues.add(new FiedValue("relive", (byte)3, member.getRelive()));
        fieldValues.add(new FiedValue("name", (byte)4, member.getName()));
        fieldValues.add(new FiedValue("serial", (byte)3, member.getSerial()));
        fieldValues.add(new FiedValue("inTeam", (byte)1, member.isInTeam() ? 1 : 2));
        final List<FiedValue> deyList = SpringUtils.getRoleService().deyList(member.getColor());
        if (deyList.size() > 0) {
            fieldValues.addAll(deyList);
        }
        return fieldValues;
    }
    
    public boolean isInTeam(final Role role) {
        final Team team = this.getTeam(role.getRoleId());
        if (team == null) {
            return false;
        }
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            if (member.getRoleId().equals(role.getRoleId())) {
                return member.isInTeam();
            }
        }
        return false;
    }
    
    public boolean isTeamLeader(final Role role) {
        final Team team = this.getTeam(role.getRoleId());
        return team != null && team.isLeader(role.getRoleId());
    }
    
    public void pushMessage(final Role role, final Message message) {
        if (this.isTeamLeader(role)) {
            this.pushMessageToTeam(role, message);
        }
        else {
            MessagePusher.pushMessage(role, message);
        }
    }
    
    private void pushMessageToTeam(final Role role, final Message message) {
        final Team team = this.getTeam(role.getRoleId());
        this.pushMessageToTeam(team, message);
    }
    
    public void pushMessageToTeam(final Team team, final Message message) {
        if (team == null || message == null) {
            return;
        }
        final List<Member> members = team.getList();
        for (int i = 0; i < members.size(); ++i) {
            final Member member = members.get(i);
            if (member != null) {
                if (member.isInTeam()) {
                    final Role membeRole = this.roleService.getPlayer(member.getId());
                    MessagePusher.pushMessage(membeRole, message);
                }
            }
        }
    }
    
    public int getTeamCount(final Role role) {
        final Team team = this.getTeam(role.getRoleId());
        return this.getTeamCount(team);
    }
    
    public Team getTeamByTeamId(final int teamId) {
        return SessionCache.TEAMS.get(teamId);
    }
    
    public Team getTeam(final int id) {
        final int teamId = this.getTeamId(id);
        return SessionCache.TEAMS.get(teamId);
    }
    
    public int getTeamId(final int id) {
        final Integer teamId = SessionCache.ROLE_TEAM.get(id);
        if (teamId == null) {
            return 0;
        }
        if (teamId <= 0) {
            return 0;
        }
        return teamId;
    }
    
    public void handleRoleExpire(final Role role) {
        if (role == null) {
            return;
        }
        final Team team = this.getTeam(role.getRoleId());
        if (team != null) {
            this.leave(role, false);
        }
    }
    
    public List<Integer> getMemberLevel(final Role role) {
        final List<Integer> list = new ArrayList<Integer>();
        if (this.isInTeam(role)) {
            try {
                final Team team = this.getTeam(role.getRoleId());
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    if (member != null) {
                        if (member.isInTeam()) {
                            final Role player = this.roleService.getPlayer(member.getId());
                            if (player != null) {
                                list.add(player.getRelive() * 1000 + player.getLevel());
                            }
                        }
                    }
                }
            }
            catch (final Exception e) {
                list.add(role.getRelive() * 1000 + role.getLevel());
                e.getStackTrace();
            }
        }
        else {
            list.add(role.getRelive() * 1000 + role.getLevel());
        }
        return list;
    }
    
    public List<Integer> getMemberGrade(final Role role) {
        final List<Integer> list = new ArrayList<Integer>();
        if (this.isInTeam(role)) {
            final Team team = this.getTeam(role.getRoleId());
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                if (member != null) {
                    if (member.isInTeam()) {
                        final Role player = this.roleService.getPlayer(member.getId());
                        if (player != null) {
                            list.add(player.getRelive() * 1000 + player.getLevel());
                        }
                    }
                }
            }
        }
        else {
            list.add(role.getRelive() * 1000 + role.getLevel());
        }
        return list;
    }
    
    public String checkMember(final Role role, final Predicate<Role> predicate) {
        final Team team = this.getTeam(role.getRoleId());
        if (team == null) {
            return null;
        }
        return this.checkMember(team, predicate);
    }
    
    public String checkMember(final Team team, final Predicate<Role> predicate) {
        int count = 0;
        StringBuilder stringBuilder = null;
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            if (member != null) {
                if (member.isInTeam()) {
                    final Role memberRole = this.roleService.getPlayer(member.getId());
                    if (memberRole != null) {
                        if (predicate.test(memberRole)) {
                            if (stringBuilder == null) {
                                stringBuilder = new StringBuilder();
                            }
                            if (stringBuilder.length() > 0) {
                                stringBuilder.append("、");
                            }
                            stringBuilder.append(memberRole.getName());
                            if (++count >= 3) {
                                stringBuilder.append("...");
                                break;
                            }
                        }
                    }
                }
            }
        }
        return (stringBuilder != null) ? stringBuilder.toString() : null;
    }
    
    public void leave(final Role role, final boolean isKickOut) {
        this.leave(new RoleView(role), isKickOut);
    }
    
    public void leave(final RoleView roleView, final boolean isKickOut) {
        if (roleView == null) {
            return;
        }
        Team team = this.getTeam(roleView.getRoleId());
        if (team == null) {
            return;
        }
        final Role role = roleView.getRole();
        if (!this.isInLeaveTeam(role)) {
            return;
        }
        final Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId());
        if (fight != null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("战斗结束后会自动离开队伍"));
            team.getFightLeaveNutMap().setv(role.getId().toString(), 1);
            return;
        }
        final boolean boo = this.quiltTeam(team, roleView.getRoleId());
        if (boo) {
            if (role != null) {
                final RespUpdateTeamList respUpdateTeamList = new RespUpdateTeamList();
                respUpdateTeamList.setList(new ArrayList<TeamMember>());
                MessagePusher.pushMessage(role, respUpdateTeamList);
                final RespMsg respServer_onlineMsg = new RespMsg();
                respServer_onlineMsg.setMsg("队伍解散了。");
                MessagePusher.pushMessage(role, respServer_onlineMsg);
                this.broadcastService.sendTitleInfo(role);
                this.broadcastService.sendUpdateAppear(role);
            }
        }
        else {
            team = this.getTeamByTeamId(team.getId());
            if (team != null) {
                final Role leader = this.roleService.getOnlinePlayer(team.getLeaderUid());
                if (leader == null) {
                    return;
                }
                this.broadcastService.sendTitleInfo(leader);
                this.updateTeamList(leader);
                if (role != null) {
                    final RespUpdateTeamList respUpdateTeamList2 = new RespUpdateTeamList();
                    respUpdateTeamList2.setList(new ArrayList<TeamMember>());
                    MessagePusher.pushMessage(role, respUpdateTeamList2);
                    this.broadcastService.sendTitleInfo(role);
                    this.broadcastService.sendUpdateAppear(role);
                    if (isKickOut) {
                        MessagePusher.pushMessage(leader, new RespMsg("#<LB1>" + role.getName() + "#被清理开队伍！"));
                        MessagePusher.pushMessage(role, new RespMsg("你被请离了队伍。"));
                    }
                }
            }
            this.sendTeamChangeMsg(team, "#<LB1>" + roleView.getName() + "#离开了队伍。", true);
        }
        if (role != null && team != null) {
            EventDispatcher.getInstance().fireEvent(new TeamLeaveEvent(EventType.TEAM_LEAVE, role, team.isLeader(roleView.getRoleId()), team));
        }
    }
    
    public boolean isInLeaveTeam(final Role role) {
        return role.getTempCache("at_leave", false) || SpringUtils.getBean(AtWillHandler.class).leaveConfirm(role);
    }
    
    public int getTeamCount(final Team team) {
        if (team == null) {
            return 0;
        }
        int count = 0;
        final List<Member> members = team.getList();
        for (final Member member : members) {
            if (member != null) {
                if (member.isInTeam()) {
                    ++count;
                }
            }
        }
        return count;
    }
    
    public List<Integer> getTeamAfkMember(final Team team) {
        final List<Integer> objects = new ArrayList<Integer>();
        if (team != null) {
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                if (member != null) {
                    if (!member.isInTeam()) {
                        objects.add(member.getRoleId());
                    }
                }
            }
        }
        return objects;
    }
    
    public void refreshRequestInfo(final IoSession session, final ReqRefreshRequestInfo reqRefreshRequestInfo) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final RespLobbyInfo respLobbyInfo = new RespLobbyInfo();
        MessagePusher.pushMessage(session, respLobbyInfo);
    }
    
    public void teamTemporary(final Role role, final int mapId) {
        if (mapId == 3002 || mapId == 3021 || mapId == 3022) {
            final Party party = SpringUtils.getPartyService().getParty(role);
            if (party == null) {
                MessagePusher.pushMessage(role, new RespMsg("你还没有帮派哦#<E:51>#"));
                return;
            }
            if (this.isInTeam(role)) {
                String msg = "";
                final Team team = this.getTeam(role.getRoleId());
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    if (member.isInTeam()) {
                        final Role player = this.roleService.getPlayer(member.getRoleId());
                        if (player != null && !player.getFaction().equals(role.getFaction())) {
                            msg = msg + player.getName() + "、";
                            member.setInTeamTemporary(false);
                            MessagePusher.pushMessage(player, new RespMsg("你#<LB1>" + party.getName() + "#成员，无法跟随队长回帮"));
                            this.leaveTemp(player, false);
                        }
                    }
                }
                if (msg.length() > 0) {
                    msg = msg.substring(0, msg.length() - 1);
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format("#<LB1>%s#不是本帮成员禁止进入", msg), true));
                }
            }
        }
        else if ((mapId != 3002 || mapId != 3021 || mapId != 3022) && this.isInTeam(role)) {
            final Team team2 = this.getTeam(role.getRoleId());
            for (int j = 0; j < team2.getList().size(); ++j) {
                final Member member2 = team2.getList().get(j);
                if (!member2.isInTeamTemporary()) {
                    final Role player2 = this.roleService.getPlayer(member2.getRoleId());
                    if (!player2.getFaction().equals(role.getFaction())) {
                        member2.setInTeamTemporary(true);
                        this.returnTeam(player2, false);
                    }
                }
            }
        }
    }
    
    public void leaveTemp(final Role role, final boolean is) {
        final Team team = this.getTeam(role.getRoleId());
        if (team == null) {
            return;
        }
        if (!this.isLeaveTempTeam(role, team)) {
            return;
        }
        final Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId());
        if (fight != null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("战斗结束后会自动暂离队伍"));
            team.getFightLeaveNutMap().setv(role.getId().toString(), 0);
            return;
        }
        final BroadcastService broadcastService = SpringUtils.getBean(BroadcastService.class);
        this.updateTeam(1, role.getRoleId());
        this.updateTeamList(role);
        broadcastService.sendTitleInfoNew(role);
        final RespUpdateTeamList respUpdateTeamList = new RespUpdateTeamList();
        final List<TeamMember> teamMemberList = this.getTeamMemberList(team);
        respUpdateTeamList.setList(teamMemberList);
        SceneManager.INSTANCE.sendMessages(role, respUpdateTeamList);
        EventDispatcher.getInstance().fireEvent(new TeamLeaveMomentEvent(EventType.TEAM_LEAVE_MOMENT, role));
        if (is) {
            this.sendTeamChangeMsg(team, "#<LB1>" + role.getName() + "#暂离离开了队伍。", false);
        }
        MessagePusher.pushMessage(role.getId(), new RespMsg("你暂时离开了当前所在队伍，如果需要归队，请点击组队页面的回归队伍按钮"));
    }
    
    public boolean isLeaveTempTeam(final Role role, final Team team) {
        return role.getTempCache("at_leave_temp", false) || SpringUtils.getBean(AtWillHandler.class).leaveMomentConfirm(role);
    }
    
    public void operTeleport(final Role role, final ReqOperTeleportItem item) {
        final Team team = this.getTeam(role.getRoleId());
        if (team == null) {
            return;
        }
        if (!team.isLeader(role.getRoleId())) {
            return;
        }
        final RespConfirm RespConfirm = new RespConfirm();
        RespConfirm.setTips("#<LB1>" + role.getName() + "#邀请你回归队伍。");
        int i = 0;
        while (i < team.getList().size()) {
            final Member member = team.getList().get(i);
            if (member.getRoleId().equals(item.getId()) && !member.isInTeam()) {
                final Role teamRole = this.roleService.getPlayer(member.getId());
                if (teamRole == null) {
                    break;
                }
                teamRole.setConfirm(new ReturnTeamConfirm());
                MessagePusher.pushMessage(teamRole, RespConfirm);
                MessagePusher.pushMessage(role.getId(), new RespMsg("你邀请了#<LB1>" + teamRole.getName() + "#回归队伍"));
                break;
            }
            else {
                ++i;
            }
        }
    }
    
    public void kickOutTeam(final IoSession session, final ReqKickOutTeam reqKickOutTeam) {
        try {
            final String name = reqKickOutTeam.getName();
            final long uid = SpringUtils.getPlayerService().getUidBy(name);
            this.leave(new RoleView(uid), true);
        }
        catch (final Exception e) {
            final Role role = SessionUtils.getRoleBySession(session);
            final Team team = this.getTeam(role.getRoleId());
            if (team != null) {
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    if (member.getName().equals(reqKickOutTeam.getName())) {
                        team.getList().remove(member);
                        break;
                    }
                }
            }
            this.updateTeamList(role);
        }
    }
    
    @EventHandler({ EventType.ENTER_MAP })
    public void handleEnterMapEvent(final EnterMapEvent enterMapEvent) {
        final Role role = enterMapEvent.getRole();
        final Team team = this.getTeam(role.getRoleId());
        if (team != null && team.isLeader(role.getRoleId())) {
            final MapService mapService = SpringUtils.getMapService();
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                if (!member.getId().equals(role.getId())) {
                    if (member.isInTeam()) {
                        final Role player = this.roleService.getPlayer(member.getId());
                        if (player != null) {
                            mapService.changeMap(player, role.getPos().getMapId(), role.getPos().getX(), role.getPos().getY(), "runs_prison");
                        }
                    }
                }
            }
        }
    }
    
    private void fetchMember(final Role leader, final Role role) {
        if (!leader.getPos().getMapId().equals(role.getPos().getMapId())) {
            SpringUtils.getMapService().changeMap(role, leader.getPos().getMapId(), leader.getPos().getX(), leader.getPos().getY(), "runs_prison");
        }
        else {
            SceneManager.INSTANCE.goToPosition(role);
        }
    }
    
    public void reject(final IoSession session, final ReqReject reqReject) {
        final Role role = SessionUtils.getRoleBySession(session);
        final String type = reqReject.getType();
        switch (type) {
            case "request_join": {
                MessagePusher.pushMessage(this.roleService.getOnlinePlayer(reqReject.getName()), new RespMsg("对方拒绝了你的申请。"));
                break;
            }
            case "party": {
                SpringUtils.getBean(PartyService.class).refuseJoinParty(role, reqReject.getName());
                break;
            }
            case "invite_join": {
                MessagePusher.pushMessage(this.roleService.getOnlinePlayer(reqReject.getName()), new RespMsg(role.getName() + "拒绝了你的组队邀请！"));
                break;
            }
        }
        this.cleanTeamRequest(role, reqReject.getType(), reqReject.getName());
    }
    
    private void cleanTeamRequest(final Role role, final String type, final String name) {
        final RespClearnRequest respClearnRequest = new RespClearnRequest();
        respClearnRequest.setType(type);
        final ArrayList<String> names = new ArrayList<String>();
        names.add(name);
        respClearnRequest.setNames(names);
        MessagePusher.pushMessage(role, respClearnRequest);
    }
    
    public void accept(final Role role, final ReqAccept reqAccept) {
        final NutMap sessionData = SessionDataUtils.getData(role);
        Team team = null;
        if ("request_join".equals(reqAccept.getType())) {
            final Role newTeamMember = this.roleService.getOnlinePlayer(reqAccept.getName());
            if (newTeamMember == null) {
                return;
            }
            this.deleteRequest(role.getRoleId() + "request_join", newTeamMember.getRoleId());
            if (!this.isAcceptJoinTeam(role, newTeamMember.getRoleId())) {
                return;
            }
            if (this.fightNewService.getFightByRoleId(role.getRoleId()) != null) {
                MessagePusher.pushMessage(role, new RespMsg("当前不能同意申请。"));
                return;
            }
            if (this.fightNewService.getFightByRoleId(newTeamMember.getRoleId()) != null) {
                MessagePusher.pushMessage(role, new RespMsg("对方当前无法接受同意申请。"));
                return;
            }
            team = this.getTeam(role.getRoleId());
            if (team == null) {
                MessagePusher.pushMessage(role, new RespMsg("未找到队伍信息。"));
                return;
            }
            if (!team.isLeader(role.getRoleId())) {
                final RespMsg respMsg = new RespMsg();
                respMsg.setMsg("只有队长才能邀请别的玩家加入队伍。");
                MessagePusher.pushMessage(role, respMsg);
                return;
            }
            if (team.getList().size() >= 5) {
                MessagePusher.pushMessage(role, new RespMsg("队伍人数已经满，无法增加队员。"));
                return;
            }
            final Team tempTeam = this.getTeam(newTeamMember.getRoleId());
            if (tempTeam != null) {
                MessagePusher.pushMessage(role, new RespMsg("对方已有队伍，无法邀请其加入队伍。"));
                return;
            }
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member1 = team.getList().get(i);
                if (member1.getRoleId().equals(newTeamMember.getRoleId())) {
                    final RespMsg respInviteMsg = new RespMsg();
                    respInviteMsg.setMsg("对方已有队伍，无法邀请其加入队伍。");
                    MessagePusher.pushMessage(role, respInviteMsg);
                    return;
                }
            }
            MessagePusher.pushMessage(newTeamMember, new RespMsg("队长通过您的申请，组队成功"));
            this.sendTeamChangeMsg(team, "欢迎#<LB1>" + role.getName() + "#加入队伍。", false);
            team.getList().add(this.getMember(newTeamMember, role));
            this.saveTeam(team);
            final RespClearnAllRequest respClearnAllRequest = new RespClearnAllRequest();
            respClearnAllRequest.setType(reqAccept.getType());
            MessagePusher.pushMessage(newTeamMember, respClearnAllRequest);
            this.newMemberJoinTeam(team, role, newTeamMember, reqAccept.getType());
            this.broadcastService.giveAppear(newTeamMember, team);
            EventDispatcher.getInstance().fireEvent(new TeamJoinEvent(EventType.TEAM_JOIN, newTeamMember, "request_join"));
            this.returnTeam(newTeamMember, false);
        }
        else if ("invite_join".equals(reqAccept.getType())) {
            final int roleId = sessionData.getInt("team:" + reqAccept.getName());
            this.deleteRequest(roleId + "request_join", role.getRoleId());
            if (!this.isAcceptJoinTeam(role, roleId)) {
                return;
            }
            if (this.fightNewService.getFightByRoleId(role.getRoleId()) != null) {
                MessagePusher.pushMessage(role, new RespMsg("当前不能同意邀请。"));
                return;
            }
            if (this.fightNewService.getFightByRoleId(roleId) != null) {
                MessagePusher.pushMessage(role, new RespMsg("对方当前无法接受同意邀请。"));
                return;
            }
            team = this.getTeam(roleId);
            if (team == null) {
                final RespMsg respMsg = new RespMsg();
                respMsg.setMsg("未找到队伍信息。");
                MessagePusher.pushMessage(role, respMsg);
                return;
            }
            final Role leader = this.roleService.getOnlinePlayer(team.getLeaderUid());
            if (leader == null) {
                return;
            }
            if (team.getList().size() >= 5) {
                final RespMsg respMsg2 = new RespMsg();
                respMsg2.setMsg("队伍人数已经满，无法增加队员。");
                MessagePusher.pushMessage(role, respMsg2);
                return;
            }
            final Team roleTeam = this.getTeam(role.getRoleId());
            if (roleTeam != null) {
                final RespMsg respMsg3 = new RespMsg();
                respMsg3.setMsg("你已经有一个队伍了。");
                MessagePusher.pushMessage(role, respMsg3);
                return;
            }
            MessagePusher.pushMessage(role, new RespMsg("您接收了邀请，组队成功"));
            MessagePusher.pushMessage(leader, new RespMsg("对方已接收你的组队邀请，组队成功"));
            this.sendTeamChangeMsg(team, "欢迎#<LB1>" + role.getName() + "#加入队伍。", false);
            team.getList().add(this.getMember(role, leader));
            this.saveTeam(team);
            final RespClearnAllRequest respClearnAllRequest2 = new RespClearnAllRequest();
            respClearnAllRequest2.setType(reqAccept.getType());
            MessagePusher.pushMessage(role, respClearnAllRequest2);
            this.newMemberJoinTeam(team, leader, role, reqAccept.getType());
            this.broadcastService.giveAppear(this.roleService.getOnlinePlayer(roleId), team);
            EventDispatcher.getInstance().fireEvent(new TeamJoinEvent(EventType.TEAM_JOIN, role, "invite_join"));
            this.returnTeam(role, false);
        }
        else if ("party".equals(reqAccept.getType())) {
            SpringUtils.getBean(PartyService.class).agreeJoinParty(role, reqAccept.getName());
        }
    }
    
    public boolean isAcceptJoinTeam(final Role role, final int roleId) {
        if (!SpringUtils.getBean(TowerTaskHandler.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(LookForTaskHandler.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(DungeonXmHandler.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(DungeonMwkFront.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(DungeonCmHandler.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(AtWillHandler.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(CaHandler.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(WaterHandler.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(KejuTaskHandler.class).isAcceptJoinTeam(role, roleId)) {
            return false;
        }
        if (role.getPos().getMapId().equals(1201)) {
            MessagePusher.pushMessage(role, new RespMsg("天牢重地，禁止拉帮结伙！"));
            return false;
        }
        return true;
    }
    
    @EventHandler({ EventType.ROLE_LEVEL_UP })
    public void handleRoleLevelUpEvent(final RoleLevelUpEvent roleLevelUpEvent) {
        final Role role = roleLevelUpEvent.getRole();
        if (role == null) {
            return;
        }
        final Team team = this.getTeam(role.getRoleId());
        if (team != null) {
            this.saveTeam(team);
            this.updateTeamList(role);
        }
    }
    
    public void returnTeam(final Role role, final boolean flag) {
        final Team team = this.getTeam(role.getRoleId());
        if (team == null) {
            return;
        }
        final Role leader = this.roleService.getPlayer(team.getLeaderUid());
        if (leader == null) {
            return;
        }
        final int mapId = leader.getPos().getMapId();
        if ((mapId == 3002 || mapId == 3021 || mapId == 3022) && !leader.getFaction().equals(role.getFaction())) {
            MessagePusher.pushMessage(role, new RespMsg("队长正在#<LB1>帮派#中，非同帮成员无法归队"));
            return;
        }
        if (!this.isBackToTeam(team, role)) {
            return;
        }
        if (this.fightNewService.getFightByRoleId(role.getRoleId()) != null) {
            MessagePusher.pushMessage(role, new RespMsg("当前不能归队。"));
            return;
        }
        if (this.fightNewService.getFightByRoleId(leader.getRoleId()) != null) {
            MessagePusher.pushMessage(role, new RespMsg("对方当前无法接受归队。"));
            return;
        }
        if (flag) {
            MessagePusher.pushMessage(role, new RespMsg("你回归队伍了"));
            this.sendTeamChangeMsg(team, "#<LB1>" + role.getName() + "#重新归队。", false);
        }
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            if (member.getRoleId().equals(role.getRoleId())) {
                member.setInTeam(true);
                break;
            }
        }
        this.saveTeam(team);
        this.updateTeamList(role);
        this.broadcastService.sendTitleInfo(role);
        this.broadcastService.giveAppear(role, team);
        this.fetchMember(leader, role);
        EventDispatcher.getInstance().fireEvent(new TeamJoinEvent(EventType.TEAM_JOIN, role, "return_join"));
    }
    
    public boolean isBackToTeam(final Team team, final Role role) {
        return SpringUtils.getBean(TowerTaskHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(LookForTaskHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(DungeonXmHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(DungeonMwkFront.class).isBackToTeam(team, role)
                && SpringUtils.getBean(DungeonCmHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(AtWillHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(CaHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(WaterHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(GangWarsHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(HgFightHandler.class).isBackToTeam(team, role)
                && SpringUtils.getBean(KejuTaskHandler.class).isBackToTeam(team, role);
    }
    
    private void newMemberJoinTeam(final Team team, final Role leader, final Role role, final String opType) {
        this.cleanTeamRequest(leader, opType, role.getName());
        this.broadcastService.sendTitleInfo(role);
        this.broadcastService.sendTitleInfo(leader);
        this.broadcastService.sendUpdateAppear(role);
        this.updateTeamList(role);
        this.sendAppearFormTeam(team);
    }
    
    public void foundJoin(final IoSession session, final ReqFoundJoin foundJoin) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (!this.isFoundJoinTeam(role)) {
            return;
        }
        if (foundJoin.getName().equals(role.getName())) {
            this.createTeam(role, foundJoin);
        }
    }
    
    public boolean isFoundJoinTeam(final Role role) {
        if (role.getPos().getMapId().equals(1201)) {
            MessagePusher.pushMessage(role, new RespMsg("天牢重地，禁止拉帮结伙！"));
            return false;
        }
        return SpringUtils.getBean(KejuTaskHandler.class).isFoundJoinTeam(role);
    }
    
    public void requestJoin(final Role role, final ReqRequestJoin requestJoin) {
        if ("request_join".equals(requestJoin.getType())) {
            int toRoleId = requestJoin.getRoleId();
            if (toRoleId <= 0) {
                final long toUid = SpringUtils.getPlayerService().getUidBy(requestJoin.getName());
                final PlayerProfile playerProfile = SpringUtils.getPlayerService().queryPlayerProfile(toUid);
                toRoleId = ((playerProfile != null) ? playerProfile.getRoleId() : 0);
            }
            final Team team = this.getTeam(toRoleId);
            if (team == null) {
                MessagePusher.pushMessage(role, new RespMsg("队伍不存在。"));
                return;
            }
            if (SpringUtils.getBean(PkService.class).isInPrisonTime(role)) {
                MessagePusher.notify2Player(role, 1113);
                return;
            }
            if (!this.isRequestJoinTeam(role, team)) {
                return;
            }
            if (team.getList().size() >= 5) {
                final RespMsg respInviteMsg = new RespMsg();
                respInviteMsg.setMsg("队伍人数已满。");
                MessagePusher.pushMessage(role, respInviteMsg);
                return;
            }
            final Team myTeam = this.getTeam(role.getRoleId());
            if (myTeam != null) {
                MessagePusher.pushMessage(role, new RespMsg("你已经在队伍中。"));
                return;
            }
            final Role leader = this.roleService.getOnlinePlayer(team.getLeaderUid());
            if (leader == null) {
                MessagePusher.pushMessage(role, new RespMsg("#<LB1>" + requestJoin.getName() + "#不在线！"));
                return;
            }
            MessagePusher.pushMessage(leader, new RespMsg(role.getName() + "申请加入你的队伍，请打开队伍界面查看申请信息。"));
            MessagePusher.pushMessage(role, new RespMsg("你申请加入#<LB1>" + leader.getName() + "#的队伍！"));
            MessagePusher.pushMessage(leader, this.loadTeamRequest(team, role, "request_join", String.valueOf(TimeUtil.timeInMillis())));
            this.cacheRequest(team.getLeaderRoleId() + "request_join", role.getRoleId());
        }
        else if ("invite_join".equals(requestJoin.getType())) {
            if (role.getLevel() < 20) {
                MessagePusher.pushMessage(role, new RespMsg("20级后才能邀请加入队伍。"));
                return;
            }
            Team team2 = this.getTeam(role.getRoleId());
            if (team2 == null) {
                if (role.getPos().getMapId().equals(1201)) {
                    MessagePusher.pushMessage(role, new RespMsg("天牢重地，禁止拉帮结伙！"));
                    return;
                }
                final ReqFoundJoin foundJoin = new ReqFoundJoin("", "", new int[0], 0, 0, 0, 0, 0);
                team2 = this.createTeam(role, foundJoin);
            }
            if (team2.getList().size() >= 5) {
                final RespMsg respInviteMsg2 = new RespMsg();
                respInviteMsg2.setMsg("队伍人数已满，无法邀请新成员。");
                MessagePusher.pushMessage(role, respInviteMsg2);
                return;
            }
            Role toRole = null;
            if (requestJoin.getRoleId() <= 0) {
                toRole = this.roleService.getOnlinePlayer(requestJoin.getName());
            }
            else {
                toRole = this.roleService.getPlayerBy(requestJoin.getRoleId());
            }
            if (toRole == null) {
                final RespMsg respInviteMsg = new RespMsg();
                respInviteMsg.setMsg("目标玩家不在线。");
                MessagePusher.pushMessage(role, respInviteMsg);
                return;
            }
            if (!this.isInviteJoinTeam(role, toRole.getRoleId())) {
                return;
            }
            final Team toTeam = this.getTeam(toRole.getRoleId());
            if (toTeam != null) {
                final RespMsg respInviteMsg3 = new RespMsg();
                respInviteMsg3.setMsg("对方已在队伍中。");
                MessagePusher.pushMessage(role, respInviteMsg3);
                return;
            }
            MessagePusher.pushMessage(role, new RespMsg("邀请信息已发送"));
            MessagePusher.pushMessage(toRole, this.loadTeamRequest(team2, role, "invite_join", String.valueOf(TimeUtil.timeInMillis())));
            MessagePusher.pushMessage(toRole, new RespMsg(role.getName() + "邀请你加入其队伍，请打开队伍界面查看邀请信息。"));
            final NutMap sessionData = SessionDataUtils.getData(toRole);
            sessionData.setv("team:" + role.getName(), (Object)role.getRoleId());
            this.cacheRequest(toRole.getRoleId() + "invite_join", team2.getLeaderRoleId());
        }
        else if ("request_team_leader".equals(requestJoin.getType())) {
            final Team team2 = this.getTeam(role.getRoleId());
            if (team2 == null) {
                return;
            }
            final Member member = team2.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
            if (member == null || !member.isInTeam()) {
                return;
            }
            final Role leader2 = this.roleService.getOnlinePlayer(team2.getLeaderUid());
            if (leader2 == null) {
                return;
            }
            final RespAroundInfo respAroundInfo = new RespAroundInfo();
            respAroundInfo.setInviteName(role.getName());
            respAroundInfo.setType("request_team_leader");
            MessagePusher.pushMessage(leader2, respAroundInfo);
            MessagePusher.pushMessage(role, new RespMsg("你的申请已发送。"));
            final RespConfirm confirm = new RespConfirm();
            confirm.setTips("#<LB1>" + role.getName() + "#申请成为队长，是否同意？");
            confirm.setConfirmType("reject_count_down");
            confirm.setDownCount(5);
            confirm.setShow_dlg_mode(3);
            MessagePusher.pushMessage(leader2, confirm);
            leader2.setConfirm(new RequestTeamLeaderConfirm(role.getRoleId()));
            SpringUtils.getBean(ServerService.class).getScheduledExecutorService().schedule(new Runnable() {
                @Override
                public void run() {
                    final RequestTeamLeaderConfirm requestTeamLeaderConfirm = (RequestTeamLeaderConfirm)leader2.getConfirm();
                    if (requestTeamLeaderConfirm.isAgree()) {
                        return;
                    }
                    SpringUtils.getTeamService().acceptRequestTeamLeader(leader2, requestTeamLeaderConfirm.getRequestId());
                }
            }, 5L, TimeUnit.SECONDS);
        }
        else if ("party_remote".equals(requestJoin.getType())) {
            SpringUtils.getBean(PartyService.class).reqJoinParty(role, requestJoin.getName());
        }
        else if ("party_one_key".equals(requestJoin.getType())) {
            SpringUtils.getBean(PartyService.class).oneKeyJoin(role);
        }
    }
    
    public boolean isRequestJoinTeam(final Role role, final Team team) {
        if (!SpringUtils.getBean(TowerTaskHandler.class).isRequestJoinTeam(role, team)) {
            return false;
        }
        if (!SpringUtils.getBean(LookForTaskHandler.class).isRequestJoinTeam(role, team)) {
            return false;
        }
        if (!SpringUtils.getBean(DungeonXmHandler.class).isRequestJoinTeam(role, team)) {
            return false;
        }
        if (!SpringUtils.getBean(AtWillHandler.class).isRequestJoinTeam(role, team)) {
            return false;
        }
        if (!SpringUtils.getBean(CaHandler.class).isRequestJoinTeam(role, team)) {
            return false;
        }
        if (!SpringUtils.getBean(WaterHandler.class).isRequestJoinTeam(role, team)) {
            return false;
        }
        if (!SpringUtils.getBean(KejuTaskHandler.class).isRequestJoinTeam(role)) {
            return false;
        }
        if (role.getPos().getMapId().equals(1201)) {
            MessagePusher.pushMessage(role, new RespMsg("天牢重地，禁止拉帮结伙！"));
            return false;
        }
        return true;
    }
    
    public boolean isInviteJoinTeam(final Role role, final int roleId) {
        if (!SpringUtils.getBean(TowerTaskHandler.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(LookForTaskHandler.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(DungeonXmHandler.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(DungeonMwkFront.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(DungeonCmHandler.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(AtWillHandler.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(CaHandler.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(WaterHandler.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (!SpringUtils.getBean(KejuTaskHandler.class).isInviteJoinTeam(role, roleId)) {
            return false;
        }
        if (role.getPos().getMapId().equals(1201)) {
            MessagePusher.pushMessage(role, new RespMsg("天牢重地，禁止拉帮结伙！"));
            return false;
        }
        return true;
    }
    
    public RespAroundInfo loadTeamRequest(final Team team, final Role role, final String type, final String time) {
        final RespAroundInfo respAroundInfo = new RespAroundInfo();
        respAroundInfo.setRoleId(team.getLeaderRoleId());
        respAroundInfo.setInviteName(role.getName());
        respAroundInfo.setTeamMembersCount(team.getList().size());
        respAroundInfo.setSerial(role.getSerial());
        respAroundInfo.setTime(time);
        respAroundInfo.setType(type);
        final ArrayList<FiedValue> arrayList = new ArrayList<FiedValue>();
        arrayList.add(new FiedValue("level", (byte)2, role.getLevel()));
        arrayList.add(new FiedValue("relive", (byte)1, role.getRelive()));
        arrayList.add(new FiedValue("aim", (byte)4, team.getTarget()));
        arrayList.add(new FiedValue("weapon", (byte)3, role.getWeaponIcon()));
        final List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            arrayList.add(new FiedValue("gem", (byte)1, num));
        }
        final List<FiedValue> colorList = this.roleService.deyList(role.getColor());
        if (colorList.size() > 0) {
            arrayList.addAll(colorList);
        }
        respAroundInfo.setList(arrayList);
        return respAroundInfo;
    }
    
    public void cacheRequest(final String key, final Integer value) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        TeamRequest teamRequest = this.redisService.get(key, TeamRequest.class);
        if (teamRequest != null) {
            map = teamRequest.getMap();
        }
        teamRequest = new TeamRequest();
        map.put(value, String.valueOf(TimeUtil.timeInMillis()));
        teamRequest.setMap(map);
        this.redisService.set(key, teamRequest, 600L);
    }
    
    public void deleteRequest(final String key, final Integer value) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        TeamRequest teamRequest = this.redisService.get(key, TeamRequest.class);
        if (teamRequest != null) {
            map = teamRequest.getMap();
        }
        teamRequest = new TeamRequest();
        map.remove(value);
        if (map.size() <= 0) {
            this.redisService.del(key);
        }
        else {
            teamRequest.setMap(map);
            final Long ttl = this.redisService.ttl(key);
            this.redisService.set(key, teamRequest, ttl);
        }
    }
    
    public Team createTeam(final Role role, final ReqFoundJoin reqFoundJoin) {
        final RespClearnAllRequest respClearnAllRequest = new RespClearnAllRequest();
        respClearnAllRequest.setType("invite_join");
        MessagePusher.pushMessage(role, respClearnAllRequest);
        if (this.getTeam(role.getRoleId()) != null) {
            MessagePusher.pushMessage(role, new RespMsg("你已经在队伍中了"));
            return null;
        }
        final Team team = new Team();
        team.setTarget(reqFoundJoin.getTarget());
        team.setMsg(reqFoundJoin.getMsg());
        team.setRaceLimit(reqFoundJoin.getRaceLimit());
        team.setSexLimit(reqFoundJoin.getSexLimit());
        team.setMinLevel(reqFoundJoin.getMinLevel());
        team.setMaxLevel(reqFoundJoin.getMaxLevel());
        team.setMinRelive(reqFoundJoin.getMinRelive());
        team.setMaxRelive(reqFoundJoin.getMaxRelive());
        team.setTarget(ActivityType.NO_TARGET.getActivityId());
        team.setCreateTime(new Date());
        final Member member = this.getMember(role, role);
        final List<Member> arrayList = Collections.synchronizedList(new ArrayList<Member>());
        arrayList.add(member);
        team.setList(arrayList);
        final ArrayList<Member> partnerMembers = SpringUtils.getBean(PartnerService.class).partner2Member(role);
        team.setPartner(partnerMembers);
        this.saveTeam(team);
        final Message titleInfo = this.broadcastService.getTitleInfoNew(role);
        MessagePusher.pushMessage(role, titleInfo);
        this.broadcastService.sendTitleInfo(role);
        this.broadcastService.sendUpdateAppear(role);
        this.updateTeamList(role);
        final RespMsg respMsg = new RespMsg();
        respMsg.setMsg("组队成功，您现在已经是队长啦!");
        MessagePusher.pushMessage(role, respMsg);
        return team;
    }
    
    public boolean quiltTeam(final Team team, final int id) {
        SessionCache.ROLE_TEAM.remove(id);
        final List<Member> list = team.getList();
        Member memberRemove = null;
        for (int i = 0; i < list.size(); ++i) {
            final Member member = list.get(i);
            if (member.getRoleId().equals(id)) {
                memberRemove = member;
                break;
            }
        }
        if (memberRemove != null) {
            list.remove(memberRemove);
        }
        if (list.size() <= 0) {
            SessionCache.TEAMS.remove(team.getId());
            return true;
        }
        final Member member2 = list.get(0);
        if (member2 != null) {
            member2.setInTeam(true);
            final Role role = this.roleService.getPlayer(member2.getRoleId());
            if (role != null) {
                final ArrayList<Member> partnerMembers = SpringUtils.getBean(PartnerService.class).partner2Member(role);
                team.setPartner(partnerMembers);
                this.sendAppearForm(team);
            }
        }
        return false;
    }
    
    public void acceptRequestTeamLeader(final Role leader, final int requestId) {
        final Role role = SpringUtils.getRoleService().getOnlinePlayer(requestId);
        if (role == null) {
            return;
        }
        this.changeLeader(leader, requestId);
    }
    
    public void changeLeader(final Role leader, final int roleId) {
        Team team = this.getTeam(roleId);
        if (team == null) {
            return;
        }
        if (!team.isLeader(leader.getRoleId()) || !team.getMember(roleId).isInTeam()) {
            return;
        }
        EventDispatcher.getInstance().fireEvent(new TeamChangeLeaderEvent(EventType.TEAM_CHANGE_LEADER, leader, team.getList()));
        final List<Member> members = team.getList();
        Member toLeader = null;
        int index = 0;
        for (int i = 0; i < members.size(); ++i) {
            if (members.get(i).getRoleId().equals(roleId)) {
                index = i;
                toLeader = members.get(i);
                break;
            }
        }
        final Role tmpRole = this.roleService.getPlayerBy(roleId);
        final Member oldLeader = members.get(0);
        members.set(index, oldLeader);
        members.set(0, toLeader);
        team.setList(members);
        final ArrayList<Member> partnerMembers = SpringUtils.getBean(PartnerService.class).partner2Member(tmpRole);
        team.setPartner(partnerMembers);
        this.saveTeam(team);
        final BroadcastService broadcastService = SpringUtils.getBean(BroadcastService.class);
        team = this.getTeamByTeamId(team.getId());
        final Role newLeader = this.roleService.getPlayer(team.getLeaderUid());
        broadcastService.sendTitleInfo(newLeader);
        broadcastService.sendTitleInfo(leader);
        this.updateTeamList(newLeader);
        MessagePusher.pushMessage(tmpRole, new RespMsg("你成为了队长。"));
        this.sendTeamChangeMsg(team, "#<LB1>" + newLeader.getName() + "#成为队长。", false);
    }
    
    public void changeLeader(final IoSession session, final ReqChangeTeamLeader reqChangeTeamLeader) {
        final int roleId = reqChangeTeamLeader.getRoleId();
        final Role role = SessionUtils.getRoleBySession(session);
        this.changeLeader(role, roleId);
    }
    
    public void sendTeamChangeMsg(final Team team, final String msg, final boolean flag) {
        if (team == null) {
            return;
        }
        final RespMsg respMsg = new RespMsg(msg);
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            if (!flag || !team.isLeader(member.getRoleId())) {
                if (member.isInTeam()) {
                    final Role memberRole = SpringUtils.getRoleService().getOnlinePlayer(member.getId());
                    MessagePusher.pushMessage(memberRole, respMsg);
                }
            }
        }
    }
    
    public static int getId() {
        return TeamService.atomicInteger.getAndIncrement();
    }
    
    public Role getTeamLeader(final int teamId) {
        final Team team = this.getTeamByTeamId(teamId);
        if (team == null) {
            return null;
        }
        final Role leader = this.roleService.getPlayer(team.getLeaderUid());
        if (leader == null) {
            return null;
        }
        return leader;
    }
    
    public void saveTeam(final Team team) {
        if (team.getId() <= 0) {
            team.setId(getId());
        }
        SessionCache.TEAMS.put(team.getId(), team);
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            SessionCache.ROLE_TEAM.put(member.getRoleId(), team.getId());
        }
    }
    
    private Member getMember(final Role role, final Role leader) {
        final Member member = Member.valueOf(role);
        if (!role.getId().equals(leader.getId())) {
            final int mapId = leader.getPos().getMapId();
            if ((mapId == 3002 || mapId == 3021 || mapId == 3022) && !leader.getFaction().equals(role.getFaction())) {
                member.setInTeam(false);
            }
            if (mapId == 3001) {
                if (!leader.getFaction().equals(role.getFaction())) {
                    member.setInTeam(false);
                }
                else if (!role.getPos().getMapId().equals(3001)) {
                    member.setInTeam(false);
                }
            }
        }
        return member;
    }
    
    private void getMember(final Role role, final Member member) {
        Member.valueOf(role, member);
    }
    
    private void sendJoinSuccess(final Team team, final Role joinRole) {
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            final Role teamRole = SpringUtils.getRoleService().getOnlinePlayer(member.getId());
            if (teamRole != null) {
                if (!teamRole.getRoleId().equals(joinRole.getRoleId())) {
                    this.JoinSuccess(teamRole, joinRole);
                }
            }
        }
    }
    
    private void sendAppearFormTeam(final Team team) {
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            final Role memberRole = this.roleService.getOnlinePlayer(member.getId());
            for (int j = 0; j < team.getList().size(); ++j) {
                final Member oMember = team.getList().get(j);
                final Role appearRole = this.roleService.getOnlinePlayer(oMember.getId());
                this.broadcastService.updateAppearance(memberRole, appearRole);
            }
        }
    }
    
    private void sendAppearForm(final Team team) {
        for (int j = 0; j < team.getList().size(); ++j) {
            final Member member = team.getList().get(j);
            if (!member.isPartner()) {
                final Role role = this.roleService.getOnlinePlayer(member.getId());
                if (role != null) {
                    this.broadcastService.sendUpdateAppear(role);
                }
            }
        }
    }
    
    public void JoinSuccess(final Role role, final Role leader) {
        final RespFriendAddRole respFriendAddRole = new RespFriendAddRole();
        respFriendAddRole.setName(role.getName());
        respFriendAddRole.setType1(1);
        respFriendAddRole.setType("6");
        final ArrayList<FiedValue> arrayList = new ArrayList<FiedValue>();
        arrayList.add(new FiedValue("id", (byte)3, role.getRoleId()));
        arrayList.add(new FiedValue("name", (byte)4, role.getName()));
        arrayList.add(new FiedValue("icon", (byte)3, role.getIcon()));
        arrayList.add(new FiedValue("race", (byte)3, role.getRace()));
        arrayList.add(new FiedValue("level", (byte)3, role.getLevel()));
        arrayList.add(new FiedValue("relive", (byte)3, role.getRelive()));
        respFriendAddRole.setList(arrayList);
        MessagePusher.pushMessage(leader, respFriendAddRole);
    }
    
    private void updateTeam(final int status, final int roleId) {
        final Integer integer = SessionCache.ROLE_TEAM.get(roleId);
        if (integer == null) {
            return;
        }
        final int teamId = integer;
        if (teamId <= 0) {
            return;
        }
        final Team team = SessionCache.TEAMS.get(teamId);
        if (status == 1) {
            for (int j = 0; j < team.getList().size(); ++j) {
                final Member member = team.getList().get(j);
                if (member.getRoleId().equals(roleId)) {
                    member.setInTeam(false);
                    break;
                }
            }
            this.saveTeam(team);
        }
    }
    
    public boolean isInTeamAndNotLeader(final Role role) {
        return this.isInTeam(role) && !this.isTeamLeader(role);
    }
    
    public void checkRoleInfo(final IoSession session, final ReqCheckPlayer reqCheckPlayer) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final Role player = this.roleService.getPlayerBy(reqCheckPlayer.getRoleId());
        if (player != null) {
            final RespLookPlayerInfo lookPlayerInfo = new RespLookPlayerInfo();
            lookPlayerInfo.setRoleId(player.getRoleId());
            lookPlayerInfo.setName(player.getName());
            lookPlayerInfo.setFaction(this.roleService.getFactionName(player));
            lookPlayerInfo.setSerial(player.getSerial());
            lookPlayerInfo.setLevel(player.getLevel().shortValue());
            lookPlayerInfo.setRei(player.getRelive().byteValue());
            final Team team = this.getTeam(player.getRoleId());
            if (team != null) {
                lookPlayerInfo.setTeamMembersCount(team.getList().size());
                lookPlayerInfo.setLeaderRoleId(team.getLeaderRoleId());
            }
            if (role != null) {
                final int theTime = (int)(System.currentTimeMillis() / 1000L);
                final int incense = role.getPkInfoBox().getIncense();
                if (incense > 0 && incense > theTime) {
                    lookPlayerInfo.setIsPK((byte)1);
                }
            }
            MessagePusher.pushMessage(session, lookPlayerInfo);
        }
        else {
            MessagePusher.pushMessage(session, new RespMsg("目标玩家不在线"));
        }
    }
    
    public void memberHandleThreadLocal(final Team team, final Consumer<Role> consumer) {
        Role leaderRole = null;
        for (int j = 0; j < team.getList().size(); ++j) {
            final Member member = team.getList().get(j);
            if (member != null) {
                if (member.isInTeam()) {
                    final Role memberRole = this.roleService.getPlayer(member.getRoleId());
                    if (memberRole != null) {
                        if (team.isLeader(memberRole.getRoleId())) {
                            leaderRole = memberRole;
                        }
                        else {
                            ThreadLocalUtil.addLocalTask(memberRole, () -> consumer.accept(memberRole));
                        }
                    }
                }
            }
        }
        consumer.accept(leaderRole);
    }
    
    public void memberHandle(final Role role, final Consumer<Role> consumer) {
        if (role != null && consumer != null) {
            if (this.isInTeam(role) && this.isTeamLeader(role)) {
                final Team team = this.getTeam(role.getRoleId());
                this.memberHandle(team, consumer);
            }
            else {
                consumer.accept(role);
            }
        }
    }
    
    public void memberHandleThreadLocal(final Role role, final Consumer<Role> consumer) {
        if (this.isInTeam(role) && this.isTeamLeader(role)) {
            final Team team = this.getTeam(role.getRoleId());
            this.memberHandleThreadLocal(team, consumer);
        }
        else {
            consumer.accept(role);
        }
    }
    
    public void memberHandle(final Team team, final Consumer<Role> consumer) {
        try {
            for (int j = 0; j < team.getList().size(); ++j) {
                final Member member = team.getList().get(j);
                if (member != null) {
                    if (member.isInTeam()) {
                        final Role memberRole = this.roleService.getPlayer(member.getRoleId());
                        if (memberRole != null) {
                            consumer.accept(memberRole);
                        }
                    }
                }
            }
        }
        catch (final ConcurrentModificationException ex) {}
    }
    
    public String getNotInTeam(final Team team) {
        StringBuilder stringBuilder = null;
        for (int j = 0; j < team.getList().size(); ++j) {
            final Member member = team.getList().get(j);
            if (member != null) {
                if (!member.isInTeam()) {
                    if (stringBuilder == null) {
                        stringBuilder = new StringBuilder();
                    }
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append("、");
                    }
                    stringBuilder.append(member.getName());
                }
            }
        }
        return (stringBuilder != null) ? stringBuilder.toString() : null;
    }
    
    public void getTeamList(final Role role, final String activityType) {
        List<Team> teams = null;
        teams = SessionCache.TEAMS.values().stream().filter(obj -> obj.getTarget().equals(activityType) && obj.getList().size() < 5).collect(Collectors.toList());
        final List<SmartTeam> smartTeams = new ArrayList<SmartTeam>();
        List<SmartTeamMember> smartTeamMembers = null;
        for (final Team team : teams) {
            smartTeamMembers = new ArrayList<SmartTeamMember>();
            for (int j = 0; j < team.getList().size(); ++j) {
                final Member member = team.getList().get(j);
                final SmartTeamMember smartTeamMember = new SmartTeamMember(member);
                smartTeamMembers.add(smartTeamMember);
            }
            smartTeams.add(new SmartTeam(smartTeamMembers));
        }
        MessagePusher.pushMessage(role, new RespTeamList(smartTeams));
    }
    
    public void updateTeamTarget(final Role role, final int roleId, final String activityType) {
        final Team team = this.getTeam(roleId);
        if (team == null) {
            return;
        }
        team.setTarget(activityType);
        this.updateTeamList(role);
    }
    
    public void joinTeams(final Role role, final String activityType) {
        final List<Team> teams = SessionCache.TEAMS.values().stream().filter(team -> team.getTarget().equals(activityType)).collect(Collectors.toList());
        if (teams == null || teams.size() <= 0) {
            return;
        }
        final Team myTeam = this.getTeam(role.getRoleId());
        if (myTeam != null) {
            final RespMsg respInviteMsg = new RespMsg();
            respInviteMsg.setMsg("你已经在队伍中。");
            MessagePusher.pushMessage(role, respInviteMsg);
            return;
        }
        final Iterator<Team> iterator = teams.iterator();
        while (iterator.hasNext()) {
            Team team = iterator.next();
            if (!this.isRequestJoinTeam(role, team)) {
                return;
            }
            if (team.getList().size() >= 5) {
                final RespMsg respInviteMsg2 = new RespMsg();
                respInviteMsg2.setMsg("队伍人数已满。");
                MessagePusher.pushMessage(role, respInviteMsg2);
            }
            else {
                final Role leader = this.roleService.getPlayer(team.getLeaderUid());
                MessagePusher.pushMessage(leader, new RespMsg(role.getName() + "申请加入你的队伍，请打开队伍界面查看申请信息。"));
                MessagePusher.pushMessage(leader, this.loadTeamRequest(team, role, "request_join", String.valueOf(TimeUtil.timeInMillis())));
                this.cacheRequest(team.getLeaderRoleId() + "request_join", role.getRoleId());
            }
        }
        MessagePusher.pushMessage(role, new RespMsg("你申请一键加入队伍！"));
    }
    
    public void getTeamById(final Role role, final int roleId) {
        final Team team = this.getTeam(roleId);
        if (team == null) {
            return;
        }
        final List<SmartTeamMember> list = new ArrayList<SmartTeamMember>();
        for (int j = 0; j < team.getList().size(); ++j) {
            final Member member = team.getList().get(j);
            final SmartTeamMember smartTeamMember = new SmartTeamMember(member);
            list.add(smartTeamMember);
        }
        final Optional<ActivityType> first = ActivityType.getActivity().stream().filter(obj -> obj.getActivityId().equals(team.getTarget())).findFirst();
        final RespSmartTeamMemerList respSmartTeamMemerList = new RespSmartTeamMemerList();
        respSmartTeamMemerList.setTeamMemberList(list);
        if (first.isPresent()) {
            final ActivityType activityType = first.get();
            respSmartTeamMemerList.setSerial(activityType.getActivityId());
            respSmartTeamMemerList.setName(activityType.getName());
        }
        else {
            respSmartTeamMemerList.setName("无目标");
            respSmartTeamMemerList.setSerial("0");
        }
        MessagePusher.pushMessage(role, respSmartTeamMemerList);
    }
    
    static {
        TeamService.atomicInteger = new AtomicInteger(1);
    }
}
