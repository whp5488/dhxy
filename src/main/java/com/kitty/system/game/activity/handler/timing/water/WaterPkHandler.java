package com.kitty.system.game.activity.handler.timing.water;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.system.game.activity.message.RespWaterClashResult;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.model.user.lu.WaterData;
import com.kitty.system.game.activity.model.user.lu.WaterPkData;
import com.kitty.system.game.activity.model.user.lu.WaterSet;
import com.kitty.system.game.activity.model.user.lu.vo.WaterFight;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterPkHandler extends ActivityHandler {
  @Autowired
  WaterHandler waterHandler;
  
  @Autowired
  protected MapService mapService;
  
  @Autowired
  protected TeamService teamService;
  
  @Autowired
  protected FightNewService fightNewService;
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  ServerService serverService;
  
  private ScheduledFuture scheduledFuture;
  
  private static final int SERIAL = 30023;
  
  private static final int MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_LU.getValue();
  
  private static final String FINISH_TIPS = "今晚的水陆大会已结束，各位可以离场，下次水陆大会再战吧！#<E:90>#";
  
  private static final String COMPENSATE_TIPS = String.format("你处在了水陆大会的最后一组而且不足4队无法比赛了，水陆举办人见你们太艰难，给予了你们%d水陆积分,下轮要继续加油啊少年", new Object[] { Integer.valueOf(50) });
  
  protected int getFightType() {
    return FIGHT_TYPE.WATER_PK.getValue();
  }
  
  private boolean isMatchFightType(int fightType) {
    return (getFightType() == fightType);
  }
  
  public void start() {
    this.logger.error("水陆大会对决开始");
    try {
      WaterSet waterSet = this.waterHandler.getWaterSet();
      withdraw();
      this.waterHandler.getWaterData().setStage((byte)1);
      grouping();
      match();
      this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
            public void run() {
              WaterPkHandler.this.check();
            }
          },  10000L, (waterSet.getPeriod() * 1000));
    } catch (Exception e) {
      end();
      e.printStackTrace();
      this.logger.error("水陆大会PK阶段开始错误：{}", e.getMessage());
    } 
  }
  
  public void withdraw() {
    List<Integer> clearTeamList = new ArrayList<>();
    WaterSet waterSet = this.waterHandler.getWaterSet();
    List<WaterPkData> list = this.waterHandler.getWaterData().getPkDataList();
    for (WaterPkData data : list) {
      Team team = this.teamService.getTeamByTeamId(data.getTeamId());
      if (team == null) {
        clearTeamList.add(Integer.valueOf(data.getTeamId()));
        for (Long member : data.getMembers()) {
          Role role = this.roleService.getPlayer(member.longValue());
          if (role != null)
            if (this.waterHandler.isInMap(role)) {
              role.pushTempCache("lu_leave", Boolean.valueOf(true));
              this.teamService.leave(role, false);
              this.waterHandler.leave(role);
            }  
        } 
        continue;
      } 
      if (team != null && this.teamService.getTeamCount(team) < waterSet.getMinTeamMemberCount()) {
        Role player = this.roleService.getPlayer(team.getLeaderUid());
        if (player != null) {
          this.waterHandler.leave(player);
        } else {
          this.logger.error("水陆比赛开始前弃权,队伍人数小于参赛最小人数清理出场。获取角色错误：{}", Long.valueOf(team.getLeaderUid()));
        } 
        clearTeamList.add(Integer.valueOf(team.getId()));
        continue;
      } 
      boolean flag = true;
      List<Integer> objects = this.teamService.getTeamAfkMember(team);
      for (Integer object : objects) {
        Role role = this.roleService.getPlayer(object.intValue());
        if (role != null) {
          role.pushTempCache("lu_leave", Boolean.valueOf(true));
          this.teamService.leave(role, false);
          this.waterHandler.leave(role);
          role.pushTempCache("lu_leave", Boolean.valueOf(false));
          if (this.teamService.getTeamCount(team) < waterSet.getMinTeamMemberCount()) {
            Role player = this.roleService.getPlayer(team.getLeaderUid());
            if (player != null)
              this.waterHandler.leave(player); 
            clearTeamList.add(Integer.valueOf(team.getId()));
            flag = false;
          } 
        } 
      } 
      if (flag)
        this.waterHandler.enter(data, team); 
    } 
    for (Iterator<Integer> iterator = clearTeamList.iterator(); iterator.hasNext(); ) {
      int teamId = ((Integer)iterator.next()).intValue();
      list.removeIf(e -> (e.getTeamId() == teamId));
    } 
    list = (List<WaterPkData>)list.stream().sorted(Comparator.comparing(WaterPkData::getScore, Comparator.reverseOrder()).thenComparing(WaterPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(WaterPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
    int surplus = list.size() % 4;
    for (int i = 0; i < surplus; i++) {
      int index = list.size() - 1;
      WaterPkData data = list.get(index);
      clearSurplus(data);
      list.remove(index);
    } 
    this.waterHandler.getWaterData().setPkDataList(list);
  }
  
  public void clearSurplus(WaterPkData data) {
    if (data != null) {
      Team team = this.teamService.getTeamByTeamId(data.getTeamId());
      Role leader = this.roleService.getPlayer(team.getLeaderUid());
      if (leader != null)
        this.waterHandler.leave(leader); 
      for (Long id : data.getMembers()) {
        Role player = this.roleService.getPlayer(id.longValue());
        if (player != null) {
          this.waterHandler.compensate(player, 50);
          MessagePusher.pushMessage(player, (Message)new RespMsg(COMPENSATE_TIPS));
        } 
      } 
    } 
  }
  
  public void grouping() {
    short rank = 1;
    LinkedHashMap<Integer, WaterPkData> map = new LinkedHashMap<>(4);
    List<WaterPkData> pkDataList = this.waterHandler.getWaterData().getPkDataList();
    for (int i = 0; i < pkDataList.size(); i++) {
      map.put(Integer.valueOf(i), pkDataList.get(i));
      if (map.size() >= 4) {
        grouping(pkDataList, map, rank);
        map = new LinkedHashMap<>(4);
        rank = (short)(rank + 1);
      } 
    } 
  }
  
  public static void grouping(List<WaterPkData> pkDataList, LinkedHashMap<Integer, WaterPkData> map, short rank) {
    for (Iterator<Integer> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
      int index = ((Integer)iterator.next()).intValue();
      boolean flag = true;
      WaterPkData data = map.get(Integer.valueOf(index));
      while (flag) {
        for (Iterator<Integer> iterator1 = map.keySet().iterator(); iterator1.hasNext(); ) {
          int key = ((Integer)iterator1.next()).intValue();
          WaterPkData pkData = map.get(Integer.valueOf(key));
          WaterFight fight = data.getFight().stream().filter(e -> (e.getTeamId() == pkData.getTeamId())).findFirst().orElse(null);
          if (data.getTeamId() != pkData.getTeamId() && fight == null) {
            byte session = getSession(data.getFight());
            WaterFight waterFight = pkData.getFight().stream().filter(e -> (e.getSession() == session)).findFirst().orElse(null);
            if (waterFight == null) {
              data.getFight().add(new WaterFight(pkData.getTeamId(), pkData.getMembers(), (byte)0, session));
              pkData.getFight().add(new WaterFight(data.getTeamId(), data.getMembers(), (byte)0, session));
            } 
          } 
        } 
        if (data.getFight().size() >= 3)
          flag = false; 
      } 
      data.setRank(rank);
      List<WaterFight> collect = (List<WaterFight>)data.getFight().stream().sorted(Comparator.comparing(WaterFight::getSession)).collect(Collectors.toList());
      data.setFight(collect);
      pkDataList.set(index, data);
    } 
  }
  
  public static byte getSession(List<WaterFight> list) {
    if (list.size() <= 0)
      return 1; 
    List<Byte> integers = new ArrayList<Byte>() {
      
      };
    for (WaterFight waterFight : list) {
      if (integers.contains(Byte.valueOf(waterFight.getSession())))
        integers.removeIf(e -> e.equals(Byte.valueOf(waterFight.getSession()))); 
    } 
    return ((Byte)Collections.<Byte>min(integers)).byteValue();
  }
  
  private void check() {
    try {
      if (isCanEnd()) {
        this.logger.info("水陆大会结束");
        end();
      } 
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("水陆大会匹配出错-----------------------------------------------");
    } 
  }
  
  private boolean isCanEnd() {
    WaterData data = this.waterHandler.getWaterData();
    if (data == null)
      return true; 
    if (sceneTeamCount(data) <= 1)
      return true; 
    if (Calendar.getInstance().after(data.getEndTime()))
      return true; 
    return false;
  }
  
  public int sceneTeamCount(WaterData data) {
    int count = 0;
    if (data != null)
      for (WaterPkData obj : data.getPkDataList()) {
        Role leader = this.teamService.getTeamLeader(obj.getTeamId());
        if (leader == null)
          continue; 
        if (this.waterHandler.isInMap(leader))
          count++; 
      }  
    return count;
  }
  
  public void end() {
    try {
      WaterData waterData = this.waterHandler.getWaterData();
      waterData.setStage((byte)2);
      this.logger.error("水陆大会对决结束");
      for (WaterPkData obj : waterData.getPkDataList()) {
        Role leaderRole = this.teamService.getTeamLeader(obj.getTeamId());
        if (leaderRole == null)
          continue; 
        this.waterHandler.leaveMap(leaderRole);
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (this.scheduledFuture != null)
      this.scheduledFuture.cancel(Boolean.FALSE.booleanValue()); 
  }
  
  private void match() {
    try {
      WaterData obj = this.waterHandler.getWaterData();
      List<WaterPkData> objPkDataList = obj.getPkDataList();
      Map<Integer, Integer> map = new HashMap<>(objPkDataList.size());
      for (WaterPkData data : objPkDataList) {
        if (map.get(Integer.valueOf(data.getTeamId())) == null) {
          WaterFight fight = EventsUtils.getMinNotFight(data);
          if (fight != null) {
            Team teamA = this.teamService.getTeamByTeamId(data.getTeamId());
            Team teamB = this.teamService.getTeamByTeamId(fight.getTeamId());
            match(teamA, teamB);
            map.put(Integer.valueOf(teamA.getId()), Integer.valueOf(teamB.getId()));
            map.put(Integer.valueOf(teamB.getId()), Integer.valueOf(teamA.getId()));
          } 
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("匹配对手出错-----------------------------------------------");
    } 
  }
  
  private void match(Team teamA, Team teamB) {
    if (teamA != null && teamB != null) {
      WaterData data = this.waterHandler.getWaterData();
      WaterPkData pkDataA = data.getPkDataList().stream().filter(e -> (e.getTeamId() == teamA.getId())).findFirst().orElse(null);
      WaterPkData pkDataB = data.getPkDataList().stream().filter(e -> (e.getTeamId() == teamB.getId())).findFirst().orElse(null);
      Role playerA = this.roleService.getPlayer(teamA.getLeaderUid());
      Role playerB = this.roleService.getPlayer(teamB.getLeaderUid());
      boolean inMapA = this.waterHandler.isInMap(playerA);
      boolean inMapB = this.waterHandler.isInMap(playerB);
      if (!inMapA && !inMapB)
        return; 
      if (!inMapA) {
        jumpWin(pkDataB, EventsUtils.getMinNotFight(pkDataB));
        return;
      } 
      if (!inMapB) {
        jumpWin(pkDataA, EventsUtils.getMinNotFight(pkDataA));
        return;
      } 
      doStartFight(teamA, teamB);
      if (pkDataA != null) {
        pkDataA.setTime(0);
        pkDataA.updateTeamMember((byte)1);
      } else {
        this.logger.error("1.更新组队成员获取信息错误：队伍ID = {}", Integer.valueOf(teamA.getId()));
      } 
      if (pkDataB != null) {
        pkDataB.setTime(0);
        pkDataB.updateTeamMember((byte)1);
      } else {
        this.logger.error("2.更新组队成员获取信息错误：队伍ID = {}", Integer.valueOf(teamB.getId()));
      } 
    } 
  }
  
  private void doStartFight(final Team teamA, final Team teamB) {
    final int mapId = 3003;
    final int fightType = getFightType();
    EventsUtils.clashPlans(teamA.getList(), teamB.getList(), 3003, 10);
    SchedulerManager.getInstance().schedule(new Runnable() {
          public void run() {
            Fight fight = FightFactory.getFightFactory(fightType).create(mapId, new Object[] {fightType, teamA.getList(), teamB.getList() });
            fight.setMaxRound(WaterPkHandler.MAX_ROUND);
            fight.start();
          }
        }, 10000L);
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
      if (!role.getPos().getMapId().equals(Integer.valueOf(3003)))
        return; 
      WaterData waterData = this.waterHandler.getWaterData();
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team == null)
        return; 
      WaterPkData itselfData = waterData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
      if (itselfData != null) {
        itselfData.updateTeamMember((byte)0);
      } else {
        this.logger.error("战斗结束后获取队伍ID信息失败");
        return;
      } 
      itselfData.subPkValue((short)1);
      FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), role.getId().longValue());
      if (fightInfo == null)
        return; 
      WaterFight fight = updateFightData(itselfData, fightInfo, fightEndEvent.getStatus());
      doFightEnd(fightEndEvent.getStatus(), fightInfo, itselfData, fight.getSession());
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("战斗结束后操作错误-------------------------------------------------------");
    } 
  }
  
  private WaterFight updateFightData(WaterPkData pkData, FightInfo fightInfo, byte state) {
    WaterFight fight = EventsUtils.getMinNotFight(pkData);
    if (fight == null)
      return null; 
    for (WaterFight waterFight : pkData.getFight()) {
      if (waterFight.getTeamId() == fight.getTeamId()) {
        waterFight.setState(state);
        waterFight.setMembers(fightInfo.getEnemy());
        break;
      } 
    } 
    return fight;
  }
  
  private void doFightWin(FightInfo fightInfo, int score, int feats) {
    for (Long id : fightInfo.getItself()) {
      Role player = this.roleService.getPlayer(id.longValue());
      if (player != null) {
        this.waterHandler.compensate(player, score);
        this.roleService.addCurrency(player, Const.RES_TYPE_FEATS, Long.valueOf(feats), "水陆大会", false, false);
      } 
    } 
  }
  
  public void doFightEnd(byte result, FightInfo fightInfo, WaterPkData itselfData, byte session) {
    try {
      WaterSet waterSet = this.waterHandler.getWaterSet();
      RespWaterClashResult resp = new RespWaterClashResult();
      resp.setItself(this.waterHandler.fightMember(fightInfo.getItself(), fightInfo.getItselfLeader()));
      resp.setEnemy(this.waterHandler.fightMember(fightInfo.getEnemy(), fightInfo.getEnemyLeader()));
      resp.setResult(result);
      if (result == 1) {
        int score = waterSet.getScore() + waterSet.getRankExtraScore().getInt(itselfData.getRank() + "_SCORE", 0);
        int feats = waterSet.getFeats() + waterSet.getRankExtraScore().getInt(itselfData.getRank() + "_FEATS", 0);
        resp.setScore((short)score);
        resp.setFeats((short)feats);
        doFightWin(fightInfo, score, feats);
      } 
      itselfData.setTime(0);
      for (int i = 0; i < fightInfo.getItself().size(); i++) {
        Role player = this.roleService.getPlayer(((Long)fightInfo.getItself().get(i)).longValue());
        if (player != null) {
          sendAward(player, result);
          this.waterHandler.sendRespIcon(player);
          MessagePusher.pushMessage(player, (Message)resp);
        } 
      } 
      assigning((byte)(session + 1), itselfData);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private void assigning(byte session, WaterPkData itselfData) {
    if (session > 3) {
      Team team = this.teamService.getTeamByTeamId(itselfData.getTeamId());
      if (team == null)
        return; 
      this.teamService.memberHandleThreadLocal(team, memberRole -> MessagePusher.pushMessage(memberRole, (Message)new RespMsg("今晚的水陆大会已结束，各位可以离场，下次水陆大会再战吧！#<E:90>#")));
      long count = itselfData.getFight().stream().filter(e -> (e.getState() == 1)).count();
      if (count >= 3L) {
        BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
        this.teamService.memberHandleThreadLocal(team, memberRole -> bagService.addGoods(memberRole, 30023, 1, 0, true, true, "水陆三胜"));
      } 
      return;
    } 
    WaterFight needFight = itselfData.getFight().stream().filter(e -> (e.getSession() == session)).findFirst().orElse(null);
    WaterPkData enemyData = this.waterHandler.getWaterData().getPkDataList().stream().filter(e -> (e.getTeamId() == needFight.getTeamId())).findFirst().orElse(null);
    if (enemyData == null) {
      jumpWin(itselfData, needFight);
      return;
    } 
    final Team enemyTeam = this.teamService.getTeamByTeamId(enemyData.getTeamId());
    if (enemyTeam == null) {
      jumpWin(itselfData, needFight);
      return;
    } 
    Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
    if (player == null) {
      jumpWin(itselfData, needFight);
      return;
    } 
    if (!this.waterHandler.isInMap(player)) {
      jumpWin(itselfData, needFight);
      return;
    } 
    if (enemyData.getState() == 0) {
      final Team itselfTeam = this.teamService.getTeamByTeamId(itselfData.getTeamId());
      cancelTask(enemyData.getSchedule());
      int time = (new Long(System.currentTimeMillis() / 1000L)).intValue() + 60;
      enemyData.setTime(time);
      itselfData.setTime(time);
      this.teamService.memberHandleThreadLocal(itselfTeam, memberRole -> this.waterHandler.sendRespIcon(memberRole));
      this.teamService.memberHandleThreadLocal(enemyTeam, memberRole -> this.waterHandler.sendRespIcon(memberRole));
      SchedulerManager.getInstance().schedule(() -> awaitFight(itselfTeam, enemyTeam), 60000L);
    } else {
      final Team itselfTeam = this.teamService.getTeamByTeamId(itselfData.getTeamId());
      ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
            public void run() {
              WaterPkHandler.this.scheduler(itselfTeam.getId(), enemyTeam.getId());
            }
          }, 0L, 60000L);
      itselfData.setSchedule(schedule);
    } 
  }
  
  public void awaitFight(Team teamA, Team teamB) {
    match(teamA, teamB);
  }
  
  public void scheduler(int itselfTeamId, int enemyTeamId) {
    WaterPkData itselfData = this.waterHandler.getWaterData().getPkDataList().stream().filter(e -> (e.getTeamId() == itselfTeamId)).findFirst().orElse(null);
    WaterFight needFight = itselfData.getFight().stream().filter(e -> (e.getTeamId() == enemyTeamId)).findFirst().orElse(null);
    Team enemyTeam = this.teamService.getTeamByTeamId(enemyTeamId);
    if (enemyTeam == null) {
      cancelTask(itselfData.getSchedule());
      jumpWin(itselfData, needFight);
      return;
    } 
    Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
    if (player == null) {
      cancelTask(itselfData.getSchedule());
      jumpWin(itselfData, needFight);
      return;
    } 
    if (!this.waterHandler.isInMap(player)) {
      cancelTask(itselfData.getSchedule());
      jumpWin(itselfData, needFight);
      return;
    } 
  }
  
  public void jumpWin(WaterPkData itselfData, WaterFight fight) {
    Team itselfTeam = this.teamService.getTeamByTeamId(itselfData.getTeamId());
    if (itselfTeam == null)
      return; 
    List<Role> listA = new ArrayList<>();
    for (int i = 0; i < itselfTeam.getList().size(); i++) {
      Member member = itselfTeam.getList().get(i);
      if (member != null)
        if (member.isInTeam()) {
          Role player = this.roleService.getPlayer(member.getId().longValue());
          if (player != null)
            listA.add(player); 
        }  
    } 
    List<Role> listB = new ArrayList<>();
    for (Long id : fight.getMembers()) {
      Role player = this.roleService.getPlayer(id.longValue());
      if (player != null)
        listA.add(player); 
    } 
    FightInfo fightInfo = EventsUtils.getFightInfo(listA, listB, itselfTeam.getLeaderUid());
    if (fightInfo == null)
      return; 
    WaterFight atFight = updateFightData(itselfData, fightInfo, (byte)1);
    doFightEnd((byte)1, fightInfo, itselfData, atFight.getSession());
  }
  
  private void cancelTask(ScheduledFuture<?> task) {
    if (task != null)
      task.cancel(Boolean.FALSE.booleanValue()); 
  }
  
  private List<FiedValue> sendAward(Role role, byte type) {
    List<FiedValue> objects = new ArrayList<>();
    long heroExp = calculate(role.getLevel().intValue(), role.getRelive().intValue(), ItemClassify.HERO_EXP);
    heroExp = (long)((type == 2) ? (heroExp * 0.75D) : heroExp);
    objects.add(new FiedValue(String.valueOf(ItemClassify.HERO_EXP.getId()), (byte)4, String.valueOf(heroExp)));
    this.roleService.addExp(role, heroExp, "", true);
    Integer combatPet = role.getExtendBox().getCombatPet();
    if (combatPet.intValue() > 0) {
      PetService petService = SpringUtils.getPetService();
      Pet pet = (Pet)petService.selectPet(role).get(combatPet);
      if (pet != null) {
        long calculate = calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), ItemClassify.PET_EXP);
        calculate = (long)((type == 2) ? (calculate * 0.75D) : calculate);
        objects.add(new FiedValue(String.valueOf(ItemClassify.PET_EXP.getId()), (byte)4, String.valueOf(calculate)));
        petService.addExp(role, pet, calculate, true);
      } 
    } 
    return objects;
  }
  
  private static long calculate(int grade, int rei, ItemClassify type) {
    if (type == ItemClassify.HERO_EXP)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.9E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 1.39D * (0.23D + Math.pow(1.0D, 0.35D)) * 1.3D)).longValue(); 
    if (type == ItemClassify.PET_EXP)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.9E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 1.39D * (0.23D + Math.pow(1.0D, 0.35D)) * 1.3D * 1.2D)).longValue(); 
    return 0L;
  }
}
