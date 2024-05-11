
package com.kitty.system.game.fightnew.utils;

import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Collection;
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
import java.util.function.Function;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import java.util.List;
import com.kitty.system.game.fightnew.bean.Fight;

public class FightBeforeActionUtil
{
    public static void beforeAction(final Fight fight) {
        try {
            excuteBeforeAction(fight);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void excuteBeforeAction(final Fight fight) {
        final List<FightObject> listA = fight.getListA().stream().filter(obj -> LOGIC.LOGICTYPE_PET.getValue() == obj.getLogic() && !obj.isFallDown()).collect(Collectors.toList());
        final List<FightObject> listB = fight.getListB().stream().filter(obj -> LOGIC.LOGICTYPE_PET.getValue() == obj.getLogic() && !obj.isFallDown()).collect(Collectors.toList());
        final Map<Integer, List<Integer>> mapActionA = getActionSkill(fight, listA);
        final Map<Integer, List<Integer>> mapActionB = getActionSkill(fight, listB);
        if (mapActionA != null) {
            final List<FightObject> targets = fight.getTargetSide(listA.get(0).getId());
            excuteSendAction(fight, targets, mapActionA);
        }
        if (mapActionB != null) {
            final List<FightObject> targets = fight.getTargetSide(listB.get(0).getId());
            excuteSendAction(fight, targets, mapActionB);
        }
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (fightObject.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
                PetPassiveSkillUtil.enteringSideAction(fight, fightObject);
            }
        }
    }
    
    public static Map<Integer, List<Integer>> getActionSkill(final Fight fight, List<FightObject> petList) {
        petList = petList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        if (petList.size() > 0) {
            final Map<Integer, List<Integer>> petSkillMap = new LinkedHashMap<Integer, List<Integer>>();
            petList.stream().forEach(pet -> {
                final List<Integer> listAction = new ArrayList<Integer>();
                pet.getSkillIds().stream().filter(skill -> -1 != skill.getSkillId()).forEach(skill -> {
                    if (fight.isPvp() == 1) {
                        final int[] beforeActionSkillPVP = SkillConst.beforeActionSkillPVP;
                        int i = 0;
                        for (int length = beforeActionSkillPVP.length; i < length; ++i) {
                            final int beforeSkill = beforeActionSkillPVP[i];
                            if (beforeSkill == skill.getSkillId()) {
                                listAction.add(beforeSkill);
                            }
                        }
                    }
                    else {
                        final int[] beforeActionSkillNPVP = SkillConst.beforeActionSkillNPVP;
                        int j = 0;
                        for (int length2 = beforeActionSkillNPVP.length; j < length2; ++j) {
                            final int beforeSkill2 = beforeActionSkillNPVP[j];
                            if (beforeSkill2 == skill.getSkillId()) {
                                listAction.add(beforeSkill2);
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
    
    public static void excuteSendAction(final Fight fight, final List<FightObject> targets, final Map<Integer, List<Integer>> mapAction) {
        final FightObject target = getTargetForPos(targets, 3);
        if (target == null) {
            return;
        }
        sendAction(fight, target, mapAction);
    }
    
    public static FightObject getTargetForPos(final List<FightObject> targets, final int pos) {
        FightObject target = null;
        final Optional<FightObject> first = targets.stream().filter(obj -> obj.getPos() % 10000 == pos).findFirst();
        if (first.isPresent()) {
            target = first.get();
        }
        if (target == null && targets != null && targets.size() > 0) {
            target = targets.get(0);
        }
        return target;
    }
    
    private static void sendAction(final Fight fight, final FightObject target, final Map<Integer, List<Integer>> mapAction) {
        final BeforeActionSkill beforeActionSkillMy = fight.getBeforeActionMap().get(!target.isASide());
        final BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(target.isASide());
        final BeforeActionSkill beforeActionSkill2 = assembleAction(fight, mapAction, target, beforeActionSkillMy, beforeActionSkill, fight.getActionParam().getActionDetailParams());
        saveBeforeActionSkill(fight, target, beforeActionSkill2.getRoleSkillMap(), beforeActionSkill2.getSkills());
    }
    
    public static BeforeActionSkill assembleAction(final Fight fight, final Map<Integer, List<Integer>> mapAction, final FightObject target, final BeforeActionSkill beforeActionSkillMy, final BeforeActionSkill beforeActionSkill, final List<ActionDetailParam> actionDetailParams) {
        boolean flag = true;
        final List<Integer> skillIdList = new ArrayList<Integer>();
        final Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        for (final Map.Entry<Integer, List<Integer>> entry : mapAction.entrySet()) {
            final List<Integer> skillIds = new ArrayList<Integer>();
            final Integer key = entry.getKey();
            for (final Integer skill : entry.getValue()) {
                final SysPetSkill petSkill = SkillDataPool.petSkillMap.get(skill);
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
                    if (81028 == skill) {
                        if (skillIdList.contains(82009)) {
                            continue;
                        }
                        if (beforeActionSkillMy != null && beforeActionSkillMy.getSkills().contains(82009)) {
                            continue;
                        }
                    }
                    if (82009 == skill) {
                        if (skillIdList.contains(81028)) {
                            final Integer petId = deleteAction(map, 81028);
                            if (petId != 0) {
                                sendCancelActionExcute(actionDetailParams, petId, 81028);
                            }
                        }
                        if (beforeActionSkill != null && beforeActionSkill.isPassiveAction() && beforeActionSkill.getSkills().contains(81028)) {
                            final Integer petId = deleteAction(beforeActionSkill.getRoleSkillMap(), 81028);
                            if (petId != 0) {
                                sendCancelActionExcute(actionDetailParams, petId, 81028);
                            }
                        }
                        if (beforeActionSkillMy != null && beforeActionSkillMy.getSkills().contains(81028)) {
                            final Integer petId = deleteAction(beforeActionSkillMy.getRoleSkillMap(), 81028);
                            if (petId != 0) {
                                sendCancelActionExcute(actionDetailParams, petId, 81028);
                            }
                        }
                    }
                }
                skillIds.add(skill);
                skillIdList.add(skill);
                if (!flag) {
                    continue;
                }
                if (Arrays.asList(SkillConst.beforeActionSkillOnlyOnce).contains(skill) && beforeActionSkillMy != null && beforeActionSkillMy.isPassiveAction() && beforeActionSkillMy.getSkills().contains(skill)) {
                    continue;
                }
                sendActionExcute(fight.queryFightObject(key), actionDetailParams, skill, target.getId());
            }
            if (skillIds.size() > 0) {
                map.put(key, skillIds);
            }
        }
        final BeforeActionSkill beforeActionSkillReturn = new BeforeActionSkill();
        beforeActionSkillReturn.setSkills(skillIdList);
        beforeActionSkillReturn.setRoleSkillMap(map);
        return beforeActionSkillReturn;
    }
    
    public static void saveBeforeActionSkill(final Fight fight, final FightObject target, final Map<Integer, List<Integer>> map, final List<Integer> skillIdList) {
        BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(target.isASide());
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
        fight.getBeforeActionMap().put(target.isASide(), beforeActionSkill);
    }
    
    public static void sendActionExcute(final FightObject effector, final List<ActionDetailParam> actionDetailParams, final int skill, final Integer targetId) {
        ActionParamUtils.advanceNotice(effector, actionDetailParams, targetId, skill);
    }
    
    public static Integer deleteAction(final Map<Integer, List<Integer>> map, final Integer skillId) {
        if (map != null && map.size() > 0) {
            final List<Integer> removeList = new ArrayList<Integer>();
            Integer petId = 0;
            int index = 0;
            for (final Map.Entry<Integer, List<Integer>> entryTemp : map.entrySet()) {
                final List<Integer> valueTemp = entryTemp.getValue();
                final Integer keyTemp = entryTemp.getKey();
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
            }
            for (final Integer removeObj : removeList) {
                map.remove(removeObj);
            }
            return petId;
        }
        return 0;
    }
    
    public static void sendCancelActionExcute(final List<ActionDetailParam> actionDetailParams, final Integer actorId, final Integer skill) {
        if (actionDetailParams != null) {
            int removeIndex = -1;
            for (int i = 0; i < actionDetailParams.size(); ++i) {
                if (actionDetailParams.get(i).getType() == 33 && actionDetailParams.get(i).getAttackerId() == actorId && actionDetailParams.get(i).getSkillSerial() == skill) {
                    removeIndex = i;
                    break;
                }
            }
            if (removeIndex != -1) {
                actionDetailParams.remove(removeIndex);
            }
        }
    }
}
