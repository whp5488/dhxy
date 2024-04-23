package com.kitty.system.game.fightnew.service;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.start.ServerService;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.vo.SkillAutoPicker;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import com.kitty.system.game.fightnew.messages.RespExcuteFight;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightObjectParam;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.ActionParam;
import com.kitty.system.game.fightnew.model.AutoFightSkillParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.task.FightingTask;
import com.kitty.system.game.fightnew.task.TimeOutNewTask;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FightMessageNewService {
  private final Logger logger = LoggerFactory.getLogger(FightMessageNewService.class);
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  PetService petService;
  
  @Autowired
  FightNewService fightNewService;
  
  @Autowired
  ServerService serverService;
  
  @Autowired
  FightExecutorNewService fightExecutorNewService;
  
  public AutoFightSkillParam selfAuto(FightObject fightObject, Fight fight) {
    if (fightObject.getAutoFight() == 1)
      try {
        AutoFightSkillParam autoFightSkillParam = new AutoFightSkillParam();
        autoFightSkillParam.setObjId(fightObject.getId().intValue());
        SkillAutoPicker autoPicker = this.fightNewService.getAutoObject(fightObject, fight);
        autoFightSkillParam.setSkillId(autoPicker.getSkillId());
        byte type = 0;
        if (autoPicker.isDef()) {
          type = 1;
        } else if (autoPicker.getSkillId() == 0) {
          type = 0;
        } else {
          type = (byte)autoPicker.getType();
        } 
        autoFightSkillParam.setAction(type);
        if (fightObject.isPlayer() && RACE.RACE_LONG.getValue() == fightObject.getRace()) {
          Map<String, Object> autoFightSet = fightObject.getRole().getExtendBox().getAutoFightSet();
          if (autoFightSet != null) {
            Integer born = (Integer)autoFightSet.get("born");
            if (born != null)
              autoFightSkillParam.setBorn(born.intValue()); 
          } 
        } 
        return autoFightSkillParam;
      } catch (Exception e) {
        this.logger.error("进战斗发送自动战斗状态报错==={}", Lang.getStackTrace(e));
      }  
    return null;
  }
  
  public ReqFightHandle autoFightOne(Fight fight, FightObject fightObject) {
    try {
      if (fight.isOver())
        return null; 
      IoSession session = fightObject.getSession();
      if (session == null) {
        ReqFightHandle reqFightHandle1 = new ReqFightHandle();
        reqFightHandle1.setRound(fight.getRound());
        reqFightHandle1.setAutoFight(1);
        reqFightHandle1.setAttackerId(fightObject.getId().intValue());
        reqFightHandle1.setType(0);
        reqFightHandle1.setSkillId(0);
        reqFightHandle1.setTargetId(0);
        return reqFightHandle1;
      } 
      ReqFightHandle reqFightHandle = new ReqFightHandle();
      reqFightHandle.setAutoFight(1);
      reqFightHandle.setRound(fight.getRound());
      reqFightHandle.setAttackerId(fightObject.getId().intValue());
      SkillAutoPicker autoPicker = this.fightNewService.getAutoObject(fightObject, fight);
      reqFightHandle.setTargetId(autoPicker.getTargetId());
      reqFightHandle.setSkillId(autoPicker.getSkillId());
      if (autoPicker.isDef()) {
        reqFightHandle.setType(1);
        return reqFightHandle;
      } 
      if (autoPicker.getType() == 5 && autoPicker.getSkillId() > 0) {
        reqFightHandle.setType(5);
        return reqFightHandle;
      } 
      if (reqFightHandle.getSkillId() == 0) {
        reqFightHandle.setType(0);
        return reqFightHandle;
      } 
      RoleSkill roleSkill = fightObject.getSkillBy(Integer.valueOf(reqFightHandle.getSkillId()));
      if (roleSkill != null) {
        reqFightHandle.setType(7);
        reqFightHandle.setSkillId(roleSkill.getSkillId().intValue());
        return reqFightHandle;
      } 
      reqFightHandle.setType(0);
      reqFightHandle.setSkillId(0);
      reqFightHandle.setTargetId(0);
      return reqFightHandle;
    } catch (Exception e) {
      LoggerUtils.error("定时战斗报错={}", e);
      ReqFightHandle reqFightHandle = new ReqFightHandle();
      reqFightHandle.setRound(fight.getRound());
      reqFightHandle.setAutoFight(1);
      reqFightHandle.setAttackerId(fightObject.getId().intValue());
      reqFightHandle.setType(1);
      reqFightHandle.setSkillId(0);
      reqFightHandle.setTargetId(fightObject.getId().intValue());
      return reqFightHandle;
    } 
  }
  
  public RespFightShouting fightShouting(FightObject fightObject, String type) {
    try {
      if (fightObject == null)
        return null; 
      List<NutMap> fightMsg = fightObject.getFightMsg();
      if (fightMsg == null || fightMsg.size() <= 0)
        return null; 
      Role role = fightObject.getRole();
      if (role == null) {
        role = SessionUtils.getRoleBySession(fightObject.getSession());
        if (role == null)
          return null; 
      } 
      int id = fightObject.getId().intValue();
      String content = null;
      switch (type) {
        case "战斗出场":
          for (NutMap nutMap : fightMsg) {
            if (nutMap.getInt("type") == 1)
              content = nutMap.getString("msg"); 
          } 
          break;
        case "法攻":
          for (NutMap nutMap : fightMsg) {
            if (nutMap.getInt("type") == 2)
              content = nutMap.getString("msg"); 
          } 
          break;
        case "挨打":
          for (NutMap nutMap : fightMsg) {
            if (nutMap.getInt("type") == 3)
              content = nutMap.getString("msg"); 
          } 
          break;
        case "主人倒地":
          for (NutMap nutMap : fightMsg) {
            if (nutMap.getInt("type") == 4)
              content = nutMap.getString("msg"); 
          } 
          break;
        case "使用药品":
          for (NutMap nutMap : fightMsg) {
            if (nutMap.getInt("type") == 5)
              content = nutMap.getString("msg"); 
          } 
          break;
      } 
      if (Strings.isEmpty(content))
        return null; 
      RespFightShouting respFightShouting = new RespFightShouting();
      respFightShouting.setId(id);
      respFightShouting.setMsg(content);
      return respFightShouting;
    } catch (Exception e) {
      LoggerUtils.error("", e);
      return null;
    } 
  }
  
  public RespFightShouting fightShouting(FightObject fightObject, String msg, int showTime, int msgDelayTime) {
    RespFightShouting fightShouting = new RespFightShouting();
    fightShouting.setId(fightObject.getId().intValue());
    fightShouting.setMsg(msg);
    fightShouting.setShow_time(showTime);
    fightShouting.setMsgDelayTime(msgDelayTime);
    return fightShouting;
  }
  
  public NutMap poisonOrRecoverActionParam(Fight fight, FightObject fightObject) {
    boolean poison = ((Boolean)fightObject.getTempCache("poison", Boolean.valueOf(false))).booleanValue();
    boolean recover = ((Boolean)fightObject.getTempCache("recover", Boolean.valueOf(false))).booleanValue();
    boolean alive = ((Boolean)fightObject.getTempCache("alive", Boolean.valueOf(false))).booleanValue();
    int poisonValue = ((Integer)fightObject.getTempCache("poisonValue", Integer.valueOf(0))).intValue();
    int recoverLife = ((Integer)fightObject.getTempCache("recoverLife", Integer.valueOf(0))).intValue();
    int recoverMana = ((Integer)fightObject.getTempCache("recoverManaValue", Integer.valueOf(0))).intValue();
    if (recover) {
      NutMap map = NutMap.NEW();
      map.setv("currLife", Integer.valueOf(fightObject.getCurrLife()));
      map.setv("changeLife", Integer.valueOf(recoverLife));
      if (recoverMana > 0) {
        map.setv("currMana", Integer.valueOf(fightObject.getCurrMana()));
        map.setv("changeMana", Integer.valueOf(recoverMana));
      } 
      map.setv("missed", Integer.valueOf(2));
      return map;
    } 
    if (poison && alive) {
      NutMap map = NutMap.NEW();
      if (!fightObject.isFrozen()) {
        map.setv("currLife", Integer.valueOf(fightObject.getCurrLife()));
        map.setv("changeLife", Integer.valueOf(-poisonValue));
        map.setv("missed", Integer.valueOf(1));
      } else {
        fightObject.removeStatus(1005);
        map.setv("clearStatus", Integer.valueOf(1005));
        map.setv("missed", Integer.valueOf(2));
      } 
      return map;
    } 
    return null;
  }
  
  public RespExcuteFight excuteSendFightMsg(Fight fight) {
    ActionParam actionParam = fight.getActionParam();
    int fightRound = actionParam.getFightRound();
    List<ActionDetailParam> actionDetailParams = actionParam.getActionDetailParams();
    if (actionDetailParams != null && actionDetailParams.size() > 0) {
      RespExcuteFight respExcuteFight = new RespExcuteFight();
      respExcuteFight.setRound(fightRound);
      List<ExcuteFightParam> excuteFightParams = getExcuteFightParams(actionDetailParams);
      if (excuteFightParams.size() > 0)
        respExcuteFight.setExcuteFightParams(excuteFightParams); 
      respExcuteFight.setEndTime(String.valueOf(System.currentTimeMillis()));
      return respExcuteFight;
    } 
    return null;
  }
  
  public List<ExcuteFightParam> getExcuteFightParams(List<ActionDetailParam> actionDetailParams) {
    List<ExcuteFightParam> excuteFightParams = new ArrayList<>();
    for (ActionDetailParam actionDetailParam : actionDetailParams) {
      ExcuteFightParam excuteFightParam = getExcuteParam(actionDetailParam);
      if (excuteFightParam != null)
        excuteFightParams.add(excuteFightParam); 
    } 
    return excuteFightParams;
  }
  
  private ExcuteFightParam getExcuteParam(ActionDetailParam actionDetailParam) {
    if (actionDetailParam == null)
      return null; 
    ExcuteFightParam excuteFightParam = new ExcuteFightParam();
    if (actionDetailParam.getType() != -1)
      excuteFightParam.setType(actionDetailParam.getType()); 
    if (actionDetailParam.getSkillSerial() != -1) {
      excuteFightParam.setSkillId(actionDetailParam.getSkillSerial());
      excuteFightParam.setHfType(actionDetailParam.getHfType());
    } 
    if (actionDetailParam.getColorId() != 0)
      excuteFightParam.setColorId(actionDetailParam.getColorId()); 
    if (actionDetailParam.getAttackerId() != 0)
      excuteFightParam.setAttaId(actionDetailParam.getAttackerId()); 
    if (actionDetailParam.getRoleId() != 0L)
      excuteFightParam.setRoleId(actionDetailParam.getRoleId()); 
    List<RefreshPet> petList = actionDetailParam.getPetList();
    if (petList != null && petList.size() > 0)
      excuteFightParam.setPetList(petList); 
    List<CombatAddObject> combatAddObjects = actionDetailParam.getCombatAddObjects();
    if (combatAddObjects != null && combatAddObjects.size() > 0)
      excuteFightParam.setCombatAddObjects(combatAddObjects); 
    List<FightObjectParam> targetParams = actionDetailParam.getTargetParamList();
    if (targetParams.size() > 0) {
      List<ExcuteFightObjectParam> targetExcuteParams = new ArrayList<>();
      for (FightObjectParam targetParam : targetParams) {
        if (targetParam != null) {
          ExcuteFightObjectParam excuteFightObjectParam = excuteParamTarget(targetParam);
          targetExcuteParams.add(excuteFightObjectParam);
        } 
      } 
      if (targetExcuteParams.size() > 0)
        excuteFightParam.setTargetParams(targetExcuteParams); 
    } 
    List<FightObjectParam> attackerParam = actionDetailParam.getAttackerParam();
    if (attackerParam != null && attackerParam.size() > 0) {
      FightObjectParam fightObjectParam = attackerParam.get(0);
      ExcuteFightObjectParam attaParam = new ExcuteFightObjectParam();
      List<FiedValue> fiedValues = getFiedValues(fightObjectParam, false);
      if (fiedValues != null)
        attaParam.setFiedValues(fiedValues); 
      if (fightObjectParam.getDamageType() != null && fightObjectParam.getDamageType().size() > 0)
        attaParam.setDamageType(fightObjectParam.getDamageType()); 
      if (fightObjectParam.getStatusMap() != null && fightObjectParam.getStatusMap().size() > 0) {
        List<Integer> statusList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)fightObjectParam.getStatusMap().entrySet()) {
          statusList.add(entry.getKey());
          statusList.add(entry.getValue());
        } 
        if (statusList.size() > 0)
          attaParam.setStatusList(statusList); 
      } 
      if (fightObjectParam.getClearStatus() != null && fightObjectParam.getClearStatus().size() > 0)
        attaParam.setClearStatus(fightObjectParam.getClearStatus()); 
      if (excuteFightParam.getAttaParams() == null)
        excuteFightParam.setAttaParams(new ArrayList()); 
      excuteFightParam.getAttaParams().add(attaParam);
    } 
    return excuteFightParam;
  }
  
  public ExcuteFightObjectParam excuteParamTarget(FightObjectParam targetParam) {
    ExcuteFightObjectParam excuteFightObjectParam = new ExcuteFightObjectParam();
    List<FiedValue> fiedValues = getFiedValues(targetParam, true);
    if (fiedValues != null)
      excuteFightObjectParam.setFiedValues(fiedValues); 
    if (targetParam.getDamageType() != null && targetParam.getDamageType().size() > 0)
      excuteFightObjectParam.setDamageType(targetParam.getDamageType()); 
    if (targetParam.getStatusMap() != null && targetParam.getStatusMap().size() > 0) {
      List<Integer> statusList = new ArrayList<>();
      for (Map.Entry<Integer, Integer> entry : (Iterable<Map.Entry<Integer, Integer>>)targetParam.getStatusMap().entrySet()) {
        statusList.add(entry.getKey());
        statusList.add(entry.getValue());
      } 
      if (statusList.size() > 0)
        excuteFightObjectParam.setStatusList(statusList); 
    } 
    if (targetParam.getClearStatus() != null && targetParam.getClearStatus().size() > 0)
      excuteFightObjectParam.setClearStatus(targetParam.getClearStatus()); 
    return excuteFightObjectParam;
  }
  
  public List<FiedValue> getFiedValues(FightObjectParam targetParam, boolean isTarget) {
    List<FiedValue> fiedValues = new ArrayList<>();
    fiedValues.add(new FiedValue("targetId", (byte)3, Integer.valueOf(targetParam.getObjId())));
    int isHit = 1;
    if (!targetParam.isHit())
      isHit = 0; 
    if (isTarget) {
      fiedValues.add(new FiedValue("isHit", (byte)1, Integer.valueOf(isHit)));
    } else if (4 == targetParam.getType()) {
      fiedValues.add(new FiedValue("isHit", (byte)1, Integer.valueOf(isHit)));
    } 
    if (targetParam.getChangeLife() != 0) {
      FiedValue fiedValue = new FiedValue("changeHp", (byte)3, Integer.valueOf(targetParam.getChangeLife()));
      fiedValues.add(fiedValue);
    } 
    if (targetParam.getChangeMana() != 0) {
      FiedValue fiedValue = new FiedValue("changeMp", (byte)3, Integer.valueOf(targetParam.getChangeMana()));
      fiedValues.add(fiedValue);
    } 
    if (isTarget) {
      if (targetParam.getChangeLife() != 0)
        fiedValues.add(new FiedValue("currHp", (byte)3, Integer.valueOf(targetParam.getCurrentLife()))); 
    } else {
      fiedValues.add(new FiedValue("currHp", (byte)3, Integer.valueOf(targetParam.getCurrentLife())));
    } 
    if (isTarget) {
      if (targetParam.getChangeMana() != 0)
        fiedValues.add(new FiedValue("currMp", (byte)3, Integer.valueOf(targetParam.getCurrentMana()))); 
    } else {
      fiedValues.add(new FiedValue("currMp", (byte)3, Integer.valueOf(targetParam.getCurrentMana())));
    } 
    if (targetParam.getMaxLife() != 0) {
      FiedValue fiedValue = new FiedValue("maxHp", (byte)3, Integer.valueOf(targetParam.getMaxLife()));
      fiedValues.add(fiedValue);
    } 
    if (targetParam.getMaxMana() != 0) {
      FiedValue fiedValue = new FiedValue("maxMp", (byte)3, Integer.valueOf(targetParam.getMaxMana()));
      fiedValues.add(fiedValue);
    } 
    if (targetParam.getSpecialStatus() > 0)
      fiedValues.add(new FiedValue("specialStatus", (byte)3, Integer.valueOf(targetParam.getSpecialStatus()))); 
    if (targetParam.getGuardTarget() > 0)
      fiedValues.add(new FiedValue("guardTarget", (byte)3, Integer.valueOf(targetParam.getGuardTarget()))); 
    RespFightShouting fightShouting = targetParam.getFightShouting();
    if (fightShouting != null) {
      if (fightShouting.getId() != 0) {
        FiedValue fiedValue1 = new FiedValue("msgTarget", (byte)3, Integer.valueOf(fightShouting.getId()));
        fiedValues.add(fiedValue1);
      } 
      if (fightShouting.getChannel() != 0) {
        FiedValue fiedValue1 = new FiedValue("channel", (byte)2, Short.valueOf(fightShouting.getChannel()));
        fiedValues.add(fiedValue1);
      } 
      if (fightShouting.getMsg() != null && !"".equals(fightShouting.getMsg())) {
        FiedValue fiedValue1 = new FiedValue("msg", (byte)4, fightShouting.getMsg());
        fiedValues.add(fiedValue1);
      } 
      if (fightShouting.getShow_time() != -1) {
        FiedValue fiedValue1 = new FiedValue("msgST", (byte)3, Integer.valueOf(fightShouting.getShow_time()));
        fiedValues.add(fiedValue1);
      } 
      FiedValue fiedValue = new FiedValue("msgDelayTime", (byte)3, Integer.valueOf(fightShouting.getMsgDelayTime()));
      fiedValues.add(fiedValue);
    } 
    if (fiedValues.size() > 0)
      return fiedValues; 
    return null;
  }
  
  public Map<Integer, Integer> sendFightObjectStatus(Fight fight, FightObject fightObject) {
    NutMap statusMap = fightObject.getStatusMap();
    List<Integer> removeList = new ArrayList<>();
    Map<Integer, Integer> map = new HashMap<>();
    statusMap.forEach((key, value) -> {
          if (value instanceof Integer && key.matches("^[0-9]+$"))
            if (((Integer)value).intValue() > 0) {
              map.put(Integer.valueOf(Integer.parseInt(key)), Integer.valueOf(Integer.parseInt(String.valueOf(value))));
            } else if (200 == Integer.parseInt(key)) {
              TianFuProcessor.queryProcessor(Integer.valueOf(323301)).otherExcute(fight, fightObject, new Object[] { Boolean.valueOf(false) });
              map.put(Integer.valueOf(Integer.parseInt(key)), Integer.valueOf(fightObject.getIcon()));
              removeList.add(Integer.valueOf(Integer.parseInt(key)));
            }  
        });
    if (removeList.size() > 0)
      for (Integer key : removeList)
        fightObject.removeStatus(key.intValue());  
    if (map.size() > 0)
      return map; 
    return null;
  }
  
  public void excuteRoundEnd(final Fight fight) {
    this.fightExecutorNewService.addFightTask(fight, new FightingTask() {
          public void run() {
            fight.cancelTask(fight.getTimeOutTask());
            TimeOutNewTask timeOut = new TimeOutNewTask(fight.getId());
            ScheduledFuture<?> timeOutTask = SchedulerManager.getInstance().schedule((Runnable)timeOut, 30500L);
            fight.setTimeOutTask(timeOutTask);
          }
        });
    for (FightObject fightObject : fight.getAllFightObjects()) {
      if (!fightObject.isDoAction() && 
        fightObject.isPlayer()) {
        Role role = (Role)DataCache.ONLINE_ROLES.get(fightObject.getRole().getId());
        if (role != null) {
          this.fightNewService.autoFightOne(fight, fightObject);
          setRoundStartOther(fightObject, fight);
          PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
          playerFightObject.setAnimationFinish(false);
        } 
      } 
    } 
    this.fightNewService.excuteRoundStartSend(fight);
    fight.setRoundType(1);
  }
  
  public void setRoundStartOther(FightObject fightObject, Fight fight) {
    if (fightObject.isPlayer()) {
      RespRoundStartParam respRoundStartParam = this.fightNewService.getRespRoundStartParam(fight, fightObject.getId().intValue());
      List<SkillCool> cools = fightObject.getSkillCoolList();
      if (cools.size() > 0)
        respRoundStartParam.addSkillCools(cools); 
      if (fightObject.getAutoFight() == 1)
        respRoundStartParam.setAutoFight(true); 
      AutoFightSkillParam autoFightSkillParam = selfAuto(fightObject, fight);
      if (autoFightSkillParam != null)
        respRoundStartParam.addAutoFightSkillParam(autoFightSkillParam); 
      FightObject petObject = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
      if (petObject != null) {
        List<SkillCool> petCools = petObject.getSkillCoolList();
        if (petCools.size() > 0)
          respRoundStartParam.addSkillCools(petCools); 
        if (fightObject.getAutoFight() == 1)
          petObject.setAutoFight(1); 
        AutoFightSkillParam autoFightSkillParamPet = selfAuto(petObject, fight);
        if (autoFightSkillParamPet != null)
          respRoundStartParam.addAutoFightSkillParam(autoFightSkillParamPet); 
      } 
      fight.getRespRoundStartParamMap().put(Integer.valueOf(respRoundStartParam.getObjId()), respRoundStartParam);
    } 
  }
}
