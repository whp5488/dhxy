package com.kitty.system.game.team.service;

import com.kitty.cache.RedisService;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.listener.event.LogoutSelfEvent;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.listener.event.TeamChangeLeaderEvent;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.listener.event.TeamLeaveEvent;
import com.kitty.listener.event.TeamLeaveMomentEvent;
import com.kitty.mina.cache.SessionCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.utils.SessionDataUtils;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.hg.HgFightHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.confirm.model.RequestTeamLeaderConfirm;
import com.kitty.system.game.confirm.model.ReturnTeamConfirm;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.friend.RoleView;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.game.player.PlayerProfile;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.task.service.handler.LookForTaskHandler;
import com.kitty.system.game.task.service.handler.TowerTaskHandler;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import com.kitty.system.game.task.service.handler.dungeon.DungeonXmHandler;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.RespClearnAllRequest;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.domain.TeamMember;
import com.kitty.system.game.team.domain.vo.SmartTeam;
import com.kitty.system.game.team.domain.vo.SmartTeamMember;
import com.kitty.system.game.team.domain.vo.TeamRequest;
import com.kitty.system.game.team.message.ReqAccept;
import com.kitty.system.game.team.message.ReqChangeTeamLeader;
import com.kitty.system.game.team.message.ReqCheckPlayer;
import com.kitty.system.game.team.message.ReqFoundJoin;
import com.kitty.system.game.team.message.ReqKickOutTeam;
import com.kitty.system.game.team.message.ReqOperTeleportItem;
import com.kitty.system.game.team.message.ReqRefreshRequestInfo;
import com.kitty.system.game.team.message.ReqReject;
import com.kitty.system.game.team.message.ReqRequestJoin;
import com.kitty.system.game.team.message.RespAroundInfo;
import com.kitty.system.game.team.message.RespClearnRequest;
import com.kitty.system.game.team.message.RespConfirm;
import com.kitty.system.game.team.message.RespFriendAddRole;
import com.kitty.system.game.team.message.RespLobbyInfo;
import com.kitty.system.game.team.message.RespLookPlayerInfo;
import com.kitty.system.game.team.message.RespSmartTeamMemerList;
import com.kitty.system.game.team.message.RespTeamList;
import com.kitty.system.game.team.message.RespUpdateTeamList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
  private static AtomicInteger atomicInteger = new AtomicInteger(1);
  
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
  
  @EventHandler({EventType.LOGOUT_SELF})
  public void handleLogoutSelfEvent(LogoutSelfEvent logoutSelfEvent) {
    Role role = logoutSelfEvent.getRole();
    Team team = getTeam(role.getRoleId().intValue());
    if (team != null)
      leave(role, false); 
  }
  
  public void updateTeamList(Role role) {
    Team team = getTeam(role.getRoleId().intValue());
    if (team != null) {
      RespUpdateTeamList respUpdateTeamList = new RespUpdateTeamList();
      respUpdateTeamList.setTarget(team.getTarget());
      respUpdateTeamList.setList(getTeamMemberList(team));
      for (int i = 0; i < team.getList().size(); i++) {
        Member member = team.getList().get(i);
        if (member != null && !member.isPartner()) {
          Role memberRole = this.roleService.getPlayer(member.getId().longValue());
          this.broadcastService.updateAppearance(role);
          MessagePusher.pushMessage(memberRole, (Message)respUpdateTeamList);
        } 
      } 
    } 
  }
  
  public void liveLoadTeamList(Role role) {
    Team team = getTeam(role.getRoleId().intValue());
    if (team != null && team.getLeaderRoleId() == role.getRoleId().intValue()) {
      TeamRequest request = (TeamRequest)this.redisService.get(team.getLeaderRoleId() + "request_join", TeamRequest.class);
      liveLoadTeamList(role, team, request, "request_join");
      TeamRequest invite = (TeamRequest)this.redisService.get(team.getLeaderRoleId() + "invite_join", TeamRequest.class);
      liveLoadTeamList(role, team, invite, "invite_join");
    } 
  }
  
  public void liveLoadTeamList(Role role, Team team, TeamRequest request, String type) {
    if (request != null && request.getMap() != null) {
      LinkedHashMap<Integer, String> map = request.getMap();
      int size = map.size();
      int index = 0;
      for (Integer key : map.keySet()) {
        if (index >= size - 5) {
          Role player = this.roleService.getPlayerBy(key.intValue());
          if (player != null)
            MessagePusher.pushMessage(role, (Message)loadTeamRequest(team, player, type, map.get(key))); 
        } 
        index++;
      } 
    } 
  }
  
  public List<TeamMember> getTeamMemberList(Team team) {
    List<TeamMember> list = new ArrayList<>();
    int index = 0;
    Member captainMember = null;
    boolean isHavePartner = false;
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member != null) {
        if (member.isPartner())
          isHavePartner = true; 
        TeamMember teamMember = new TeamMember();
        if (!member.isInTeam())
          teamMember.setStatus(2); 
        if (member.getObjectId() == null || "".equals(member.getObjectId())) {
          teamMember.setRoleId(member.getRoleId());
        } else {
          teamMember.setRoleId(member.getObjectId());
        } 
        if (!member.isPartner()) {
          Role role = this.roleService.getPlayer(member.getId().longValue());
          getMember(role, member);
          teamMember.setLight_effect(this.equipService.getWearEffect(role));
        } 
        teamMember.setWeaponIcon(Integer.valueOf(member.getWeaponIcon()));
        teamMember.setMasquerade(member.getMasquerade());
        teamMember.setList(getMemberField(member));
        teamMember.setPet(member.getPet());
        teamMember.setGem((byte)member.getGem());
        list.add(teamMember);
        if (index == 0)
          captainMember = member; 
        index++;
      } 
    } 
    if (captainMember != null && list.size() < 5 && team.getPartner().size() > 0 && !isHavePartner)
      for (Member partner : team.getPartner()) {
        TeamMember teamMember = new TeamMember();
        teamMember.setRoleId(partner.getObjectId());
        teamMember.setList(getMemberField(partner));
        list.add(teamMember);
      }  
    return list;
  }
  
  private List<FiedValue> getMemberField(Member member) {
    List<FiedValue> fieldValues = new ArrayList<>();
    fieldValues.add(new FiedValue("level", (byte)3, Short.valueOf(member.getLevel())));
    fieldValues.add(new FiedValue("relive", (byte)3, Byte.valueOf(member.getRelive())));
    fieldValues.add(new FiedValue("name", (byte)4, member.getName()));
    fieldValues.add(new FiedValue("serial", (byte)3, member.getSerial()));
    fieldValues.add(new FiedValue("inTeam", (byte)1, Integer.valueOf((member.isInTeam() == true) ? 1 : 2)));
    List<FiedValue> deyList = SpringUtils.getRoleService().deyList(member.getColor());
    if (deyList.size() > 0)
      fieldValues.addAll(deyList); 
    return fieldValues;
  }
  
  public boolean isInTeam(Role role) {
    Team team = getTeam(role.getRoleId().intValue());
    if (team == null)
      return false; 
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member.getRoleId().equals(role.getRoleId()))
        return member.isInTeam(); 
    } 
    return false;
  }
  
  public boolean isTeamLeader(Role role) {
    Team team = getTeam(role.getRoleId().intValue());
    if (team == null)
      return false; 
    return team.isLeader(role.getRoleId().intValue());
  }
  
  public void pushMessage(Role role, Message message) {
    if (isTeamLeader(role)) {
      pushMessageToTeam(role, message);
    } else {
      MessagePusher.pushMessage(role, message);
    } 
  }
  
  private void pushMessageToTeam(Role role, Message message) {
    Team team = getTeam(role.getRoleId().intValue());
    pushMessageToTeam(team, message);
  }
  
  public void pushMessageToTeam(Team team, Message message) {
    if (team == null || message == null)
      return; 
    List<Member> members = team.getList();
    for (int i = 0; i < members.size(); i++) {
      Member member = members.get(i);
      if (member != null)
        if (member.isInTeam()) {
          Role membeRole = this.roleService.getPlayer(member.getId().longValue());
          MessagePusher.pushMessage(membeRole, message);
        }  
    } 
  }
  
  public int getTeamCount(Role role) {
    Team team = getTeam(role.getRoleId().intValue());
    return getTeamCount(team);
  }
  
  public Team getTeamByTeamId(int teamId) {
    return (Team)SessionCache.TEAMS.get(Integer.valueOf(teamId));
  }
  
  public Team getTeam(int id) {
    int teamId = getTeamId(id);
    return (Team)SessionCache.TEAMS.get(Integer.valueOf(teamId));
  }
  
  public int getTeamId(int id) {
    Integer teamId = (Integer)SessionCache.ROLE_TEAM.get(Integer.valueOf(id));
    if (teamId == null)
      return 0; 
    if (teamId.intValue() <= 0)
      return 0; 
    return teamId.intValue();
  }
  
  public void handleRoleExpire(Role role) {
    if (role == null)
      return; 
    Team team = getTeam(role.getRoleId().intValue());
    if (team != null)
      leave(role, false); 
  }
  
  public List<Integer> getMemberLevel(Role role) {
    List<Integer> list = new ArrayList<>();
    if (isInTeam(role)) {
      try {
        Team team = getTeam(role.getRoleId().intValue());
        for (int i = 0; i < team.getList().size(); i++) {
          Member member = team.getList().get(i);
          if (member != null)
            if (member.isInTeam()) {
              Role player = this.roleService.getPlayer(member.getId().longValue());
              if (player != null)
                list.add(Integer.valueOf(player.getRelive().intValue() * 1000 + player.getLevel().intValue())); 
            }  
        } 
      } catch (Exception e) {
        list.add(Integer.valueOf(role.getRelive().intValue() * 1000 + role.getLevel().intValue()));
        e.getStackTrace();
      } 
    } else {
      list.add(Integer.valueOf(role.getRelive().intValue() * 1000 + role.getLevel().intValue()));
    } 
    return list;
  }
  
  public List<Integer> getMemberGrade(Role role) {
    List<Integer> list = new ArrayList<>();
    if (isInTeam(role)) {
      Team team = getTeam(role.getRoleId().intValue());
      for (int i = 0; i < team.getList().size(); i++) {
        Member member = team.getList().get(i);
        if (member != null)
          if (member.isInTeam()) {
            Role player = this.roleService.getPlayer(member.getId().longValue());
            if (player != null)
              list.add(Integer.valueOf(player.getRelive().intValue() * 1000 + player.getLevel().intValue())); 
          }  
      } 
    } else {
      list.add(Integer.valueOf(role.getRelive().intValue() * 1000 + role.getLevel().intValue()));
    } 
    return list;
  }
  
  public String checkMember(Role role, Predicate<Role> predicate) {
    Team team = getTeam(role.getRoleId().intValue());
    if (team == null)
      return null; 
    return checkMember(team, predicate);
  }
  
  public String checkMember(Team team, Predicate<Role> predicate) {
    int count = 0;
    StringBuilder stringBuilder = null;
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member != null)
        if (member.isInTeam()) {
          Role memberRole = this.roleService.getPlayer(member.getId().longValue());
          if (memberRole != null)
            if (predicate.test(memberRole)) {
              if (stringBuilder == null)
                stringBuilder = new StringBuilder(); 
              if (stringBuilder.length() > 0)
                stringBuilder.append("、"); 
              stringBuilder.append(memberRole.getName());
              count++;
              if (count >= 3) {
                stringBuilder.append("...");
                break;
              } 
            }  
        }  
    } 
    return (stringBuilder != null) ? stringBuilder.toString() : null;
  }
  
  public void leave(Role role, boolean isKickOut) {
    leave(new RoleView(role), isKickOut);
  }
  
  public void leave(RoleView roleView, boolean isKickOut) {
    if (roleView == null)
      return; 
    Team team = getTeam(roleView.getRoleId());
    if (team == null)
      return; 
    Role role = roleView.getRole();
    if (!isInLeaveTeam(role))
      return; 
    Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId().intValue());
    if (fight != null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("战斗结束后会自动离开队伍"));
      team.getFightLeaveNutMap().setv(role.getId().toString(), Byte.valueOf((byte)1));
      return;
    } 
    boolean boo = quiltTeam(team, roleView.getRoleId());
    if (boo) {
      if (role != null) {
        RespUpdateTeamList respUpdateTeamList = new RespUpdateTeamList();
        respUpdateTeamList.setList(new ArrayList());
        MessagePusher.pushMessage(role, (Message)respUpdateTeamList);
        RespMsg respServer_onlineMsg = new RespMsg();
        respServer_onlineMsg.setMsg("队伍解散了。");
        MessagePusher.pushMessage(role, (Message)respServer_onlineMsg);
        this.broadcastService.sendTitleInfo(role);
        this.broadcastService.sendUpdateAppear(role);
      } 
    } else {
      team = getTeamByTeamId(team.getId());
      if (team != null) {
        Role leader = this.roleService.getOnlinePlayer(team.getLeaderUid());
        if (leader == null)
          return; 
        this.broadcastService.sendTitleInfo(leader);
        updateTeamList(leader);
        if (role != null) {
          RespUpdateTeamList respUpdateTeamList = new RespUpdateTeamList();
          respUpdateTeamList.setList(new ArrayList());
          MessagePusher.pushMessage(role, (Message)respUpdateTeamList);
          this.broadcastService.sendTitleInfo(role);
          this.broadcastService.sendUpdateAppear(role);
          if (isKickOut) {
            MessagePusher.pushMessage(leader, (Message)new RespMsg("#<LB1>" + role.getName() + "#被清理开队伍！"));
            MessagePusher.pushMessage(role, (Message)new RespMsg("你被请离了队伍。"));
          } 
        } 
      } 
      sendTeamChangeMsg(team, "#<LB1>" + roleView.getName() + "#离开了队伍。", true);
    } 
    if (role != null && team != null)
      EventDispatcher.getInstance().fireEvent((BaseGameEvent)new TeamLeaveEvent(EventType.TEAM_LEAVE, role, team.isLeader(roleView.getRoleId()), team)); 
  }
  
  public boolean isInLeaveTeam(Role role) {
    if (!((Boolean)role.getTempCache("at_leave", Boolean.valueOf(false))).booleanValue() && 
      !((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).leaveConfirm(role))
      return false; 
    return true;
  }
  
  public int getTeamCount(Team team) {
    if (team == null)
      return 0; 
    int count = 0;
    List<Member> members = team.getList();
    for (int i = 0; i < members.size(); i++) {
      Member member = members.get(i);
      if (member != null)
        if (member.isInTeam())
          count++;  
    } 
    return count;
  }
  
  public List<Integer> getTeamAfkMember(Team team) {
    List<Integer> objects = new ArrayList<>();
    if (team != null)
      for (int i = 0; i < team.getList().size(); i++) {
        Member member = team.getList().get(i);
        if (member != null)
          if (!member.isInTeam())
            objects.add(member.getRoleId());  
      }  
    return objects;
  }
  
  public void refreshRequestInfo(IoSession session, ReqRefreshRequestInfo reqRefreshRequestInfo) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    RespLobbyInfo respLobbyInfo = new RespLobbyInfo();
    MessagePusher.pushMessage(session, (Message)respLobbyInfo);
  }
  
  public void teamTemporary(Role role, int mapId) {
    if (mapId == 3002 || mapId == 3021 || mapId == 3022) {
      Party party = SpringUtils.getPartyService().getParty(role);
      if (party == null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("你还没有帮派哦#<E:51>#"));
        return;
      } 
      if (isInTeam(role)) {
        String msg = "";
        Team team = getTeam(role.getRoleId().intValue());
        for (int i = 0; i < team.getList().size(); i++) {
          Member member = team.getList().get(i);
          if (member.isInTeam()) {
            Role player = this.roleService.getPlayer(member.getRoleId().intValue());
            if (player != null && !player.getFaction().equals(role.getFaction())) {
              msg = msg + player.getName() + "、";
              member.setInTeamTemporary(false);
              MessagePusher.pushMessage(player, (Message)new RespMsg("你#<LB1>" + party.getName() + "#成员，无法跟随队长回帮"));
              leaveTemp(player, false);
            } 
          } 
        } 
        if (msg.length() > 0) {
          msg = msg.substring(0, msg.length() - 1);
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), String.format("#<LB1>%s#不是本帮成员禁止进入", new Object[] { msg }), true));
          return;
        } 
      } 
    } else if (mapId != 3002 || mapId != 3021 || mapId != 3022) {
      if (isInTeam(role)) {
        Team team = getTeam(role.getRoleId().intValue());
        for (int i = 0; i < team.getList().size(); i++) {
          Member member = team.getList().get(i);
          if (!member.isInTeamTemporary()) {
            Role player = this.roleService.getPlayer(member.getRoleId().intValue());
            if (!player.getFaction().equals(role.getFaction())) {
              member.setInTeamTemporary(true);
              returnTeam(player, false);
            } 
          } 
        } 
      } 
    } 
  }
  
  public void leaveTemp(Role role, boolean is) {
    Team team = getTeam(role.getRoleId().intValue());
    if (team == null)
      return; 
    if (!isLeaveTempTeam(role, team))
      return; 
    Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId().intValue());
    if (fight != null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("战斗结束后会自动暂离队伍"));
      team.getFightLeaveNutMap().setv(role.getId().toString(), Byte.valueOf((byte)0));
      return;
    } 
    BroadcastService broadcastService = (BroadcastService)SpringUtils.getBean(BroadcastService.class);
    updateTeam(1, role.getRoleId().intValue());
    updateTeamList(role);
    broadcastService.sendTitleInfoNew(role);
    RespUpdateTeamList respUpdateTeamList = new RespUpdateTeamList();
    List<TeamMember> teamMemberList = getTeamMemberList(team);
    respUpdateTeamList.setList(teamMemberList);
    SceneManager.INSTANCE.sendMessages(role, new Message[] { (Message)respUpdateTeamList });
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new TeamLeaveMomentEvent(EventType.TEAM_LEAVE_MOMENT, role));
    if (is)
      sendTeamChangeMsg(team, "#<LB1>" + role.getName() + "#暂离离开了队伍。", false); 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你暂时离开了当前所在队伍，如果需要归队，请点击组队页面的回归队伍按钮"));
  }
  
  public boolean isLeaveTempTeam(Role role, Team team) {
    if (!((Boolean)role.getTempCache("at_leave_temp", Boolean.valueOf(false))).booleanValue() && 
      !((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).leaveMomentConfirm(role))
      return false; 
    return true;
  }
  
  public void operTeleport(Role role, ReqOperTeleportItem item) {
    Team team = getTeam(role.getRoleId().intValue());
    if (team == null)
      return; 
    if (!team.isLeader(role.getRoleId().intValue()))
      return; 
    RespConfirm RespConfirm = new RespConfirm();
    RespConfirm.setTips("#<LB1>" + role.getName() + "#邀请你回归队伍。");
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member.getRoleId().equals(Integer.valueOf(item.getId())) && !member.isInTeam()) {
        Role teamRole = this.roleService.getPlayer(member.getId().longValue());
        if (teamRole == null)
          break; 
        teamRole.setConfirm((RoleConfirm)new ReturnTeamConfirm());
        MessagePusher.pushMessage(teamRole, (Message)RespConfirm);
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你邀请了#<LB1>" + teamRole.getName() + "#回归队伍"));
        break;
      } 
    } 
  }
  
  public void kickOutTeam(IoSession session, ReqKickOutTeam reqKickOutTeam) {
    try {
      String name = reqKickOutTeam.getName();
      long uid = SpringUtils.getPlayerService().getUidBy(name);
      leave(new RoleView(uid), true);
    } catch (Exception e) {
      Role role = SessionUtils.getRoleBySession(session);
      Team team = getTeam(role.getRoleId().intValue());
      if (team != null)
        for (int i = 0; i < team.getList().size(); i++) {
          Member member = team.getList().get(i);
          if (member.getName().equals(reqKickOutTeam.getName())) {
            team.getList().remove(member);
            break;
          } 
        }  
      updateTeamList(role);
    } 
  }
  
  @EventHandler({EventType.ENTER_MAP})
  public void handleEnterMapEvent(EnterMapEvent enterMapEvent) {
    Role role = enterMapEvent.getRole();
    Team team = getTeam(role.getRoleId().intValue());
    if (team != null && team.isLeader(role.getRoleId().intValue())) {
      MapService mapService = SpringUtils.getMapService();
      for (int i = 0; i < team.getList().size(); i++) {
        Member member = team.getList().get(i);
        if (!member.getId().equals(role.getId()))
          if (member.isInTeam()) {
            Role player = this.roleService.getPlayer(member.getId().longValue());
            if (player != null)
              mapService.changeMap(player, role.getPos().getMapId().intValue(), role.getPos().getX().intValue(), role.getPos().getY().intValue(), "runs_prison"); 
          }  
      } 
    } 
  }
  
  private void fetchMember(Role leader, Role role) {
    if (!leader.getPos().getMapId().equals(role.getPos().getMapId())) {
      SpringUtils.getMapService().changeMap(role, leader.getPos().getMapId().intValue(), leader.getPos().getX().intValue(), leader.getPos().getY().intValue(), "runs_prison");
    } else {
      SceneManager.INSTANCE.goToPosition(role);
    } 
  }
  
  public void reject(IoSession session, ReqReject reqReject) {
    Role role = SessionUtils.getRoleBySession(session);
    switch (reqReject.getType()) {
      case "request_join":
        MessagePusher.pushMessage(this.roleService.getOnlinePlayer(reqReject.getName()), (Message)new RespMsg("对方拒绝了你的申请。"));
        break;
      case "party":
        ((PartyService)SpringUtils.getBean(PartyService.class)).refuseJoinParty(role, reqReject.getName());
        break;
      case "invite_join":
        MessagePusher.pushMessage(this.roleService.getOnlinePlayer(reqReject.getName()), (Message)new RespMsg(role.getName() + "拒绝了你的组队邀请！"));
        break;
    } 
    cleanTeamRequest(role, reqReject.getType(), reqReject.getName());
  }
  
  private void cleanTeamRequest(Role role, String type, String name) {
    RespClearnRequest respClearnRequest = new RespClearnRequest();
    respClearnRequest.setType(type);
    ArrayList<String> names = new ArrayList<>();
    names.add(name);
    respClearnRequest.setNames(names);
    MessagePusher.pushMessage(role, (Message)respClearnRequest);
  }
  
  public void accept(Role role, ReqAccept reqAccept) {
    NutMap sessionData = SessionDataUtils.getData(role);
    Team team = null;
    if ("request_join".equals(reqAccept.getType())) {
      Role newTeamMember = this.roleService.getOnlinePlayer(reqAccept.getName());
      if (newTeamMember == null)
        return; 
      deleteRequest(role.getRoleId() + "request_join", newTeamMember.getRoleId());
      if (!isAcceptJoinTeam(role, newTeamMember.getRoleId().intValue()))
        return; 
      if (this.fightNewService.getFightByRoleId(role.getRoleId().intValue()) != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("当前不能同意申请。"));
        return;
      } 
      if (this.fightNewService.getFightByRoleId(newTeamMember.getRoleId().intValue()) != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方当前无法接受同意申请。"));
        return;
      } 
      team = getTeam(role.getRoleId().intValue());
      if (team == null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("未找到队伍信息。"));
        return;
      } 
      if (!team.isLeader(role.getRoleId().intValue())) {
        RespMsg respMsg = new RespMsg();
        respMsg.setMsg("只有队长才能邀请别的玩家加入队伍。");
        MessagePusher.pushMessage(role, (Message)respMsg);
        return;
      } 
      if (team.getList().size() >= 5) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("队伍人数已经满，无法增加队员。"));
        return;
      } 
      Team tempTeam = getTeam(newTeamMember.getRoleId().intValue());
      if (tempTeam != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方已有队伍，无法邀请其加入队伍。"));
        return;
      } 
      for (int i = 0; i < team.getList().size(); i++) {
        Member member1 = team.getList().get(i);
        if (member1.getRoleId().equals(newTeamMember.getRoleId())) {
          RespMsg respInviteMsg = new RespMsg();
          respInviteMsg.setMsg("对方已有队伍，无法邀请其加入队伍。");
          MessagePusher.pushMessage(role, (Message)respInviteMsg);
          return;
        } 
      } 
      MessagePusher.pushMessage(newTeamMember, (Message)new RespMsg("队长通过您的申请，组队成功"));
      sendTeamChangeMsg(team, "欢迎#<LB1>" + role.getName() + "#加入队伍。", false);
      team.getList().add(getMember(newTeamMember, role));
      saveTeam(team);
      RespClearnAllRequest respClearnAllRequest = new RespClearnAllRequest();
      respClearnAllRequest.setType(reqAccept.getType());
      MessagePusher.pushMessage(newTeamMember, (Message)respClearnAllRequest);
      newMemberJoinTeam(team, role, newTeamMember, reqAccept.getType());
      this.broadcastService.giveAppear(newTeamMember, team);
      EventDispatcher.getInstance().fireEvent((BaseGameEvent)new TeamJoinEvent(EventType.TEAM_JOIN, newTeamMember, "request_join"));
      returnTeam(newTeamMember, false);
    } else if ("invite_join".equals(reqAccept.getType())) {
      int roleId = sessionData.getInt("team:" + reqAccept.getName());
      deleteRequest(roleId + "request_join", role.getRoleId());
      if (!isAcceptJoinTeam(role, roleId))
        return; 
      if (this.fightNewService.getFightByRoleId(role.getRoleId().intValue()) != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("当前不能同意邀请。"));
        return;
      } 
      if (this.fightNewService.getFightByRoleId(roleId) != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方当前无法接受同意邀请。"));
        return;
      } 
      team = getTeam(roleId);
      if (team == null) {
        RespMsg respMsg = new RespMsg();
        respMsg.setMsg("未找到队伍信息。");
        MessagePusher.pushMessage(role, (Message)respMsg);
        return;
      } 
      Role leader = this.roleService.getOnlinePlayer(team.getLeaderUid());
      if (leader == null)
        return; 
      if (team.getList().size() >= 5) {
        RespMsg respMsg = new RespMsg();
        respMsg.setMsg("队伍人数已经满，无法增加队员。");
        MessagePusher.pushMessage(role, (Message)respMsg);
        return;
      } 
      Team roleTeam = getTeam(role.getRoleId().intValue());
      if (roleTeam != null) {
        RespMsg respMsg = new RespMsg();
        respMsg.setMsg("你已经有一个队伍了。");
        MessagePusher.pushMessage(role, (Message)respMsg);
        return;
      } 
      MessagePusher.pushMessage(role, (Message)new RespMsg("您接收了邀请，组队成功"));
      MessagePusher.pushMessage(leader, (Message)new RespMsg("对方已接收你的组队邀请，组队成功"));
      sendTeamChangeMsg(team, "欢迎#<LB1>" + role.getName() + "#加入队伍。", false);
      team.getList().add(getMember(role, leader));
      saveTeam(team);
      RespClearnAllRequest respClearnAllRequest = new RespClearnAllRequest();
      respClearnAllRequest.setType(reqAccept.getType());
      MessagePusher.pushMessage(role, (Message)respClearnAllRequest);
      newMemberJoinTeam(team, leader, role, reqAccept.getType());
      this.broadcastService.giveAppear(this.roleService.getOnlinePlayer(roleId), team);
      EventDispatcher.getInstance().fireEvent((BaseGameEvent)new TeamJoinEvent(EventType.TEAM_JOIN, role, "invite_join"));
      returnTeam(role, false);
    } else if ("party".equals(reqAccept.getType())) {
      ((PartyService)SpringUtils.getBean(PartyService.class)).agreeJoinParty(role, reqAccept.getName());
    } 
  }
  
  public boolean isAcceptJoinTeam(Role role, int roleId) {
    if (!((TowerTaskHandler)SpringUtils.getBean(TowerTaskHandler.class)).isAcceptJoinTeam(role, roleId))
      return false; 
    if (!((LookForTaskHandler)SpringUtils.getBean(LookForTaskHandler.class)).isAcceptJoinTeam(role, roleId))
      return false; 
    if (!((DungeonXmHandler)SpringUtils.getBean(DungeonXmHandler.class)).isAcceptJoinTeam(role, roleId))
      return false; 
    if (!((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).isAcceptJoinTeam(role, roleId))
      return false; 
    if (!((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).isAcceptJoinTeam(role, roleId))
      return false; 
    if (!((CaHandler)SpringUtils.getBean(CaHandler.class)).isAcceptJoinTeam(role, roleId))
      return false; 
    if (!((WaterHandler)SpringUtils.getBean(WaterHandler.class)).isAcceptJoinTeam(role, roleId))
      return false; 
    if (!((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).isAcceptJoinTeam(role, roleId))
      return false; 
    if (role.getPos().getMapId().equals(Integer.valueOf(1201))) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("天牢重地，禁止拉帮结伙！"));
      return false;
    } 
    return true;
  }
  
  @EventHandler({EventType.ROLE_LEVEL_UP})
  public void handleRoleLevelUpEvent(RoleLevelUpEvent roleLevelUpEvent) {
    Role role = roleLevelUpEvent.getRole();
    if (role == null)
      return; 
    Team team = getTeam(role.getRoleId().intValue());
    if (team != null) {
      saveTeam(team);
      updateTeamList(role);
    } 
  }
  
  public void returnTeam(Role role, boolean flag) {
    Team team = getTeam(role.getRoleId().intValue());
    if (team == null)
      return; 
    Role leader = this.roleService.getPlayer(team.getLeaderUid());
    if (leader == null)
      return; 
    int mapId = leader.getPos().getMapId().intValue();
    if (mapId == 3002 || mapId == 3021 || mapId == 3022)
      if (!leader.getFaction().equals(role.getFaction())) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("队长正在#<LB1>帮派#中，非同帮成员无法归队"));
        return;
      }  
    if (!isBackToTeam(team, role))
      return; 
    if (this.fightNewService.getFightByRoleId(role.getRoleId().intValue()) != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("当前不能归队。"));
      return;
    } 
    if (this.fightNewService.getFightByRoleId(leader.getRoleId().intValue()) != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方当前无法接受归队。"));
      return;
    } 
    if (flag) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你回归队伍了"));
      sendTeamChangeMsg(team, "#<LB1>" + role.getName() + "#重新归队。", false);
    } 
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member.getRoleId().equals(role.getRoleId())) {
        member.setInTeam(true);
        break;
      } 
    } 
    saveTeam(team);
    updateTeamList(role);
    this.broadcastService.sendTitleInfo(role);
    this.broadcastService.giveAppear(role, team);
    fetchMember(leader, role);
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new TeamJoinEvent(EventType.TEAM_JOIN, role, "return_join"));
  }
  
  public boolean isBackToTeam(Team team, Role role) {
    if (!((TowerTaskHandler)SpringUtils.getBean(TowerTaskHandler.class)).isBackToTeam(team, role))
      return false; 
    if (!((LookForTaskHandler)SpringUtils.getBean(LookForTaskHandler.class)).isBackToTeam(team, role))
      return false; 
    if (!((DungeonXmHandler)SpringUtils.getBean(DungeonXmHandler.class)).isBackToTeam(team, role))
      return false; 
    if (!((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).isBackToTeam(team, role))
      return false; 
    if (!((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).isBackToTeam(team, role))
      return false; 
    if (!((CaHandler)SpringUtils.getBean(CaHandler.class)).isBackToTeam(team, role))
      return false; 
    if (!((WaterHandler)SpringUtils.getBean(WaterHandler.class)).isBackToTeam(team, role))
      return false; 
    if (!((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).isBackToTeam(team, role))
      return false; 
    if (!((HgFightHandler)SpringUtils.getBean(HgFightHandler.class)).isBackToTeam(team, role))
      return false; 
    if (!((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).isBackToTeam(team, role))
      return false; 
    return true;
  }
  
  private void newMemberJoinTeam(Team team, Role leader, Role role, String opType) {
    cleanTeamRequest(leader, opType, role.getName());
    this.broadcastService.sendTitleInfo(role);
    this.broadcastService.sendTitleInfo(leader);
    this.broadcastService.sendUpdateAppear(role);
    updateTeamList(role);
    sendAppearFormTeam(team);
  }
  
  public void foundJoin(IoSession session, ReqFoundJoin foundJoin) {
    Role role = SessionUtils.getRoleBySession(session);
    if (!isFoundJoinTeam(role))
      return; 
    if (foundJoin.getName().equals(role.getName()))
      createTeam(role, foundJoin); 
  }
  
  public boolean isFoundJoinTeam(Role role) {
    if (role.getPos().getMapId().equals(Integer.valueOf(1201))) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("天牢重地，禁止拉帮结伙！"));
      return false;
    } 
    if (!((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).isFoundJoinTeam(role))
      return false; 
    return true;
  }
  
  public void requestJoin(Role role, ReqRequestJoin requestJoin) {
    if ("request_join".equals(requestJoin.getType())) {
      int toRoleId = requestJoin.getRoleId();
      if (toRoleId <= 0) {
        long toUid = SpringUtils.getPlayerService().getUidBy(requestJoin.getName());
        PlayerProfile playerProfile = SpringUtils.getPlayerService().queryPlayerProfile(toUid);
        toRoleId = (playerProfile != null) ? playerProfile.getRoleId().intValue() : 0;
      } 
      Team team = getTeam(toRoleId);
      if (team == null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("队伍不存在。"));
        return;
      } 
      if (((PkService)SpringUtils.getBean(PkService.class)).isInPrisonTime(role)) {
        MessagePusher.notify2Player(role, 1113, new Object[0]);
        return;
      } 
      if (!isRequestJoinTeam(role, team))
        return; 
      if (team.getList().size() >= 5) {
        RespMsg respInviteMsg = new RespMsg();
        respInviteMsg.setMsg("队伍人数已满。");
        MessagePusher.pushMessage(role, (Message)respInviteMsg);
        return;
      } 
      Team myTeam = getTeam(role.getRoleId().intValue());
      if (myTeam != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("你已经在队伍中。"));
        return;
      } 
      final Role leader = this.roleService.getOnlinePlayer(team.getLeaderUid());
      if (leader == null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("#<LB1>" + requestJoin.getName() + "#不在线！"));
        return;
      } 
      MessagePusher.pushMessage(leader, (Message)new RespMsg(role.getName() + "申请加入你的队伍，请打开队伍界面查看申请信息。"));
      MessagePusher.pushMessage(role, (Message)new RespMsg("你申请加入#<LB1>" + leader.getName() + "#的队伍！"));
      MessagePusher.pushMessage(leader, (Message)loadTeamRequest(team, role, "request_join", String.valueOf(TimeUtil.timeInMillis())));
      cacheRequest(team.getLeaderRoleId() + "request_join", role.getRoleId());
    } else if ("invite_join".equals(requestJoin.getType())) {
      if (role.getLevel().intValue() < 20) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("20级后才能邀请加入队伍。"));
        return;
      } 
      Team team = getTeam(role.getRoleId().intValue());
      if (team == null) {
        if (role.getPos().getMapId().equals(Integer.valueOf(1201))) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("天牢重地，禁止拉帮结伙！"));
          return;
        } 
        ReqFoundJoin foundJoin = new ReqFoundJoin("", "", new int[0], 0, 0, 0, 0, 0);
        team = createTeam(role, foundJoin);
      } 
      if (team.getList().size() >= 5) {
        RespMsg respInviteMsg = new RespMsg();
        respInviteMsg.setMsg("队伍人数已满，无法邀请新成员。");
        MessagePusher.pushMessage(role, (Message)respInviteMsg);
        return;
      } 
      Role toRole = null;
      if (requestJoin.getRoleId() <= 0) {
        toRole = this.roleService.getOnlinePlayer(requestJoin.getName());
      } else {
        toRole = this.roleService.getPlayerBy(requestJoin.getRoleId());
      } 
      if (toRole == null) {
        RespMsg respInviteMsg = new RespMsg();
        respInviteMsg.setMsg("目标玩家不在线。");
        MessagePusher.pushMessage(role, (Message)respInviteMsg);
        return;
      } 
      if (!isInviteJoinTeam(role, toRole.getRoleId().intValue()))
        return; 
      Team toTeam = getTeam(toRole.getRoleId().intValue());
      if (toTeam != null) {
        RespMsg respInviteMsg = new RespMsg();
        respInviteMsg.setMsg("对方已在队伍中。");
        MessagePusher.pushMessage(role, (Message)respInviteMsg);
        return;
      } 
      MessagePusher.pushMessage(role, (Message)new RespMsg("邀请信息已发送"));
      MessagePusher.pushMessage(toRole, (Message)loadTeamRequest(team, role, "invite_join", String.valueOf(TimeUtil.timeInMillis())));
      MessagePusher.pushMessage(toRole, (Message)new RespMsg(role.getName() + "邀请你加入其队伍，请打开队伍界面查看邀请信息。"));
      NutMap sessionData = SessionDataUtils.getData(toRole);
      sessionData.setv("team:" + role.getName(), role.getRoleId());
      cacheRequest(toRole.getRoleId() + "invite_join", Integer.valueOf(team.getLeaderRoleId()));
    } else if ("request_team_leader".equals(requestJoin.getType())) {
      Team team = getTeam(role.getRoleId().intValue());
      if (team == null)
        return; 
      Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
      if (member == null || !member.isInTeam())
        return; 
      final Role leader = this.roleService.getOnlinePlayer(team.getLeaderUid());
      if (leader == null)
        return; 
      RespAroundInfo respAroundInfo = new RespAroundInfo();
      respAroundInfo.setInviteName(role.getName());
      respAroundInfo.setType("request_team_leader");
      MessagePusher.pushMessage(leader, (Message)respAroundInfo);
      MessagePusher.pushMessage(role, (Message)new RespMsg("你的申请已发送。"));
      RespConfirm confirm = new RespConfirm();
      confirm.setTips("#<LB1>" + role.getName() + "#申请成为队长，是否同意？");
      confirm.setConfirmType("reject_count_down");
      confirm.setDownCount(5);
      confirm.setShow_dlg_mode(3);
      MessagePusher.pushMessage(leader, (Message)confirm);
      leader.setConfirm((RoleConfirm)new RequestTeamLeaderConfirm(role.getRoleId().intValue()));
      ((ServerService)SpringUtils.getBean(ServerService.class)).getScheduledExecutorService().schedule(new Runnable() {
            public void run() {
              RequestTeamLeaderConfirm requestTeamLeaderConfirm = (RequestTeamLeaderConfirm)leader.getConfirm();
              if (requestTeamLeaderConfirm.isAgree())
                return; 
              SpringUtils.getTeamService().acceptRequestTeamLeader(leader, requestTeamLeaderConfirm.getRequestId());
            }
          }, 5L, TimeUnit.SECONDS);
    } else if ("party_remote".equals(requestJoin.getType())) {
      ((PartyService)SpringUtils.getBean(PartyService.class)).reqJoinParty(role, requestJoin.getName());
    } else if ("party_one_key".equals(requestJoin.getType())) {
      ((PartyService)SpringUtils.getBean(PartyService.class)).oneKeyJoin(role);
    } 
  }
  
  public boolean isRequestJoinTeam(Role role, Team team) {
    if (!((TowerTaskHandler)SpringUtils.getBean(TowerTaskHandler.class)).isRequestJoinTeam(role, team))
      return false; 
    if (!((LookForTaskHandler)SpringUtils.getBean(LookForTaskHandler.class)).isRequestJoinTeam(role, team))
      return false; 
    if (!((DungeonXmHandler)SpringUtils.getBean(DungeonXmHandler.class)).isRequestJoinTeam(role, team))
      return false; 
    if (!((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).isRequestJoinTeam(role, team))
      return false; 
    if (!((CaHandler)SpringUtils.getBean(CaHandler.class)).isRequestJoinTeam(role, team))
      return false; 
    if (!((WaterHandler)SpringUtils.getBean(WaterHandler.class)).isRequestJoinTeam(role, team))
      return false; 
    if (!((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).isRequestJoinTeam(role))
      return false; 
    if (role.getPos().getMapId().equals(Integer.valueOf(1201))) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("天牢重地，禁止拉帮结伙！"));
      return false;
    } 
    return true;
  }
  
  public boolean isInviteJoinTeam(Role role, int roleId) {
    if (!((TowerTaskHandler)SpringUtils.getBean(TowerTaskHandler.class)).isInviteJoinTeam(role, roleId))
      return false; 
    if (!((LookForTaskHandler)SpringUtils.getBean(LookForTaskHandler.class)).isInviteJoinTeam(role, roleId))
      return false; 
    if (!((DungeonXmHandler)SpringUtils.getBean(DungeonXmHandler.class)).isInviteJoinTeam(role, roleId))
      return false; 
    if (!((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).isInviteJoinTeam(role, roleId))
      return false; 
    if (!((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).isInviteJoinTeam(role, roleId))
      return false; 
    if (!((CaHandler)SpringUtils.getBean(CaHandler.class)).isInviteJoinTeam(role, roleId))
      return false; 
    if (!((WaterHandler)SpringUtils.getBean(WaterHandler.class)).isInviteJoinTeam(role, roleId))
      return false; 
    if (!((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).isInviteJoinTeam(role, roleId))
      return false; 
    if (role.getPos().getMapId().equals(Integer.valueOf(1201))) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("天牢重地，禁止拉帮结伙！"));
      return false;
    } 
    return true;
  }
  
  public RespAroundInfo loadTeamRequest(Team team, Role role, String type, String time) {
    RespAroundInfo respAroundInfo = new RespAroundInfo();
    respAroundInfo.setRoleId(team.getLeaderRoleId());
    respAroundInfo.setInviteName(role.getName());
    respAroundInfo.setTeamMembersCount(team.getList().size());
    respAroundInfo.setSerial(role.getSerial().intValue());
    respAroundInfo.setTime(time);
    respAroundInfo.setType(type);
    ArrayList<FiedValue> arrayList = new ArrayList<>();
    arrayList.add(new FiedValue("level", (byte)2, role.getLevel()));
    arrayList.add(new FiedValue("relive", (byte)1, role.getRelive()));
    arrayList.add(new FiedValue("aim", (byte)4, team.getTarget()));
    arrayList.add(new FiedValue("weapon", (byte)3, Integer.valueOf(role.getWeaponIcon())));
    List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
    if (wearEquips.size() > 0) {
      int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
      arrayList.add(new FiedValue("gem", (byte)1, Integer.valueOf(num)));
    } 
    List<FiedValue> colorList = this.roleService.deyList(role.getColor());
    if (colorList.size() > 0)
      arrayList.addAll(colorList); 
    respAroundInfo.setList(arrayList);
    return respAroundInfo;
  }
  
  public void cacheRequest(String key, Integer value) {
    LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
    TeamRequest teamRequest = (TeamRequest)this.redisService.get(key, TeamRequest.class);
    if (teamRequest != null)
      map = teamRequest.getMap(); 
    teamRequest = new TeamRequest();
    map.put(value, String.valueOf(TimeUtil.timeInMillis()));
    teamRequest.setMap(map);
    this.redisService.set(key, (Serializable)teamRequest, 600L);
  }
  
  public void deleteRequest(String key, Integer value) {
    LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
    TeamRequest teamRequest = (TeamRequest)this.redisService.get(key, TeamRequest.class);
    if (teamRequest != null)
      map = teamRequest.getMap(); 
    teamRequest = new TeamRequest();
    map.remove(value);
    if (map.size() <= 0) {
      this.redisService.del(new Serializable[] { key });
    } else {
      teamRequest.setMap(map);
      Long ttl = this.redisService.ttl(key);
      this.redisService.set(key, (Serializable)teamRequest, ttl.longValue());
    } 
  }
  
  public Team createTeam(Role role, ReqFoundJoin reqFoundJoin) {
    RespClearnAllRequest respClearnAllRequest = new RespClearnAllRequest();
    respClearnAllRequest.setType("invite_join");
    MessagePusher.pushMessage(role, (Message)respClearnAllRequest);
    if (getTeam(role.getRoleId().intValue()) != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你已经在队伍中了"));
      return null;
    } 
    Team team = new Team();
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
    Member member = getMember(role, role);
    List<Member> arrayList = Collections.synchronizedList(new ArrayList<>());
    arrayList.add(member);
    team.setList(arrayList);
    ArrayList<Member> partnerMembers = ((PartnerService)SpringUtils.getBean(PartnerService.class)).partner2Member(role);
    team.setPartner(partnerMembers);
    saveTeam(team);
    Message titleInfo = this.broadcastService.getTitleInfoNew(role);
    MessagePusher.pushMessage(role, titleInfo);
    this.broadcastService.sendTitleInfo(role);
    this.broadcastService.sendUpdateAppear(role);
    updateTeamList(role);
    RespMsg respMsg = new RespMsg();
    respMsg.setMsg("组队成功，您现在已经是队长啦!");
    MessagePusher.pushMessage(role, (Message)respMsg);
    return team;
  }
  
  public boolean quiltTeam(Team team, int id) {
    SessionCache.ROLE_TEAM.remove(Integer.valueOf(id));
    List<Member> list = team.getList();
    Member memberRemove = null;
    for (int i = 0; i < list.size(); i++) {
      Member member1 = list.get(i);
      if (member1.getRoleId().equals(Integer.valueOf(id))) {
        memberRemove = member1;
        break;
      } 
    } 
    if (memberRemove != null)
      list.remove(memberRemove); 
    if (list.size() <= 0) {
      SessionCache.TEAMS.remove(Integer.valueOf(team.getId()));
      return true;
    } 
    Member member = list.get(0);
    if (member != null) {
      member.setInTeam(true);
      Role role = this.roleService.getPlayer(member.getRoleId().intValue());
      if (role != null) {
        ArrayList<Member> partnerMembers = ((PartnerService)SpringUtils.getBean(PartnerService.class)).partner2Member(role);
        team.setPartner(partnerMembers);
        sendAppearForm(team);
      } 
    } 
    return false;
  }
  
  public void acceptRequestTeamLeader(Role leader, int requestId) {
    Role role = SpringUtils.getRoleService().getOnlinePlayer(requestId);
    if (role == null)
      return; 
    changeLeader(leader, requestId);
  }
  
  public void changeLeader(Role leader, int roleId) {
    Team team = getTeam(roleId);
    if (team == null)
      return; 
    if (!team.isLeader(leader.getRoleId().intValue()) || !team.getMember(roleId).isInTeam())
      return; 
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new TeamChangeLeaderEvent(EventType.TEAM_CHANGE_LEADER, leader, team.getList()));
    List<Member> members = team.getList();
    Member toLeader = null;
    int index = 0;
    for (int i = 0; i < members.size(); i++) {
      if (((Member)members.get(i)).getRoleId().equals(Integer.valueOf(roleId))) {
        index = i;
        toLeader = members.get(i);
        break;
      } 
    } 
    Role tmpRole = this.roleService.getPlayerBy(roleId);
    Member oldLeader = members.get(0);
    members.set(index, oldLeader);
    members.set(0, toLeader);
    team.setList(members);
    ArrayList<Member> partnerMembers = ((PartnerService)SpringUtils.getBean(PartnerService.class)).partner2Member(tmpRole);
    team.setPartner(partnerMembers);
    saveTeam(team);
    BroadcastService broadcastService = (BroadcastService)SpringUtils.getBean(BroadcastService.class);
    team = getTeamByTeamId(team.getId());
    Role newLeader = this.roleService.getPlayer(team.getLeaderUid());
    broadcastService.sendTitleInfo(newLeader);
    broadcastService.sendTitleInfo(leader);
    updateTeamList(newLeader);
    MessagePusher.pushMessage(tmpRole, (Message)new RespMsg("你成为了队长。"));
    sendTeamChangeMsg(team, "#<LB1>" + newLeader.getName() + "#成为队长。", false);
  }
  
  public void changeLeader(IoSession session, ReqChangeTeamLeader reqChangeTeamLeader) {
    int roleId = reqChangeTeamLeader.getRoleId();
    Role role = SessionUtils.getRoleBySession(session);
    changeLeader(role, roleId);
  }
  
  public void sendTeamChangeMsg(Team team, String msg, boolean flag) {
    if (team == null)
      return; 
    RespMsg respMsg = new RespMsg(msg);
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (!flag || !team.isLeader(member.getRoleId().intValue()))
        if (member.isInTeam()) {
          Role memberRole = SpringUtils.getRoleService().getOnlinePlayer(member.getId().longValue());
          MessagePusher.pushMessage(memberRole, (Message)respMsg);
        }  
    } 
  }
  
  public static int getId() {
    return atomicInteger.getAndIncrement();
  }
  
  public Role getTeamLeader(int teamId) {
    Team team = getTeamByTeamId(teamId);
    if (team == null)
      return null; 
    Role leader = this.roleService.getPlayer(team.getLeaderUid());
    if (leader == null)
      return null; 
    return leader;
  }
  
  public void saveTeam(Team team) {
    if (team.getId() <= 0)
      team.setId(getId()); 
    SessionCache.TEAMS.put(Integer.valueOf(team.getId()), team);
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      SessionCache.ROLE_TEAM.put(member.getRoleId(), Integer.valueOf(team.getId()));
    } 
  }
  
  private Member getMember(Role role, Role leader) {
    Member member = Member.valueOf(role);
    if (!role.getId().equals(leader.getId())) {
      int mapId = leader.getPos().getMapId().intValue();
      if (mapId == 3002 || mapId == 3021 || mapId == 3022)
        if (!leader.getFaction().equals(role.getFaction()))
          member.setInTeam(false);  
      if (mapId == 3001)
        if (!leader.getFaction().equals(role.getFaction())) {
          member.setInTeam(false);
        } else if (!role.getPos().getMapId().equals(Integer.valueOf(3001))) {
          member.setInTeam(false);
        }  
    } 
    return member;
  }
  
  private void getMember(Role role, Member member) {
    Member.valueOf(role, member);
  }
  
  private void sendJoinSuccess(Team team, Role joinRole) {
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      Role teamRole = SpringUtils.getRoleService().getOnlinePlayer(member.getId().longValue());
      if (teamRole != null)
        if (!teamRole.getRoleId().equals(joinRole.getRoleId()))
          JoinSuccess(teamRole, joinRole);  
    } 
  }
  
  private void sendAppearFormTeam(Team team) {
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      Role memberRole = this.roleService.getOnlinePlayer(member.getId().longValue());
      for (int j = 0; j < team.getList().size(); j++) {
        Member oMember = team.getList().get(j);
        Role appearRole = this.roleService.getOnlinePlayer(oMember.getId().longValue());
        this.broadcastService.updateAppearance(memberRole, appearRole);
      } 
    } 
  }
  
  private void sendAppearForm(Team team) {
    for (int j = 0; j < team.getList().size(); j++) {
      Member member = team.getList().get(j);
      if (!member.isPartner()) {
        Role role = this.roleService.getOnlinePlayer(member.getId().longValue());
        if (role != null)
          this.broadcastService.sendUpdateAppear(role); 
      } 
    } 
  }
  
  public void JoinSuccess(Role role, Role leader) {
    RespFriendAddRole respFriendAddRole = new RespFriendAddRole();
    respFriendAddRole.setName(role.getName());
    respFriendAddRole.setType1(1);
    respFriendAddRole.setType("6");
    ArrayList<FiedValue> arrayList = new ArrayList<>();
    arrayList.add(new FiedValue("id", (byte)3, role.getRoleId()));
    arrayList.add(new FiedValue("name", (byte)4, role.getName()));
    arrayList.add(new FiedValue("icon", (byte)3, Integer.valueOf(role.getIcon())));
    arrayList.add(new FiedValue("race", (byte)3, role.getRace()));
    arrayList.add(new FiedValue("level", (byte)3, role.getLevel()));
    arrayList.add(new FiedValue("relive", (byte)3, role.getRelive()));
    respFriendAddRole.setList(arrayList);
    MessagePusher.pushMessage(leader, (Message)respFriendAddRole);
  }
  
  private void updateTeam(int status, int roleId) {
    Integer integer = (Integer)SessionCache.ROLE_TEAM.get(Integer.valueOf(roleId));
    if (integer == null)
      return; 
    int teamId = integer.intValue();
    if (teamId <= 0)
      return; 
    Team team = (Team)SessionCache.TEAMS.get(Integer.valueOf(teamId));
    if (status == 1) {
      for (int j = 0; j < team.getList().size(); j++) {
        Member member = team.getList().get(j);
        if (member.getRoleId().equals(Integer.valueOf(roleId))) {
          member.setInTeam(false);
          break;
        } 
      } 
      saveTeam(team);
    } 
  }
  
  public boolean isInTeamAndNotLeader(Role role) {
    return (isInTeam(role) && !isTeamLeader(role));
  }
  
  public void checkRoleInfo(IoSession session, ReqCheckPlayer reqCheckPlayer) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Role player = this.roleService.getPlayerBy(reqCheckPlayer.getRoleId());
    if (player != null) {
      RespLookPlayerInfo lookPlayerInfo = new RespLookPlayerInfo();
      lookPlayerInfo.setRoleId(player.getRoleId().intValue());
      lookPlayerInfo.setName(player.getName());
      lookPlayerInfo.setFaction(this.roleService.getFactionName(player));
      lookPlayerInfo.setSerial(player.getSerial().intValue());
      lookPlayerInfo.setLevel(player.getLevel().shortValue());
      lookPlayerInfo.setRei(player.getRelive().byteValue());
      Team team = getTeam(player.getRoleId().intValue());
      if (team != null) {
        lookPlayerInfo.setTeamMembersCount(team.getList().size());
        lookPlayerInfo.setLeaderRoleId(team.getLeaderRoleId());
      } 
      if (role != null) {
        int theTime = (int)(System.currentTimeMillis() / 1000L);
        int incense = role.getPkInfoBox().getIncense();
        if (incense > 0 && incense > theTime)
          lookPlayerInfo.setIsPK((byte)1); 
      } 
      MessagePusher.pushMessage(session, (Message)lookPlayerInfo);
    } else {
      MessagePusher.pushMessage(session, (Message)new RespMsg("目标玩家不在线"));
    } 
  }
  
  public void memberHandleThreadLocal(Team team, Consumer<Role> consumer) {
    Role leaderRole = null;
    for (int j = 0; j < team.getList().size(); j++) {
      Member member = team.getList().get(j);
      if (member != null)
        if (member.isInTeam()) {
          Role memberRole = this.roleService.getPlayer(member.getRoleId().intValue());
          if (memberRole != null)
            if (team.isLeader(memberRole.getRoleId().intValue())) {
              leaderRole = memberRole;
            } else {
              ThreadLocalUtil.addLocalTask(memberRole, () -> consumer.accept(memberRole));
            }  
        }  
    } 
    consumer.accept(leaderRole);
  }
  
  public void memberHandle(Role role, Consumer<Role> consumer) {
    if (role != null && consumer != null)
      if (isInTeam(role) && isTeamLeader(role)) {
        Team team = getTeam(role.getRoleId().intValue());
        memberHandle(team, consumer);
      } else {
        consumer.accept(role);
      }  
  }
  
  public void memberHandleThreadLocal(Role role, Consumer<Role> consumer) {
    if (isInTeam(role) && isTeamLeader(role)) {
      Team team = getTeam(role.getRoleId().intValue());
      memberHandleThreadLocal(team, consumer);
    } else {
      consumer.accept(role);
    } 
  }
  
  public void memberHandle(Team team, Consumer<Role> consumer) {
    try {
      for (int j = 0; j < team.getList().size(); j++) {
        Member member = team.getList().get(j);
        if (member != null)
          if (member.isInTeam()) {
            Role memberRole = this.roleService.getPlayer(member.getRoleId().intValue());
            if (memberRole != null)
              consumer.accept(memberRole); 
          }  
      } 
    } catch (ConcurrentModificationException concurrentModificationException) {}
  }
  
  public String getNotInTeam(Team team) {
    StringBuilder stringBuilder = null;
    for (int j = 0; j < team.getList().size(); j++) {
      Member member = team.getList().get(j);
      if (member != null)
        if (!member.isInTeam()) {
          if (stringBuilder == null)
            stringBuilder = new StringBuilder(); 
          if (stringBuilder.length() > 0)
            stringBuilder.append("、"); 
          stringBuilder.append(member.getName());
        }  
    } 
    return (stringBuilder != null) ? stringBuilder.toString() : null;
  }
  
  public void getTeamList(Role role, String activityType) {
    List<Team> teams = null;
    teams = (List<Team>)SessionCache.TEAMS.values().stream().filter(obj -> (obj.getTarget().equals(activityType) && obj.getList().size() < 5)).collect(Collectors.toList());
    List<SmartTeam> smartTeams = new ArrayList<>();
    List<SmartTeamMember> smartTeamMembers = null;
    for (Team team : teams) {
      smartTeamMembers = new ArrayList<>();
      for (int j = 0; j < team.getList().size(); j++) {
        Member member = team.getList().get(j);
        SmartTeamMember smartTeamMember = new SmartTeamMember(member);
        smartTeamMembers.add(smartTeamMember);
      } 
      smartTeams.add(new SmartTeam(smartTeamMembers));
    } 
    MessagePusher.pushMessage(role, (Message)new RespTeamList(smartTeams));
  }
  
  public void updateTeamTarget(Role role, int roleId, String activityType) {
    Team team = getTeam(roleId);
    if (team == null)
      return; 
    team.setTarget(activityType);
    updateTeamList(role);
  }
  
  public void joinTeams(Role role, String activityType) {
    List<Team> teams = (List<Team>)SessionCache.TEAMS.values().stream().filter(team -> team.getTarget().equals(activityType)).collect(Collectors.toList());
    if (teams == null || teams.size() <= 0)
      return; 
    Team myTeam = getTeam(role.getRoleId().intValue());
    if (myTeam != null) {
      RespMsg respInviteMsg = new RespMsg();
      respInviteMsg.setMsg("你已经在队伍中。");
      MessagePusher.pushMessage(role, (Message)respInviteMsg);
      return;
    } 
    for (Team team : teams) {
      if (!isRequestJoinTeam(role, team))
        return; 
      if (team.getList().size() >= 5) {
        RespMsg respInviteMsg = new RespMsg();
        respInviteMsg.setMsg("队伍人数已满。");
        MessagePusher.pushMessage(role, (Message)respInviteMsg);
        continue;
      } 
      Role leader = this.roleService.getPlayer(team.getLeaderUid());
      MessagePusher.pushMessage(leader, (Message)new RespMsg(role.getName() + "申请加入你的队伍，请打开队伍界面查看申请信息。"));
      MessagePusher.pushMessage(leader, (Message)loadTeamRequest(team, role, "request_join", String.valueOf(TimeUtil.timeInMillis())));
      cacheRequest(team.getLeaderRoleId() + "request_join", role.getRoleId());
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("你申请一键加入队伍！"));
  }
  
  public void getTeamById(Role role, int roleId) {
    Team team = getTeam(roleId);
    if (team == null)
      return; 
    List<SmartTeamMember> list = new ArrayList<>();
    for (int j = 0; j < team.getList().size(); j++) {
      Member member = team.getList().get(j);
      SmartTeamMember smartTeamMember = new SmartTeamMember(member);
      list.add(smartTeamMember);
    } 
    Optional<ActivityType> first = ActivityType.getActivity().stream().filter(obj -> obj.getActivityId().equals(team.getTarget())).findFirst();
    RespSmartTeamMemerList respSmartTeamMemerList = new RespSmartTeamMemerList();
    respSmartTeamMemerList.setTeamMemberList(list);
    if (first.isPresent()) {
      ActivityType activityType = first.get();
      respSmartTeamMemerList.setSerial(activityType.getActivityId());
      respSmartTeamMemerList.setName(activityType.getName());
    } else {
      respSmartTeamMemerList.setName("无目标");
      respSmartTeamMemerList.setSerial("0");
    } 
    MessagePusher.pushMessage(role, (Message)respSmartTeamMemerList);
  }
}
