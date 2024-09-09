package com.kitty.system.game.fightnew.utils;

import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Arrays;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.HashMap;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import java.util.Optional;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.LOGIC;
import java.util.List;
import com.kitty.system.game.fightnew.bean.Fight;

public class FightBeforeActionUtil
{
    public FightBeforeActionUtil() {
    }
    
    public static void beforeAction(Fight fight) {
        try {
            excuteBeforeAction(fight);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void excuteBeforeAction(Fight fight) {
        List<FightObject> listA = (List)fight.getListA().stream().filter(obj -> LOGIC.LOGICTYPE_PET.getValue() == obj.getLogic() && !obj.isFallDown()).collect(Collectors.toList());
        List<FightObject> listB = (List)fight.getListB().stream().filter(obj -> LOGIC.LOGICTYPE_PET.getValue() == obj.getLogic() && !obj.isFallDown()).collect(Collectors.toList());
        Map<Integer, List<Integer>> mapActionA = getActionSkill(fight, listA);
        Map<Integer, List<Integer>> mapActionB = getActionSkill(fight, listB);
        if (mapActionA != null) {
            List<FightObject> targets = fight.getTargetSide((int)((FightObject)listA.get(0)).getId());
            excuteSendAction(fight, targets, mapActionA);
        }
        if (mapActionB != null) {
            List<FightObject> targets = fight.getTargetSide((int)((FightObject)listB.get(0)).getId());
            excuteSendAction(fight, targets, mapActionB);
        }
        for (FightObject fightObject : fight.getAllFightObjects()) {
            if (fightObject.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
                PetPassiveSkillUtil.enteringSideAction(fight, fightObject);
            }
            else {
                continue;
            }
        }
    }
    
    public static Map<Integer, List<Integer>> getActionSkill(Fight fight, List<FightObject> petList) {
        petList = (List)petList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        if (petList.size() > 0) {
            Map<Integer, List<Integer>> petSkillMap = new LinkedHashMap();
            petList.stream().forEach(pet -> {
                List<Integer> listAction = new ArrayList<>();
                pet.getSkillIds().stream().filter(skill -> -1 != (int)skill.getSkillId()).forEach(skill -> {
                    if (fight.isPvp() == 1) {
                        int[] beforeActionSkillPVP = SkillConst.beforeActionSkillPVP;
                        int j = 0;
                        for (int length2 = beforeActionSkillPVP.length; j < length2; ++j) {
                            int beforeSkill2 = beforeActionSkillPVP[j];
                            if (beforeSkill2 == (int)skill.getSkillId()) {
                                listAction.add(Integer.valueOf(beforeSkill2));
                            }
                        }
                    }
                    else {
                        int[] beforeActionSkillNPVP = SkillConst.beforeActionSkillNPVP;
                        int i = 0;
                        for (int length = beforeActionSkillNPVP.length; i < length; ++i) {
                            int beforeSkill = beforeActionSkillNPVP[i];
                            if (beforeSkill == (int)skill.getSkillId()) {
                                listAction.add(Integer.valueOf(beforeSkill));
                            }
                        }
                    }
                    return;
                });
                if (listAction.size() > 0) {
                    petSkillMap.put(pet.getId(), listAction);
                }
                return;
            });
            if (petSkillMap.size() > 0) {
                return petSkillMap;
            }
        }
        return null;
    }
    
    public static void excuteSendAction(Fight fight, List<FightObject> targets, Map<Integer, List<Integer>> mapAction) {
        FightObject target = getTargetForPos(targets, 3);
        if (target == null) {
            return;
        }
        sendAction(fight, target, mapAction);
        return;
    }
    
    public static FightObject getTargetForPos(List<FightObject> targets, int pos) {
        FightObject target = null;
        Optional<FightObject> first = targets.stream().filter(obj -> obj.getPos() % 10000 == pos).findFirst();
        if (first.isPresent()) {
            target = (FightObject)first.get();
        }
        if (target == null && targets != null && targets.size() > 0) {
            target = (FightObject)targets.get(0);
            return target;
        }
        return target;
    }
    
    private static void sendAction(Fight fight, FightObject target, Map<Integer, List<Integer>> mapAction) {
        BeforeActionSkill beforeActionSkillMy = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(!target.isASide()));
        BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(target.isASide()));
        BeforeActionSkill beforeActionSkill2 = assembleAction(fight, mapAction, target, beforeActionSkillMy, beforeActionSkill, fight.getActionParam().getActionDetailParams());
        saveBeforeActionSkill(fight, target, beforeActionSkill2.getRoleSkillMap(), beforeActionSkill2.getSkills());
    }
    
