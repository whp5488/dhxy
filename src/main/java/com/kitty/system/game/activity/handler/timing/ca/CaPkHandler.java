package com.kitty.system.game.activity.handler.timing.ca;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.system.game.activity.message.RespCaClashResult;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.model.user.ca.CaData;
import com.kitty.system.game.activity.model.user.ca.CaPkData;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.system.game.activity.model.user.ca.CaSet;
import com.kitty.system.game.activity.model.user.ca.vo.CaFight;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaPkHandler extends ActivityHandler {
  @Autowired
  CaHandler caHandler;
  
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
  
  byte session = 1;
  
  int period = 2;
  
  private static final int MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_CA.getValue();
  
  private static final String FINISH_TIPS = "今晚的决战长安已结束，各位可以离场，下次决战长安再战吧！#<E:90>#";
  
  protected int getFightType() {
    return FIGHT_TYPE.CA_PK.getValue();
  }
  
  private boolean isMatchFightType(int fightType) {
    return (getFightType() == fightType);
  }
  
  public void start() {
    this.logger.error("决战长安对决开始");
    try {
      CaSet set = this.caHandler.getCaSet();
      withdraw();
      this.caHandler.getCaData().setStage((byte)1);
      grouping();
      match();
      this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
            public void run() {
              CaPkHandler.this.check();
            }
          },  10000L, (set.getPeriod() * 1000));
    } catch (Exception e) {
      end();
      e.printStackTrace();
      this.logger.error("决战长安PK阶段开始错误：{}", e.getMessage());
    } 
  }
  
  public void withdraw() {
    List<Integer> clearTeamList = new ArrayList<>();
    CaSet set = this.caHandler.getCaSet();
    List<CaPkData> list = this.caHandler.getCaData().getPkDataList();
    for (CaPkData data : list) {
      Team team = this.teamService.getTeamByTeamId(data.getTeamId());
      if (team == null) {
        clearTeamList.add(Integer.valueOf(data.getTeamId()));
        for (Long member : data.getMembers()) {
          Role role = this.roleService.getPlayer(member.longValue());
          if (role != null)
            if (this.caHandler.isInMap(role)) {
              role.pushTempCache("ca_leave", Boolean.valueOf(true));
              this.teamService.leave(role, false);
              this.caHandler.leave(role);
            }  
        } 
        continue;
      } 
      if (team != null && this.teamService.getTeamCount(team) < set.getMinTeamMemberCount()) {
        Role player = this.roleService.getPlayer(team.getLeaderUid());
        if (player != null) {
          this.caHandler.leave(player);
        } else {
          this.logger.error("决战长安比赛开始前弃权,队伍人数小于参赛最小人数清理出场。获取角色错误：{}", Long.valueOf(team.getLeaderUid()));
        } 
        clearTeamList.add(Integer.valueOf(team.getId()));
        continue;
      } 
      boolean flag = true;
      List<Integer> objects = this.teamService.getTeamAfkMember(team);
      for (Integer object : objects) {
        Role role = this.roleService.getPlayer(object.intValue());
        if (role != null) {
          role.pushTempCache("ca_leave", Boolean.valueOf(true));
          this.teamService.leave(role, false);
          this.caHandler.leave(role);
          role.pushTempCache("ca_leave", Boolean.valueOf(false));
          if (this.teamService.getTeamCount(team) < set.getMinTeamMemberCount()) {
            Role player = this.roleService.getPlayer(team.getLeaderUid());
            if (player != null)
              this.caHandler.leave(player); 
            clearTeamList.add(Integer.valueOf(team.getId()));
            flag = false;
          } 
        } 
      } 
      if (flag)
        this.caHandler.enter(data, team); 
    } 
    for (Iterator<Integer> iterator = clearTeamList.iterator(); iterator.hasNext(); ) {
      int teamId = ((Integer)iterator.next()).intValue();
      list.removeIf(e -> (e.getTeamId() == teamId));
    } 
    list = (List<CaPkData>)list.stream().sorted(Comparator.comparing(CaPkData::getScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
    this.caHandler.getCaData().setPkDataList(list);
  }
  
  private void check() {
    try {
      if (isCanEnd()) {
        this.logger.info("决战长安结束");
        end();
      } 
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("决战长安匹配出错-----------------------------------------------");
    } 
  }
  
  private boolean isCanEnd() {
    CaData data = this.caHandler.getCaData();
    if (data == null)
      return true; 
    if (sceneTeamCount(data) <= 1)
      return true; 
    if (Calendar.getInstance().after(data.getEndTime()))
      return true; 
    return false;
  }
  
  public int sceneTeamCount(CaData data) {
    int count = 0;
    if (data != null)
      for (CaPkData obj : data.getPkDataList()) {
        Role leader = this.teamService.getTeamLeader(obj.getTeamId());
        if (leader == null)
          continue; 
        if (this.caHandler.isInMap(leader))
          count++; 
      }  
    return count;
  }
  
  public void end() {
    try {
      CaData caData = this.caHandler.getCaData();
      caData.setStage((byte)2);
      this.logger.error("决战长安对决结束");
      for (CaPkData obj : caData.getPkDataList()) {
        Role leaderRole = this.teamService.getTeamLeader(obj.getTeamId());
        if (leaderRole == null)
          continue; 
        this.caHandler.leave(leaderRole);
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (this.scheduledFuture != null)
      this.scheduledFuture.cancel(Boolean.FALSE.booleanValue()); 
  }
  
  public void grouping() {
    CaData obj = this.caHandler.getCaData();
    Map<Integer, List<CaPkData>> map = new HashMap<>();
    for (CaPkData caPkData : obj.getPkDataList()) {
      Team team = this.teamService.getTeamByTeamId(caPkData.getTeamId());
      if (team == null)
        continue; 
      Role player = this.roleService.getPlayer(team.getLeaderUid());
      if (player == null)
        continue; 
      if (!this.caHandler.isInMap(player))
        continue; 
      List<CaPkData> list = map.get(Integer.valueOf(caPkData.getGroup()));
      if (list == null)
        list = new ArrayList<>(); 
      list.add(caPkData);
      map.put(Integer.valueOf(caPkData.getGroup()), list);
    } 
    grouping(map);
  }
  
  public void grouping(Map<Integer, List<CaPkData>> map) {
    for (Integer key : map.keySet()) {
      List<CaPkData> caPkDataList = map.get(key);
      caPkDataList = (List<CaPkData>)caPkDataList.stream().sorted(Comparator.comparing(CaPkData::getScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
      for (CaPkData datum : caPkDataList) {
        if (datum.getFight().size() >= this.session)
          continue; 
        int index = caPkDataList.indexOf(datum);
        if (index + 1 >= caPkDataList.size()) {
          datum.getFight().add(new CaFight(-1, new ArrayList(), (byte)0, this.session));
          break;
        } 
        CaPkData caPkData = caPkDataList.get(index + 1);
        CaFight caFight = datum.getFight().stream().filter(e -> (e.getTeamId() == caPkData.getTeamId())).findFirst().orElse(null);
        if (caFight == null) {
          datum.getFight().add(new CaFight(caPkData.getTeamId(), caPkData.getMembers(), (byte)0, this.session));
          caPkData.getFight().add(new CaFight(datum.getTeamId(), datum.getMembers(), (byte)0, this.session));
          continue;
        } 
        List<CaPkData> resList = new ArrayList<>();
        int i = index;
        boolean flag = true;
        while (flag) {
          i++;
          if (i >= caPkDataList.size()) {
            flag = false;
            continue;
          } 
          CaPkData pkData = caPkDataList.get(i);
          if (pkData.getFight().size() >= this.session)
            continue; 
          int teamId = pkData.getTeamId();
          caFight = datum.getFight().stream().filter(e -> (e.getTeamId() == teamId)).findFirst().orElse(null);
          if (caFight == null) {
            datum.getFight().add(new CaFight(caPkData.getTeamId(), caPkData.getMembers(), (byte)0, this.session));
            caPkData.getFight().add(new CaFight(datum.getTeamId(), datum.getMembers(), (byte)0, this.session));
            flag = false;
            continue;
          } 
          resList.add(pkData);
        } 
        if (datum.getFight().size() < this.session && resList.size() > 0) {
          CaPkData data = resList.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(resList.size() - 1)));
          int temId = data.getTeamId();
          datum.getFight().add(new CaFight(data.getTeamId(), data.getMembers(), (byte)0, this.session));
          data = caPkDataList.stream().filter(e -> (e.getTeamId() == temId)).findFirst().orElse(null);
          data.getFight().add(new CaFight(datum.getTeamId(), datum.getMembers(), (byte)0, this.session));
          continue;
        } 
        datum.getFight().add(new CaFight(-1, new ArrayList(), (byte)0, this.session));
      } 
    } 
  }
  
  private void match() {
    try {
      CaData obj = this.caHandler.getCaData();
      List<CaPkData> objPkDataList = obj.getPkDataList();
      Map<Integer, Integer> map = new HashMap<>(objPkDataList.size());
      for (CaPkData data : objPkDataList) {
        if (map.get(Integer.valueOf(data.getTeamId())) == null) {
          CaFight fight = EventsUtils.getMinNotFight(data);
          Team teamA = this.teamService.getTeamByTeamId(data.getTeamId());
          if (fight.getTeamId() == -1) {
            FightInfo fightInfo = new FightInfo();
            fightInfo.setItselfLeader(teamA.getLeaderUid());
            fightInfo.setItself(data.getMembers());
            doFightEnd((byte)1, fightInfo, data);
            continue;
          } 
          if (fight != null) {
            Team teamB = this.teamService.getTeamByTeamId(fight.getTeamId());
            match(teamA, teamB);
            map.put(Integer.valueOf(teamA.getId()), Integer.valueOf(teamB.getId()));
            map.put(Integer.valueOf(teamB.getId()), Integer.valueOf(teamA.getId()));
          } 
        } 
      } 
      needOver();
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("匹配对手出错-----------------------------------------------");
    } 
  }
  
  public void needOver() {
    this.session = (byte)(this.session + 1);
    if (this.session > 3) {
      this.caHandler.needTime = 0L;
      return;
    } 
    SchedulerManager.getInstance().schedule(new Runnable() {
          public void run() {
            try {
              CaData obj = CaPkHandler.this.caHandler.getCaData();
              List<CaPkData> objPkDataList = obj.getPkDataList();
              for (CaPkData caPkData : objPkDataList) {
                Team team = CaPkHandler.this.teamService.getTeamByTeamId(caPkData.getTeamId());
                if (team != null) {
                  Role player = CaPkHandler.this.roleService.getPlayer(team.getLeaderUid());
                  if (CaPkHandler.this.caHandler.isInMap(player) && CaPkHandler.this.fightNewService.isInFight(CaPkHandler.this.roleService.getPlayer(team.getLeaderUid()))) {
                    Fight fight = CaPkHandler.this.fightNewService.getFightByRoleId(player.getRoleId().intValue());
                    CaPkHandler.this.fightNewService.quitFight(fight);
                  } 
                } 
              } 
              CaPkHandler.this.grouping();
              CaPkHandler.this.match();
            } catch (Exception e) {
              e.printStackTrace();
            } 
          }
        },  this.period * 60000L);
    this.caHandler.needTime = System.currentTimeMillis() + this.period * 60000L;
    System.out.println("下一场等待时间 = " + this.caHandler.needTime);
  }
  
  private void match(Team teamA, Team teamB) {
    if (teamA != null && teamB != null) {
      CaData data = this.caHandler.getCaData();
      CaPkData pkDataA = data.getPkDataList().stream().filter(e -> (e.getTeamId() == teamA.getId())).findFirst().orElse(null);
      CaPkData pkDataB = data.getPkDataList().stream().filter(e -> (e.getTeamId() == teamB.getId())).findFirst().orElse(null);
      Role playerA = this.roleService.getPlayer(teamA.getLeaderUid());
      Role playerB = this.roleService.getPlayer(teamB.getLeaderUid());
      boolean inMapA = this.caHandler.isInMap(playerA);
      boolean inMapB = this.caHandler.isInMap(playerB);
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
    final int mapId = 3005;
    final int fightType = getFightType();
    EventsUtils.clashPlans(teamA.getList(), teamB.getList(), 3002, 10);
    SchedulerManager.getInstance().schedule(new Runnable() {
          public void run() {
            Fight fight = FightFactory.getFightFactory(fightType).create(mapId, new Object[] {fightType, teamA.getList(), teamB.getList() });
            fight.setMaxRound(CaPkHandler.MAX_ROUND);
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
      if (!role.getPos().getMapId().equals(Integer.valueOf(3005)))
        return; 
      CaData caData = this.caHandler.getCaData();
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team == null)
        return; 
      CaPkData itselfData = caData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
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
      CaFight fight = updateFightData(itselfData, fightInfo, fightEndEvent.getStatus());
      doFightEnd(fightEndEvent.getStatus(), fightInfo, itselfData);
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("战斗结束后操作错误-------------------------------------------------------");
    } 
  }
  
  private CaFight updateFightData(CaPkData pkData, FightInfo fightInfo, byte state) {
    CaFight fight = EventsUtils.getMinNotFight(pkData);
    if (fight == null)
      return null; 
    for (CaFight caFight : pkData.getFight()) {
      if (caFight.getTeamId() == fight.getTeamId()) {
        caFight.setState(state);
        caFight.setMembers(fightInfo.getEnemy());
        break;
      } 
    } 
    return fight;
  }
  
  private void doFightWin(FightInfo fightInfo, int score) {
    for (Long id : fightInfo.getItself()) {
      Role player = this.roleService.getPlayer(id.longValue());
      if (player != null)
        this.caHandler.compensate(player, score); 
    } 
  }
  
  public void doFightEnd(byte result, FightInfo fightInfo, CaPkData itselfData) {
    try {
      CaSet set = this.caHandler.getCaSet();
      RespCaClashResult resp = new RespCaClashResult();
      resp.setItself(this.caHandler.fightMember(fightInfo.getItself(), fightInfo.getItselfLeader()));
      resp.setEnemy(this.caHandler.fightMember(fightInfo.getEnemy(), fightInfo.getEnemyLeader()));
      resp.setResult(result);
      if (result == 1) {
        doFightWin(fightInfo, set.getWinScore());
      } else {
        doFightWin(fightInfo, set.getFailScore());
      } 
      itselfData.setTime(0);
      for (int i = 0; i < fightInfo.getItself().size(); i++) {
        Role player = this.roleService.getPlayer(((Long)fightInfo.getItself().get(i)).longValue());
        if (player != null) {
          sendAward(player, result);
          this.caHandler.sendRespIcon(player);
          CaRankRecord record = (CaRankRecord)RankingList.CA_RANK_RECORDS.get(player.getId());
          resp.setScore((short)record.getScore());
          resp.setMaxScore((short)(fightInfo.getItself().size() * set.getWinScore() + record.getScore()));
          MessagePusher.pushMessage(player, (Message)resp);
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void jumpWin(CaPkData itselfData, CaFight fight) {
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
    CaFight atFight = updateFightData(itselfData, fightInfo, (byte)1);
    doFightEnd((byte)1, fightInfo, itselfData);
  }
  
  private List<FiedValue> sendAward(Role role, byte type) {
    List<FiedValue> objects = new ArrayList<>();
    long heroExp = calculate(role.getLevel().intValue(), role.getRelive().intValue(), ItemClassify.HERO_EXP);
    heroExp = (long)((type == 2) ? (heroExp * 2.0D) : heroExp);
    objects.add(new FiedValue(String.valueOf(ItemClassify.HERO_EXP.getId()), (byte)4, String.valueOf(heroExp)));
    this.roleService.addExp(role, heroExp, "", true);
    Integer combatPet = role.getExtendBox().getCombatPet();
    if (combatPet.intValue() > 0) {
      PetService petService = SpringUtils.getPetService();
      Pet pet = (Pet)petService.selectPet(role).get(combatPet);
      if (pet != null) {
        long calculate = calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), ItemClassify.PET_EXP);
        calculate = (long)((type == 2) ? (calculate * 2.0D) : calculate);
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
