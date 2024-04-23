package com.kitty.system.game.fightnew.book.pet;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.MonsterFightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuSkillUtils;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.book.vo.StatusForSkillDispose;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.vo.AssistParam;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class PetPassiveSkillUtil {
  public static void roundStartPassive(Fight fight) {
    try {
      excuteRoundStartPassive(fight);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private static void excuteRoundStartPassive(Fight fight) {
    ArrayList<FightObject> allFightObjects = fight.getAllFightObjects();
    for (FightObject effector : allFightObjects) {
      SkillProcessor skillProcessor = null;
      if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic() && !effector.isFallDown()) {
        int isPvp = fight.isPvp();
        List<RoleSkill> petRoleSkills = effector.getSkillIds().stream().filter(skill -> (-1 != skill.getSkillId())).collect(Collectors.toList());
        List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START.getValue(), petRoleSkills, isPvp);
        if (petSkillList.size() > 0)
          for (SysPetSkill petSkill : petSkillList) {
            skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
            skillProcessor.passive(fight, effector, petSkill);
          }  
        if (fight.getRound() == 1) {
          List<FightObject> enemyPetSide = fight.getTargetSide(effector.getId()).stream().filter(obj -> (!obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue())).collect(Collectors.toList());
          for (FightObject fightObject : enemyPetSide) {
            List<SysPetSkill> petSkillListEntering = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_ENTERING_SIDE.getValue(), fightObject
                .getSkillIds().stream().filter(skill -> (-1 != skill.getSkillId())).collect(Collectors.toList()), isPvp);
            if (petSkillListEntering != null && petSkillListEntering.size() > 0)
              for (SysPetSkill petSkill : petSkillListEntering) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                skillProcessor.passive(fight, fightObject, effector, petSkill);
              }  
          } 
        } 
        Map<Integer, SysPetSkill> petSkillMapForN = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_CONTROL.getValue());
        if (petSkillMapForN.size() > 0)
          for (RoleSkill roleSkill : petRoleSkills) {
            SysPetSkill petSkill = petSkillMapForN.get(roleSkill.getSkillId());
            if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
              skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
              skillProcessor.passive(fight, effector, new Object[0]);
            } 
          }  
        if ((effector.getCurrLife() / effector.getMaxLife()) < 0.4D) {
          List<SysPetSkill> petSkillList40 = PetTriggerPassiveSkill.currLifeLessThan40(petRoleSkills, isPvp);
          if (petSkillList40 != null)
            for (SysPetSkill petSkill : petSkillList40) {
              skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
              skillProcessor.passive(fight, effector, new Object[0]);
            }  
        } 
      } 
      TianFuSkillUtils.enteringSide(fight, effector);
      TianFuSkillUtils.roundStart(fight, effector);
      if (fight.getRound() == 1)
        PassiveSkillUtils.enterFight(fight, effector); 
    } 
    roundStartAction(fight);
    deathExcute(fight);
  }
  
  private static void roundStartAction(Fight fight) {
    BeforeActionSkill beforeActionSkillA = fight.getBeforeActionMap().get(Boolean.TRUE);
    BeforeActionSkill beforeActionSkillB = fight.getBeforeActionMap().get(Boolean.FALSE);
    roundStartActionExcute(fight, beforeActionSkillA, beforeActionSkillB, PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START_ACTION.getValue(), false);
  }
  
  public static void roundStartActionExcute(Fight fight, BeforeActionSkill beforeActionSkillM, BeforeActionSkill beforeActionSkillT, String type, boolean isA) {
    Map<Integer, SysPetSkill> resourceActionMap = PetTriggerPassiveSkill.querySkills(type);
    boolean flag = false;
    for (Map.Entry<Integer, SysPetSkill> entry : resourceActionMap.entrySet()) {
      SysPetSkill petSkill = entry.getValue();
      if (beforeActionSkillM != null && beforeActionSkillM.isPassiveAction())
        for (Map.Entry<Integer, List<Integer>> entry1 : beforeActionSkillM.getRoleSkillMap().entrySet()) {
          if ((entry1.getValue()).contains(petSkill.getSerial())) {
            FightObject obj = fight.queryFightObject(entry1.getKey());
            if (obj == null || obj.isFallDown())
              return; 
            SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(entry.getValue().getSerial());
            passiveSkillProcessor.passive(fight, obj, petSkill);
            flag = true;
            break;
          } 
        }  
      if (flag) {
        flag = false;
        break;
      } 
      if (beforeActionSkillT != null && beforeActionSkillT.isPassiveAction())
        for (Map.Entry<Integer, List<Integer>> entry1 : beforeActionSkillT.getRoleSkillMap().entrySet()) {
          if ((entry1.getValue()).contains(petSkill.getSerial())) {
            FightObject obj = fight.queryFightObject(entry1.getKey());
            if (obj == null || obj.isFallDown())
              return; 
            SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(entry.getValue().getSerial());
            passiveSkillProcessor.passive(fight, obj, petSkill);
          } 
        }  
    } 
  }
  
  public static void enteringSideAction(Fight fight, FightObject petObject) {
    Map<Integer, SysPetSkill> enteringSideSysPetSkillMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_ACTION.getValue());
    List<RoleSkill> petRoleSkill = petObject.getSkillIds().stream().filter(skill -> (skill.getSkillId() != -1)).collect(Collectors.toList());
    if (petRoleSkill.size() > 0)
      for (Map.Entry<Integer, SysPetSkill> entry : enteringSideSysPetSkillMap.entrySet()) {
        for (RoleSkill roleSkill : petRoleSkill) {
          if (entry.getKey().equals(roleSkill.getSkillId())) {
            SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(roleSkill.getSkillId());
            passiveSkillProcessor.passive(fight, petObject, new Object[0]);
          } 
        } 
      }  
  }
  
  public static double getSuffix(Integer lv, Integer rei, long close) {
    return Math.pow(lv, 0.5D) / 10.0D + Math.pow(close, 0.16666666666666666D) * 10.0D / (100 + rei * 20);
  }
  
  public static List<FiedValue> clearStatus(FightObject target, String type) {
    if (target != null) {
      StatusForSkillDispose statusForSkillDispose = new StatusForSkillDispose();
      List<Integer> statusList = new ArrayList<>();
      List<FiedValue> list = new ArrayList<>();
      switch (type) {
        case "abnormal":
          statusList = statusForSkillDispose.disposeAbnormalStatus(target, true);
          if (statusList.size() > 0)
            list.add(new FiedValue("targetClearStatus", (byte)3, statusList)); 
          break;
        case "positive":
          if (!target.isFrozen()) {
            for (Iterator<Integer> iterator = StatusConst.getPositiveStatus().iterator(); iterator.hasNext(); ) {
              int status = iterator.next();
              if (target.getStatusMap().getInt(String.valueOf(status)) > 0) {
                statusList.add(status);
                statusForSkillDispose.disposePositiveStatus(target, status, true);
                if (1023 == status) {
                  list.add(new FiedValue("targetMaxHp", (byte)3, target.getMaxLife()));
                  list.add(new FiedValue("targetMaxMp", (byte)3, target.getMaxMana()));
                  list.add(new FiedValue("targetCurrHp", (byte)3, target.getCurrLife()));
                  list.add(new FiedValue("targetCurrMp", (byte)3, target.getCurrMana()));
                } 
              } 
            } 
            if (statusList.size() > 0)
              list.add(new FiedValue("targetClearStatus", (byte)3, statusList)); 
          } 
          break;
        case "wuXinModification":
          if (!target.isFrozen()) {
            statusList = statusForSkillDispose.disposeWuXinModification(target, true);
            if (statusList.size() > 0)
              list.add(new FiedValue("targetClearStatus", (byte)3, statusList)); 
          } 
          break;
      } 
      return list;
    } 
    return null;
  }
  
  public static void otherEnterBattle(Fight fight, Integer newPetId) {
    try {
      excuteOtherEnterBattle(fight, newPetId);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private static void excuteOtherEnterBattle(Fight fight, Integer newPetId) {
    FightObject petObject = fight.queryFightObject(newPetId);
    List<RoleSkill> petSkills = petObject.getSkillIds().stream().filter(skill -> (-1 != skill.getSkillId())).collect(Collectors.toList());
    SkillProcessor skillProcessor = null;
    List<SysPetSkill> petSkillListNF = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF.getValue(), petSkills, fight.isPvp());
    if (petSkillListNF.size() > 0)
      for (SysPetSkill petSkill : petSkillListNF) {
        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
        skillProcessor.passive(fight, petObject, null, petSkill);
      }  
    List<FightObject> enemyPetSide = fight.getTargetSide(petObject.getId()).stream().filter(obj -> (!obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue())).collect(Collectors.toList());
    for (FightObject fightObject : enemyPetSide) {
      List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_ENTERING_SIDE.getValue(), fightObject
          .getSkillIds().stream().filter(skill -> (-1 != skill.getSkillId())).collect(Collectors.toList()), fight.isPvp());
      if (petSkillList != null && petSkillList.size() > 0)
        for (SysPetSkill petSkill : petSkillList) {
          skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
          skillProcessor.passive(fight, fightObject, petObject, petSkill);
        }  
    } 
    if (LOGIC.LOGICTYPE_PET.getValue() == petObject.getLogic())
      fight.excuteReduceRatio(petObject, true); 
    Map<Integer, List<Integer>> petSkillMap = PetSkillEffectorUtil.getActionSkill(fight, Arrays.asList(petObject));
    if (petSkillMap != null && petSkillMap.size() > 0) {
      List<FightObject> targets = fight.getTargetSide(petObject.getId());
      PetSkillEffectorUtil.excuteSendAction(fight, targets, petSkillMap);
    } 
    enteringSideAction(fight, petObject);
    List<SysPetSkill> petSkillListEntering = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE.getValue(), petSkills, fight.isPvp());
    petSkillListEntering = petSkillListEntering.stream().sorted(Comparator.comparing(SysPetSkill::getExecutionSequence)).collect(Collectors.toList());
    if (petSkillListEntering.size() > 0)
      for (SysPetSkill petSkill : petSkillListEntering) {
        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
        skillProcessor.passive(fight, petObject, petSkill);
      }  
    List<SysPetSkill> petSkillListNFAction = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF_ACTION.getValue(), petSkills, fight.isPvp());
    if (petSkillListNFAction.size() > 0)
      for (SysPetSkill petSkill : petSkillListNFAction) {
        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
        skillProcessor.passive(fight, petObject, null, petSkill);
      }  
    TianFuSkillUtils.enteringSide(fight, petObject);
    AwakenSkillUtils.enteringSide(fight, petObject);
    PassiveSkillUtils.enterFight(fight, petObject);
  }
  
  public static boolean isExcuteControlleSkill(FightObject effector, int count) {
    boolean returnFlag = false;
    NutMap statusMap = effector.getStatusMap();
    int anInt = statusMap.getInt("1999record");
    if (anInt >= count)
      returnFlag = true; 
    return returnFlag;
  }
  
  public static boolean isAbnormalOrPositiveStatus(NutMap map, String type) {
    List<Integer> statuss = null;
    if ("abnormal".equals(type)) {
      statuss = StatusConst.getAnomalousStatus();
    } else if ("positive".equals(type)) {
      statuss = StatusConst.getPositiveStatus();
    } else if ("wuXinModification".equals(type)) {
      statuss = PRO_DICT_TYPE.getWuXinDist();
    } 
    for (Iterator<Integer> iterator = statuss.iterator(); iterator.hasNext(); ) {
      int status = iterator.next();
      if (map.getInt(String.valueOf(status)) > 0)
        return true; 
    } 
    return false;
  }
  
  public static void deathExcute(Fight fight) {
    try {
      petDeathExcute(fight);
      monsterDeathExcute(fight);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private static void petDeathExcute(Fight fight) {
    List<FightObject> fallDownObjs = fight.getAllFightObjects().stream().filter(obj -> (obj.isFallDown() && obj.isPet())).collect(Collectors.toList());
    if (fallDownObjs != null && fallDownObjs.size() > 0) {
      for (FightObject target : fallDownObjs) {
        if (!fight.getDeadPetIds().contains(target.getId()))
          excuteDieAction(fight, target); 
      } 
      petDeathExcute(fight);
    } else {
      List<FightObject> fallDownPlayers = fight.getAllFightObjects().stream().filter(obj -> (obj.isFallDown() && obj.isPlayer())).collect(Collectors.toList());
      for (FightObject obj : fallDownPlayers) {
        FightObject objPet = fight.queryFightObject(obj.getFightPetId());
        if (objPet != null) {
          boolean flag = PetTriggerPassiveSkill.heroFallDown(fight, objPet);
          if (flag)
            petDeathExcute(fight); 
        } 
        if (obj.isFallDown() && 
          obj.getDieRound() <= 0)
          obj.setDieRound(fight.getRound()); 
      } 
    } 
  }
  
  public static void monsterDeathExcute(Fight fight) {
    List<FightObject> fallDownMonsters = fight.getAllFightObjects().stream().filter(obj -> (obj.isFallDown() && obj.isMonster())).collect(Collectors.toList());
    if (fallDownMonsters != null && fallDownMonsters.size() > 0)
      for (FightObject monsterOjb : fallDownMonsters) {
        int monsterType = ((MonsterFightObject)monsterOjb).getMonsterType();
        SysDataMonster monster = FightDataPool.data_monster.get(monsterOjb.getSerial());
        if (monster != null && monster.getVanish() == 1)
          return; 
        if (!Const.vanish.contains(monsterType)) {
          sendDisappearData(fight, monsterOjb);
          fight.removeFightObject(monsterOjb);
        } 
      }  
  }
  
  public static void excuteDieAction(Fight fight, FightObject target) {
    if (target.isPet() || target.isPetMirror() || target.isMonster()) {
      if (target.isPet()) {
        PetFightObject petFightObject = (PetFightObject)target;
        FightParamPool.addDeathPet(fight, target.getHostId(), petFightObject);
      } 
      boolean isRevive = petDieProcessing(fight, target);
      if (!isRevive) {
        AwakenSkillUtils.exitSide(fight, target);
        sendDisappearData(fight, target);
        triggerFlashBeforeOne(fight, target);
      } 
    } 
  }
  
  public static void sendDisappearData(Fight fight, FightObject target) {
    ActionParamUtils.excuteDisappear(fight, target, target.getId(), 46, 0, 1);
  }
  
  public static boolean petDieProcessing(Fight fight, FightObject target) {
    boolean isRevive = false;
    if (target.isPet() || target.isPetMirror()) {
      isRevive = excuteReviveSkill(fight, target);
      if (!isRevive)
        isRevive = AwakenSkillUtils.revive(fight, target); 
      if (!isRevive)
        fight.excuteExitPassive(target); 
    } 
    if (!isRevive)
      fight.petDie(target); 
    return isRevive;
  }
  
  public static void friendMagic(Fight fight, FightObject attacker, int isPvp, ActionDetailParam actionDetailParam) {
    List<FightObject> objList = getSkillForPet(fight, attacker.isASide());
    Map<Integer, SysPetSkill> resourceMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.FRIEND_MAGIC.getValue());
    for (FightObject obj : objList) {
      List<RoleSkill> roleSkills = obj.getSkillIds().stream().filter(roleSkill -> (-1 != roleSkill.getSkillId())).collect(Collectors.toList());
      for (RoleSkill roleSkill : roleSkills) {
        SysPetSkill petSkill = resourceMap.get(roleSkill.getSkillId());
        if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
          SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
          passiveSkillProcessor.normalAttack(fight, obj, attacker, actionDetailParam, new Object[0]);
        } 
      } 
    } 
  }
  
  private static List<FightObject> getSkillForPet(Fight fight, boolean isASide) {
    List<FightObject> objList = null;
    if (isASide) {
      objList = fight.getListA().stream().filter(obj -> (obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue())).collect(Collectors.toList());
    } else {
      objList = fight.getListB().stream().filter(obj -> (obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue())).collect(Collectors.toList());
    } 
    return objList;
  }
  
  public static boolean unitMagic(Fight fight, FightObject attacker, SkillProcessor processor, RoleSkill skill) {
    enemyMagic(fight, attacker, processor, skill, fight.isPvp());
    return false;
  }
  
  public static void enemyMagic(Fight fight, FightObject attacker, SkillProcessor attackerProcessor, RoleSkill skill, int isPvp) {
    List<FightObject> objList = getSkillForPet(fight, !attacker.isASide());
    Map<Integer, SysPetSkill> resourceMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_MAGIC.getValue());
    excutePetSkill(fight, attacker, attackerProcessor, skill, resourceMap, objList, isPvp);
    BeforeActionSkill beforeActionSkillM = fight.getBeforeActionMap().get(attacker.isASide());
    BeforeActionSkill beforeActionSkillT = fight.getBeforeActionMap().get(!attacker.isASide());
    Map<Integer, SysPetSkill> resourceActionMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.UNIT_MAGIC_ACTION.getValue());
    boolean flag = false;
    for (Map.Entry<Integer, SysPetSkill> entry : resourceActionMap.entrySet()) {
      if (beforeActionSkillM != null && beforeActionSkillM.isPassiveAction())
        for (Map.Entry<Integer, List<Integer>> entry1 : beforeActionSkillM.getRoleSkillMap().entrySet()) {
          if ((entry1.getValue()).contains(entry.getKey())) {
            SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(entry.getValue().getSerial());
            passiveSkillProcessor.effect(fight, fight.queryFightObject(entry1.getKey()), attacker, attackerProcessor.costMana(skill, new Object[0]));
            flag = true;
            break;
          } 
        }  
      if (flag) {
        flag = false;
        break;
      } 
      if (beforeActionSkillT != null && beforeActionSkillT.isPassiveAction())
        for (Map.Entry<Integer, List<Integer>> entry1 : beforeActionSkillT.getRoleSkillMap().entrySet()) {
          if (((List)entry1.getValue()).contains(entry.getKey())) {
            SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(entry.getValue().getSerial());
            passiveSkillProcessor.effect(fight, fight.queryFightObject(entry1.getKey()), attacker, attackerProcessor.costMana(skill, new Object[0]));
          } 
        }  
    } 
  }
  
  private static Effect excutePetSkill(Fight fight, FightObject attacker, SkillProcessor attackerProcessor, RoleSkill skill, Map<Integer, SysPetSkill> resourceMap, List<FightObject> objList, int isPvp) {
    Effect effect = null;
    for (FightObject obj : objList) {
      List<RoleSkill> roleSkills = obj.getSkillIds().stream().filter(roleSkill -> (-1 != roleSkill.getSkillId())).collect(Collectors.toList());
      for (RoleSkill roleSkill : roleSkills) {
        SysPetSkill petSkill = resourceMap.get(roleSkill.getSkillId());
        if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
          SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
          effect = passiveSkillProcessor.effect(fight, obj, attacker, new Object[] {attackerProcessor.costMana(skill, new Object[0]), petSkill });
        } 
      } 
    } 
    return effect;
  }
  
  public static boolean enemyFristMagic(Fight fight, FightObject attacker, SkillProcessor attackerProcessor, RoleSkill skill, int isPvp) {
    BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(attacker.isASide()));
    if (beforeActionSkill == null || !beforeActionSkill.isPassiveAction())
      return false; 
    boolean returnFlag = excuteEnemyMagic(fight, attacker, beforeActionSkill, PetTriggerPassiveSkill.data_pet_skill_maps
        .get(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_FIRST_MAGIC.getValue()), attackerProcessor.costMana(skill, new Object[0]));
    return returnFlag;
  }
  
  private static boolean excuteEnemyMagic(Fight fight, FightObject attacker, BeforeActionSkill beforeActionSkill, Map<Integer, SysPetSkill> petSkills, int hurtParam) {
    boolean returnFlag = false;
    List<Integer> keys = new ArrayList<>(beforeActionSkill.getRoleSkillMap().keySet());
    for (int j = keys.size() - 1; j >= 0; j--) {
      List<Integer> values = beforeActionSkill.getRoleSkillMap().get(keys.get(j));
      for (int i = values.size() - 1; i >= 0; i--) {
        Integer skillId = values.get(i);
        SysPetSkill petSkill = petSkills.get(skillId);
        if (petSkill != null) {
          SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(skillId);
          FightObject pet = fight.queryFightObject(keys.get(j));
          if (pet == null || pet.isFallDown())
            pet = attacker; 
          Effect effect = passiveSkillProcessor.effect(fight, pet, attacker, new Object[] {hurtParam, petSkill });
          if (effect != null && 99 == effect.getType())
            returnFlag = true; 
        } 
      } 
    } 
    return returnFlag;
  }
  
  public static void checkControlledStatus(FightObject target) {
    if (target.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
      NutMap statusMap = target.getStatusMap();
      int recordValue = 1;
      for (Map.Entry<String, Object> entry : statusMap.entrySet()) {
        if (entry.getValue() instanceof Integer && (Integer) entry.getValue() > 0 && (entry.getKey().equals(String.valueOf(1002)) || entry.getKey().equals(String.valueOf(1003)) || entry
          .getKey().equals(String.valueOf(1004)) || entry.getKey().equals(String.valueOf(1006)) || entry
          .getKey().equals(String.valueOf(1019)))) {
          if (statusMap.get("1999record") != null)
            recordValue = statusMap.getInt("1999record") + 1; 
          statusMap.put("1999record", recordValue);
          break;
        } 
      } 
    } 
  }
  
  public static ActionDetailParam passivePhysics(Fight fight, FightObject effector, FightObject target, BattleUnit battleUnit, ActionDetailParam actionDetailParam, int skillSerial) {
    createActionDetailParam(battleUnit, effector, target, actionDetailParam, skillSerial);
    if (effector.isPlayer()) {
      battleUnit.doPhyHit();
    } else if (effector.isPet()) {
      battleUnit.petDoPhyHit();
    } else if (effector.isMonster()) {
      if (fight.getSpecialMonsterType() == 1 && effector.getPos() == 10003 && effector.getName().equals("地飞星")) {
        battleUnit.doPhyHit();
      } else if (skillSerial > 80000 && skillSerial < 90000) {
        battleUnit.petDoPhyHit();
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
    fightNewService.checkOrderTarget(fight, Collections.singletonList(target.getId()));
    if (target.isFallDown()) {
      effector.addFallDownList(target.getId());
      fightNewService.checkGuessSuccess(fight);
      fight.onActorDie(target);
    } 
    if (effector.isFallDown())
      fight.onActorDie(effector); 
    return battleUnit.getActionDetailParam();
  }
  
  private static void createActionDetailParam(BattleUnit battleUnit, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, int currSkill) {
    if (actionDetailParam != null && (
      80011 == currSkill || 81032 == currSkill || 80023 == currSkill || 81024 == currSkill || 81002 == currSkill || 83039 == currSkill))
      battleUnit.setActionDetailParamBac(actionDetailParam); 
    battleUnit.createActionDetailParam(effector, target, 7, currSkill, true);
  }
  
  public static void magicOther(Fight fight, FightObject effector, FightObject target, RoleSkill skill, ActionDetailParam actionDetailParam) {
    Map<Integer, SysPetSkill> map = PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.MAGIC_ATTACK_END.getValue());
    if (skill != null && map.get(skill.getSkillId()) != null) {
      SkillProcessor skillProcessor = SkillProcessor.queryProcessor(skill.getSkillId());
      skillProcessor.normalAttack(fight, effector, target, actionDetailParam, new Object[] { skill.getSkillId() });
    } 
  }
  
  private static void triggerFlashBeforeOne(Fight fight, FightObject actor) {
    Optional<FightObject> mySideOpt = fight.getMySide(actor.getId()).stream().filter(obj -> (!obj.isFallDown() && !obj.getId().equals(actor.getId()))).findFirst();
    if (!mySideOpt.isPresent())
      return; 
    FightObject host = fight.queryFightObject(actor.getHostId());
    List<AssistParam> assistParams = ((PlayerFightObject)host).getAssistParams();
    Set<Integer> deadPetIds = fight.getDeadPetIds();
    int petId = 0;
    for (AssistParam assistParam : assistParams) {
      if (!deadPetIds.contains(assistParam.getPetId()) && !assistParam.isTrigger()) {
        petId = assistParam.getPetId();
        boolean isExcuteSuccess = excuteFlash(fight, host, petId);
        if (isExcuteSuccess)
          assistParam.setTrigger(true); 
        break;
      } 
    } 
  }
  
  private static boolean excuteFlash(Fight fight, FightObject host, Integer petId) {
    Map<Integer, SysPetSkill> petSkillMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.FLASH_BEFORE_ONE.getValue());
    Role role = host.getRole();
    Pet pet = (Pet)SpringUtils.getPetService().selectPet(role).get(petId);
    if (pet == null)
      return false; 
    boolean inFight = PetSkillEffectorUtil.isInFight(role, pet);
    if (!inFight)
      return false; 
    List<PetSkill> petSkills = pet.getSkills().stream().filter(skill -> (skill.getSerial() != -1)).collect(Collectors.toList());
    List<SysPetSkill> newSysPetSkills = new ArrayList<>();
    for (PetSkill skill : petSkills) {
      SysPetSkill petSkill = petSkillMap.get(skill.getSerial());
      if (petSkill != null && skill.getSerial() == petSkill.getSerial())
        newSysPetSkills.add(petSkill); 
    } 
    if (newSysPetSkills.size() <= 0)
      return false; 
    newSysPetSkills = newSysPetSkills.stream().sorted(Comparator.comparing(SysPetSkill::getSerial)).collect(Collectors.toList());
    Map<Integer, SysPetSkill> newSysPetSkillMap = newSysPetSkills.stream().collect(Collectors.toMap(SysPetSkill::getSerial, sysPetSkill -> sysPetSkill));
    SysPetSkill excuteSkill = newSysPetSkills.get(0);
    if (newSysPetSkills.size() > 1)
      excuteSkill = PetSkillEffectorUtil.queryCover(excuteSkill, newSysPetSkillMap); 
    SkillProcessor processor = SkillProcessor.queryProcessor(excuteSkill.getSerial());
    NutMap passive = processor.passive(fight, host, new Object[] { role, pet });
    if (passive == null)
      return false; 
    return true;
  }
  
  public static boolean excuteReviveSkill(Fight fight, FightObject attacker) {
    Map<Integer, SysPetSkill> reviveSkills = PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.REVIVE_SKILL.getValue());
    boolean isRevive = false;
    if (reviveSkills.size() > 0) {
      List<Integer> skillIds = PetTriggerPassiveSkill.getInegers(attacker.getSkillIds().stream().filter(roleSkill -> (roleSkill.getSkillId() > 1)).collect(Collectors.toList()));
      for (Map.Entry<Integer, SysPetSkill> entry : reviveSkills.entrySet()) {
        Integer key = entry.getKey();
        if (skillIds.contains(key)) {
          SkillProcessor skillProcessor = SkillProcessor.queryProcessor(key);
          NutMap passive = skillProcessor.passive(fight, attacker, new Object[0]);
          isRevive = passive.getBoolean("successful");
        } 
      } 
    } 
    return isRevive;
  }
  
  public static Effect getPassiveSkill(Fight fight, FightObject effector, FightObject target, RoleSkill skill, BattleUnit battleUnit, String skillTrigget, int hurt) {
    SkillProcessor skillProcessor = null;
    Effect effectPart = null;
    if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic()) {
      List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(skillTrigget, effector.getSkillIds(), fight.isPvp());
      if (petSkillList.size() > 0)
        for (SysPetSkill petSkill : petSkillList) {
          if (skill == null) {
            skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
            effectPart = skillProcessor.effect(fight, effector, target, hurt, petSkill, null, battleUnit);
            continue;
          } 
          if (!petSkill.getSerial().equals(skill.getSkillId())) {
            skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
            effectPart = skillProcessor.effect(fight, effector, target, hurt, petSkill, skill.getSkillId(), battleUnit);
          } 
        }  
    } 
    return effectPart;
  }
  
  public static void writeActionDetailParam(BattleUnit battleUnit, int currSkill) {
    if (80011 == currSkill || 81032 == currSkill || 80023 == currSkill || 81024 == currSkill || 81002 == currSkill || 83039 == currSkill) {
      ActionDetailParam actionDetailParamBac = battleUnit.getActionDetailParamBac();
      if (actionDetailParamBac != null) {
        Integer status = null;
        if (80011 == currSkill || 81032 == currSkill) {
          status = 105;
        } else if (80023 == currSkill || 81024 == currSkill) {
          status = 116;
        } else if (81002 == currSkill) {
          status = 117;
        } else if (83039 == currSkill) {
          status = 120;
        } 
        ActionParamUtils.setNormalAttack(battleUnit.getTarget().getId(), battleUnit.getActionDetailParamBac(), battleUnit.getActionDetailParam(), status);
        battleUnit.setActionDetailParam(battleUnit.getActionDetailParamBac());
      } 
    } 
  }
  
  public static void normalAttackDoubleHit(Fight fight, FightObject effector, FightObject target, BattleUnit battleUnit, int hurt, int skillSerial) {
    writeActionDetailParam(battleUnit, skillSerial);
    if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic() && hurt > 0) {
      SkillProcessor skillProcessor = null;
      List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.NORMAL_ATTACK.getValue(), effector.getSkillIds(), fight.isPvp());
      if (petSkillList.size() > 0)
        for (SysPetSkill petSkill : petSkillList) {
          if (isTriggerGFF(petSkill, skillSerial)) {
            skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
            skillProcessor.normalAttack(fight, effector, target, battleUnit.getActionDetailParam(), hurt, skillSerial);
          } 
        }  
    } 
  }
  
  private static boolean isTriggerGFF(SysPetSkill petSkill, int currSkill) {
    if (petSkill == null)
      return false; 
    if (currSkill <= 0)
      return true; 
    if (80011 != petSkill.getSerial() && 81032 != petSkill.getSerial() && 80023 != petSkill
            .getSerial() && 81024 != petSkill.getSerial() && 81002 != petSkill.getSerial())
      return true; 
    if (80011 == currSkill || 81032 == currSkill)
      return false; 
    if ((80023 == petSkill.getSerial().intValue() || 81024 == petSkill.getSerial().intValue()) && (80023 == currSkill || 81024 == currSkill))
      return false; 
    if (81002 == petSkill.getSerial().intValue() && 81002 == currSkill)
      return false; 
    return true;
  }
  
  public static void normalAttackAfter(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam) {
    if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic()) {
      SkillProcessor skillProcessor = null;
      List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.NORMAL_ATTACK_AFTER.getValue(), effector.getSkillIds(), fight.isPvp());
      if (petSkillList.size() > 0)
        for (SysPetSkill petSkill : petSkillList) {
          if (isTriggerGFF(petSkill, actionDetailParam.getSkillSerial())) {
            skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
            skillProcessor.normalAttack(fight, effector, target, actionDetailParam, new Object[0]);
          } 
        }  
    } 
  }
  
  public static int reduceDoubleHit(Fight fight, FightObject effector) {
    try {
      SkillProcessor skillProcessor = SkillProcessor.queryProcessor(81057);
      if (skillProcessor != null) {
        NutMap passive = skillProcessor.passive(fight, effector, new Object[0]);
        int num = passive.getInt("num");
        return num;
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return 0;
  }
}
