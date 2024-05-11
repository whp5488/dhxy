
package com.kitty.system.game.fightnew.utils;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import java.util.Optional;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.Map;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.function.Function;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class SkillTargetPicker
{
    public static List<Integer> getRandomAttackTarget(final Fight fight, final FightObject effector, final int maxTarget) {
        final List<Integer> targets = new ArrayList<Integer>();
        FightObject target = fight.queryFightObject(effector.getTargetId());
        if (target != null) {
            if (target.isFallDown()) {
                target = null;
            }
            else if (!isAttack(fight, effector, target)) {
                target = null;
            }
        }
        if (target != null) {
            targets.add(target.getId());
        }
        boolean isEnemy = true;
        if (target != null && target.isASide() == effector.isASide()) {
            isEnemy = false;
        }
        final List<FightObject> targetFightObjects = getFightObjects(fight, effector, isEnemy);
        if (targets.size() >= maxTarget) {
            return targets;
        }
        if (targetFightObjects.size() <= targets.size()) {
            return targets;
        }
        final ArrayList<Integer> otherTargets = new ArrayList<Integer>();
        final List<FightObject> frozenObjects = new ArrayList<FightObject>();
        for (final FightObject tempFightObject : targetFightObjects) {
            if (target != null && tempFightObject.getId().equals(target.getId())) {
                continue;
            }
            if (tempFightObject.getCurrLife() <= 0) {
                continue;
            }
            if (tempFightObject.getStatusValue(1003) > 0) {
                frozenObjects.add(tempFightObject);
            }
            else {
                otherTargets.add(tempFightObject.getId());
            }
        }
        if (otherTargets.size() + targets.size() < maxTarget && frozenObjects.size() > 0) {
            for (final FightObject frozenObject : frozenObjects) {
                if (otherTargets.size() + targets.size() < maxTarget) {
                    otherTargets.add(frozenObject.getId());
                }
            }
        }
        Collections.shuffle(otherTargets);
        int size = maxTarget - targets.size();
        size = Math.min(size, otherTargets.size());
        targets.addAll(otherTargets.subList(0, size));
        return targets;
    }
    
    public static FightObject getPhysicTarget(final FightObject fightObject, final Fight fight) {
        final FightObject target = fight.queryFightObject(fightObject.getTargetId());
        if (target != null && !target.isFallDown() && !fightObject.getId().equals(target.getId())) {
            return target;
        }
        final List<FightObject> selfList = getFightObjects(fight, fightObject, true);
        if (selfList.size() <= 0) {
            return null;
        }
        final Random random = new Random();
        final int index = random.nextInt(selfList.size());
        return selfList.get(index);
    }
    
    public static List<Integer> getFightObjectIds(final Fight fight, final FightObject fightObject, final boolean isEnemy) {
        final List<FightObject> fightObjects = getFightObjects(fight, fightObject, isEnemy);
        final List<Integer> ids = new ArrayList<Integer>();
        if (fightObjects != null && fightObjects.size() > 0) {
            for (final FightObject obj : fightObjects) {
                ids.add(obj.getId());
            }
        }
        return ids;
    }
    
    public static List<FightObject> getFightObjects(final Fight fight, final FightObject fightObject, final boolean isEnemy) {
        List<FightObject> list = null;
        if (isEnemy) {
            list = fight.getTargetSide(fightObject.getId());
            final RoleSkill skill = fightObject.getSkillBy(fightObject.getSkillId());
            final boolean attackCloaking = PassiveSkillUtils.isAttactCloaking(fight, fightObject, skill);
            list = list.stream().filter(obj -> !obj.isFallDown() && (attackCloaking || obj.getStatusMap().getInt(String.valueOf(1011)) <= 0)).collect(Collectors.toList());
            list = list.stream().sorted(Comparator.comparing(FightObject::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
        }
        else {
            final RoleSkill skill = fightObject.getSkillBy(fightObject.getSkillId());
            final boolean attackCloaking = PassiveSkillUtils.isAttactCloaking(fight, fightObject, skill);
            list = fight.getMySide(fightObject.getId()).stream().filter(obj -> attackCloaking || obj.getStatusValue(1011) <= 0).collect(Collectors.toList());
            list = list.stream().sorted(Comparator.comparing(FightObject::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
        }
        return list;
    }
    
    public static List<FightObject> getFightObjectsOldTarget(final Fight fight, final FightObject effector, final int oldTargetId) {
        List<FightObject> list = fight.getMySide(oldTargetId);
        final RoleSkill skill = effector.getSkillBy(effector.getSkillId());
        final boolean attackCloaking = PassiveSkillUtils.isAttactCloaking(fight, effector, skill);
        list = list.stream().filter(obj -> !obj.isFallDown() && (attackCloaking || obj.getStatusMap().getInt(String.valueOf(1011)) <= 0)).collect(Collectors.toList());
        list = list.stream().sorted(Comparator.comparing(FightObject::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
        return list;
    }
    
    public static boolean isAttack(final Fight fight, final FightObject effector, final FightObject target) {
        final RoleSkill skill = effector.getSkillBy(effector.getSkillId());
        final boolean attackCloaking = PassiveSkillUtils.isAttactCloaking(fight, effector, skill);
        return attackCloaking || target.getStatusValue(1011) <= 0;
    }
    
    public static boolean isMagicAttack(final FightObject effector, final FightObject target) {
        boolean isAttack = false;
        if ((target.getStatusValue(1011) > 0 && isAttackCloaking(effector, 7)) || target.getStatusValue(1011) <= 0) {
            isAttack = true;
        }
        return isAttack;
    }
    
    public static boolean isAttackCloaking(final FightObject effector, final int type) {
        boolean isAttackCloaking = false;
        final Optional<RoleSkill> any = effector.getSkillIds().stream().filter(skill -> 83020 == skill.getSkillId()).findAny();
        switch (type) {
            case 0: {
                isAttackCloaking = true;
                break;
            }
            case 7: {
                final Map<Integer, SysPetSkill> sysPetSkillMap = PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue());
                final List<RoleSkill> skills = effector.getSkillIds().stream().filter(skill -> 83020 == skill.getSkillId() || 83036 == skill.getSkillId()).collect(Collectors.toList());
                final SysPetSkill petSkill = sysPetSkillMap.get(effector.getSkillId());
                if (petSkill != null && (any.isPresent() || skills.size() > 0)) {
                    isAttackCloaking = true;
                    break;
                }
                break;
            }
        }
        return isAttackCloaking;
    }
    
    public static List<Integer> getTargetListNew(final FightObject fightObject, final int maxNum, final Fight fight) {
        final RoleSkill skill = fightObject.getSkillBy(fightObject.getSkillId());
        if (skill == null) {
            return Collections.emptyList();
        }
        if (SkillFunc.isAssistSkill(skill.getSkillId(), fightObject.getLogic())) {
            return getAssistTarget(fight, fightObject, maxNum);
        }
        return getRandomAttackTarget(fight, fightObject, maxNum);
    }
    
    public static List<Integer> getAssistTarget(final Fight fight, final FightObject effector, final int maxTarget) {
        FightObject target = fight.queryFightObject(effector.getTargetId());
        if (target != null && !isAttack(fight, effector, target)) {
            target = null;
        }
        List<FightObject> selfList = getFightObjects(fight, effector, false);
        final ArrayList<Integer> resultList = new ArrayList<Integer>();
        final RoleSkill skill = effector.getSkillBy(effector.getSkillId());
        if (skill.getType() == SKILLATTR.SKILLATTR_XIXUE.getValue() || skill.getType() == SKILLATTR.SKILLATTR_PAN.getValue()) {
            selfList = selfList.stream().sorted(Comparator.comparing(FightObject::getSurplusLifePercent)).collect(Collectors.toList());
        }
        else if (skill.getType() == SKILLATTR.SKILLATTR_ATTACK.getValue()) {
            selfList = SkillAi.certainProMax(selfList, 3);
        }
        else {
            selfList = SkillAi.certainProMax(selfList, 4);
        }
        boolean flag = false;
        if (skill.getType() == SKILLATTR.SKILLATTR_ZHIYU.getValue() && effector.getStatusValue(1023) > 0) {
            flag = true;
        }
        if (skill.getType() == SKILLATTR.SKILLATTR_XIXUE.getValue() || flag) {
            if (target != null && SpringUtils.getBean(FightNewService.class).isSameTeam(effector, target) && whether(skill, target)) {
                resultList.add(target.getId());
            }
            if (resultList.size() >= maxTarget) {
                return resultList;
            }
            for (final FightObject tempfightObject : selfList) {
                if (resultList.size() >= maxTarget) {
                    return resultList;
                }
                if (target != null && target.getId().equals(tempfightObject.getId())) {
                    continue;
                }
                if (!whether(skill, tempfightObject) || resultList.contains(tempfightObject.getId())) {
                    continue;
                }
                resultList.add(tempfightObject.getId());
            }
        }
        else {
            if (target != null && ((target.getCurrLife() > 0 && SpringUtils.getBean(FightNewService.class).isSameTeam(effector, target)) || LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic()) && whether(skill, target)) {
                resultList.add(target.getId());
            }
            for (final FightObject temp : selfList) {
                if (resultList.size() >= maxTarget) {
                    return resultList;
                }
                if (target != null && target.getId().equals(temp.getId())) {
                    continue;
                }
                if (!whether(skill, temp) || temp.getCurrLife() <= 0 || resultList.contains(temp.getId())) {
                    continue;
                }
                resultList.add(temp.getId());
            }
        }
        return resultList;
    }
    
    public static boolean whether(final RoleSkill skill, final FightObject temp) {
        boolean flag = true;
        if ((skill.getSkillId() == 30037 || skill.getSkillId() == 30038) && temp.getStatusMap().getInt(String.valueOf(1011)) > 0) {
            flag = false;
        }
        return flag;
    }
    
    public static FightObject getTargetBySkill(final FightObject attacker, final Fight fight) {
        final RoleSkill skill = attacker.getSkillBy(attacker.getSkillId());
        if (attacker.getType() == 0) {
            return fight.getAttackTarget(attacker);
        }
        if (attacker.getType() == 1) {
            return attacker;
        }
        if (attacker.getType() == 7) {
            return getAutoTargetMagic(skill, attacker, fight);
        }
        return null;
    }
    
    private static FightObject getAutoTargetMagic(final RoleSkill roleSkill, final FightObject fightObject, final Fight fight) {
        FightObject autoTarget = fight.queryFightObject(fightObject.getAutoTarget());
        if (fightObject.getAutoFightType() == 1) {
            return fightObject;
        }
        if (fightObject.getAutoFightType() != 2) {
            if (autoTarget == null) {
                final List<FightObject> targetSide = getFightObjects(fight, fightObject, true);
                if (targetSide != null && targetSide.size() > 0) {
                    autoTarget = targetSide.get(0);
                    return autoTarget;
                }
            }
            return null;
        }
        if (autoTarget == null || autoTarget.isFallDown()) {
            return fight.getAttackTarget(fightObject);
        }
        if (autoTarget.isASide() != fightObject.isASide()) {
            return autoTarget;
        }
        return fight.getAttackTarget(fightObject);
    }
    
    public static List<Integer> getRowAttackTarget(final Fight fight, final FightObject effector, final int maxTarget) {
        Set<Integer> targetSet = new HashSet<Integer>();
        FightObject target = fight.queryFightObject(effector.getTargetId());
        if (target != null && target.getCurrLife() > 0 && !fight.isSameSide(effector, target)) {
            targetSet.add(target.getId());
        }
        if (target == null) {
            target = SpringUtils.getBean(FightNewService.class).getPhysicTarget(effector, fight);
            if (target == null) {
                return new ArrayList<Integer>();
            }
        }
        effector.setTargetId(target.getId());
        final Map<Integer, Integer> pos2Actors = new HashMap<Integer, Integer>();
        for (final FightObject obj : fight.getTargetSide(effector.getId())) {
            if (obj.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                continue;
            }
            pos2Actors.put(obj.getPos(), obj.getId());
        }
        if (maxTarget > 1) {
            targetSet = multiTarget(targetSet, fight, effector, target, pos2Actors, maxTarget);
        }
        else {
            final Set<Integer> parallel = new HashSet<Integer>();
            parallel.add(target.getPos());
            targetSet = parallelGetTarget(targetSet, parallel, fight, effector, pos2Actors);
        }
        return new ArrayList<Integer>(targetSet);
    }
    
    public static Set<Integer> multiTarget(final Set<Integer> targetSet, final Fight fight, final FightObject effector, final FightObject target, final Map<Integer, Integer> pos2Actors, final int maxTarget) {
        final String joint = String.valueOf(target.getPos());
        final int target_pos = target.getPos() % 10000 % 100;
        List<Integer> targets = xAxisTargets(target_pos, joint.substring(0, joint.length() - 1));
        targets = targets.subList(0, targets.size() - (5 - maxTarget));
        final Set<Integer> parallel = new HashSet<Integer>();
        for (final Integer pos : targets) {
            final int targetId = pos2Actors.getOrDefault(pos, 0);
            if (targetId <= 0) {
                continue;
            }
            final FightObject tmpObj = fight.queryFightObjectAnyWay(targetId);
            if (tmpObj == null || tmpObj.isFallDown()) {
                continue;
            }
            if (fight.isSameSide(effector, tmpObj)) {
                continue;
            }
            parallel.add(pos);
            targetSet.add(targetId);
            if (targetSet.size() >= maxTarget) {
                break;
            }
        }
        return parallelGetTarget(targetSet, parallel, fight, effector, pos2Actors);
    }
    
    public static Set<Integer> parallelGetTarget(final Set<Integer> targetSet, final Set<Integer> parallel, final Fight fight, final FightObject effector, final Map<Integer, Integer> pos2Actors) {
        for (final Integer pos : parallel) {
            int parallel_pos = pos + 100;
            int targetId = pos2Actors.getOrDefault(parallel_pos, 0);
            if (targetId <= 0) {
                parallel_pos = pos - 100;
                targetId = pos2Actors.getOrDefault(parallel_pos, 0);
                if (targetId <= 0) {
                    continue;
                }
            }
            final FightObject tmpObj = fight.queryFightObjectAnyWay(targetId);
            if (tmpObj != null && !tmpObj.isFallDown()) {
                if (fight.isSameSide(effector, tmpObj)) {
                    continue;
                }
                targetSet.add(targetId);
            }
        }
        return targetSet;
    }
    
    public static List<Integer> xAxisTargets(final int pos, final String joint) {
        final List<Integer> orders = new ArrayList<Integer>();
        orders.add(Integer.valueOf(joint + pos));
        if (inRange(1, 5, pos)) {
            for (int i = 1; i <= 4; ++i) {
                if (inRange(1, 5, pos - i)) {
                    orders.add(Integer.valueOf(joint + (pos - i)));
                }
                if (inRange(1, 5, pos + i)) {
                    orders.add(Integer.valueOf(joint + (pos + i)));
                }
            }
        }
        return orders;
    }
    
    private static boolean inRange(final int l, final int r, final int v) {
        return l <= v && v <= r;
    }
}
