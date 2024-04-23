package com.kitty.system.game.fightnew.utils;

import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FightBeforeActionUtil {
  public static void beforeAction(Fight fight) {
    try {
      excuteBeforeAction(fight);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private static void excuteBeforeAction(Fight fight) {
    List<FightObject> listA = (List<FightObject>)fight.getListA().stream().filter(obj -> (LOGIC.LOGICTYPE_PET.getValue() == obj.getLogic() && !obj.isFallDown())).collect(Collectors.toList());
    List<FightObject> listB = (List<FightObject>)fight.getListB().stream().filter(obj -> (LOGIC.LOGICTYPE_PET.getValue() == obj.getLogic() && !obj.isFallDown())).collect(Collectors.toList());
    Map<Integer, List<Integer>> mapActionA = getActionSkill(fight, listA);
    Map<Integer, List<Integer>> mapActionB = getActionSkill(fight, listB);
    if (mapActionA != null) {
      List<FightObject> targets = fight.getTargetSide(((FightObject)listA.get(0)).getId().intValue());
      excuteSendAction(fight, targets, mapActionA);
    } 
    if (mapActionB != null) {
      List<FightObject> targets = fight.getTargetSide(((FightObject)listB.get(0)).getId().intValue());
      excuteSendAction(fight, targets, mapActionB);
    } 
    for (FightObject fightObject : fight.getAllFightObjects()) {
      if (fightObject.getLogic() == LOGIC.LOGICTYPE_PET.getValue())
        PetPassiveSkillUtil.enteringSideAction(fight, fightObject); 
    } 
  }
  
  public static Map<Integer, List<Integer>> getActionSkill(Fight fight, List<FightObject> petList) {
    petList = (List<FightObject>)petList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
    if (petList.size() > 0) {
      Map<Integer, List<Integer>> petSkillMap = new LinkedHashMap<>();
      petList.stream().forEach(pet -> {
            List<Integer> listAction = new ArrayList<>();
        pet.getSkillIds().stream().filter((skill) -> {
          return -1 != skill.getSkillId();
        }).forEach((skill) -> {
          int[] var3;
          int var4;
          int var5;
          int beforeSkill;
          if (fight.isPvp() == 1) {
            var3 = SkillConst.beforeActionSkillPVP;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
              beforeSkill = var3[var5];
              if (beforeSkill == skill.getSkillId()) {
                listAction.add(beforeSkill);
              }
            }
          } else {
            var3 = SkillConst.beforeActionSkillNPVP;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
              beforeSkill = var3[var5];
              if (beforeSkill == skill.getSkillId()) {
                listAction.add(beforeSkill);
              }
            }
          }
          });
            //pet.getSkillIds().stream().filter(()).forEach(());
            if (listAction.size() > 0)
              petSkillMap.put(pet.getId(), listAction); 
          });
      if (petSkillMap.size() > 0)
        return petSkillMap; 
    } 
    return null;
  }
  
  public static void excuteSendAction(Fight fight, List<FightObject> targets, Map<Integer, List<Integer>> mapAction) {
    FightObject target = getTargetForPos(targets, 3);
    if (target == null)
      return; 
    sendAction(fight, target, mapAction);
  }
  
  public static FightObject getTargetForPos(List<FightObject> targets, int pos) {
    FightObject target = null;
    Optional<FightObject> first = targets.stream().filter(obj -> (obj.getPos() % 10000 == pos)).findFirst();
    if (first.isPresent())
      target = first.get(); 
    if (target == null && targets != null && targets.size() > 0)
      target = targets.get(0); 
    return target;
  }
  
  private static void sendAction(Fight fight, FightObject target, Map<Integer, List<Integer>> mapAction) {
    BeforeActionSkill beforeActionSkillMy = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(!target.isASide()));
    BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(target.isASide()));
    BeforeActionSkill beforeActionSkill1 = assembleAction(fight, mapAction, target, beforeActionSkillMy, beforeActionSkill, fight.getActionParam().getActionDetailParams());
    saveBeforeActionSkill(fight, target, beforeActionSkill1.getRoleSkillMap(), beforeActionSkill1.getSkills());
  }
  
  public static BeforeActionSkill assembleAction(Fight fight, Map<Integer, List<Integer>> mapAction, FightObject target, BeforeActionSkill beforeActionSkillMy, BeforeActionSkill beforeActionSkill, List<ActionDetailParam> actionDetailParams) {
    boolean flag = true;
    List<Integer> skillIdList = new ArrayList<>();
    Map<Integer, List<Integer>> map = new HashMap<>();
    for (Map.Entry<Integer, List<Integer>> entry : mapAction.entrySet()) {
      List<Integer> skillIds = new ArrayList<>();
      Integer key = entry.getKey();
      for (Integer skill : entry.getValue()) {
        SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skill);
        if (petSkill == null)
          continue; 
        if ((skillIdList.contains(skill) || (beforeActionSkill != null && beforeActionSkill.getSkills().contains(skill))) && PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_FIRST_MAGIC
          .getValue().equals(petSkill.getTriggerType()))
          continue; 
        if (skillIdList.contains(skill))
          flag = false; 
        if (Arrays.<Integer>asList(SkillConst.beforeActionSkillOnlyOnce).contains(skill)) {
          if (81028 == skill.intValue()) {
            if (skillIdList.contains(Integer.valueOf(82009)))
              continue; 
            if (beforeActionSkillMy != null && beforeActionSkillMy.getSkills().contains(Integer.valueOf(82009)))
              continue; 
          } 
          if (82009 == skill.intValue()) {
            if (skillIdList.contains(Integer.valueOf(81028))) {
              Integer petId = deleteAction(map, Integer.valueOf(81028));
              if (petId.intValue() != 0)
                sendCancelActionExcute(actionDetailParams, petId, Integer.valueOf(81028)); 
            } 
            if (beforeActionSkill != null && beforeActionSkill.isPassiveAction() && beforeActionSkill
              .getSkills().contains(Integer.valueOf(81028))) {
              Integer petId = deleteAction(beforeActionSkill.getRoleSkillMap(), Integer.valueOf(81028));
              if (petId.intValue() != 0)
                sendCancelActionExcute(actionDetailParams, petId, Integer.valueOf(81028)); 
            } 
            if (beforeActionSkillMy != null && beforeActionSkillMy.getSkills().contains(Integer.valueOf(81028))) {
              Integer petId = deleteAction(beforeActionSkillMy.getRoleSkillMap(), Integer.valueOf(81028));
              if (petId.intValue() != 0)
                sendCancelActionExcute(actionDetailParams, petId, Integer.valueOf(81028)); 
            } 
          } 
        } 
        skillIds.add(skill);
        skillIdList.add(skill);
        if (!flag || (
          Arrays.<Integer>asList(SkillConst.beforeActionSkillOnlyOnce).contains(skill) && 
          beforeActionSkillMy != null && beforeActionSkillMy.isPassiveAction() && beforeActionSkillMy.getSkills().contains(skill)))
          continue; 
        sendActionExcute(fight.queryFightObject(key), actionDetailParams, skill.intValue(), target.getId());
      } 
      if (skillIds.size() > 0)
        map.put(key, skillIds); 
    } 
    BeforeActionSkill beforeActionSkillReturn = new BeforeActionSkill();
    beforeActionSkillReturn.setSkills(skillIdList);
    beforeActionSkillReturn.setRoleSkillMap(map);
    return beforeActionSkillReturn;
  }
  
  public static void saveBeforeActionSkill(Fight fight, FightObject target, Map<Integer, List<Integer>> map, List<Integer> skillIdList) {
    BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(target.isASide()));
    if (beforeActionSkill == null || !beforeActionSkill.isPassiveAction()) {
      beforeActionSkill = new BeforeActionSkill();
      beforeActionSkill.setRoleSkillMap(map);
      beforeActionSkill.setSkills(skillIdList);
    } else {
      beforeActionSkill.getRoleSkillMap().putAll(map);
      beforeActionSkill.getSkills().addAll(skillIdList);
    } 
    beforeActionSkill.setASide(target.isASide());
    beforeActionSkill.setRound(1);
    beforeActionSkill.setPassiveAction(true);
    fight.getBeforeActionMap().put(Boolean.valueOf(target.isASide()), beforeActionSkill);
  }
  
  public static void sendActionExcute(FightObject effector, List<ActionDetailParam> actionDetailParams, int skill, Integer targetId) {
    ActionParamUtils.advanceNotice(effector, actionDetailParams, targetId.intValue(), skill);
  }
  
  public static Integer deleteAction(Map<Integer, List<Integer>> map, Integer skillId) {
    if (map != null && map.size() > 0) {
      List<Integer> removeList = new ArrayList<>();
      Integer petId = Integer.valueOf(0);
      int index = 0;
      for (Map.Entry<Integer, List<Integer>> entryTemp : map.entrySet()) {
        List<Integer> valueTemp = entryTemp.getValue();
        Integer keyTemp = entryTemp.getKey();
        if (valueTemp != null && valueTemp.size() > 0 && valueTemp.contains(skillId)) {
          valueTemp.remove(skillId);
          if (index == 0)
            petId = keyTemp; 
          index++;
        } 
        if (valueTemp == null || valueTemp.size() <= 0)
          removeList.add(keyTemp); 
      } 
      for (Integer removeObj : removeList)
        map.remove(removeObj); 
      return petId;
    } 
    return Integer.valueOf(0);
  }
  
  public static void sendCancelActionExcute(List<ActionDetailParam> actionDetailParams, Integer actorId, Integer skill) {
    if (actionDetailParams != null) {
      int removeIndex = -1;
      for (int i = 0; i < actionDetailParams.size(); i++) {
        if (((ActionDetailParam)actionDetailParams.get(i)).getType() == 33 && ((ActionDetailParam)actionDetailParams.get(i)).getAttackerId() == actorId.intValue() && ((ActionDetailParam)actionDetailParams
          .get(i)).getSkillSerial() == skill.intValue()) {
          removeIndex = i;
          break;
        } 
      } 
      if (removeIndex != -1)
        actionDetailParams.remove(removeIndex); 
    } 
  }
}
