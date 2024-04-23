package com.kitty.system.game.fightnew.bean;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.messages.RespFightInParam;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.ActionParam;
import com.kitty.system.game.fightnew.model.AnimationPlayParam;
import com.kitty.system.game.fightnew.model.PetFightInParam;
import com.kitty.system.game.fightnew.model.SkillParam;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.spectator.domain.SpectatorObject;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.task.AutoRoundFinishTask;
import com.kitty.system.game.fightnew.task.TimeOutNewTask;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysPetSkill;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;

public class Fight {
  public void setLogger(Logger logger) {
    this.logger = logger;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setRecordId(long recordId) {
    this.recordId = recordId;
  }
  
  public void setLeaderId(int leaderId) {
    this.leaderId = leaderId;
  }
  
  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
  
  public void setListA(List<FightObject> listA) {
    this.listA = listA;
  }
  
  public void setListB(List<FightObject> listB) {
    this.listB = listB;
  }
  
  public void setFightMemberA(ArrayList<FightMember> fightMemberA) {
    this.fightMemberA = fightMemberA;
  }
  
  public void setFightMemberB(ArrayList<FightMember> fightMemberB) {
    this.fightMemberB = fightMemberB;
  }
  
  public void setMapId(int mapId) {
    this.mapId = mapId;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setSpecialMonsterType(byte specialMonsterType) {
    this.specialMonsterType = specialMonsterType;
  }
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setRoundParam(RoundParam roundParam) {
    this.roundParam = roundParam;
  }
  
  public void setMaxRound(int maxRound) {
    this.maxRound = maxRound;
  }
  
  public void setTaskId(int taskId) {
    this.taskId = taskId;
  }
  
  public void setExpMap(NutMap expMap) {
    this.expMap = expMap;
  }
  
  public void setBossReward(NutMap bossReward) {
    this.bossReward = bossReward;
  }
  
  public void setNpcId(int npcId) {
    this.npcId = npcId;
  }
  
  public void setData(NutMap data) {
    this.data = data;
  }
  
  public void setTimeOutTask(ScheduledFuture<?> timeOutTask) {
    this.timeOutTask = timeOutTask;
  }
  
  public void setAstrictSkillMap(Map<Boolean, AstrictSkill> astrictSkillMap) {
    this.astrictSkillMap = astrictSkillMap;
  }
  
  public void setBeforeActionMap(Map<Boolean, BeforeActionSkill> beforeActionMap) {
    this.beforeActionMap = beforeActionMap;
  }
  
  public void setReduceRatioMap(Map<Boolean, Map<Integer, Integer>> reduceRatioMap) {
    this.reduceRatioMap = reduceRatioMap;
  }
  
  public void setObjectAttributeMap(Map<Boolean, Map<Integer, Double>> ObjectAttributeMap) {
    this.ObjectAttributeMap = ObjectAttributeMap;
  }
  
  public void setMonsterAstrict(NutMap monsterAstrict) {
    this.monsterAstrict = monsterAstrict;
  }
  
  public void setFightInParams(List<RespFightInParam> fightInParams) {
    this.fightInParams = fightInParams;
  }
  
  public void setActionParam(ActionParam actionParam) {
    this.actionParam = actionParam;
  }
  
  public void setRespRoundStartParamMap(Map<Integer, RespRoundStartParam> respRoundStartParamMap) {
    this.respRoundStartParamMap = respRoundStartParamMap;
  }
  
  public void setIsCompleted(Set<Integer> isCompleted) {
    this.isCompleted = isCompleted;
  }
  
  public void setFleeList(List<Integer> fleeList) {
    this.fleeList = fleeList;
  }
  
  public void setFightActors(Map<Integer, FightObject> fightActors) {
    this.fightActors = fightActors;
  }
  
  public void setDeadPetIds(Set<Integer> deadPetIds) {
    this.deadPetIds = deadPetIds;
  }
  
  public void setFightType(byte fightType) {
    this.fightType = fightType;
  }
  
  public void setRewardType(int rewardType) {
    this.rewardType = rewardType;
  }
  
  public void setRoundType(int roundType) {
    this.roundType = roundType;
  }
  
  public void setFightId_timer_host(Map<Integer, ScheduledFuture<?>> fightId_timer_host) {
    this.fightId_timer_host = fightId_timer_host;
  }
  
  public void setRoundFinish_timer_player(Map<Integer, ScheduledFuture<?>> roundFinish_timer_player) {
    this.roundFinish_timer_player = roundFinish_timer_player;
  }
  
  public void setFightId_timer_pet(Map<Integer, ScheduledFuture<?>> fightId_timer_pet) {
    this.fightId_timer_pet = fightId_timer_pet;
  }
  
  public void setAnimationPlayMap(Map<Integer, AnimationPlayParam> animationPlayMap) {
    this.animationPlayMap = animationPlayMap;
  }
  
  public void setFightNewService(FightNewService fightNewService) {
    this.fightNewService = fightNewService;
  }
  
  public void setWatchFightOnOff(boolean watchFightOnOff) {
    this.watchFightOnOff = watchFightOnOff;
  }
  
  public void setSpectatorObjectMap(ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap) {
    this.spectatorObjectMap = spectatorObjectMap;
  }
  
  public void setFightOverReason(byte fightOverReason) {
    this.fightOverReason = fightOverReason;
  }
  
  public void setWinSide(byte winSide) {
    this.winSide = winSide;
  }
  
  public void setExitRoles(Map<Boolean, Set<FieldValue>> exitRoles) {
    this.exitRoles = exitRoles;
  }
  
  @ConstructorProperties({"logger", "id", "recordId", "leaderId", "createTime", "listA", "listB", "fightMemberA", "fightMemberB", "mapId", "type", "specialMonsterType", "round", "roundParam", "maxRound", "taskId", "expMap", "bossReward", "npcId", "data", "timeOutTask", "astrictSkillMap", "beforeActionMap", "reduceRatioMap", "ObjectAttributeMap", "monsterAstrict", "fightInParams", "actionParam", "respRoundStartParamMap", "isCompleted", "fleeList", "fightActors", "deadPetIds", "fightType", "rewardType", "roundType", "fightId_timer_host", "roundFinish_timer_player", "fightId_timer_pet", "animationPlayMap", "fightNewService", "watchFightOnOff", "spectatorObjectMap", "fightOverReason", "winSide", "exitRoles"})
  public Fight(Logger logger, int id, long recordId, int leaderId, long createTime, List<FightObject> listA, List<FightObject> listB, ArrayList<FightMember> fightMemberA, ArrayList<FightMember> fightMemberB, int mapId, int type, byte specialMonsterType, int round, RoundParam roundParam, int maxRound, int taskId, NutMap expMap, NutMap bossReward, int npcId, NutMap data, ScheduledFuture<?> timeOutTask, Map<Boolean, AstrictSkill> astrictSkillMap, Map<Boolean, BeforeActionSkill> beforeActionMap, Map<Boolean, Map<Integer, Integer>> reduceRatioMap, Map<Boolean, Map<Integer, Double>> ObjectAttributeMap, NutMap monsterAstrict, List<RespFightInParam> fightInParams, ActionParam actionParam, Map<Integer, RespRoundStartParam> respRoundStartParamMap, Set<Integer> isCompleted, List<Integer> fleeList, Map<Integer, FightObject> fightActors, Set<Integer> deadPetIds, byte fightType, int rewardType, int roundType, Map<Integer, ScheduledFuture<?>> fightId_timer_host, Map<Integer, ScheduledFuture<?>> roundFinish_timer_player, Map<Integer, ScheduledFuture<?>> fightId_timer_pet, Map<Integer, AnimationPlayParam> animationPlayMap, FightNewService fightNewService, boolean watchFightOnOff, ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap, byte fightOverReason, byte winSide, Map<Boolean, Set<FieldValue>> exitRoles) {
    this.logger = logger;
    this.id = id;
    this.recordId = recordId;
    this.leaderId = leaderId;
    this.createTime = createTime;
    this.listA = listA;
    this.listB = listB;
    this.fightMemberA = fightMemberA;
    this.fightMemberB = fightMemberB;
    this.mapId = mapId;
    this.type = type;
    this.specialMonsterType = specialMonsterType;
    this.round = round;
    this.roundParam = roundParam;
    this.maxRound = maxRound;
    this.taskId = taskId;
    this.expMap = expMap;
    this.bossReward = bossReward;
    this.npcId = npcId;
    this.data = data;
    this.timeOutTask = timeOutTask;
    this.astrictSkillMap = astrictSkillMap;
    this.beforeActionMap = beforeActionMap;
    this.reduceRatioMap = reduceRatioMap;
    this.ObjectAttributeMap = ObjectAttributeMap;
    this.monsterAstrict = monsterAstrict;
    this.fightInParams = fightInParams;
    this.actionParam = actionParam;
    this.respRoundStartParamMap = respRoundStartParamMap;
    this.isCompleted = isCompleted;
    this.fleeList = fleeList;
    this.fightActors = fightActors;
    this.deadPetIds = deadPetIds;
    this.fightType = fightType;
    this.rewardType = rewardType;
    this.roundType = roundType;
    this.fightId_timer_host = fightId_timer_host;
    this.roundFinish_timer_player = roundFinish_timer_player;
    this.fightId_timer_pet = fightId_timer_pet;
    this.animationPlayMap = animationPlayMap;
    this.fightNewService = fightNewService;
    this.watchFightOnOff = watchFightOnOff;
    this.spectatorObjectMap = spectatorObjectMap;
    this.fightOverReason = fightOverReason;
    this.winSide = winSide;
    this.exitRoles = exitRoles;
  }
  
  protected Logger logger = LoggerFunction.FIGHT.getLogger();
  
  public Logger getLogger() {
    return this.logger;
  }
  
  private static final AtomicInteger idFactory = new AtomicInteger(1);
  
  private int id;
  
  private long recordId;
  
  private int leaderId;
  
  private long createTime;
  
  public int getId() {
    return this.id;
  }
  
  public long getRecordId() {
    return this.recordId;
  }
  
  public int getLeaderId() {
    return this.leaderId;
  }
  
  public long getCreateTime() {
    return this.createTime;
  }
  
  private List<FightObject> listA = Collections.synchronizedList(new ArrayList<>());
  
  public List<FightObject> getListA() {
    return this.listA;
  }
  
  private List<FightObject> listB = Collections.synchronizedList(new ArrayList<>());
  
  public List<FightObject> getListB() {
    return this.listB;
  }
  
  private ArrayList<FightMember> fightMemberA = new ArrayList<>();
  
  public ArrayList<FightMember> getFightMemberA() {
    return this.fightMemberA;
  }
  
  private ArrayList<FightMember> fightMemberB = new ArrayList<>();
  
  protected int mapId;
  
  private int type;
  
  private byte specialMonsterType;
  
  public ArrayList<FightMember> getFightMemberB() {
    return this.fightMemberB;
  }
  
  public int getMapId() {
    return this.mapId;
  }
  
  public int getType() {
    return this.type;
  }
  
  public byte getSpecialMonsterType() {
    return this.specialMonsterType;
  }
  
  private int round = 1;
  
  public int getRound() {
    return this.round;
  }
  
  private RoundParam roundParam = new RoundParam(this.round);
  
  public RoundParam getRoundParam() {
    return this.roundParam;
  }
  
  private int maxRound = WarMaxRound.WAR_MAX_ROUND.getValue();
  
  private int taskId;
  
  private NutMap expMap;
  
  private NutMap bossReward;
  
  protected int npcId;
  
  private NutMap data;
  
  private ScheduledFuture<?> timeOutTask;
  
  public int getMaxRound() {
    return this.maxRound;
  }
  
  public int getTaskId() {
    return this.taskId;
  }
  
  public NutMap getExpMap() {
    return this.expMap;
  }
  
  public NutMap getBossReward() {
    return this.bossReward;
  }
  
  public int getNpcId() {
    return this.npcId;
  }
  
  public NutMap getData() {
    return this.data;
  }
  
  public ScheduledFuture<?> getTimeOutTask() {
    return this.timeOutTask;
  }
  
  private Map<Boolean, AstrictSkill> astrictSkillMap = new HashMap<>();
  
  public Map<Boolean, AstrictSkill> getAstrictSkillMap() {
    return this.astrictSkillMap;
  }
  
  private Map<Boolean, BeforeActionSkill> beforeActionMap = new HashMap<>();
  
  public Map<Boolean, BeforeActionSkill> getBeforeActionMap() {
    return this.beforeActionMap;
  }
  
  private Map<Boolean, Map<Integer, Integer>> reduceRatioMap = new HashMap<>();
  
  public Map<Boolean, Map<Integer, Integer>> getReduceRatioMap() {
    return this.reduceRatioMap;
  }
  
  private Map<Boolean, Map<Integer, Double>> ObjectAttributeMap = new HashMap<>();
  
  public Map<Boolean, Map<Integer, Double>> getObjectAttributeMap() {
    return this.ObjectAttributeMap;
  }
  
  private NutMap monsterAstrict = NutMap.NEW();
  
  public NutMap getMonsterAstrict() {
    return this.monsterAstrict;
  }
  
  private List<RespFightInParam> fightInParams = Collections.synchronizedList(new ArrayList<>());
  
  public List<RespFightInParam> getFightInParams() {
    return this.fightInParams;
  }
  
  private ActionParam actionParam = new ActionParam();
  
  public ActionParam getActionParam() {
    return this.actionParam;
  }
  
  private Map<Integer, RespRoundStartParam> respRoundStartParamMap = new HashMap<>();
  
  public Map<Integer, RespRoundStartParam> getRespRoundStartParamMap() {
    return this.respRoundStartParamMap;
  }
  
  private Set<Integer> isCompleted = new HashSet<>();
  
  public Set<Integer> getIsCompleted() {
    return this.isCompleted;
  }
  
  private List<Integer> fleeList = new ArrayList<>();
  
  public List<Integer> getFleeList() {
    return this.fleeList;
  }
  
  private Map<Integer, FightObject> fightActors = new HashMap<>();
  
  public Map<Integer, FightObject> getFightActors() {
    return this.fightActors;
  }
  
  private Set<Integer> deadPetIds = new HashSet<>();
  
  public Set<Integer> getDeadPetIds() {
    return this.deadPetIds;
  }
  
  private byte fightType = 0;
  
  public byte getFightType() {
    return this.fightType;
  }
  
  private int rewardType = -1;
  
  private int roundType;
  
  public int getRewardType() {
    return this.rewardType;
  }
  
  public int getRoundType() {
    return this.roundType;
  }
  
  private Map<Integer, ScheduledFuture<?>> fightId_timer_host = new HashMap<>();
  
  public Map<Integer, ScheduledFuture<?>> getFightId_timer_host() {
    return this.fightId_timer_host;
  }
  
  private Map<Integer, ScheduledFuture<?>> roundFinish_timer_player = new HashMap<>();
  
  public Map<Integer, ScheduledFuture<?>> getRoundFinish_timer_player() {
    return this.roundFinish_timer_player;
  }
  
  private Map<Integer, ScheduledFuture<?>> fightId_timer_pet = new HashMap<>();
  
  public Map<Integer, ScheduledFuture<?>> getFightId_timer_pet() {
    return this.fightId_timer_pet;
  }
  
  private Map<Integer, AnimationPlayParam> animationPlayMap = new HashMap<>();
  
  public Map<Integer, AnimationPlayParam> getAnimationPlayMap() {
    return this.animationPlayMap;
  }
  
  protected FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
  
  public FightNewService getFightNewService() {
    return this.fightNewService;
  }
  
  private boolean watchFightOnOff = false;
  
  public boolean isWatchFightOnOff() {
    return this.watchFightOnOff;
  }
  
  private ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap = new ConcurrentHashMap<>();
  
  public ConcurrentHashMap<Long, SpectatorObject> getSpectatorObjectMap() {
    return this.spectatorObjectMap;
  }
  
  private byte fightOverReason = 0;
  
  public byte getFightOverReason() {
    return this.fightOverReason;
  }
  
  private byte winSide = 0;
  
  public byte getWinSide() {
    return this.winSide;
  }
  
  private Map<Boolean, Set<FieldValue>> exitRoles = new HashMap<>();
  
  public Map<Boolean, Set<FieldValue>> getExitRoles() {
    return this.exitRoles;
  }
  
  public Fight(int type) {
    this.id = idFactory.getAndIncrement();
    this.createTime = System.currentTimeMillis();
    this.type = type;
  }
  
  public ArrayList<FightObject> getAllFightObjects() {
    ArrayList<FightObject> container = new ArrayList<>();
    for (FightObject object : this.listA) {
      if (object != null)
        container.add(object); 
    } 
    for (FightObject object : this.listB) {
      if (object != null)
        container.add(object); 
    } 
    return container;
  }
  
  private boolean isCompleted(int round) {
    boolean boo = this.isCompleted.contains(Integer.valueOf(round));
    if (!boo)
      this.isCompleted.add(Integer.valueOf(round)); 
    return boo;
  }
  
  public ArrayList<FightObject> getSideAllFightObjects(boolean isASide) {
    ArrayList<FightObject> container = new ArrayList<>();
    if (isASide) {
      container.addAll(this.listA);
    } else {
      container.addAll(this.listB);
    } 
    return container;
  }
  
  public FightObject queryFightObject(Integer roleId) {
    if (getListA() != null)
      for (FightObject fightObject : getListA()) {
        if (fightObject.getId().equals(roleId))
          return fightObject; 
      }  
    if (getListB() != null)
      for (FightObject fightObject : getListB()) {
        if (fightObject.getId().equals(roleId))
          return fightObject; 
      }  
    return null;
  }
  
  public FightObject getAttackTarget(FightObject effector) {
    List<FightObject> targets = effector.isASide() ? getListB() : getListA();
    for (FightObject target : targets) {
      if (!target.isFallDown())
        return target; 
    } 
    return null;
  }
  
  public FightObject getAttackSelf(FightObject effector) {
    List<FightObject> targets = effector.isASide() ? getListA() : getListB();
    for (FightObject target : targets) {
      if (!target.isFallDown())
        return target; 
    } 
    return null;
  }
  
  public boolean isSameSide(FightObject o1, FightObject o2) {
    return (o1.isASide() == o2.isASide());
  }
  
  public List<FightObject> getMySide(int roleId) {
    for (FightObject fightObject : getListA()) {
      if (fightObject.getId().intValue() == roleId)
        return getListA(); 
    } 
    return getListB();
  }
  
  public List<FightObject> getTargetSide(int roleId) {
    for (FightObject fightObject : getListA()) {
      if (fightObject.getId().intValue() == roleId)
        return getListB(); 
    } 
    return getListA();
  }
  
  public boolean isWinner(int roleId) {
    if (getFightOverReason() == 1) {
      FightObject fightObject = queryFightObject(Integer.valueOf(roleId));
      if (this.winSide == 0)
        return defineWin(fightObject); 
      if (this.winSide == 1) {
        if (fightObject.isASide())
          return true; 
        return false;
      } 
      if (fightObject.isASide())
        return false; 
      return true;
    } 
    List<FightObject> mySide = getMySide(roleId);
    for (FightObject fightObject : mySide) {
      if (fightObject.getCurrLife() > 0)
        return true; 
    } 
    return false;
  }
  
  public boolean isWinner(int roleId, boolean isA) {
    List<FightObject> mySide;
    if (getFightOverReason() == 1) {
      FightObject fightObject = queryFightObject(Integer.valueOf(roleId));
      if (this.winSide == 0)
        return defineWin(roleId, isA); 
      if (this.winSide == 1) {
        if (isA)
          return true; 
        return false;
      } 
      if (isA)
        return false; 
      return true;
    } 
    if (isA) {
      mySide = this.listA;
    } else {
      mySide = this.listB;
    } 
    for (FightObject fightObject : mySide) {
      if (fightObject.getCurrLife() > 0)
        return true; 
    } 
    return false;
  }
  
  public boolean isStartFight() {
    for (FightObject fightObject : getListA()) {
      if (!fightObject.isDoAction())
        return false; 
    } 
    for (FightObject fightObject : getListB()) {
      if (!fightObject.isDoAction())
        return false; 
    } 
    return true;
  }
  
  public boolean isOver() {
    return (isSideOver(getListA()) || isSideOver(getListB()) || (
      getData() != null && getData().getBoolean("fight_result_fail", false)) || (
      getData() != null && getData().getBoolean("fight_result_success", false)));
  }
  
  public boolean isSideOver(List<FightObject> members) {
    if (members != null && members.size() > 0)
      for (int i = 0; i < members.size(); i++) {
        FightObject fightObject = members.get(i);
        if (fightObject != null && !fightObject.isFallDown())
          return false; 
      }  
    return true;
  }
  
  public void addFightObject(FightObject addObject) {
    this.fightActors.put(addObject.getId(), addObject);
    for (FightObject object : getListA()) {
      if (object.getId().intValue() == addObject.getHostId()) {
        getListA().add(addObject);
        return;
      } 
    } 
    for (FightObject fightObject : getListB()) {
      if (fightObject.getId().intValue() == addObject.getHostId()) {
        getListB().add(addObject);
        return;
      } 
    } 
  }
  
  public void removeFightObject(FightObject fightObject) {
    if (getType() == 25 && fightObject.isPet() && fightObject.getPos() == 8) {
      if (getData() == null)
        setData(NutMap.NEW()); 
      getData().setv("fight_result_fail", Boolean.valueOf(true));
    } 
    removeFightObject(getListA(), fightObject);
    removeFightObject(getListB(), fightObject);
  }
  
  private void removeFightObject(List<FightObject> pool, FightObject toRemove) {
    pool.removeIf(next -> next.getId().equals(toRemove.getId()));
  }
  
  public FightObject queryFightObjectAnyWay(int objId) {
    return this.fightActors.get(Integer.valueOf(objId));
  }
  
  public void destroy() {
    FightParamPool.fightDestroy(this);
    for (FightObject fightObject : getAllFightObjects()) {
      if (fightObject != null && fightObject.isPlayer()) {
        Role role = SpringUtils.getRoleService().getPlayer(fightObject.getRole().getId().longValue());
        if (role != null) {
          role.setPrevFightEndTime(System.currentTimeMillis());
          role.setCanFight(Boolean.valueOf(true));
          removeRoundFinishTask(role.getRoleId().intValue());
        } 
      } 
      Integer temp = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(fightObject.getId());
      if (temp == null);
    } 
    if (this.watchFightOnOff && this.spectatorObjectMap.size() > 0) {
      setWatchFightOnOff(false);
      RespEndCombat respEndCombat = new RespEndCombat();
      for (Map.Entry<Long, SpectatorObject> entry : this.spectatorObjectMap.entrySet())
        MessagePusher.pushMessage(((Long)entry.getKey()).longValue(), (Message)respEndCombat); 
    } 
    FightExecutorNewService.FIGHTS.remove(Integer.valueOf(getId()));
    cancelTask(this.timeOutTask);
  }
  
  public void addHostTask(int roleId, ScheduledFuture<?> task) {
    removeHostTask(roleId);
    this.fightId_timer_host.put(Integer.valueOf(roleId), task);
  }
  
  public void removeHostTask(int roleId) {
    ScheduledFuture<?> task = this.fightId_timer_host.get(Integer.valueOf(roleId));
    if (task != null) {
      cancelTask(task);
      this.fightId_timer_host.remove(Integer.valueOf(roleId));
    } 
  }
  
  public void addPetTask(int roleId, ScheduledFuture<?> task) {
    removePetTask(roleId);
    this.fightId_timer_pet.put(Integer.valueOf(roleId), task);
  }
  
  public void removePetTask(int memberId) {
    ScheduledFuture<?> task = this.fightId_timer_pet.get(Integer.valueOf(memberId));
    if (task != null) {
      cancelTask(task);
      this.fightId_timer_host.remove(Integer.valueOf(memberId));
    } 
  }
  
  public void cancelTask(ScheduledFuture<?> task) {
    if (task != null && !task.isDone())
      task.cancel(Boolean.FALSE.booleanValue()); 
  }
  
  public void addRoundFinishTask(int objId, ScheduledFuture<?> task) {
    removeRoundFinishTask(objId);
    this.roundFinish_timer_player.put(Integer.valueOf(objId), task);
  }
  
  public void removeRoundFinishTask(int objId) {
    ScheduledFuture<?> task = this.roundFinish_timer_player.get(Integer.valueOf(objId));
    if (task != null) {
      cancelTask(task);
      this.roundFinish_timer_player.remove(Integer.valueOf(objId));
    } 
  }
  
  public void start() {
    FightExecutorNewService fightExecutorService = (FightExecutorNewService)SpringUtils.getBean(FightExecutorNewService.class);
    fightExecutorService.addFightTask(this, () -> {
          try {
            loadActors();
            notifyAndStart();
            for (FightObject object : getListA())
              this.fightActors.put(object.getId(), object); 
            for (FightObject object : getListB())
              this.fightActors.put(object.getId(), object); 
            ((FightNewService)SpringUtils.getBean(FightNewService.class)).excuteFightInSend(this);
            ((FightNewService)SpringUtils.getBean(FightNewService.class)).excuteRoundStartSend(this);
            setRoundType(1);
          } catch (Exception e) {
            LoggerFunction.FIGHT.getLogger().error(e.getMessage(), e);
            LoggerFunction.TASK.getLogger().error(e.getMessage(), e);
          } 
        });
    ((FightNewService)SpringUtils.getBean(FightNewService.class)).besure(this);
    TimeOutNewTask timeOut = new TimeOutNewTask(getId());
    ScheduledFuture<?> timeOutTask = SchedulerManager.getInstance().schedule((Runnable)timeOut, 30000L);
    setTimeOutTask(timeOutTask);
  }
  
  protected void loadActors() {}
  
  protected void notifyAndStart() {}
  
  public boolean isPkFight() {
    return false;
  }
  
  public int getMaxFightObjectId() {
    int maxId = 0;
    ArrayList<FightObject> allFightObjects = getAllFightObjects();
    for (FightObject fightObject : allFightObjects) {
      if (fightObject.getId().intValue() > maxId)
        maxId = fightObject.getId().intValue(); 
    } 
    return maxId;
  }
  
  public FightObject getTargetNoCurrentTarget(int attackerId, int oldTargetId) {
    FightObject attactor = queryFightObject(Integer.valueOf(attackerId));
    List<FightObject> targetSideList = null;
    if (oldTargetId > 0) {
      targetSideList = SkillTargetPicker.getFightObjectsOldTarget(this, attactor, oldTargetId);
    } else {
      targetSideList = SkillTargetPicker.getFightObjects(this, attactor, true);
    } 
    if (targetSideList == null || targetSideList.size() <= 0)
      return null; 
    targetSideList = (List<FightObject>)targetSideList.stream().filter(obj -> (obj.getId().intValue() != oldTargetId && obj.getId().intValue() != attackerId)).collect(Collectors.toList());
    if (targetSideList.size() <= 0)
      return null; 
    Random random = new Random();
    int index = random.nextInt(targetSideList.size());
    return targetSideList.get(index);
  }
  
  public void clearAstrictSkills() {
    if (getAstrictSkillMap().get(Boolean.valueOf(true)) != null)
      ((AstrictSkill)getAstrictSkillMap().get(Boolean.valueOf(true))).getSkillMap().clear(); 
    if (getAstrictSkillMap().get(Boolean.valueOf(false)) != null)
      ((AstrictSkill)getAstrictSkillMap().get(Boolean.valueOf(false))).getSkillMap().clear(); 
  }
  
  public void excuteReduceRatio(FightObject fightObject, boolean isExcute) {
    if (fightObject == null)
      return; 
    List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.PET_BE.getValue(), fightObject
        .getSkillIds(), isPvp());
    SkillProcessor skillProcessor = null;
    if (petSkillList.size() > 0)
      for (SysPetSkill petSkill : petSkillList) {
        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
        if (skillProcessor != null)
          skillProcessor.passive(this, fightObject, new Object[] { Boolean.valueOf(isExcute) }); 
      }  
    for (Integer skillId : SkillConst.getPetBeSkills()) {
      for (RoleSkill roleSkill : fightObject.getSkillIds()) {
        if (roleSkill.getSkillId().intValue() > 0 && roleSkill.getSkillId().equals(skillId)) {
          skillProcessor = SkillProcessor.queryProcessor(skillId);
          if (skillProcessor != null)
            skillProcessor.death(this, fightObject, fightObject, new Object[0]); 
        } 
      } 
    } 
  }
  
  public boolean diedTriggerSkill(FightObject fightObject, String type) {
    if (fightObject == null)
      return false; 
    boolean isRever = false;
    List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.petExit(type, fightObject
        .getSkillIds(), isPvp());
    SkillProcessor skillProcessor = null;
    if (petSkillList.size() > 0) {
      petSkillList = (List<SysPetSkill>)petSkillList.stream().sorted(Comparator.comparing(SysPetSkill::getSerial)).collect(Collectors.toList());
      for (SysPetSkill petSkill : petSkillList) {
        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
        NutMap passive = skillProcessor.passive(this, fightObject, new Object[] { petSkill });
        if (passive != null && passive.getInt(String.valueOf(41)) > 0)
          isRever = true; 
      } 
    } 
    return isRever;
  }
  
  public List<FightObject> queryAnomalousObject(FightObject effecter, int num, List<Integer> logics) {
    List<FightObject> returnObj = new ArrayList<>();
    List<FightObject> myFightObjects = getListA();
    if (!effecter.isASide())
      myFightObjects = getListB(); 
    Collections.sort(myFightObjects);
    Iterator<FightObject> it = myFightObjects.iterator();
    FightObject obj = null;
    int count = 0;
    while (it.hasNext()) {
      obj = it.next();
      if (!logics.contains(Integer.valueOf(obj.getLogic())))
        continue; 
      boolean isAdd = false;
      if (obj.getStatusMap().getInt(String.valueOf(1002)) > 0)
        isAdd = true; 
      if (obj.getStatusMap().getInt(String.valueOf(1003)) > 0)
        isAdd = true; 
      if (obj.getStatusMap().getInt(String.valueOf(1004)) > 0)
        isAdd = true; 
      if (obj.getStatusMap().getInt(String.valueOf(1006)) > 0)
        isAdd = true; 
      if (obj.getStatusMap().getInt(String.valueOf(1019)) > 0)
        isAdd = true; 
      if (isAdd) {
        count++;
        returnObj.add(obj);
      } 
      if (count >= num)
        break; 
    } 
    return returnObj;
  }
  
  public int isPvp() {
    int isPvp = 0;
    if (1 == FIGHT_TYPE.getType(Integer.valueOf(getType())).intValue())
      isPvp = 1; 
    return isPvp;
  }
  
  public void excuteExitPassive(FightObject actor) {
    try {
      diedTriggerSkill(actor, PET_PASSIVE_SKILL_TRIGGER_DICT.PET_EXIT.getValue());
      excuteReduceRatio(actor, false);
      List<FightObject> otherPets = (List<FightObject>)getMySide(actor.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && !obj.getId().equals(actor.getId()))).collect(Collectors.toList());
      for (FightObject otherPet : otherPets)
        diedTriggerSkill(otherPet, PET_PASSIVE_SKILL_TRIGGER_DICT.NON_SELF_PET_EXIT.getValue()); 
      PetSkillEffectorUtil.sendCancelAction(this, actor);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public RespFightInParam getRoleFightInParam(Role role) {
    RespFightInParam fightInParam = null;
    for (int i = 0; i < this.fightInParams.size(); i++) {
      if (((RespFightInParam)this.fightInParams.get(i)).getRoleId() == role.getRoleId().intValue()) {
        fightInParam = this.fightInParams.get(i);
        break;
      } 
    } 
    if (fightInParam == null) {
      fightInParam = new RespFightInParam();
      fightInParam.setId(String.valueOf(role.getId()));
      List<SkillParam> skillParams = getSkillParams(role);
      if (skillParams != null)
        fightInParam.setSkillParams(skillParams); 
      fightInParam.setRoleId(role.getRoleId().intValue());
      this.fightInParams.add(fightInParam);
    } 
    return fightInParam;
  }
  
  public PetFightInParam addPetFightInParam(Role role, Pet pet) {
    PetFightInParam petFightInParam = null;
    RespFightInParam fightInParam = null;
    for (int i = 0; i < this.fightInParams.size(); i++) {
      if (((RespFightInParam)this.fightInParams.get(i)).getRoleId() == role.getRoleId().intValue()) {
        fightInParam = this.fightInParams.get(i);
        if (fightInParam.getPetFightInParam() != null && fightInParam.getPetFightInParam().size() > 0)
          for (PetFightInParam petFightInParam1 : fightInParam.getPetFightInParam()) {
            if (pet.getPetId().intValue() == petFightInParam1.getPetId())
              petFightInParam = petFightInParam1; 
          }  
        break;
      } 
    } 
    if (fightInParam == null) {
      fightInParam = new RespFightInParam();
      fightInParam.setId(String.valueOf(role.getId()));
      List<SkillParam> skillParams = getSkillParams(role);
      if (skillParams != null)
        fightInParam.setSkillParams(skillParams); 
      fightInParam.setRoleId(role.getRoleId().intValue());
      this.fightInParams.add(fightInParam);
    } 
    if (petFightInParam == null) {
      petFightInParam = getPetFightParam(pet);
      fightInParam.addPetFightInParam(petFightInParam);
    } 
    return petFightInParam;
  }
  
  public PetFightInParam getPetFightParam(Pet pet) {
    PetFightInParam petFightInParam = new PetFightInParam();
    petFightInParam.setPetId(pet.getPetId().intValue());
    petFightInParam.addSkillIds(pet);
    return petFightInParam;
  }
  
  public void onActorDie(FightObject actor) {
    if (actor.getLogic() == LOGIC.LOGICTYPE_HERO.getValue()) {
      FightObject object = queryFightObject(Integer.valueOf(actor.getFightPetId()));
      if (object != null);
    } 
  }
  
  public void petDie(FightObject actor) {
    if (actor.isPet() && 
      !this.deadPetIds.contains(actor.getId()))
      this.deadPetIds.add(actor.getId()); 
    removeFightObject(actor);
  }
  
  public List<SkillParam> getSkillParams(Role role) {
    Map<Integer, RoleSkill> skills = role.getSkills();
    if (skills != null && skills.size() > 0) {
      List<SkillParam> skillParams = new ArrayList<>();
      for (Map.Entry<Integer, RoleSkill> entry : skills.entrySet()) {
        RoleSkill value = entry.getValue();
        if (value != null) {
          SkillParam skillParam = new SkillParam();
          skillParam.setSkillId(value.getSkillId().intValue());
          skillParam.setProficient(value.getProficient().intValue() / 100);
          skillParams.add(skillParam);
        } 
      } 
      return skillParams;
    } 
    return null;
  }
  
  public boolean petDeadOnce(int petId) {
    return this.deadPetIds.contains(Integer.valueOf(petId));
  }
  
  public List<ExcuteFightParam> getFightStatus() {
    List<ActionDetailParam> actionDetailParams = PetSkillEffectorUtil.checkActionForPrivate(this);
    if (actionDetailParams != null && actionDetailParams.size() > 0) {
      List<ExcuteFightParam> excuteFightParams = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).getExcuteFightParams(actionDetailParams);
      return excuteFightParams;
    } 
    return null;
  }
  
  public void roundFinishTask(FightObject fightObject, long time) {
    AutoRoundFinishTask autoRoundFinishTask = new AutoRoundFinishTask(this, fightObject.getId().intValue());
    ScheduledFuture<?> timeOut = SchedulerManager.getInstance().schedule((Runnable)autoRoundFinishTask, time);
    addRoundFinishTask(fightObject.getId().intValue(), timeOut);
  }
  
  public boolean defineWin(FightObject fightObject) {
    int isWin = roundExceeded(fightObject.getId().intValue());
    boolean flag = false;
    if (isWin == 2) {
      flag = SkillAi.canAffectOnPro(0.5D);
    } else if (isWin == 1) {
      flag = true;
    } 
    if (flag) {
      if (fightObject.isASide()) {
        setWinSide((byte)1);
      } else {
        setWinSide((byte)2);
      } 
    } else if (fightObject.isASide()) {
      setWinSide((byte)2);
    } else {
      setWinSide((byte)1);
    } 
    return flag;
  }
  
  public boolean defineWin(int roleId, boolean isA) {
    int isWin = roundExceeded(roleId);
    boolean flag = false;
    if (isWin == 2) {
      flag = SkillAi.canAffectOnPro(0.5D);
    } else if (isWin == 1) {
      flag = true;
    } 
    if (flag) {
      if (isA) {
        setWinSide((byte)1);
      } else {
        setWinSide((byte)2);
      } 
    } else if (isA) {
      setWinSide((byte)2);
    } else {
      setWinSide((byte)1);
    } 
    return flag;
  }
  
  public byte roundExceeded(int roleId) {
    byte returnFlag = 2;
    List<FightObject> mySide = getMySide(roleId);
    List<FightObject> targetSide = getTargetSide(roleId);
    int mySideNum = 0;
    int mySideCurrLife = 0;
    int mySideMaxLife = 0;
    for (FightObject fightObject : mySide) {
      if (fightObject != null && !fightObject.isFallDown())
        mySideNum++; 
      mySideCurrLife += fightObject.getCurrLife();
      mySideMaxLife += fightObject.getMaxLife();
    } 
    int targetSideNum = 0;
    int targetSideCurrLife = 0;
    int targetSideMaxLife = 0;
    for (FightObject fightObject : targetSide) {
      if (fightObject != null && !fightObject.isFallDown())
        targetSideNum++; 
      targetSideCurrLife += fightObject.getCurrLife();
      targetSideMaxLife += fightObject.getMaxLife();
    } 
    if (mySideNum > targetSideNum)
      return 1; 
    if (mySideNum < targetSideNum)
      return 0; 
    int myDefeatPet = 0;
    for (FightObject fightObject : mySide) {
      if (fightObject.isPlayer())
        myDefeatPet += FightParamPool.getDeathPetNum(getId(), fightObject.getId().intValue()); 
    } 
    int targetDefeatPet = 0;
    for (FightObject fightObject : targetSide) {
      if (fightObject.isPlayer())
        targetDefeatPet += FightParamPool.getDeathPetNum(getId(), fightObject.getId().intValue()); 
    } 
    if (myDefeatPet < targetDefeatPet)
      return 1; 
    if (myDefeatPet > targetDefeatPet)
      return 0; 
    if (mySideMaxLife > 0 && targetSideMaxLife > 0) {
      String mySidePCTStr = BigDecimalUtil.divide(String.valueOf(mySideCurrLife), String.valueOf(mySideMaxLife), 4);
      String targetSidePCTStr = BigDecimalUtil.divide(String.valueOf(targetSideCurrLife), String.valueOf(targetSideMaxLife), 4);
      Double mySidePCT = Double.valueOf(mySidePCTStr);
      Double targetSidePCT = Double.valueOf(targetSidePCTStr);
      if (mySidePCT.doubleValue() > targetSidePCT.doubleValue())
        return 1; 
      if (mySidePCT.doubleValue() < targetSidePCT.doubleValue())
        return 0; 
    } 
    int myControlNum = 0;
    for (FightObject fightObject : mySide) {
      if (fightObject.isPlayer())
        myControlNum += FightParamPool.getControlNum(getId(), fightObject.getId().intValue()); 
    } 
    int targetControlNum = 0;
    for (FightObject fightObject : targetSide) {
      if (fightObject.isPlayer())
        targetControlNum += FightParamPool.getControlNum(getId(), fightObject.getId().intValue()); 
    } 
    if (myControlNum < targetControlNum)
      return 1; 
    if (myControlNum > targetControlNum)
      return 0; 
    return returnFlag;
  }
  
  public void addExitRole(Boolean isA, FieldValue fieldValue) {
    Set<FieldValue> fieldValues = this.exitRoles.get(isA);
    if (fieldValues == null) {
      fieldValues = new HashSet<>();
      this.exitRoles.put(isA, fieldValues);
    } 
    fieldValues.add(fieldValue);
  }
  
  public boolean isSameCamp(int objIdA, int objIdB) {
    boolean isInA = isSameCamp(this.listA, objIdA, objIdB);
    if (isInA)
      return true; 
    boolean isInB = isSameCamp(this.listB, objIdA, objIdB);
    if (isInB)
      return true; 
    return false;
  }
  
  private boolean isSameCamp(List<FightObject> list, int objIdA, int objIdB) {
    if (list != null && list.size() > 0) {
      int flag = 0;
      for (FightObject obj : list) {
        if (obj.getId().equals(Integer.valueOf(objIdA)))
          flag++; 
        if (obj.getId().equals(Integer.valueOf(objIdB)))
          flag++; 
      } 
      if (flag == 2)
        return true; 
      if (flag == 1)
        return false; 
    } 
    return false;
  }
  
  public Fight() {}
}
