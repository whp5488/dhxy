package com.kitty.system.game.activity.handler.timing.wars;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.system.game.activity.message.RespGangWarsGateLife;
import com.kitty.system.game.activity.message.RespGangWarsRoleState;
import com.kitty.system.game.activity.message.RespNearby;
import com.kitty.system.game.activity.message.RespOffRoleState;
import com.kitty.system.game.activity.message.vo.Nearby;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.model.user.wars.GangWarsData;
import com.kitty.system.game.activity.model.user.wars.GangWarsPkData;
import com.kitty.system.game.activity.model.user.wars.vo.Equipment;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsInfo;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.Npc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GangWarsPkHandler extends ActivityHandler {
  @Autowired
  GangWarsHandler gangWarsHandler;
  
  @Autowired
  protected MapService mapService;
  
  @Autowired
  protected TeamService teamService;
  
  @Autowired
  protected FightNewService fightNewService;
  
  @Autowired
  PartyService partyService;
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  NpcService npcService;
  
  @Autowired
  ServerService serverService;
  
  private static final int MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_GANG_WARS.getValue();
  
  private static final String ATK_GATE_TIPS = "本队对敌方#<O>城门#造成了#<G>30点伤害##<E:50>#";
  
  private static final String EQUIPMENT_GATE_TIPS = "我方#<R>%s#的队伍正在进攻城门";
  
  private static final String EQUIPMENT_CANNON_TIPS = "我帮#<R>%s#正在准备发射";
  
  private static final String ACTIVATE_CANNON = "燃烧吧神龙大炮/activate_cannon|我只是参观的/离开#<E:17>#";
  
  private static final String BREAK_OFF_CANNON = "是/break_off_cannon|否/离开";
  
  private static final String EQUIPMENT_ATK_GATE_TIPS = "为了我帮，开打/atk_cannon|还是先干点别的/离开#<E:17>#";
  
  private static final String IS_EQUIPMENT_GATE_TIPS = "停止攻击/off_cannon|根本停不下来/离开#<E:17>#";
  
  private static final String IS_EQUIPMENT_GATE_BREAK_OFF_TIPS = "是/break_off_gate|否/离开";
  
  private static final String GATE_BEAR_ATK_TIPS = "%s#<EP>启动龙神大炮击中#%s#<EP>总部，总部耐久度降低#%d点";
  
  private static final String REQUEST_FIGHT_NOT = "人数不足#<R>3#人，不能发起攻击";
  
  private static final String REQUEST_CANNON_FIGHT_NOT = "人数不足#<R>3#人，不能操作";
  
  protected int getFightType() {
    return FIGHT_TYPE.GANG_WARS.getValue();
  }
  
  private boolean isMatchFightType(int fightType) {
    return (getFightType() == fightType);
  }
  
  public void start() {
    this.logger.error("帮派大战对决开始");
    try {
      GangWarsData objData = this.gangWarsHandler.getObjData();
      objData.setStage((byte)1);
      for (Integer key : objData.getWarsMap().keySet()) {
        GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(key);
        Party party = this.partyService.getParty(key);
        Party enemy = this.partyService.getParty(Integer.valueOf(warsInfo.getEnemy()));
        if (party != null && enemy != null)
          this.partyService.npcPartyChat(party, I18nIdDataPool.getI18nContent(370, new Object[] { this.gangWarsHandler.getBye(warsInfo.getGrade()), enemy.getName() })); 
      } 
      for (Integer serial : objData.getPkData().keySet()) {
        Map<Long, GangWarsPkData> dataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(serial);
        GangWarsInfo sideInfo = (GangWarsInfo)objData.getWarsMap().get(serial);
        if (sideInfo.getCombatResults() != 3)
          continue; 
        Map<Long, GangWarsPkData> enemyDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(Integer.valueOf(sideInfo.getEnemy()));
        if (enemyDataMap.size() <= 0) {
          GangWarsInfo enemyInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(sideInfo.getEnemy()));
          sideInfo.setCombatResults((byte)1);
          enemyInfo.setCombatResults((byte)0);
          this.gangWarsHandler.end(serial.intValue(), sideInfo, enemyInfo);
          continue;
        } 
        for (Long id : dataMap.keySet()) {
          GangWarsPkData warsPkData = dataMap.get(id);
          if (warsPkData.getPosition() != 3)
            this.gangWarsHandler.sendRespIcon(id.longValue(), serial.intValue()); 
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("帮战PK阶段开始错误：{}", e.getMessage());
    } 
  }
  
  public void reqOperateEquipment(Role role, int type, String string) {
    if (this.teamService.getTeamCount(role) < 3) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("人数不足#<R>3#人，不能操作"));
      return;
    } 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    GangWarsData objData = this.gangWarsHandler.getObjData();
    if (objData == null)
      return; 
    GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
    if (warsInfo == null || warsInfo.getCombatResults() != 3)
      return; 
    if (type == 0) {
      Equipment equipment = (Equipment)objData.getCannonMap().get(Integer.valueOf(warsInfo.getVisible()));
      if (equipment == null) {
        Npc npc1 = this.mapService.getNpc(90996);
        MessagePusher.pushMessage(role, (Message)this.npcService.getRespNpcContent(role, npc1, "燃烧吧神龙大炮/activate_cannon|我只是参观的/离开#<E:17>#", String.format(npc1.getTalk(), new Object[] { Short.valueOf((short)180) })));
        return;
      } 
      if (equipment.getStance() == warsInfo.getStance()) {
        MessagePusher.pushMessage(role, (Message)new RespMsg(String.format("我帮#<R>%s#正在准备发射", new Object[] { equipment.getName() })));
        return;
      } 
      Npc npc = this.mapService.getNpc(90996);
      MessagePusher.pushMessage(role, (Message)this.npcService.getRespNpcContent(role, npc, "是/break_off_cannon|否/离开", "对方正在操作神龙大炮，要阻止吗？"));
      return;
    } 
    if (type == 1) {
      byte stance = 0;
      if (NumberUtil.isNumber(string)) {
        stance = Byte.valueOf(string).byteValue();
      } else {
        return;
      } 
      if (stance < 0 || stance > 1) {
        this.logger.error("玩家：{}，攻击城门立场错误{}", role.getRoleId(), string);
        return;
      } 
      if (warsInfo.getStance() == stance) {
        Party sideParty = this.partyService.getParty(role.getFaction());
        Equipment equipment1 = (Equipment)objData.getGateMap().get(Integer.valueOf(warsInfo.getEnemy()));
        if (equipment1 != null) {
          Npc npc1 = this.mapService.getNpc(90995);
          npc1.setName(sideParty.getLevel() + "级城门");
          MessagePusher.pushMessage(role, (Message)this.npcService.getRespNpcContent(role, npc1, "是/break_off_gate|否/离开", "对方正在进攻城门，要阻止吗？"));
        } 
        return;
      } 
      Party enemyParty = this.partyService.getParty(Integer.valueOf(warsInfo.getEnemy()));
      Equipment equipment = (Equipment)objData.getGateMap().get(role.getFaction());
      if (equipment != null) {
        if (team.getId() == equipment.getTeam()) {
          Npc npc1 = this.mapService.getNpc(90995);
          npc1.setName(enemyParty.getLevel() + "级城门");
          MessagePusher.pushMessage(role, (Message)this.npcService.getRespNpcContent(role, npc1, "停止攻击/off_cannon|根本停不下来/离开#<E:17>#", "我队正在向对方城门发起攻击"));
        } else {
          MessagePusher.pushMessage(role, (Message)new RespMsg(String.format("我方#<R>%s#的队伍正在进攻城门", new Object[] { equipment.getName() })));
        } 
        return;
      } 
      Npc npc = this.mapService.getNpc(90995);
      npc.setName(enemyParty.getLevel() + "级城门");
      GangWarsInfo enemyInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(warsInfo.getEnemy()));
      MessagePusher.pushMessage(role, (Message)this.npcService.getRespNpcContent(role, npc, "为了我帮，开打/atk_cannon|还是先干点别的/离开#<E:17>#", "这是敌方城门，耐久剩余#<R>" + enemyInfo.getGateLife() + "#"));
      return;
    } 
  }
  
  public void operateCannon(final Role role, String type) {
    if (this.teamService.getTeamCount(role) < 3)
      return; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team.getLeaderUid() != role.getId().longValue()) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("老老实实听从队长指挥吧"));
      return;
    } 
    GangWarsData objData = this.gangWarsHandler.getObjData();
    if (objData == null)
      return; 
    GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
    if (warsInfo == null || warsInfo.getCombatResults() != 3)
      return; 
    Equipment equipment = (Equipment)objData.getCannonMap().get(Integer.valueOf(warsInfo.getVisible()));
    if ("activate_cannon".equals(type)) {
      if (equipment == null) {
        int time = (new Long(System.currentTimeMillis() / 1000L)).intValue() + 20;
        ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(new Runnable() {
              public void run() {
                GangWarsPkHandler.this.cannonTimer(role.getFaction().intValue());
              }
            },  20000L);
        RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)2, time);
        List<Long> members = operateMembers(team, (Map<Long, GangWarsPkData>)objData.getPkData().get(role.getFaction()), respGangWarsRoleState, (byte)2);
        objData.getCannonMap().put(Integer.valueOf(warsInfo.getVisible()), new Equipment(schedule, warsInfo.getStance(), team.getId(), role.getName(), members, time));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("大炮引线已被其他玩家点燃"));
      } 
    } else if ("break_off_cannon".equals(type)) {
      if (equipment == null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("神龙大炮引线已熄灭"));
        return;
      } 
      Map<Long, GangWarsPkData> enemyWarsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(Integer.valueOf(warsInfo.getEnemy()));
      if (enemyWarsPkDataMap == null)
        return; 
      Map<Long, GangWarsPkData> sideWarsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(role.getFaction());
      if (sideWarsPkDataMap == null)
        return; 
      cancelTask(equipment.getScheduledFuture());
      objData.getCannonMap().remove(Integer.valueOf(warsInfo.getVisible()));
      abortOperate(role, equipment, team, sideWarsPkDataMap, enemyWarsPkDataMap, (byte)2, "敌方阻止，操作神龙大炮失败");
    } 
  }
  
  public void cannonTimer(int faction) {
    GangWarsData objData = this.gangWarsHandler.getObjData();
    if (objData == null)
      return; 
    List<Long> objects = new ArrayList<>();
    GangWarsInfo sideInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(faction));
    if (sideInfo == null || sideInfo.getCombatResults() != 3)
      return; 
    Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(Integer.valueOf(faction));
    StringBuilder stringBuilder = new StringBuilder();
    Equipment equipment = (Equipment)objData.getCannonMap().get(Integer.valueOf(sideInfo.getVisible()));
    for (Long id : equipment.getList()) {
      GangWarsPkData gangWarsPkData = warsPkDataMap.get(id);
      if (gangWarsPkData != null && gangWarsPkData.getPosition() != 3) {
        objects.add(id);
        gangWarsPkData.setState((byte)0);
      } 
      Role player = this.roleService.getPlayer(id.longValue());
      if (player != null) {
        if (stringBuilder.length() > 0)
          stringBuilder.append("、"); 
        stringBuilder.append(player.getName());
      } 
    } 
    objData.getCannonMap().remove(Integer.valueOf(sideInfo.getVisible()));
    GangWarsInfo enemyInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(sideInfo.getEnemy()));
    int gateLife = enemyInfo.getGateLife() - 180;
    enemyInfo.setGateLife((short)((gateLife < 0) ? 0 : gateLife));
    int count = objData.getRecordMap().getInt(faction + "_cannon", 0);
    objData.getRecordMap().setv(faction + "_cannon", Integer.valueOf(count + 1));
    updateGateLife(faction, sideInfo, enemyInfo, objects, (byte)2);
    Party enemyParty = this.partyService.getParty(Integer.valueOf(sideInfo.getEnemy()));
    if (enemyParty != null && stringBuilder.length() > 0) {
      this.partyService.npcPartyChat(enemyParty, String.format("%s#<EP>启动龙神大炮击中#%s#<EP>总部，总部耐久度降低#%d点", new Object[] { stringBuilder.toString(), "我方", Short.valueOf((short)180) }));
      Party sideParty = this.partyService.getParty(Integer.valueOf(faction));
      if (sideParty != null)
        this.partyService.npcPartyChat(sideParty, String.format("%s#<EP>启动龙神大炮击中#%s#<EP>总部，总部耐久度降低#%d点", new Object[] { stringBuilder.toString(), enemyParty.getName(), Short.valueOf((short)180) })); 
    } 
  }
  
  public void operateGate(Role role, String type) {
    GangWarsData objData = this.gangWarsHandler.getObjData();
    if (objData == null)
      return; 
    GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
    if (warsInfo == null || warsInfo.getCombatResults() != 3)
      return; 
    if ("atk_cannon".equals(type)) {
      atkGate(role, objData, warsInfo);
    } else if ("off_cannon".equals(type)) {
      offGate(role, objData, "队长停止了攻击敌方城门");
    } else if ("break_off_gate".equals(type)) {
      breakOffGate(role, objData, warsInfo);
    } 
  }
  
  public void atkGate(Role role, GangWarsData objData, GangWarsInfo warsInfo) {
    Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(role.getFaction());
    if (warsPkDataMap == null)
      return; 
    if (this.teamService.getTeamCount(role) < 3)
      return; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    Equipment equipment = (Equipment)objData.getGateMap().get(role.getFaction());
    if (equipment == null) {
      ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(() -> gateTimer(role.getFaction().intValue()), 10000L, 10000L);
      RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)3, 0);
      List<Long> members = operateMembers(team, warsPkDataMap, respGangWarsRoleState, (byte)3);
      objData.getGateMap().put(role.getFaction(), new Equipment(schedule, warsInfo.getStance(), team.getId(), role.getName(), members, 0));
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg(String.format("我方#<R>%s#的队伍正在进攻城门", new Object[] { equipment.getName() })));
    } 
  }
  
  public void offGate(Role role, GangWarsData objData, String tips) {
    Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(role.getFaction());
    if (warsPkDataMap == null)
      return; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team.getLeaderUid() != role.getId().longValue()) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("老老实实听从队长指挥吧"));
      return;
    } 
    Equipment equipment = (Equipment)objData.getGateMap().get(role.getFaction());
    if (equipment == null)
      return; 
    if (team.getId() != equipment.getTeam())
      return; 
    cancelTask(equipment.getScheduledFuture());
    objData.getGateMap().remove(role.getFaction());
    RespOffRoleState offRoleState = new RespOffRoleState("wars", (byte)3);
    RespNotifyMiscEx miscEx = new RespNotifyMiscEx(tips);
    ChatService chatService = SpringUtils.getChatService();
    for (Long id : equipment.getList()) {
      GangWarsPkData warsPkData = warsPkDataMap.get(id);
      if (warsPkData != null)
        warsPkData.setState((byte)0); 
      MessagePusher.pushMessage(id.longValue(), (Message)offRoleState);
      MessagePusher.pushMessage(id.longValue(), (Message)miscEx);
      chatService.sendInfo(id, tips);
    } 
  }
  
  public void breakOffGate(Role role, GangWarsData objData, GangWarsInfo warsInfo) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team.getLeaderUid() != role.getId().longValue()) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("老老实实听从队长指挥吧"));
      return;
    } 
    Equipment equipment = (Equipment)objData.getGateMap().get(Integer.valueOf(warsInfo.getEnemy()));
    if (equipment == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("敌方已经停止了攻击城门"));
      return;
    } 
    Map<Long, GangWarsPkData> enemyWarsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(Integer.valueOf(warsInfo.getEnemy()));
    if (enemyWarsPkDataMap == null)
      return; 
    Map<Long, GangWarsPkData> sideWarsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(role.getFaction());
    if (sideWarsPkDataMap == null)
      return; 
    cancelTask(equipment.getScheduledFuture());
    objData.getGateMap().remove(Integer.valueOf(warsInfo.getEnemy()));
    abortOperate(role, equipment, team, sideWarsPkDataMap, enemyWarsPkDataMap, (byte)3, "敌方阻止，停止攻击城门");
  }
  
  public void abortOperate(Role role, Equipment equipment, Team team, Map<Long, GangWarsPkData> sideWarsPkDataMap, Map<Long, GangWarsPkData> enemyWarsPkDataMap, byte state, String msg) {
    RespOffRoleState offRoleState = new RespOffRoleState("wars", state);
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member.isInTeam()) {
        GangWarsPkData warsPkData = enemyWarsPkDataMap.get(member.getId());
        if (warsPkData != null)
          warsPkData.setState((byte)0); 
        MessagePusher.pushMessage(member.getId().longValue(), (Message)offRoleState);
      } 
    } 
    Team enemyTeam = this.teamService.getTeamByTeamId(equipment.getTeam());
    if (enemyTeam == null)
      return; 
    Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
    if (player == null)
      return; 
    for (int j = 0; j < enemyTeam.getList().size(); j++) {
      Member member = enemyTeam.getList().get(j);
      if (member.isInTeam())
        MessagePusher.pushMessage(member.getId().longValue(), (Message)new RespNotifyMiscEx(member.getId(), msg, true)); 
    } 
    upMemberState(enemyWarsPkDataMap, enemyTeam, (byte)1);
    upMemberState(sideWarsPkDataMap, team, (byte)1);
    doStartFightPackage(role, team, player, enemyTeam);
  }
  
  public void upMemberState(Map<Long, GangWarsPkData> warsPkDataMap, Team team, byte state) {
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member.isInTeam())
        upMember(member.getId(), warsPkDataMap, state, 0); 
    } 
  }
  
  public void upMember(Long id, Map<Long, GangWarsPkData> warsPkDataMap, byte state, int time) {
    GangWarsPkData side = warsPkDataMap.get(id);
    if (side != null) {
      side.setState(state);
      if (time > 0) {
        side.setRebirth(time);
        side.setPosition((byte)0);
      } 
    } 
  }
  
  public List<Long> operateMembers(Team team, Map<Long, GangWarsPkData> warsPkDataMap, RespGangWarsRoleState respGangWarsRoleState, byte state) {
    List<Long> objects = new ArrayList<>();
    for (Member member : team.getList()) {
      if (member.isInTeam()) {
        MessagePusher.pushMessage(member.getId().longValue(), (Message)respGangWarsRoleState);
        GangWarsPkData gangWarsPkData = warsPkDataMap.get(member.getId());
        if (gangWarsPkData != null) {
          gangWarsPkData.setState(state);
          objects.add(member.getId());
        } 
      } 
    } 
    return objects;
  }
  
  public void gateTimer(int faction) {
    GangWarsData objData = this.gangWarsHandler.getObjData();
    if (objData == null)
      return; 
    List<Long> objects = new ArrayList<>();
    GangWarsInfo sideInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(faction));
    if (sideInfo == null || sideInfo.getCombatResults() != 3) {
      Equipment equipment1 = (Equipment)objData.getGateMap().get(Integer.valueOf(sideInfo.getVisible()));
      if (equipment1 != null) {
        cancelTask(equipment1.getScheduledFuture());
        objData.getGateMap().remove(Integer.valueOf(sideInfo.getVisible()));
      } 
      return;
    } 
    Equipment equipment = (Equipment)objData.getGateMap().get(Integer.valueOf(faction));
    if (equipment != null) {
      Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(Integer.valueOf(faction));
      for (Long id : equipment.getList()) {
        GangWarsPkData gangWarsPkData = warsPkDataMap.get(id);
        if (gangWarsPkData != null && gangWarsPkData.getPosition() != 3) {
          objects.add(id);
          gangWarsPkData.setState((byte)0);
          MessagePusher.pushMessage(id.longValue(), (Message)new RespMsg("本队对敌方#<O>城门#造成了#<G>30点伤害##<E:50>#"));
        } 
      } 
      GangWarsInfo enemyInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(sideInfo.getEnemy()));
      int gateLife = enemyInfo.getGateLife() - 30;
      enemyInfo.setGateLife((short)((gateLife < 0) ? 0 : gateLife));
      updateGateLife(faction, sideInfo, enemyInfo, objects, (byte)3);
    } 
  }
  
  public void updateGateLife(int serial, GangWarsInfo sideInfo, GangWarsInfo enemyInfo, List<Long> objects, byte type) {
    if (enemyInfo.getGateLife() <= 0) {
      sideInfo.setCombatResults((byte)1);
      enemyInfo.setCombatResults((byte)0);
      this.gangWarsHandler.end(serial, sideInfo, enemyInfo);
    } else {
      GangWarsData objData = this.gangWarsHandler.getObjData();
      if (objData == null)
        return; 
      RespGangWarsGateLife gangWarsGateLife = new RespGangWarsGateLife(sideInfo.getGateLife(), enemyInfo.getGateLife(), type, enemyInfo.getStance());
      for (Long id : objects)
        MessagePusher.pushMessage(id.longValue(), (Message)gangWarsGateLife); 
      Map<Long, GangWarsPkData> sideMemberMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(Integer.valueOf(serial));
      for (Long id : sideMemberMap.keySet()) {
        if (!objects.contains(id))
          MessagePusher.pushMessage(id.longValue(), (Message)gangWarsGateLife); 
      } 
      Map<Long, GangWarsPkData> enemyMemberMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(Integer.valueOf(sideInfo.getEnemy()));
      gangWarsGateLife = new RespGangWarsGateLife(enemyInfo.getGateLife(), sideInfo.getGateLife(), (byte)2, enemyInfo.getStance());
      for (Long id : enemyMemberMap.keySet()) {
        if (!objects.contains(id))
          MessagePusher.pushMessage(id.longValue(), (Message)gangWarsGateLife); 
      } 
    } 
  }
  
  public void requestFight(Role role, int id) {
    GangWarsData objData = this.gangWarsHandler.getObjData();
    if (objData == null)
      return; 
    Role player = this.roleService.getPlayer(id);
    if (player == null || role.getFaction().equals(player.getFaction()))
      return; 
    int count = this.teamService.getTeamCount(role);
    if (count < 3) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("人数不足#<R>3#人，不能发起攻击"));
      return;
    } 
    GangWarsInfo gangWarsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
    if (gangWarsInfo.getCombatResults() != 3 || !player.getFaction().equals(Integer.valueOf(gangWarsInfo.getEnemy())))
      return; 
    Fight fight = this.fightNewService.getFightByRoleId(player.getRoleId().intValue());
    if (fight != null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("对方在战斗中"));
      return;
    } 
    doStartFightPackage(role, this.teamService.getTeam(role.getRoleId().intValue()), (Map<Long, GangWarsPkData>)objData.getPkData().get(role.getFaction()), (Map<Long, GangWarsPkData>)objData.getPkData().get(player.getFaction()), player);
  }
  
  public void doStartFightPackage(Role role, Team team, Map<Long, GangWarsPkData> sideWarsPkDataMap, Map<Long, GangWarsPkData> enemyWarsPkDataMap, Role player) {
    GangWarsData objData = this.gangWarsHandler.getObjData();
    if (objData == null)
      return; 
    upMemberState(sideWarsPkDataMap, team, (byte)1);
    Team enemyTeam = this.teamService.getTeam(player.getRoleId().intValue());
    Role enemyLeader = player;
    Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(player.getFaction());
    if (enemyTeam == null) {
      if (!isStartFight(role.getId().longValue(), warsPkDataMap.get(player.getId())))
        return; 
      upMember(player.getId(), enemyWarsPkDataMap, (byte)1, 0);
    } else {
      for (int i = 0; i < enemyTeam.getList().size(); i++) {
        Member member = enemyTeam.getList().get(i);
        if (member.isInTeam())
          if (!isStartFight(role.getId().longValue(), warsPkDataMap.get(member.getId())))
            return;  
      } 
      upMemberState(enemyWarsPkDataMap, enemyTeam, (byte)1);
      enemyLeader = this.roleService.getPlayer(enemyTeam.getLeaderUid());
    } 
    GangWarsInfo gangWarsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
    Equipment equipment = (Equipment)objData.getCannonMap().get(Integer.valueOf(gangWarsInfo.getVisible()));
    if (equipment != null && 
      equipment.getList().contains(player.getId()))
      startFightBreakOff(role, player, equipment, objData, enemyTeam, (byte)2); 
    equipment = (Equipment)objData.getGateMap().get(player.getFaction());
    if (equipment != null)
      startFightBreakOff(role, player, equipment, objData, enemyTeam, (byte)3); 
    doStartFightPackage(role, team, enemyLeader, enemyTeam);
  }
  
  private void startFightBreakOff(Role role, Role player, Equipment equipment, GangWarsData objData, Team enemyTeam, byte state) {
    RespOffRoleState offRoleState = new RespOffRoleState("wars", state);
    cancelTask(equipment.getScheduledFuture());
    GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
    if (state == 3) {
      objData.getGateMap().remove(Integer.valueOf(warsInfo.getEnemy()));
    } else {
      objData.getCannonMap().remove(Integer.valueOf(warsInfo.getVisible()));
    } 
    if (enemyTeam != null) {
      for (int i = 0; i < enemyTeam.getList().size(); i++) {
        Member member = enemyTeam.getList().get(i);
        if (member.isInTeam())
          MessagePusher.pushMessage(member.getId().longValue(), (Message)offRoleState); 
      } 
    } else {
      MessagePusher.pushMessage(player.getId().longValue(), (Message)offRoleState);
    } 
  }
  
  private boolean isStartFight(long id, GangWarsPkData enemy) {
    if (enemy != null) {
      if (enemy.getState() == 1 || enemy.getState() == 4) {
        MessagePusher.pushMessage(id, (Message)new RespMsg("对方在战斗中"));
        return false;
      } 
      if (enemy.getRebirth() > 0) {
        int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
        if (enemy.getRebirth() > currTime) {
          MessagePusher.pushMessage(id, (Message)new RespMsg("对方正在保护阶段"));
          return false;
        } 
        enemy.setRebirth(0);
      } 
    } 
    return true;
  }
  
  public void doStartFightPackage(Role roleA, Team teamA, Role roleB, Team teamB) {
    List<Member> listA = new ArrayList<>();
    if (teamA == null) {
      listA.add(Member.valueOf(roleA));
    } else {
      listA.addAll(this.fightNewService.excuteMemberLeaveTemp(roleA, teamA));
    } 
    List<Member> listB = new ArrayList<>();
    if (teamB == null) {
      listB.add(Member.valueOf(roleB));
    } else {
      listB.addAll(this.fightNewService.excuteMemberLeaveTemp(roleB, teamB));
    } 
    doStartFight(listA, listB);
  }
  
  public void doStartFight(List<Member> listA, List<Member> listB) {
    if (listA == null || listB == null)
      return; 
    if (listA.size() < 0 || listB.size() < 0)
      return; 
    int fightType = getFightType();
    Fight fight = FightFactory.getFightFactory(fightType).create(3001, new Object[] { Integer.valueOf(fightType), listA, listB });
    fight.setMaxRound(MAX_ROUND);
    fight.start();
  }
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {
    ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> doFightEnd(fightEndEvent.getRole(), fightEndEvent));
  }
  
  private void doFightEnd(Role role, FightEndEvent fightEndEvent) {
    try {
      if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role))
        return; 
      if (!isMatchFightType(fightEndEvent.getFightType()))
        return; 
      if (!role.getPos().getMapId().equals(Integer.valueOf(3001)))
        return; 
      GangWarsData objData = this.gangWarsHandler.getObjData();
      if (objData == null)
        return; 
      GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
      if (warsInfo == null || warsInfo.getCombatResults() != 3)
        return; 
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      Map<Long, GangWarsPkData> sideWarsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(role.getFaction());
      if (sideWarsPkDataMap == null)
        return; 
      int time = 0;
      RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)0, time);
      if (fightEndEvent.getStatus() == 2) {
        time = (new Long(System.currentTimeMillis() / 1000L)).intValue() + 90;
        respGangWarsRoleState.setTime(time);
      } 
      FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), role.getId().longValue());
      for (Long id : fightInfo.getItself()) {
        upMember(id, sideWarsPkDataMap, (byte)0, time);
        if (time > 0)
          MessagePusher.pushMessage(id.longValue(), (Message)respGangWarsRoleState); 
      } 
      doFightEnd(role, team, fightEndEvent.getStatus(), objData, warsInfo.getStance());
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("战斗结束后操作错误-------------------------------------------------------");
    } 
  }
  
  public void doFightEnd(Role role, Team team, byte status, GangWarsData objData, byte stance) {
    if (status == 2) {
      int x = 24, y = 19;
      if (stance != 1) {
        x = 195;
        y = 109;
      } 
      if (team != null) {
        this.teamService.memberHandleThreadLocal(team, memberRole -> memberRole.pushTempCache("not_check_change_map", Boolean.valueOf(true)));
      } else {
        role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
      } 
      SpringUtils.getMapService().changeMap(role, new ReqTelePort(3001, x, y), null);
    } 
    if (status == 1) {
      int anInt = objData.getRecordMap().getInt(role.getFaction() + "_win", 0);
      objData.getRecordMap().setv(role.getFaction() + "_win", Integer.valueOf(anInt + 1));
    } 
  }
  
  public void getNearby(Role role) {
    GangWarsData objData = this.gangWarsHandler.getObjData();
    if (objData == null)
      return; 
    GangWarsInfo sideInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
    if (sideInfo == null || sideInfo.getCombatResults() != 3)
      return; 
    Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)objData.getPkData().get(Integer.valueOf(sideInfo.getEnemy()));
    if (warsPkDataMap == null)
      return; 
    List<Nearby> objects = new ArrayList<>(warsPkDataMap.size());
    for (Long id : warsPkDataMap.keySet()) {
      GangWarsPkData data = warsPkDataMap.get(id);
      if (data.getPosition() != 1)
        continue; 
      Role player = this.roleService.getPlayer(id.longValue());
      if (player == null)
        continue; 
      if (this.teamService.isInTeam(player) && !this.teamService.isTeamLeader(player))
        continue; 
      if (DataGlobalFunc.visual(role.getPos().getX().intValue(), role.getPos().getY().intValue(), player.getPos().getX().intValue(), player.getPos().getY().intValue(), 50.0D))
        objects.add(new Nearby(player)); 
    } 
    if (objects.size() > 0)
      MessagePusher.pushMessage(role, (Message)new RespNearby(objects)); 
  }
  
  public void cancelTask(ScheduledFuture<?> task) {
    if (task != null)
      task.cancel(Boolean.FALSE.booleanValue()); 
  }
}
