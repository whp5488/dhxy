package com.kitty.system.game.pk.service;

import com.kitty.common.model.Pos;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.friend.message.vo.SearchFriend;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.pk.message.RespPrisonerInfo;
import com.kitty.system.game.pk.message.RespReleaseInfo;
import com.kitty.system.game.pk.message.RespReleasePrisonerSuccess;
import com.kitty.system.game.pk.message.RespSearchRole;
import com.kitty.system.game.pk.message.RespTraceRole;
import com.kitty.system.game.pk.message.vo.PrisonerInfo;
import com.kitty.system.game.player.bean.PkInfoBox;
import com.kitty.system.game.player.bean.PkRecord;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysMapInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PkService extends ActivityHandler {
  @Autowired
  RoleService roleService;
  
  @Autowired
  EquipService equipService;
  
  @Autowired
  TeamService teamService;
  
  @Autowired
  MapService mapService;
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {
    doFightEnd(fightEndEvent.getRole(), fightEndEvent);
  }
  
  public void handleHeartBeat(Role role, long prevHeartTime) {
    int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    PkInfoBox box = role.getPkInfoBox();
    if (box.getIncense() > 0 && currTime >= box.getIncense()) {
      box.setIncense(0);
      this.roleService.stateRefresh(role);
      role.save();
    } 
    if (role.getPkInfoBox().isRedName()) {
      boolean flag = false;
      if (box.getConceal() > 0 && currTime >= box.getConceal()) {
        flag = true;
        box.setConceal(0);
      } 
      if (box.getRedTime() > 0 && currTime >= box.getRedTime()) {
        flag = true;
        box.setRedTime(0);
      } 
      if (box.getPrisonTime() > 0 && currTime >= box.getPrisonTime()) {
        flag = true;
        removeFromPrison(role, 1011, 124, 67);
      } 
      if (flag) {
        this.roleService.stateRefresh(role);
        role.save();
      } 
    } 
  }
  
  public void doFightEnd(Role role, FightEndEvent fightEndEvent) {
    if (!isMatchFightType(role, fightEndEvent.getFightType()))
      return; 
    switch (fightEndEvent.getStatus()) {
      case 1:
        doFightWin(role, fightEndEvent);
        break;
      case 2:
        doFightFail(role, fightEndEvent);
        break;
      case 3:
        doFightRunAway(role, fightEndEvent);
        doFightFail(role, fightEndEvent);
        break;
      case 40:
        doFightMaxRound(role, fightEndEvent);
        break;
    } 
  }
  
  private boolean isMatchFightType(Role role, int fightType) {
    return (FIGHT_TYPE.FORCE_PK.getValue() == fightType);
  }
  
  public void doFightWin(Role role, FightEndEvent fightEndEvent) {
    if (isASide(role, fightEndEvent.getRolesA()) && !isAllRedName(fightEndEvent.getRolesB())) {
      PkInfoBox box = role.getPkInfoBox();
      SysMapInfo mapInfo = this.mapService.getMap(role.getPos().getMapId().intValue());
      if (role.getLastDead() > 0)
        if (box.getConceal() <= 0 && !box.isRedName() && mapInfo.getIsWanted() != 1) {
          box.setConceal((int)((System.currentTimeMillis() + 60000L) / 1000L));
          MessagePusher.notify2Player(role, 1107, new Object[0]);
          role.save();
        }  
      box.setRedName(true);
      box.setRedTime((int)((System.currentTimeMillis() + 28800000L) / 1000L));
      this.roleService.stateRefresh(role);
      SpringUtils.getBroadcastService().sendTitleInfo(role);
      checkAfterWin(role);
    } 
    if (role.getLastDead() == 0) {
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team != null && !team.isLeader(role.getRoleId().intValue()))
        this.teamService.leaveTemp(role, true); 
      role.setVisible(1201);
      this.roleService.goBackBorn(role, new Pos(Integer.valueOf(107), Integer.valueOf(64), Integer.valueOf(5), Integer.valueOf(1002)));
      long exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf(role.getLevel().intValue() + 1), role.getRelive()) * 0.2D)).longValue();
      this.teamService.memberHandleThreadLocal(role, memberRole -> this.roleService.punishFightDead(memberRole, -exp, "由于你的死亡，你丢失了"));
      return;
    } 
  }
  
  public boolean isASide(Role role, List<Role> rolesA) {
    if (rolesA != null)
      for (Role roleA : rolesA) {
        if (role.getId().equals(roleA.getId()))
          return true; 
      }  
    return false;
  }
  
  private boolean isAllRedName(List<Role> roles) {
    if (roles != null)
      for (Role role : roles) {
        if (!role.getPkInfoBox().isRedName())
          return false; 
      }  
    return true;
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    this.roleService.goBackBorn(role, new Pos(Integer.valueOf(107), Integer.valueOf(64), Integer.valueOf(5), Integer.valueOf(1002)));
    long exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf(role.getLevel().intValue() + 1), role.getRelive()) * 0.2D)).longValue();
    this.roleService.punishFightDead(role, -exp, "由于你的死亡，你丢失了");
    if (!this.teamService.isInTeam(role) || this.teamService.isTeamLeader(role)) {
      Role killRole = null;
      if (isASide(role, fightEndEvent.getRolesA())) {
        killRole = getOperRole(fightEndEvent.getRolesB());
      } else {
        killRole = getOperRole(fightEndEvent.getRolesA());
      } 
      if (killRole != null) {
        MessagePusher.notify2Player(role, 1105, new Object[] { killRole.getName() });
        addKillRole(killRole, role);
        addBeKillRole(role, killRole);
      } 
    } 
  }
  
  private void addKillRole(Role role, Role targetRole) {
    if (role.getPkInfoBox().getKillRoles() == null)
      role.getPkInfoBox().setKillRoles(new HashMap<>()); 
    int now = (int)(System.currentTimeMillis() / 1000L);
    PkRecord pkRecord = new PkRecord(targetRole.getId().longValue(), now);
    role.getPkInfoBox().getKillRoles().put(Long.valueOf(pkRecord.getId()), pkRecord);
  }
  
  private void addBeKillRole(Role role, Role targetRole) {
    if (role.getPkInfoBox().getBeKillRoles() == null)
      role.getPkInfoBox().setBeKillRoles(new HashMap<>()); 
    int now = (int)(System.currentTimeMillis() / 1000L);
    PkRecord pkRecord = new PkRecord(targetRole.getId().longValue(), now);
    role.getPkInfoBox().getBeKillRoles().put(Long.valueOf(pkRecord.getId()), pkRecord);
  }
  
  private Role getOperRole(List<Role> roles) {
    if (roles != null && roles.size() > 0) {
      if (roles.size() == 1)
        return roles.get(0); 
      for (Role role : roles) {
        if (this.teamService.isTeamLeader(role))
          return role; 
      } 
    } 
    return null;
  }
  
  public void doFightRunAway(Role role, FightEndEvent fightEndEvent) {}
  
  public void doFightMaxRound(Role role, FightEndEvent fightEndEvent) {
    if (isASide(role, fightEndEvent.getRolesA())) {
      long exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf(role.getLevel().intValue() + 1), role.getRelive()) * 0.2D)).longValue();
      this.roleService.punishFightDead(role, -exp, "由于你的死亡，你丢失了");
    } 
  }
  
  public boolean isInPrisonTime(Role role) {
    return (role.getPkInfoBox().getPrisonTime() > 0);
  }
  
  public void checkEnterMap(Role role) {
    this.teamService.memberHandle(role, memberRole -> {
          if (isNeedInPrison(memberRole))
            add2Prison(memberRole); 
        });
  }
  
  public void checkAfterWin(Role role) {
    this.teamService.memberHandle(role, memberRole -> {
          if (isNeedInPrison(memberRole))
            add2Prison(memberRole); 
        });
  }
  
  private boolean isNeedInPrison(Role role) {
    if (role.getVisible() == 1201) {
      role.setVisible(1);
      return false;
    } 
    if (DataGlobalFunc.isInMap(role, 1201))
      return false; 
    if (role.getPkInfoBox().isRedName()) {
      int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
      SysMapInfo mapInfo = this.mapService.getMap(role.getPos().getMapId().intValue());
      if (mapInfo.getIsWanted() == 1 && role.getPkInfoBox().getRedTime() > currTime)
        return true; 
    } 
    return false;
  }
  
  public void add2Prison(Role role) {
    if (this.teamService.isInTeam(role))
      this.teamService.leave(role, false); 
    PkInfoBox pkInfoBox = role.getPkInfoBox();
    int x = 62, y = 5, time = 60;
    ReqTelePort reqTelePort = new ReqTelePort();
    reqTelePort.setMapId(1201);
    int size = pkInfoBox.getKillRoles().size();
    if (size > 3 && size < 6) {
      x = 27;
      y = 34;
      time = 90;
      int round = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(10));
      if (round < 5) {
        x = 10;
        y = 43;
      } 
    } else if (size > 6) {
      x = 63;
      y = 57;
      time = 120;
    } 
    pkInfoBox.setConceal(0);
    pkInfoBox.setRedTime(0);
    reqTelePort.setX(x);
    reqTelePort.setY(y);
    this.mapService.changeMap(role, reqTelePort, null);
    pkInfoBox.setPrisonTime((int)((System.currentTimeMillis() + time * 60000L) / 1000L));
    this.roleService.stateRefresh(role);
    role.save();
  }
  
  public void removeFromPrison(Role role, int map, int x, int y) {
    role.getPkInfoBox().setRedName(false);
    role.getPkInfoBox().setRedTime(0);
    role.getPkInfoBox().setPrisonTime(0);
    this.mapService.changeMap(role, new ReqTelePort(map, x, y), "runs_prison");
  }
  
  public void loginCheckFromPrison(Role role) {
    Pos pos = role.getPos();
    if (pos.getMapId().equals(Integer.valueOf(1201))) {
      int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
      PkInfoBox box = role.getPkInfoBox();
      if (box.getPrisonTime() < currTime) {
        box.setRedName(false);
        box.setRedTime(0);
        role.getPkInfoBox().setPrisonTime(0);
        pos.setX(Integer.valueOf(44));
        pos.setY(Integer.valueOf(19));
      } 
    } 
  }
  
  public void npcButton(Role role, String msg, Npc npc) {
    if (((PkService)SpringUtils.getBean(PkService.class)).isInPrisonTime(role)) {
      MessagePusher.notify2Player(role, 1113, new Object[0]);
      return;
    } 
    ReqTelePort reqTelePort = new ReqTelePort();
    reqTelePort.setMapId(1011);
    reqTelePort.setX(124);
    reqTelePort.setY(67);
    SpringUtils.getMapService().changeMap(role, reqTelePort, msg);
  }
  
  public void searchRole(IoSession session, String name) {
    if (name.length() <= 0) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("你要找的人目前不在大话的世界中#<E:17>#"));
      return;
    } 
    List<Role> collect = (List<Role>)DataCache.ONLINE_ROLES.values().stream().filter(e -> (e.getName().contains(name) || e.getRoleId().toString().equals(name))).collect(Collectors.toList());
    if (collect.size() <= 0) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("你要找的人目前不在大话的世界中#<E:17>#"));
      return;
    } 
    List<SearchFriend> objects = new ArrayList<>();
    for (Role role : collect)
      objects.add(new SearchFriend(role.getRoleId().intValue(), role.getName(), role.getSerial().intValue(), role.getRelive().byteValue(), role.getLevel().shortValue())); 
    RespSearchRole searchRole = new RespSearchRole();
    searchRole.setList(objects);
    MessagePusher.pushMessage(session, (Message)searchRole);
  }
  
  public void traceRole(IoSession session, int roleId) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    if (!this.roleService.isOnline(roleId)) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("你要找的人目前刚刚离开了大话的世界#<E:17>#"));
      return;
    } 
    Role player = this.roleService.getOnlinePlayer(roleId);
    if (player == null) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("你要找的人目前刚刚离开了大话的世界#<E:17>#"));
      return;
    } 
    Pos pos = player.getPos();
    if (pos.getMapId().equals(Integer.valueOf(3002)) || pos.getMapId().equals(Integer.valueOf(3002)) || pos.getMapId().equals(Integer.valueOf(3002))) {
      MessagePusher.pushMessage(session, (Message)new RespMsg(player.getName() + "在帮派，该场景不可被追踪"));
      return;
    } 
    if (pos.getMapId().equals(Integer.valueOf(3003))) {
      MessagePusher.pushMessage(session, (Message)new RespMsg(player.getName() + "在水陆大会，该场景不可被追踪"));
      return;
    } 
    if (pos.getMapId().equals(Integer.valueOf(4154))) {
      MessagePusher.pushMessage(session, (Message)new RespMsg(player.getName() + "在英雄争霸赛，该场景不可被追踪"));
      return;
    } 
    if (pos.getMapId().equals(Integer.valueOf(3001))) {
      MessagePusher.pushMessage(session, (Message)new RespMsg(player.getName() + "在帮派大战，该场景不可被追踪"));
      return;
    } 
    if (pos.getMapId().equals(Integer.valueOf(1201))) {
      MessagePusher.pushMessage(session, (Message)new RespMsg(player.getName() + "在天牢，该场景不可被追踪"));
      return;
    } 
    boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 5000L, "千里追踪");
    if (!currency)
      return; 
    role.pushTempCache("trace", pos.getMapId() + "-" + pos.getX() + "-" + pos.getY());
    RespTraceRole traceRole = new RespTraceRole();
    traceRole.setRoleId(player.getRoleId().intValue());
    traceRole.setSerial(player.getSerial().intValue());
    traceRole.setName(player.getName());
    traceRole.setRelive(player.getRelive().byteValue());
    traceRole.setLevel(player.getLevel().shortValue());
    traceRole.setWeapon(player.getWeaponIcon());
    traceRole.setLightEffect(this.equipService.getWearEffect(role));
    List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
    if (wearEquips.size() > 0) {
      int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
      traceRole.setGem(num);
    } 
    traceRole.setList(this.roleService.deyList(role.getColor()));
    traceRole.setMap(pos.getMapId().shortValue());
    traceRole.setMap(pos.getX().shortValue());
    traceRole.setMap(pos.getY().shortValue());
    MessagePusher.pushMessage(session, (Message)traceRole);
  }
  
  public void traceGo(IoSession session) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Object trace = role.getTempCache("trace", null);
    if (trace == null)
      return; 
    String[] split = trace.toString().split("-");
    if (split.length < 3)
      return; 
    ReqTelePort reqTelePort = new ReqTelePort(Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue(), Integer.valueOf(split[2]).intValue());
    this.mapService.changeMap(role, reqTelePort, null);
  }
  
  public void viewPrisoner(Role role, Npc npc) {
    RespPrisonerInfo respPrisonerInfo = new RespPrisonerInfo();
    List<Role> prisonRoles = getPrisonRoles();
    for (Role prisonRole : prisonRoles)
      respPrisonerInfo.getList().add(new PrisonerInfo(prisonRole)); 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)respPrisonerInfo);
  }
  
  public List<Role> getPrisonRoles() {
    List<Role> prisonRoles = new ArrayList<>();
    DataCache.ONLINE_ROLES.values().forEach(role -> {
          if (role.getPkInfoBox().getPrisonTime() > 0)
            prisonRoles.add(role); 
        });
    return prisonRoles;
  }
  
  public void releaseInfo(Role role, int id) {
    Role targetRole = this.roleService.getOnlinePlayer(id);
    if (targetRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("该玩家不在线，无法保释"));
      return;
    } 
    if (!isInPrisonTime(targetRole)) {
      MessagePusher.notify2Player(role, 1119, new Object[] { targetRole.getName() });
      return;
    } 
    RespReleaseInfo releaseInfo = new RespReleaseInfo();
    releaseInfo.setRoleId(targetRole.getRoleId().intValue());
    releaseInfo.setSerial(targetRole.getSerial().intValue());
    releaseInfo.setName(targetRole.getName());
    releaseInfo.setRelive(targetRole.getRelive().byteValue());
    releaseInfo.setLevel(targetRole.getLevel().shortValue());
    releaseInfo.setWeapon(targetRole.getWeaponIcon());
    releaseInfo.setLightEffect(this.equipService.getWearEffect(targetRole));
    List<WearEquip> wearEquips = this.equipService.getEquipValid(targetRole, false);
    if (wearEquips.size() > 0) {
      int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
      releaseInfo.setGem(num);
    } 
    releaseInfo.setList(this.roleService.deyList(targetRole.getColor()));
    releaseInfo.setBail(String.valueOf(10000L));
    MessagePusher.pushMessage(role, (Message)releaseInfo);
  }
  
  public void confirmReleasePrisoner(Role role, int id) {
    Role targetRole = this.roleService.getOnlinePlayer(id);
    if (targetRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("该玩家不在线，无法保释"));
      return;
    } 
    if (!isInPrisonTime(targetRole)) {
      MessagePusher.notify2Player(role, 1119, new Object[] { targetRole.getName() });
      return;
    } 
    if (!this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, 10000L, "监狱保释"))
      return; 
    removeFromPrison(targetRole, 1201, 47, 24);
    this.roleService.stateRefresh(targetRole);
    targetRole.save();
    MessagePusher.pushMessage(role, (Message)new RespReleasePrisonerSuccess(targetRole.getRoleId().intValue()));
  }
}
