package com.kitty.system.game.activity.handler.time;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.enter.Position;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.map.model.GroupMapParam;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.model.PartyMember;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysDataPartyBandit;
import com.kitty.system.init.domain.SysWarRole;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PartyBanditHandler extends FightActivityHandler {
  private static final String DIST = "bandit";
  
  private static final double PROB = 0.45D;
  
  private static final int[] BANDIT = new int[] { 1, 2, 3, 4 };
  
  private static final int MIN_TEAM_COUNT = 3;
  
  private static final int MAX_BANDIT_COUNT = 15;
  
  private static final int SHOW_MINUTE = 20;
  
  private static final String CONTENT = "为帮除害/threats_p_bandit|再见/离开";
  
  private static final String IN_FIGHT = "再见/离开";
  
  private static final String TEAM_NOT_PARTY = "{0}不是本帮成员哦！#<E:109>#";
  
  private static final String TIPS = "俺们老大说了：给你们20分钟时间，如果能把俺们兄弟都打到，就饶了你们帮派不抢。出手吧#<E:32>#";
  
  private static final String TIPS_NOT = "我正在战斗中，等下再来收拾你#<E:32>#";
  
  private static final String TEAM_COUNT_NOT_ENOUGH = "边凉快去，不够3人的队伍，大爷懒得出手#<E:0>#";
  
  private static final String FIGHT_END = "{0}的队伍三拳两脚就消灭了一队强盗#<E:81>#，{1}";
  
  private static final String JOIN_PARTY_TIME = "{0}进入帮派小于24小时，也敢来多管闲事！#<E:109>#";
  
  protected int getFightType(Role role) {
    return FIGHT_TYPE.PARTY_BANDIT.getValue();
  }
  
  public void flushAfterServerStart() {
    List<SysDataPartyBandit> partyBanditList = TaskDataPool.partyBanditList;
    Map<Integer, List<SysDataPartyBandit>> banditGradingMap = getBanditGrading(partyBanditList);
    int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    for (Integer key : PartyDataPool.partyMap.keySet()) {
      Party party = (Party)PartyDataPool.partyMap.get(key);
      if (party == null) {
        this.logger.error("刷新帮派强盗帮派信息为空");
        continue;
      } 
      int day = BigDecimalUtil.decimalScaleBig(Integer.valueOf(0), Integer.valueOf((time - party.getCreateTime()) / 60 / 60 / 24)).intValue();
      if (day < 3)
        continue; 
      if (!SkillAi.canAffectOnPro(0.45D))
        continue; 
      int count = 0;
      int scene = PartyDataPool.getScene(party.getLevel());
      List<SysDataPartyBandit> creationBandit = getCreationBandit(banditGradingMap, party);
      for (SysDataPartyBandit obj : creationBandit) {
        Position position = getRandomPosition(scene);
        if (position != null) {
          int id = ((BossService)SpringUtils.getBean(BossService.class)).getTempNpcId();
          Npc npc = new Npc(id, 0, scene, position.getX().intValue(), position.getY().intValue(), obj.getIcon(), obj.getName(), "", obj.getId(), obj.getLevel(), 0, 24);
          npc.setVisible(party.getSerial().intValue());
          npc.setEndTime(npc.getCreateTime() + 1200);
          GroupMapParam groupMapParam = new GroupMapParam(party.getSerial());
          this.mapService.addGroupMapNpc(groupMapParam, npc);
          this.bossService.broadcastNpcShow(groupMapParam, npc);
          SchedulerManager.getInstance().schedule(() -> clearNpcTimeOut(groupMapParam, npc), 1200000L);
          count++;
        } 
      } 
      if (count > 0) {
        party.getNutMap().setv("num", Integer.valueOf(count));
        party.getNutMap().setv("bandit", Integer.valueOf(count));
        ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(() -> partyNpcTips(party.getSerial().intValue()), 1200000L);
        party.setSchedule(schedule);
        this.partyService.npcPartyChat(party, "出大事了#<E:24>#！一大波强盗入侵我帮闹事，大家快快回帮将他们剿灭啊#<E:52>#。");
      } 
    } 
  }
  
  public void partyNpcTips(int serial) {
    Party party = this.partyService.getParty(Integer.valueOf(serial));
    int num = party.getNutMap().getInt("bandit", 0);
    if (party.getNutMap().getInt("bandit", 0) > 0) {
      this.partyService.npcPartyChat(party, "20分钟内没有将帮派强盗全部消灭，强盗看着也没什么可抢的，就默默撤离了");
      if (party.getCapital() > 0L) {
        long construct = party.getCapital() - ((int)(this.partyService.getPartyMaxMembers(party.getLevel()) * 2.01467D) * num);
        party.setCapital((construct < 0L) ? 0L : construct);
        party.save();
      } 
    } 
  }
  
  public Map<Integer, List<SysDataPartyBandit>> getBanditGrading(List<SysDataPartyBandit> partyBanditList) {
    Map<Integer, List<SysDataPartyBandit>> map = new HashMap<>(4);
    map.put(Integer.valueOf(1), (List<SysDataPartyBandit>)partyBanditList.stream().filter(e -> (e.getLevel() == 1)).collect(Collectors.toList()));
    map.put(Integer.valueOf(2), (List<SysDataPartyBandit>)partyBanditList.stream().filter(e -> (e.getLevel() == 2)).collect(Collectors.toList()));
    map.put(Integer.valueOf(3), (List<SysDataPartyBandit>)partyBanditList.stream().filter(e -> (e.getLevel() == 3)).collect(Collectors.toList()));
    map.put(Integer.valueOf(4), (List<SysDataPartyBandit>)partyBanditList.stream().filter(e -> (e.getLevel() == 4)).collect(Collectors.toList()));
    return map;
  }
  
  public List<SysDataPartyBandit> getCreationBandit(Map<Integer, List<SysDataPartyBandit>> banditGradingMap, Party party) {
    List<SysDataPartyBandit> list = new ArrayList<>(15);
    short onlineCount = this.partyService.getPartyOnlineCount(party);
    int count = BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf(onlineCount * 0.75D * party.getActive() / 100.0D + party.getLevel() * 0.5D));
    if (count > 15)
      count = 15; 
    if (count >= 8) {
      List<SysDataPartyBandit> collect = banditGradingMap.get(Integer.valueOf(4));
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
      list.add(collect.get(index));
      collect = banditGradingMap.get(Integer.valueOf(3));
      int i;
      for (i = 0; i < 2; i++) {
        index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
        list.add(collect.get(index));
      } 
      collect = banditGradingMap.get(Integer.valueOf(2));
      for (i = 0; i < 2; i++) {
        index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
        list.add(collect.get(index));
      } 
      collect = banditGradingMap.get(Integer.valueOf(1));
      for (i = 0; i < 3; i++) {
        index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
        list.add(collect.get(index));
      } 
    } else if (count >= 6) {
      List<SysDataPartyBandit> collect = banditGradingMap.get(Integer.valueOf(3));
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
      list.add(collect.get(index));
      collect = banditGradingMap.get(Integer.valueOf(2));
      int i;
      for (i = 0; i < 2; i++) {
        index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
        list.add(collect.get(index));
      } 
      collect = banditGradingMap.get(Integer.valueOf(4));
      for (i = 0; i < 3; i++) {
        index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
        list.add(collect.get(index));
      } 
    } else {
      count = 0;
    } 
    count -= list.size();
    if (count > 0)
      for (int i = 0; i < count; i++) {
        List<SysDataPartyBandit> collect = banditGradingMap.get(Integer.valueOf(BANDIT[RandomUtil.round(Integer.valueOf(0), Integer.valueOf(BANDIT.length - 1))]));
        if (collect != null && collect.size() > 0)
          list.add(collect.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1)))); 
      }  
    return list;
  }
  
  public String getNpcContent(Role role, Npc npc) {
    setContent(npc, "俺们老大说了：给你们20分钟时间，如果能把俺们兄弟都打到，就饶了你们帮派不抢。出手吧#<E:32>#");
    return "为帮除害/threats_p_bandit|再见/离开";
  }
  
  public String getNpcContentNotFight(Role role, Npc npc) {
    if (npc.isInFight()) {
      setContent(npc, "我正在战斗中，等下再来收拾你#<E:32>#");
      sendNpcContent(role, npc, "再见/离开");
      return "再见/离开";
    } 
    Party party = this.partyService.getParty(role);
    if (party == null)
      return ""; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team == null || this.teamService.getTeamCount(team) < 3) {
      npc.setTalk("边凉快去，不够3人的队伍，大爷懒得出手#<E:0>#");
      return "";
    } 
    String names = this.teamService.checkMember(team, memberRole -> !DataGlobalFunc.isTheSameParty(role.getFaction().intValue(), memberRole.getFaction().intValue()));
    if (names != null) {
      npc.setTalk(MessageFormat.format("{0}不是本帮成员哦！#<E:109>#", new Object[] { "#<LB1>" + names + "#" }));
      return "";
    } 
    int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    names = this.teamService.checkMember(team, memberRole -> !isJoinPartyTime24((PartyMember)party.getMembers().get(memberRole.getRoleId()), time));
    if (names != null) {
      npc.setTalk(MessageFormat.format("{0}进入帮派小于24小时，也敢来多管闲事！#<E:109>#", new Object[] { "#<LB1>" + names + "#" }));
      return "";
    } 
    return null;
  }
  
  public boolean isJoinPartyTime24(PartyMember member, int time) {
    if (member == null || time <= 0)
      return false; 
    int day = BigDecimalUtil.decimalScaleBig(Integer.valueOf(0), Integer.valueOf((time - member.getJoinTime()) / 60 / 60 / 24)).intValue();
    if (day < 1)
      return false; 
    return true;
  }
  
  public void setContent(Npc npc, String talk) {
    SysDataPartyBandit obj = TaskDataPool.partyBanditList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
    if (obj != null)
      npc.setTalk(MessageFormat.format(talk, new Object[] { Integer.valueOf(obj.getLevel()), Integer.valueOf(obj.getGrade()), obj.getName() })); 
  }
  
  public void doStartFight(Role role, Npc npc) {
    SysDataPartyBandit obj = TaskDataPool.partyBanditList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
    if (obj != null) {
      Random random = new Random();
      int index = random.nextInt(obj.getWar().size());
      SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(obj.getWar().get(index));
      if (warRole != null) {
        List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
        if (CollectionUtils.isEmpty(monsters)) {
          this.logger.error("帮派强盗进入战斗错误1");
          setNotInFight(npc);
          return;
        } 
        Integer monsterLevel = Collections.<Integer>max(this.teamService.getMemberLevel(role));
        monsters.stream().forEach(monster -> {
              if (monster.getPos() == 10103 || monster.getPos() == 10003) {
                monster.setName(npc.getName());
              } else {
                monster.setName(DataGlobalFunc.dynamicName(monster.getName()));
              } 
              monster.setMonsterType(FIGHT_TYPE.PARTY_BANDIT.getValue());
              DataGlobalFunc.createMonster(monsterLevel.intValue(), monster);
            });
        BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.PARTY_BANDIT.getValue(), 0, npc.getId());
        Fight fight = this.bossService.startFightToBoss(role, bossFightParam);
        if (fight == null)
          setNotInFight(npc); 
      } else {
        this.logger.error("帮派强盗进入战斗错误2");
        setNotInFight(npc);
      } 
    } else {
      this.logger.error("帮派强盗进入战斗错误3");
      setNotInFight(npc);
    } 
  }
  
  protected Npc getBossNpc(int npcId) {
    return (Npc)DataCache.GROUP_NPC.get(Integer.valueOf(npcId));
  }
  
  public void doFightWinForSingle(Role role, FightEndEvent fightEndEvent, Npc npc) {
    super.doFightWinForSingle(role, fightEndEvent, npc);
    SpringUtils.getActivityService().addFinishCount(role, ActivityType.FACTION_BANDIT, 1);
  }
  
  protected void clearNpcAfterWin(Role role, FightEndEvent fightEndEvent, Npc bossNpc) {
    clearNpcAfterFightWin(bossNpc, fightEndEvent);
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    super.doFightFail(role, fightEndEvent);
  }
  
  public void giveReward(Role role, FightEndEvent fightEndEvent, Npc npc) {
    SysDataPartyBandit obj = TaskDataPool.partyBanditList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
    if (obj != null) {
      if (SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.FACTION_BANDIT)) {
        this.roleService.addExp(role, calculate(role.getLevel().intValue(), role.getRelive().intValue(), npc.getLevel(), ItemClassify.HERO_EXP), "帮派强盗", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet.intValue() > 0) {
          Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
          if (pet != null)
            this.petService.addExp(role, pet, calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), npc.getLevel(), ItemClassify.PET_EXP), true); 
        } 
        ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, obj.getAward(), "帮派强盗", obj.getLevel());
      } else {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("帮派强盗次数已满，无法获得奖励"));
      } 
    } else {
      this.logger.error("获取帮派强盗奖励失败：{}", Integer.valueOf(npc.getNs()));
    } 
  }
  
  private void clearNpcAfterFightWin(Npc npc, FightEndEvent fightEndEvent) {
    clearNpc(npc);
    Party party = this.partyService.getParty(Integer.valueOf(npc.getVisible()));
    if (party != null) {
      int num = party.getNutMap().getInt("bandit", 0);
      List<Role> rolesA = fightEndEvent.getRolesA();
      num--;
      party.getNutMap().setv("bandit", Integer.valueOf(num));
      String msg = "现在还有" + num + "队强盗没有被消灭";
      if (num <= 0) {
        msg = "入侵帮派的强盗已经全部被消灭#<E:90>#。";
        ScheduledFuture<?> task = party.getSchedule();
        if (task != null && !task.isDone())
          task.cancel(Boolean.FALSE.booleanValue()); 
        int bot = num + (int)(this.partyService.getPartyMaxMembers(party.getLevel()) * 2.25D);
        this.partyService.addConstruction(party, bot);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < rolesA.size(); i++) {
        Role role = rolesA.get(i);
        if (stringBuilder.length() > 0)
          stringBuilder.append("、"); 
        stringBuilder.append(role.getName());
      } 
      this.partyService.npcPartyChat(party, MessageFormat.format("{0}的队伍三拳两脚就消灭了一队强盗#<E:81>#，{1}", new Object[] { "#<LB1>" + stringBuilder.toString() + "#", msg }));
    } 
  }
  
  private void clearNpcTimeOut(GroupMapParam groupMapParam, Npc npc) {
    if (npc == null)
      return; 
    if (npc.isInFight()) {
      this.bossService.broadcastNpcHide(null, npc);
    } else {
      clearNpc(npc);
      this.mapService.removeGroupMapNpc(groupMapParam, npc);
    } 
  }
  
  private void clearNpc(Npc npc) {
    if (npc == null)
      return; 
    this.mapService.removeGroupMapNpc(new GroupMapParam(Integer.valueOf(npc.getVisible())), npc);
    removeUsedPosition(npc.getMap(), npc.getX(), npc.getY());
    this.bossService.broadcastNpcHide(new GroupMapParam(Integer.valueOf(npc.getVisible())), npc);
  }
  
  protected void clearNpcTimeOut(Npc npc) {
    clearNpcTimeOut(npc);
  }
  
  private static long calculate(int grade, int rei, int cnt, ItemClassify type) {
    if (type == ItemClassify.HERO_EXP)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.9E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 27.13D * (0.2D + 0.1D * cnt + Math.pow(cnt, 0.45D)) * 1.5D)).longValue(); 
    if (type == ItemClassify.PET_EXP)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.9E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 27.13D * (0.2D + 0.1D * cnt + Math.pow(cnt, 0.45D)) * 1.5D * 1.2D)).longValue(); 
    return 0L;
  }
}