    public static BeforeActionSkill assembleAction(Fight fight, Map<Integer, List<Integer>> mapAction, FightObject target, BeforeActionSkill beforeActionSkillMy, BeforeActionSkill beforeActionSkill, List<ActionDetailParam> actionDetailParams) {
        boolean flag = true;
        List<Integer> skillIdList = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap();
        for (Map.Entry<Integer, List<Integer>> entry : mapAction.entrySet()) {
            List<Integer> skillIds = new ArrayList<>();
            Integer key = (Integer)entry.getKey();
            for (Integer skill : entry.getValue()) {
                SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skill);
                if (petSkill == null) {
                    continue;
                }
                if ((skillIdList.contains(skill) || (beforeActionSkill != null && beforeActionSkill.getSkills().contains(skill))) && PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_FIRST_MAGIC.getValue().equals(petSkill.getTriggerType())) {
                    continue;
                }
                if (skillIdList.contains(skill)) {
                    flag = false;
                }
                if (Arrays.asList(SkillConst.beforeActionSkillOnlyOnce).contains(skill)) {
                    if (81028 == (int)skill) {
                        if (skillIdList.contains(Integer.valueOf(82009))) {
                            continue;
                        }
                        if (beforeActionSkillMy != null && beforeActionSkillMy.getSkills().contains(Integer.valueOf(82009))) {
                            continue;
                        }
                    }
                    if (82009 == (int)skill) {
                        if (skillIdList.contains(Integer.valueOf(81028))) {
                            Integer petId = deleteAction(map, Integer.valueOf(81028));
                            if ((int)petId != 0) {
                                sendCancelActionExcute(actionDetailParams, petId, Integer.valueOf(81028));
                            }
                        }
                        if (beforeActionSkill != null && beforeActionSkill.isPassiveAction() && beforeActionSkill.getSkills().contains(Integer.valueOf(81028))) {
                            Integer petId = deleteAction(beforeActionSkill.getRoleSkillMap(), Integer.valueOf(81028));
                            if ((int)petId != 0) {
                                sendCancelActionExcute(actionDetailParams, petId, Integer.valueOf(81028));
                            }
                        }
                        if (beforeActionSkillMy != null && beforeActionSkillMy.getSkills().contains(Integer.valueOf(81028))) {
                            Integer petId = deleteAction(beforeActionSkillMy.getRoleSkillMap(), Integer.valueOf(81028));
                            if ((int)petId != 0) {
                                sendCancelActionExcute(actionDetailParams, petId, Integer.valueOf(81028));
                            }
                        }
                    }
                }
                skillIds.add(skill);
                skillIdList.add(skill);
                if (flag) {
                    if (Arrays.asList(SkillConst.beforeActionSkillOnlyOnce).contains(skill) && beforeActionSkillMy != null && beforeActionSkillMy.isPassiveAction() && beforeActionSkillMy.getSkills().contains(skill)) {
                        continue;
                    }
                    sendActionExcute(fight.queryFightObject(key), actionDetailParams, (int)skill, target.getId());
                }
                else {
                    continue;
                }
            }
            if (skillIds.size() > 0) {
                map.put(key, skillIds);
            }
            else {
                continue;
            }
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
        }
        else {
            beforeActionSkill.getRoleSkillMap().putAll(map);
            beforeActionSkill.getSkills().addAll(skillIdList);
        }
        beforeActionSkill.setASide(target.isASide());
        beforeActionSkill.setRound(1);
        beforeActionSkill.setPassiveAction(true);
        fight.getBeforeActionMap().put(Boolean.valueOf(target.isASide()), beforeActionSkill);
    }
    
    public static void sendActionExcute(FightObject effector, List<ActionDetailParam> actionDetailParams, int skill, Integer targetId) {
        ActionParamUtils.advanceNotice(effector, actionDetailParams, (int)targetId, skill);
    }
    
    public static Integer deleteAction(Map<Integer, List<Integer>> map, Integer skillId) {
        if (map != null && map.size() > 0) {
            List<Integer> removeList = new ArrayList<>();
            Integer petId = Integer.valueOf(0);
            int index = 0;
            for (Map.Entry<Integer, List<Integer>> entryTemp : map.entrySet()) {
                List<Integer> valueTemp = (List)entryTemp.getValue();
                Integer keyTemp = (Integer)entryTemp.getKey();
                if (valueTemp != null && valueTemp.size() > 0 && valueTemp.contains(skillId)) {
                    valueTemp.remove(skillId);
                    if (index == 0) {
                        petId = keyTemp;
                    }
                    ++index;
                }
                if (valueTemp == null || valueTemp.size() <= 0) {
                    removeList.add(keyTemp);
                }
                else {
                    continue;
                }
            }
            for (Integer removeObj : removeList) {
                map.remove(removeObj);
            }
            return petId;
        }
        return Integer.valueOf(0);
    }
    
    public static void sendCancelActionExcute(List<ActionDetailParam> actionDetailParams, Integer actorId, Integer skill) {
        if (actionDetailParams != null) {
            int removeIndex = -1;
            int i = 0;
            while (i < actionDetailParams.size()) {
                if (((ActionDetailParam)actionDetailParams.get(i)).getType() == 33 && ((ActionDetailParam)actionDetailParams.get(i)).getAttackerId() == (int)actorId && ((ActionDetailParam)actionDetailParams.get(i)).getSkillSerial() == (int)skill) {
                    removeIndex = i;
                    break;
                }
                ++i;
            }
            if (removeIndex != -1) {
                actionDetailParams.remove(removeIndex);
            }
        }
    }
}
