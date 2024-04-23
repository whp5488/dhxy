package com.kitty.system.game.activity.handler.timing.wars;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.model.Pos;
import com.kitty.common.start.ServerService;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.ArraysUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.DateUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.text.Convert;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.system.game.activity.message.RespGangWarsIcon;
import com.kitty.system.game.activity.message.RespGangWarsInfo;
import com.kitty.system.game.activity.message.RespGangWarsMatchInfo;
import com.kitty.system.game.activity.message.RespGangWarsRoleState;
import com.kitty.system.game.activity.message.RespOffRoleState;
import com.kitty.system.game.activity.model.user.wars.GangWarsData;
import com.kitty.system.game.activity.model.user.wars.GangWarsPkData;
import com.kitty.system.game.activity.model.user.wars.GangWarsSet;
import com.kitty.system.game.activity.model.user.wars.vo.Equipment;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsInfo;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsTimeSlot;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.npc.message.RespNpcContent;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.model.PartyWarsRecord;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.task.message.RespTaskIcon;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GangWarsHandler extends ActivityHandler {
  @Autowired
  IdWorker worker;
  
  @Autowired
  protected RoleService roleService;
  
  @Autowired
  protected PetService petService;
  
  @Autowired
  protected TeamService teamService;
  
  @Autowired
  PartyService partyService;
  
  @Autowired
  ChatService chatService;
  
  @Autowired
  BagService bagService;
  
  @Autowired
  NpcService npcService;
  
  private GangWarsSet objSet;
  
  private GangWarsData objData;
  
  @Autowired
  ServerService serverService;
  
  public GangWarsSet getObjSet() {
    return this.objSet;
  }
  
  public GangWarsData getObjData() {
    return this.objData;
  }
  
  public void setObjData(GangWarsData objData) {
    this.objData = objData;
  }
  
  public byte round = 1;
  
  public static final int MAP = 3001;
  
  public static final int MAP_X_A = 195;
  
  public static final int MAP_Y_A = 109;
  
  public static final int FIGHT_MAP_X_A = 160;
  
  public static final int FIGHT_MAP_Y_A = 80;
  
  public static final int MAP_X_B = 24;
  
  public static final int MAP_Y_B = 19;
  
  public static final int FIGHT_MAP_X_B = 49;
  
  public static final int FIGHT_MAP_Y_B = 40;
  
  private static final int EXIT_MAP = 1011;
  
  private static final int EXIT_X = 234;
  
  private static final int EXIT_Y = 16;
  
  private static final int GROUP_NUM = 8;
  
  private ScheduledFuture scheduledFuture;
  
  private static final String ACTIVITY_NAME = "帮派大战";
  
  private static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd-HH:mm:ss";
  
  private static final String FIGHT_LEVEL_NOT_ENOUGH = "等级达到&d级方可参加帮派大战。";
  
  private static final String REBIRTH_LIMIT_TIPS = "因刚刚复活，需要等待#<R>%s#才可以重新进入战场";
  
  private static final String CLASH = "#<LZ>%s##<EP>对阵##<LZ>%s#";
  
  private static final String WIN_TIPS = "#<EP>敌方城门耐久降为%d，我方在帮派大战中战胜##<LZ>%s##<EP>帮派##<E:86>#";
  
  private static final String FAIL_TIPS = "#<EP>我方城门耐久降为%d，我方在帮派大战中被##<LZ>%s##<EP>帮派击败##<E:86>#";
  
  private static final String DUEL_ENT_TIPS = "#<EP>帮派大战%s第%d轮，##<LZ>%s##<EP>战胜##<LZ>%s#";
  
  private static final String COMPETE_PK_LIMIT = "你现在不能与人切磋。";
  
  public void prepareOnce(GangWarsTimeSlot timeSlot) {
    initActivityData(timeSlot);
    addCheckJob();
    addStartJob();
    addEndJob();
    addBroadcastJob();
    initMatchingParty();
  }
  
  private void initActivityData(GangWarsTimeSlot timeSlot) {
    GangWarsData objData = new GangWarsData();
    initTime(objData, timeSlot.getSignUpTime(), timeSlot.getStartTime());
    this.objData = objData;
  }
  
  public void initTime(GangWarsData objData, String signUpTime, String startTime) {
    Calendar calendarStart = Calendar.getInstance();
    calendarStart.setTime(new Date());
    GangWarsSet objSet = getObjSet();
    String[] signUp = signUpTime.split(":");
    calendarStart.set(11, Integer.valueOf(signUp[0]).intValue());
    calendarStart.set(12, Integer.valueOf(signUp[1]).intValue());
    calendarStart.set(13, 0);
    objData.setSignUpTime(EventsUtils.getTime(calendarStart));
    String[] start = startTime.split(":");
    calendarStart.set(11, Integer.valueOf(start[0]).intValue());
    calendarStart.set(12, Integer.valueOf(start[1]).intValue());
    calendarStart.set(13, 0);
    objData.setStartTime(EventsUtils.getTime(calendarStart));
    calendarStart.add(12, objSet.getSustain());
    objData.setEndTime(EventsUtils.getTime(calendarStart));
  }
  
  private void addCheckJob() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this.objData.getStartTime().getTime());
    calendar.add(11, -1);
    long initialDelay = this.objData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
    this.logger.error("添加定时检测的定时任务==={}", Long.valueOf(initialDelay));
    try {
      if (this.scheduledFuture != null)
        this.scheduledFuture.cancel(false); 
    } catch (Exception e) {
      this.logger.error("定时器关闭==", e);
    } 
  }
  
  private void addStartJob() {
    long initialDelay = this.objData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
    this.logger.error("添加帮派大战开始的定时任务==={}", Long.valueOf(initialDelay));
    SchedulerManager.getInstance().schedule(() -> ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).start(), initialDelay);
  }
  
  private void addEndJob() {
    long initialDelay = this.objData.getEndTime().getTimeInMillis() - System.currentTimeMillis();
    this.logger.error("添加帮派大战结束的定时任务==={}", Long.valueOf(initialDelay));
    SchedulerManager.getInstance().schedule(() -> end(), initialDelay);
  }
  
  private void addBroadcastJob() {
    long initialDelay = this.objData.getSignUpTime().getTimeInMillis() - System.currentTimeMillis();
    this.logger.error("添加帮派大战播报的定时任务==={}", Long.valueOf(initialDelay));
    SchedulerManager.getInstance().schedule(() -> broadcast(), initialDelay);
  }
  
  public void broadcast() {
    for (String string : this.objData.getAnnounce())
      this.chatService.sendSystem(string, "role"); 
    for (Integer key : this.objData.getWarsMap().keySet()) {
      Party party = this.partyService.getParty(key);
      if (party != null) {
        GangWarsInfo warsInfo = (GangWarsInfo)this.objData.getWarsMap().get(key);
        this.partyService.npcPartyChat(party, I18nIdDataPool.getI18nContent(368, new Object[] { getBye(warsInfo.getGrade()) }));
      } 
    } 
    for (Integer key : this.objData.getByeMap().keySet()) {
      Party party = this.partyService.getParty(key);
      if (party != null)
        this.partyService.npcPartyChat(party, I18nIdDataPool.getI18nContent(369, new Object[] { getBye(((Integer)this.objData.getByeMap().get(key)).intValue()) })); 
    } 
  }
  
  private void initMatchingParty() {
    List<Integer> gradeA = new ArrayList<>();
    List<Integer> gradeB = new ArrayList<>();
    List<Integer> gradeC = new ArrayList<>();
    List<Integer> gradeD = new ArrayList<>();
    List<Integer> gradeE = new ArrayList<>();
    Map<Integer, Party> partyMap = PartyDataPool.partyMap;
    List<Party> list = (List<Party>)partyMap.values().stream().sorted(Comparator.comparing(Party::getRating).thenComparing(Party::getLevel, Comparator.reverseOrder()).thenComparing(Party::getActive, Comparator.reverseOrder()).thenComparing(Party::getMemberNum, Comparator.reverseOrder())).collect(Collectors.toList());
    if (list.size() > 32)
      list = list.subList(0, 32); 
    for (int i = 0; i < list.size(); i++) {
      Party party = list.get(i);
      if (gradeA.size() < 8) {
        listAdd(gradeA, gradeB, party.getSerial().intValue());
      } else if (gradeB.size() < 8) {
        listAdd(gradeB, gradeC, party.getSerial().intValue());
      } else if (gradeC.size() < 8) {
        listAdd(gradeC, gradeD, party.getSerial().intValue());
      } else if (gradeD.size() < 8) {
        gradeD.add(party.getSerial());
      } else {
        gradeE.add(party.getSerial());
      } 
    } 
    if (gradeA.size() < 8)
      substitution(gradeA, gradeB); 
    if (gradeB.size() < 8)
      substitution(gradeB, gradeC); 
    if (gradeC.size() < 8)
      substitution(gradeC, gradeD); 
    if (gradeC.size() >= 8 && gradeD.size() < 8)
      substitution(gradeD, gradeE); 
    if (gradeA.size() > 0)
      match(gradeA, 1); 
    if (gradeB.size() > 0)
      match(gradeB, 2); 
    if (gradeC.size() > 0)
      match(gradeC, 3); 
    if (gradeD.size() > 0)
      match(gradeD, 4); 
  }
  
  public String getBye(int bye) {
    String name = null;
    switch (bye) {
      case 1:
        name = "甲级";
        return name;
      case 2:
        name = "已级";
        return name;
      case 3:
        name = "丙级";
        return name;
      case 4:
        name = "丁级";
        return name;
    } 
    name = "丁级";
    return name;
  }
  
  public void listAdd(List<Integer> listA, List<Integer> listB, int serial) {
    if (listA.size() < 6) {
      listA.add(Integer.valueOf(serial));
    } else if (listB.size() < 2) {
      listB.add(Integer.valueOf(serial));
    } else {
      listA.add(Integer.valueOf(serial));
    } 
  }
  
  public void substitution(List<Integer> listA, List<Integer> listB) {
    int num = 8 - listA.size();
    for (int i = 0; i < num; i++) {
      if (listB.size() > 0) {
        listA.add(listB.get(0));
        listB.remove(0);
      } 
    } 
  }
  
  public void match(List<Integer> list, int bye) {
    String byeString = getBye(bye);
    StringBuilder stringBuilder = new StringBuilder();
    List<Integer> objects = new ArrayList<>();
    Map<Integer, Party> partyMap = PartyDataPool.partyMap;
    for (int i = 0; i < list.size(); i++) {
      int key = ((Integer)list.get(i)).intValue();
      if (!objects.contains(Integer.valueOf(key))) {
        Party sideParty = partyMap.get(Integer.valueOf(key));
        if (sideParty != null) {
          objects.add(sideParty.getSerial());
          List<Integer> notMatchList = new ArrayList<>(ArraysUtil.getDifferListByMapPlus(list, objects));
          if (notMatchList.size() <= 0) {
            this.objData.getByeMap().put(sideParty.getSerial(), Integer.valueOf(bye));
          } else {
            List<Integer> enemySerialList = sideParty.getWarsEnemySerial();
            List<Integer> canMatchList = new ArrayList<>(ArraysUtil.getDislodgeReDo(notMatchList, enemySerialList));
            if (canMatchList.size() <= 0)
              canMatchList = notMatchList; 
            Integer serial = canMatchList.get(0);
            Party enemyParty = partyMap.get(serial);
            if (enemyParty == null) {
              this.objData.getByeMap().put(sideParty.getSerial(), Integer.valueOf(bye));
            } else {
              objects.add(enemyParty.getSerial());
              warsRecord(sideParty, enemyParty, bye);
              if (i == 0) {
                stringBuilder.append(byeString + "帮战第" + Convert.getChineseFromArabic(Integer.valueOf(this.round)) + "轮名单:");
              } else {
                stringBuilder.append("、");
              } 
              stringBuilder.append(String.format("#<LZ>%s##<EP>对阵##<LZ>%s#", new Object[] { sideParty.getName(), enemyParty.getName() }));
            } 
          } 
        } 
      } 
    } 
    if (stringBuilder.length() > 0)
      this.objData.getAnnounce().add(stringBuilder.toString()); 
  }
  
  public void warsRecord(Party sideParty, Party enemyParty, int bye) {
    int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    PartyWarsRecord sideRecord = new PartyWarsRecord(Long.valueOf(this.worker.nextId()), sideParty.getSerial().intValue(), time, (byte)3, enemyParty.getSerial().intValue(), enemyParty.getName(), 0, 0);
    sideParty.getWarsRecordList().add(sideRecord);
    sideRecord.save();
    PartyWarsRecord enemyRecord = new PartyWarsRecord(Long.valueOf(this.worker.nextId()), enemyParty.getSerial().intValue(), time, (byte)3, sideParty.getSerial().intValue(), sideParty.getName(), 0, 0);
    enemyParty.getWarsRecordList().add(enemyRecord);
    enemyRecord.save();
    Integer visible = sideParty.getSerial();
    this.objData.getPkData().put(sideParty.getSerial(), new HashMap<>(sideParty.getMembers().size()));
    this.objData.getPkData().put(enemyParty.getSerial(), new HashMap<>(enemyParty.getMembers().size()));
    this.objData.getWarsMap().put(sideParty.getSerial(), new GangWarsInfo((byte)0, visible.intValue(), bye, enemyParty.getSerial().intValue()));
    this.objData.getWarsMap().put(enemyParty.getSerial(), new GangWarsInfo((byte)1, visible.intValue(), bye, sideParty.getSerial().intValue()));
  }
  
  private void end() {
    if (this.scheduledFuture != null)
      this.scheduledFuture.cancel(Boolean.FALSE.booleanValue()); 
    this.objData.setStage((byte)2);
    for (Iterator<?> set = this.objData.getWarsMap().keySet().iterator(); set.hasNext();) {
      int key = (Integer) set.next();
      GangWarsInfo side = (GangWarsInfo)this.objData.getWarsMap().get(key);
      if (side.getCombatResults() == 3) {
        int win = key;
        GangWarsInfo enemy = (GangWarsInfo)this.objData.getWarsMap().get(side.getEnemy());
        if (enemy.getGateLife() > side.getGateLife()) {
          win = side.getEnemy();
        } else {
          int round = RandomUtil.round(1, 100);
          if (round > 50)
            win = side.getEnemy(); 
        } 
        if (win == key) {
          side.setCombatResults((byte)1);
          enemy.setCombatResults((byte)0);
        } else {
          enemy.setCombatResults((byte)1);
          side.setCombatResults((byte)0);
        } 
        end(key, side, enemy);
      } 
    } 
    for (Equipment value : this.objData.getCannonMap().values()) {
      if (value.getScheduledFuture() != null)
        value.getScheduledFuture().cancel(Boolean.FALSE.booleanValue()); 
    } 
    for (Equipment value : this.objData.getGateMap().values()) {
      if (value.getScheduledFuture() != null)
        value.getScheduledFuture().cancel(Boolean.FALSE.booleanValue()); 
    } 
    this.objData = null;
    this.round = (byte)(this.round + 1);
    String day = DateUtils.dayForWeek(new Date());
    GangWarsTimeSlot timeSlot = this.objSet.getDayOfWeek().stream().filter(e -> (e.getDayOfWeek().equals(day) && e.getRound() == this.round)).findFirst().orElse(null);
    if (timeSlot != null)
      notifyAt(timeSlot.getRound()); 
  }
  
  public void end(int serial, GangWarsInfo side, GangWarsInfo enemy) {
    updatePartyWarsRecord(serial, side.getCombatResults());
    updatePartyWarsRecord(side.getEnemy(), enemy.getCombatResults());
    sendResult(serial, side.getCombatResults());
    sendResult(side.getEnemy(), enemy.getCombatResults());
    clearTimer(serial, side.getVisible(), side.getEnemy());
    Party sideParty = this.partyService.getParty(Integer.valueOf(serial));
    Party enemyParty = this.partyService.getParty(Integer.valueOf(side.getEnemy()));
    if (sideParty != null && enemyParty != null) {
      int sideNum = 10;
      Map<Long, GangWarsPkData> sideDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(Integer.valueOf(serial));
      if (sideDataMap != null || sideDataMap.size() > sideNum)
        sideNum = sideDataMap.size(); 
      long construction = getConstruction(sideParty.getLevel(), sideNum, side.getCombatResults());
      this.partyService.addConstruction(sideParty, construction);
      this.partyService.npcPartyChat(sideParty, String.format("参战奖励：帮派%s%d", new Object[] { (sideParty.getLevel() < 4) ? "建设" : "资金", Long.valueOf(construction) }));
      this.partyService.npcPartyChat(sideParty, String.format("#<EP>敌方城门耐久降为%d，我方在帮派大战中战胜##<LZ>%s##<EP>帮派##<E:86>#", new Object[] { Short.valueOf(enemy.getGateLife()), enemyParty.getName() }));
      int enemyNum = 10;
      Map<Long, GangWarsPkData> enemyDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(Integer.valueOf(side.getEnemy()));
      if (enemyDataMap != null || enemyDataMap.size() > enemyNum)
        enemyNum = enemyDataMap.size(); 
      long enemyConstruction = getConstruction(enemyParty.getLevel(), enemyNum, enemy.getCombatResults());
      this.partyService.addConstruction(enemyParty, enemyConstruction);
      this.partyService.npcPartyChat(enemyParty, String.format("参战奖励：帮派%s%d", new Object[] { (enemyParty.getLevel() < 4) ? "建设" : "资金", Long.valueOf(enemyConstruction) }));
      this.partyService.npcPartyChat(enemyParty, String.format("#<EP>我方城门耐久降为%d，我方在帮派大战中被##<LZ>%s##<EP>帮派击败##<E:86>#", new Object[] { Short.valueOf(enemy.getGateLife()), sideParty.getName() }));
      this.chatService.sendSystem(String.format("#<EP>帮派大战%s第%d轮，##<LZ>%s##<EP>战胜##<LZ>%s#", new Object[] { getBye(side.getGrade()), Byte.valueOf(this.round), sideParty.getName(), enemyParty.getName() }), "role");
    } 
  }
  
  public long getConstruction(int cnt, int factor, byte isWin) {
    double coefficient = 9.3889D;
    if (isWin == 0)
      coefficient = 5.3889D; 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(500.0D + (6.1228D * cnt + factor * coefficient) * factor * 0.0866D)).longValue();
  }
  
  public void clearTimer(int side, int visible, int enemy) {
    Equipment cannon = (Equipment)this.objData.getCannonMap().get(Integer.valueOf(visible));
    if (cannon != null) {
      if (cannon.getScheduledFuture() != null)
        cannon.getScheduledFuture().cancel(Boolean.FALSE.booleanValue()); 
      this.objData.getCannonMap().remove(Integer.valueOf(visible));
    } 
    Equipment sideGate = (Equipment)this.objData.getGateMap().get(Integer.valueOf(side));
    if (sideGate != null) {
      if (sideGate.getScheduledFuture() != null)
        sideGate.getScheduledFuture().cancel(Boolean.FALSE.booleanValue()); 
      this.objData.getGateMap().remove(Integer.valueOf(side));
    } 
    Equipment enemyGate = (Equipment)this.objData.getGateMap().get(Integer.valueOf(enemy));
    if (enemyGate != null) {
      if (enemyGate.getScheduledFuture() != null)
        enemyGate.getScheduledFuture().cancel(Boolean.FALSE.booleanValue()); 
      this.objData.getGateMap().remove(Integer.valueOf(enemy));
    } 
  }
  
  public void sendResult(int serial, byte result) {
    GangWarsInfo sideWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(Integer.valueOf(serial));
    if (sideWarsInfo == null)
      return; 
    GangWarsInfo enemyWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(Integer.valueOf(sideWarsInfo.getEnemy()));
    Map<Long, GangWarsPkData> enemyPkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(Integer.valueOf(sideWarsInfo.getEnemy()));
    Party enemyParty = this.partyService.getParty(Integer.valueOf(sideWarsInfo.getEnemy()));
    Map<Long, GangWarsPkData> sidePkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(Integer.valueOf(serial));
    Party sideParty = this.partyService.getParty(Integer.valueOf(serial));
    Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(Integer.valueOf(serial));
    RespGangWarsMatchInfo matchInfo = getGangWarsMatchInfo(sideParty, sideWarsInfo, sidePkDataMap, enemyParty, enemyWarsInfo, enemyPkDataMap);
    matchInfo.setType(result);
    for (Long id : warsPkDataMap.keySet()) {
      GangWarsPkData data = warsPkDataMap.get(id);
      if (data.getPosition() == 3)
        continue; 
      Role player = this.roleService.getPlayer(id.longValue());
      if (player != null) {
        leave(player);
        Team team = this.teamService.getTeam(player.getRoleId().intValue());
        if (team != null) {
          Member member = team.getList().stream().filter(e -> e.getId().equals(player.getId())).findFirst().orElse(null);
          if (member != null && !member.isInTeam()) {
            MessagePusher.pushMessage(id.longValue(), (Message)matchInfo);
            continue;
          } 
          this.teamService.memberHandleThreadLocal(team, memberRole -> MessagePusher.pushMessage(memberRole.getId().longValue(), (Message)matchInfo));
          continue;
        } 
        MessagePusher.pushMessage(id.longValue(), (Message)matchInfo);
      } 
    } 
    byte level = sideParty.getLevel();
    int num = sidePkDataMap.size();
    for (Long id : warsPkDataMap.keySet()) {
      Role player = this.roleService.getPlayer(id.longValue());
      award(player, level, sideWarsInfo.getCombatResults(), num);
    } 
  }
  
  public void award(Role role, byte level, byte combatResults, int num) {
    if (role != null) {
      this.roleService.addExp(role, calculate(role.getLevel().intValue(), role.getRelive().intValue(), level, num, ItemClassify.HERO_EXP, combatResults), "帮战奖励", true);
      Integer combatPet = role.getExtendBox().getCombatPet();
      if (combatPet.intValue() > 0) {
        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
        this.petService.addExp(role, pet, calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), level, num, ItemClassify.PET_EXP, combatResults), true);
      } 
      this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, Long.valueOf(calculate(role.getLevel().intValue(), role.getRelive().intValue(), level, num, ItemClassify.RESTYPE_TRIBUTE, combatResults)), "帮战奖励", true, false);
    } 
  }
  
  public void updatePartyWarsRecord(int serial, byte results) {
    Party party = this.partyService.getParty(Integer.valueOf(serial));
    if (party != null) {
      PartyWarsRecord warsRecord = party.getWarsRecordList().stream().filter(e -> (e.getCombatResults() == 3)).findFirst().orElse(null);
      if (warsRecord != null) {
        warsRecord.setCombatResults(results);
        warsRecord.save();
      } 
    } 
  }
  
  public void enterMap(Role role, Npc npc) {
    String talk = canEnter(role);
    if (talk != null) {
      if (!talk.isEmpty())
        ((NpcService)SpringUtils.getBean(NpcService.class)).sendNpcContent(role, npc, "", talk); 
    } else {
      doEnterMap(role);
    } 
  }
  
  public String canEnter(Role role) {
    if (role.getFaction().intValue() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你当前没有帮派"));
      return "";
    } 
    GangWarsData objData = getObjData();
    if (objData == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你们帮目前没有帮战"));
      return "";
    } 
    Calendar calendar = Calendar.getInstance();
    if (calendar.before(objData.getSignUpTime())) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你们帮目前没有帮战"));
      return "";
    } 
    if (objData.getByeMap().get(role.getFaction()) != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("本帮已经轮空获胜，无需进场"));
      return "";
    } 
    GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
    if (warsInfo == null || warsInfo.getGateLife() <= 0 || warsInfo.getCombatResults() != 3) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你们帮目前没有帮战"));
      return "";
    } 
    if (DataGlobalFunc.needGradeJudge(0, this.objSet.getMinLevel(), role.getRelive().intValue(), role.getLevel().intValue(), "") != null)
      return String.format("等级达到&d级方可参加帮派大战。", new Object[] { Integer.valueOf(this.objSet.getMinLevel()) }); 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
      if (member != null && !member.isInTeam())
        return null; 
      for (int i = 0; i < team.getList().size(); i++) {
        member = team.getList().get(i);
        if (member.isInTeam()) {
          Role player = this.roleService.getPlayer(member.getId().longValue());
          if (player != null && !player.getFaction().equals(role.getFaction()))
            this.teamService.leaveTemp(player, true); 
        } 
      } 
    } 
    return null;
  }
  
  public void doEnterMap(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    GangWarsInfo warsInfo = (GangWarsInfo)this.objData.getWarsMap().get(role.getFaction());
    if (warsInfo == null)
      return; 
    int x = 195;
    int y = 109;
    if (warsInfo.getStance() == 1) {
      x = 24;
      y = 19;
    } 
    enter(role, team, new ReqTelePort(3001, x, y));
  }
  
  public void enter(Role role, Team team, ReqTelePort reqTelePort) {
    Map<Long, GangWarsPkData> pkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
    if (pkDataMap == null)
      return; 
    GangWarsInfo warsInfo = (GangWarsInfo)this.objData.getWarsMap().get(role.getFaction());
    if (warsInfo == null)
      return; 
    if (team != null) {
      Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
      if (member != null && member.isInTeam()) {
        this.teamService.memberHandle(team, memberRole -> enterUpData(memberRole, pkDataMap));
      } else {
        enterUpData(role, pkDataMap);
      } 
    } else {
      enterUpData(role, pkDataMap);
    } 
    if (reqTelePort != null) {
      if (team != null) {
        Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
        if (member != null && member.isInTeam()) {
          this.teamService.memberHandle(team, memberRole -> memberRole.setVisible(warsInfo.getVisible()));
          this.teamService.memberHandle(team, memberRole -> sendRespIcon(memberRole.getId().longValue(), memberRole.getFaction().intValue()));
        } else {
          role.setVisible(warsInfo.getVisible());
          sendRespIcon(role.getId().longValue(), role.getFaction().intValue());
        } 
      } else {
        role.setVisible(warsInfo.getVisible());
        sendRespIcon(role.getId().longValue(), role.getFaction().intValue());
      } 
      SpringUtils.getMapService().changeMap(role, reqTelePort, null);
    } 
  }
  
  public void enterUpData(Role role, Map<Long, GangWarsPkData> pkDataMap) {
    GangWarsPkData data = pkDataMap.get(role.getRoleId());
    if (data == null)
      pkDataMap.put(role.getId(), new GangWarsPkData()); 
  }
  
  public void sendRespIcon(long id, int faction) {
    if (this.objData != null)
      MessagePusher.pushMessage(id, (Message)getRespIcon(faction)); 
  }
  
  public RespGangWarsIcon getRespIcon(int faction) {
    GangWarsInfo sideWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(Integer.valueOf(faction));
    if (sideWarsInfo == null)
      return null; 
    RespGangWarsIcon respIcon = new RespGangWarsIcon();
    respIcon.setStance(sideWarsInfo.getStance());
    Party enemyParty = this.partyService.getParty(Integer.valueOf(sideWarsInfo.getEnemy()));
    Party sideParty = this.partyService.getParty(Integer.valueOf(faction));
    respIcon.setEnemyGrade(enemyParty.getLevel());
    respIcon.setEnemyName(enemyParty.getName());
    respIcon.setSideGrade(sideParty.getLevel());
    respIcon.setSideName(sideParty.getName());
    if (this.objData.getStage() == 1) {
      GangWarsInfo enemyWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(enemyParty.getSerial());
      respIcon.setEnemyLife(enemyWarsInfo.getGateLife());
      respIcon.setSideLife(sideWarsInfo.getGateLife());
      respIcon.setState((byte)1);
      respIcon.setTime((int)(this.objData.getStartTime().getTimeInMillis() / 1000L));
    } 
    return respIcon;
  }
  
  public void eventInfo(Role role) {
    if (this.objData == null || this.objData.getStage() == 2)
      return; 
    if (!isInMap(role))
      return; 
    GangWarsInfo sideWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(role.getFaction());
    if (sideWarsInfo == null)
      return; 
    GangWarsInfo enemyWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(Integer.valueOf(sideWarsInfo.getEnemy()));
    Map<Long, GangWarsPkData> enemyPkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(Integer.valueOf(sideWarsInfo.getEnemy()));
    Party enemyParty = this.partyService.getParty(Integer.valueOf(sideWarsInfo.getEnemy()));
    Map<Long, GangWarsPkData> sidePkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
    Party sideParty = this.partyService.getParty(role.getFaction());
    if (this.objData.getStage() == 1) {
      MessagePusher.pushMessage(role, (Message)getGangWarsMatchInfo(sideParty, sideWarsInfo, sidePkDataMap, enemyParty, enemyWarsInfo, enemyPkDataMap));
    } else {
      RespGangWarsInfo gangWarsInfo = new RespGangWarsInfo();
      gangWarsInfo.setEnemyName(enemyParty.getName());
      gangWarsInfo.setEnemyNum((short)enemyPkDataMap.size());
      gangWarsInfo.setSideName(sideParty.getName());
      gangWarsInfo.setSideNum((short)sidePkDataMap.size());
      MessagePusher.pushMessage(role, (Message)gangWarsInfo);
    } 
  }
  
  public RespGangWarsMatchInfo getGangWarsMatchInfo(Party sideParty, GangWarsInfo sideWarsInfo, Map<Long, GangWarsPkData> sidePkDataMap, Party enemyParty, GangWarsInfo enemyWarsInfo, Map<Long, GangWarsPkData> enemyPkDataMap) {
    RespGangWarsMatchInfo gangWarsMatchInfo = new RespGangWarsMatchInfo();
    NutConcurrentMap recordMap = this.objData.getRecordMap();
    Integer enemyPartySerial = enemyParty.getSerial();
    gangWarsMatchInfo.setEnemyName(enemyParty.getName());
    gangWarsMatchInfo.setEnemyNum((short)enemyPkDataMap.size());
    gangWarsMatchInfo.setEnemyGateLife(enemyWarsInfo.getGateLife());
    gangWarsMatchInfo.setSideWinRonda((short)recordMap.getInt(enemyPartySerial + "_win", 0));
    gangWarsMatchInfo.setSideContestWinRonda((short)recordMap.getInt(enemyPartySerial + "_contest", 0));
    gangWarsMatchInfo.setSidePylonRonda((short)recordMap.getInt(enemyPartySerial + "_pylon", 0));
    gangWarsMatchInfo.setSideCannonRonda((short)recordMap.getInt(enemyPartySerial + "_cannon", 0));
    Integer sidePartySerial = sideParty.getSerial();
    gangWarsMatchInfo.setSideName(sideParty.getName());
    gangWarsMatchInfo.setSideNum((short)sidePkDataMap.size());
    gangWarsMatchInfo.setSideGateLife(sideWarsInfo.getGateLife());
    gangWarsMatchInfo.setSideWinRonda((short)recordMap.getInt(sidePartySerial + "_win", 0));
    gangWarsMatchInfo.setSideContestWinRonda((short)recordMap.getInt(sidePartySerial + "_contest", 0));
    gangWarsMatchInfo.setSidePylonRonda((short)recordMap.getInt(sidePartySerial + "_pylon", 0));
    gangWarsMatchInfo.setSideCannonRonda((short)recordMap.getInt(sidePartySerial + "_cannon", 0));
    return gangWarsMatchInfo;
  }
  
  public void goToMap(Role role, Npc npc, String type) {
    if (this.objData == null) {
      SpringUtils.getMapService().changeMap(role, new ReqTelePort(1011, 234, 16), null);
      return;
    } 
    int x = 0, y = 0;
    byte position = 0;
    GangWarsInfo warsInfo = (GangWarsInfo)this.objData.getWarsMap().get(role.getFaction());
    if (warsInfo == null) {
      SpringUtils.getMapService().changeMap(role, new ReqTelePort(1011, 234, 16), null);
      return;
    } 
    Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
    if (warsPkDataMap == null)
      return; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    byte stance = warsInfo.getStance();
    if ("to_fight_wars".equals(type)) {
      int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
      if (team == null) {
        GangWarsPkData gangWarsPkData = warsPkDataMap.get(role.getId());
        if (gangWarsPkData.getRebirth() > time) {
          MessagePusher.pushMessage(role, (Message)this.npcService.getRespNpcContent(role, npc, "", String.format("因刚刚复活，需要等待#<R>%s#才可以重新进入战场", new Object[] { String.format("%s(%d秒)", new Object[] { role.getName(), Integer.valueOf(gangWarsPkData.getRebirth() - time) }) })));
          return;
        } 
        gangWarsPkData.setRebirth(0);
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < team.getList().size(); i++) {
          Member member = team.getList().get(i);
          if (member.isInTeam()) {
            GangWarsPkData gangWarsPkData = warsPkDataMap.get(role.getId());
            if (gangWarsPkData.getRebirth() > time) {
              Role player = this.roleService.getPlayer(member.getId().longValue());
              if (player != null) {
                if (stringBuilder.length() > 0)
                  stringBuilder.append("、"); 
                String format = String.format("%s(%d秒)", new Object[] { player.getName(), Integer.valueOf(gangWarsPkData.getRebirth() - time) });
                stringBuilder.append(format);
              } 
            } 
          } 
        } 
        if (stringBuilder.length() > 0) {
          RespNpcContent respNpcContent = this.npcService.getRespNpcContent(role, npc, "", String.format("因刚刚复活，需要等待#<R>%s#才可以重新进入战场", new Object[] { stringBuilder.toString() }));
          this.teamService.memberHandle(team, memberRole -> MessagePusher.pushMessage(memberRole, (Message)respNpcContent));
          return;
        } 
      } 
      if (stance == 1) {
        x = 49;
        y = 40;
      } else {
        x = 160;
        y = 80;
      } 
      position = 1;
    } else if ("to_home_wars".equals(type)) {
      if (stance == 1) {
        x = 24;
        y = 19;
      } else {
        x = 195;
        y = 109;
      } 
    } 
    if (x > 0 && y > 0) {
      if (team != null) {
        this.teamService.memberHandleThreadLocal(team, memberRole -> memberRole.pushTempCache("not_check_change_map", Boolean.valueOf(true)));
        byte finalPosition = position;
        this.teamService.memberHandleThreadLocal(team, memberRole -> updatePosition(role, finalPosition));
      } else {
        role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
        updatePosition(role, position);
      } 
      SpringUtils.getMapService().changeMap(role, new ReqTelePort(3001, x, y), null);
    } 
  }
  
  public void updatePosition(Role role, byte position) {
    if (this.objData != null) {
      Map<Long, GangWarsPkData> dataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
      if (dataMap != null) {
        GangWarsPkData gangWarsPkData = dataMap.get(role.getId());
        if (gangWarsPkData != null)
          gangWarsPkData.updatePosition(position); 
      } 
    } 
  }
  
  public void leave(Role role) {
    if (role != null) {
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team != null) {
        GangWarsInfo gangWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(role.getFaction());
        if (this.teamService.getTeamCount(team) > 1 && this.objData.getStage() != 2) {
          Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
          if (leaderRole != null)
            if (leaderRole.getId().equals(role.getId()))
              this.teamService.leave(role, false);  
          Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
          GangWarsPkData gangWarsPkData = warsPkDataMap.get(role.getId());
          gangWarsPkData.setPosition((byte)3);
          gangWarsPkData.setState((byte)0);
        } else if (this.objData.getStage() == 2 || gangWarsInfo.getCombatResults() < 3) {
          Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
          Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
          if (leaderRole != null && leaderRole.getId().equals(role.getId()))
            for (Member member : team.getList()) {
              if (member.isInTeam()) {
                Role player = this.roleService.getPlayer(member.getId().longValue());
                if (player != null) {
                  GangWarsPkData gangWarsPkData = warsPkDataMap.get(player.getId());
                  if (gangWarsPkData != null) {
                    gangWarsPkData.setPosition((byte)3);
                    gangWarsPkData.setState((byte)0);
                    player.pushTempCache("not_check_change_map", Boolean.valueOf(true));
                    MessagePusher.pushMessage(player, (Message)new RespTaskIcon(3003, "wars", (byte)0));
                  } 
                } 
                leaveMap(leaderRole);
                return;
              } 
            }  
        } 
      } else {
        Map<Long, GangWarsPkData> warsPkDataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
        if (warsPkDataMap != null) {
          GangWarsPkData gangWarsPkData = warsPkDataMap.get(role.getId());
          if (gangWarsPkData != null) {
            gangWarsPkData.setPosition((byte)3);
            gangWarsPkData.setState((byte)0);
          } 
        } 
      } 
      leaveMap(role);
    } 
  }
  
  public void leaveMap(Role role) {
    if (role == null)
      return; 
    if (!isInMap(role))
      return; 
    updatePosition(role, (byte)3);
    role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
    SpringUtils.getMapService().changeMap(role, new ReqTelePort(1011, 234, 16), null);
    RespTaskIcon respTaskIcon = new RespTaskIcon(3001, "wars", (byte)0);
    MessagePusher.pushMessage(role, (Message)respTaskIcon);
  }
  
  private static long calculate(int grade, int rei, int cnt, int factor, ItemClassify type, byte isWin) {
    double exp = 0.0D;
    if (ItemClassify.HERO_EXP == type)
      exp = factor * 0.0866D * 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * ((isWin == 1) ? 2.5D : 1.5D)) / 30.0D * 1.2D * (6.0E-4D * cnt + 0.83D); 
    if (ItemClassify.PET_EXP == type)
      exp = 2.4D * factor * 0.0866D * 1.6E7D * (1.0D + (grade + rei * 15) / 140.0D * ((isWin == 1) ? 2.5D : 1.5D)) / 30.0D * 1.2D * (6.0E-4D * cnt + 0.83D); 
    if (ItemClassify.RESTYPE_TRIBUTE == type)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(69.0D + (cnt + factor) * ((isWin == 1) ? 9.575D : 6.575D) * (6.0E-4D * cnt + 0.83D))).longValue() * 2L; 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(exp)).longValue();
  }
  
  public boolean isBackToTeam(Team team, Role memberRole) {
    Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
    boolean inMapA = isInMap(leaderRole);
    boolean inMapB = isInMap(memberRole);
    if (inMapA && !inMapB) {
      Pos pos = leaderRole.getPos();
      MessagePusher.pushMessage(memberRole, (Message)new RespMsg(String.format("队伍在#<O>帮战#中 #<LG1>(%d，%d)# ，暂时无法归队。", new Object[] { pos.getX(), pos.getY() })));
      return false;
    } 
    if (inMapA && inMapB && 
      !leaderRole.getFaction().equals(memberRole.getFaction())) {
      Pos pos = leaderRole.getPos();
      MessagePusher.pushMessage(memberRole, (Message)new RespMsg(String.format("队伍在#<O>帮战#中 #<LG1>(%d，%d)# ，你与队长不同属于一个帮派，暂时无法归队。", new Object[] { pos.getX(), pos.getY() })));
      return false;
    } 
    return true;
  }
  
  public void handleLogin(Role role) {
    boolean inMap = isInMap(role);
    if (!inMap)
      return; 
    if (this.objData == null || System.currentTimeMillis() >= this.objData.getEndTime().getTimeInMillis()) {
      leaveMap(role);
      return;
    } 
    if (this.objData != null) {
      boolean is = true;
      Map<Long, GangWarsPkData> dataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
      GangWarsInfo gangWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(role.getFaction());
      if (dataMap != null && gangWarsInfo != null) {
        GangWarsPkData gangWarsPkData = dataMap.get(role.getId());
        if (gangWarsPkData != null && gangWarsInfo.getCombatResults() == 3)
          is = false; 
        if (is) {
          leaveMap(role);
        } else {
          sendRespIcon(role.getId().longValue(), role.getFaction().intValue());
          boolean flag_ = true;
          if (gangWarsPkData.getPosition() == 0 && gangWarsPkData.getRebirth() > 0) {
            int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
            if (gangWarsPkData.getRebirth() > time) {
              flag_ = false;
              MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespGangWarsRoleState((byte)0, gangWarsPkData.getRebirth()));
            } else {
              gangWarsPkData.setRebirth(0);
            } 
          } 
          if (flag_) {
            Equipment equipment = (Equipment)this.objData.getGateMap().get(role.getFaction());
            if (equipment != null && equipment.getList().contains(role.getId())) {
              flag_ = false;
              MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespGangWarsRoleState((byte)3, 0));
            } 
          } 
          if (flag_) {
            Equipment equipment = (Equipment)this.objData.getCannonMap().get(Integer.valueOf(gangWarsInfo.getVisible()));
            if (equipment != null && equipment.getList().contains(role.getId()))
              MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespGangWarsRoleState((byte)2, equipment.getTime())); 
          } 
        } 
      } 
    } 
    super.handleLogin(role);
  }
  
  public void handleTeamLeaveMoment(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    teamLeave(role, this.roleService.getPlayer(team.getLeaderUid()));
  }
  
  public void handleTeamLeave(Role role, boolean isLeader, Team team) {
    teamLeave(role, this.roleService.getPlayer(team.getLeaderUid()));
  }
  
  public void handleTeamJoinEvent(Role role) {
    if (this.objData == null)
      return; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      Map<Long, GangWarsPkData> dataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
      GangWarsInfo gangWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(role.getFaction());
      if (dataMap != null && gangWarsInfo != null) {
        Equipment equipment = (Equipment)this.objData.getGateMap().get(role.getFaction());
        if (equipment != null) {
          equipment.getList().add(role.getId());
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespGangWarsRoleState((byte)3, equipment.getTime()));
          return;
        } 
        equipment = (Equipment)this.objData.getCannonMap().get(Integer.valueOf(gangWarsInfo.getVisible()));
        if (equipment != null) {
          equipment.getList().add(role.getId());
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespGangWarsRoleState((byte)2, equipment.getTime()));
          return;
        } 
      } 
    } 
  }
  
  public void teamLeave(Role role, Role leader) {
    if (this.objData == null || leader == null)
      return; 
    Map<Long, GangWarsPkData> dataMap = (Map<Long, GangWarsPkData>)this.objData.getPkData().get(role.getFaction());
    GangWarsInfo gangWarsInfo = (GangWarsInfo)this.objData.getWarsMap().get(role.getFaction());
    if (dataMap != null && gangWarsInfo != null) {
      GangWarsPkData gangWarsPkData = dataMap.get(role.getId());
      if (gangWarsPkData == null || gangWarsInfo.getCombatResults() != 3)
        return; 
    } else {
      return;
    } 
    Equipment equipment = (Equipment)this.objData.getGateMap().get(role.getFaction());
    if (equipment != null && equipment.getList().contains(role.getId())) {
      equipment.getList().removeIf(e -> e.equals(role.getId()));
      GangWarsPkData gangWarsPkData = dataMap.get(role.getId());
      if (gangWarsPkData != null)
        gangWarsPkData.setState((byte)0); 
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespOffRoleState("wars", (byte)3));
      if (equipment.getList().size() < 3)
        ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).offGate(leader, this.objData, "队伍人数不足#<R>3#终止操作"); 
      return;
    } 
    equipment = (Equipment)this.objData.getCannonMap().get(Integer.valueOf(gangWarsInfo.getVisible()));
    if (equipment != null && equipment.getList().contains(role.getId())) {
      equipment.getList().removeIf(e -> e.equals(role.getId()));
      GangWarsPkData gangWarsPkData = dataMap.get(role.getId());
      if (gangWarsPkData != null)
        gangWarsPkData.setState((byte)0); 
      RespOffRoleState offRoleState = new RespOffRoleState("wars", (byte)2);
      MessagePusher.pushMessage(role.getId().longValue(), (Message)offRoleState);
      if (equipment.getList().size() < 3) {
        GangWarsPkHandler bean = (GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class);
        bean.cancelTask(equipment.getScheduledFuture());
        this.objData.getCannonMap().remove(Integer.valueOf(gangWarsInfo.getVisible()));
        Team team = this.teamService.getTeam(leader.getRoleId().intValue());
        if (team != null)
          for (int i = 0; i < team.getList().size(); i++) {
            Member member = team.getList().get(i);
            if (member.isInTeam()) {
              GangWarsPkData warsPkData = dataMap.get(member.getId());
              if (warsPkData != null)
                warsPkData.setState((byte)0); 
              MessagePusher.pushMessage(member.getId().longValue(), (Message)offRoleState);
              MessagePusher.pushMessage(member.getId().longValue(), (Message)new RespNotifyMiscEx(member.getId(), "队伍人数不足#<R>3#终止操作", true));
            } 
          }  
      } 
      return;
    } 
  }
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {}
  
  public boolean isInMap(Role role) {
    if (role == null)
      return false; 
    return DataGlobalFunc.isInMap(role, 3001);
  }
  
  public boolean isCompetePk(Role role, Role pkRole) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("你现在不能与人切磋。"));
      return false;
    } 
    if (isInMap(pkRole)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("你现在不能与人切磋。"));
      return false;
    } 
    return true;
  }
  
  public void loadCommonSet(Map<String, String> commonSetMap) {
    loadSet(commonSetMap);
  }
  
  private void loadSet(Map<String, String> commonSetMap) {
    String value = commonSetMap.get("wars_team_set");
    GangWarsSet objSet = null;
    if (value != null) {
      objSet = (GangWarsSet)Json.fromJson(GangWarsSet.class, value);
    } else {
      objSet = new GangWarsSet();
    } 
    this.objSet = objSet;
  }
  
  public void notifyAt(byte round) {
    String day = DateUtils.dayForWeek(new Date());
    GangWarsTimeSlot timeSlot = this.objSet.getDayOfWeek().stream().filter(e -> (e.getDayOfWeek().equals(day) && e.getRound() == round)).findFirst().orElse(null);
    if (timeSlot == null)
      return; 
    this.round = round;
    prepareOnce(timeSlot);
  }
  
  public void cs() {
    this.objData = new GangWarsData();
    Calendar calendarStart = Calendar.getInstance();
    calendarStart.setTime(new Date());
    GangWarsSet objSet = getObjSet();
    calendarStart.add(12, 1);
    this.objData.setSignUpTime(EventsUtils.getTime(calendarStart));
    calendarStart.add(12, 2);
    this.objData.setStartTime(EventsUtils.getTime(calendarStart));
    calendarStart.add(12, objSet.getSustain());
    this.objData.setEndTime(EventsUtils.getTime(calendarStart));
    addCheckJob();
    addStartJob();
    addEndJob();
    addBroadcastJob();
    initMatchingParty();
  }
}
