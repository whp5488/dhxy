package com.kitty.system.game.fightnew.utils;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import java.util.Optional;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import java.util.Map;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class SkillTargetPicker
{
    public SkillTargetPicker() {
    }
    
    public static List<Integer> getRandomAttackTarget(Fight fight, FightObject effector, int maxTarget) {
        List<Integer> targets = new ArrayList<>();
        FightObject target = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
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
        List<FightObject> targetFightObjects = getFightObjects(fight, effector, isEnemy);
        if (targets.size() >= maxTarget) {
            return targets;
        }
        if (targetFightObjects.size() <= targets.size()) {
            return targets;
        }
        ArrayList<Integer> otherTargets = new ArrayList<>();
        List<FightObject> frozenObjects = new ArrayList<>();
        for (FightObject tempFightObject : targetFightObjects) {
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
            for (FightObject frozenObject : frozenObjects) {
                if (otherTargets.size() + targets.size() < maxTarget) {
                    otherTargets.add(frozenObject.getId());
                }
                else {
                    continue;
                }
            }
        }
        Collections.shuffle(otherTargets);
        int size = maxTarget - targets.size();
        size = Math.min(size, otherTargets.size());
        targets.addAll(otherTargets.subList(0, size));
        return targets;
    }
    
    public static FightObject getPhysicTarget(FightObject fightObject, Fight fight) {
        FightObject target = fight.queryFightObject(Integer.valueOf(fightObject.getTargetId()));
        if (target != null && !target.isFallDown() && !fightObject.getId().equals(target.getId())) {
            return target;
        }
        List<FightObject> selfList = getFightObjects(fight, fightObject, true);
        if (selfList.size() <= 0) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(selfList.size());
        return (FightObject)selfList.get(index);
    }
    
    public static List<Integer> getFightObjectIds(Fight fight, FightObject fightObject, boolean isEnemy) {
        List<FightObject> fightObjects = getFightObjects(fight, fightObject, isEnemy);
        List<Integer> ids = new ArrayList<>();
        if (fightObjects != null && fightObjects.size() > 0) {
            for (FightObject obj : fightObjects) {
                ids.add(obj.getId());
            }
            return ids;
        }
        return ids;
    }
    
    public static List<FightObject> getFightObjects(Fight fight, FightObject fightObject, boolean isEnemy) {
        List<FightObject> list = null;
        if (isEnemy) {
            list = fight.getTargetSide((int)fightObject.getId());
            RoleSkill skill = fightObject.getSkillBy(Integer.valueOf(fightObject.getSkillId()));
            boolean attackCloaking = PassiveSkillUtils.isAttactCloaking(fight, fightObject, skill);
            list = (List)list.stream().filter(obj -> !obj.isFallDown() && (attackCloaking || obj.getStatusMap().getInt(String.valueOf(1011)) <= 0)).collect(Collectors.toList());
            list = (List)list.stream().sorted(Comparator.comparing(FightObject::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
            return list;
        }
        RoleSkill skill = fightObject.getSkillBy(Integer.valueOf(fightObject.getSkillId()));
        boolean attackCloaking = PassiveSkillUtils.isAttactCloaking(fight, fightObject, skill);
        list = (List)fight.getMySide((int)fightObject.getId()).stream().filter(obj -> attackCloaking || obj.getStatusValue(1011) <= 0).collect(Collectors.toList());
        list = (List)list.stream().sorted(Comparator.comparing(FightObject::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
        return list;
    }
    
    public static List<FightObject> getFightObjectsOldTarget(Fight fight, FightObject effector, int oldTargetId) {
        List<FightObject> list = fight.getMySide(oldTargetId);
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(effector.getSkillId()));
        boolean attackCloaking = PassiveSkillUtils.isAttactCloaking(fight, effector, skill);
        list = (List)list.stream().filter(obj -> !obj.isFallDown() && (attackCloaking || obj.getStatusMap().getInt(String.valueOf(1011)) <= 0)).collect(Collectors.toList());
        list = (List)list.stream().sorted(Comparator.comparing(FightObject::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
        return list;
    }
    
    public static boolean isAttack(Fight fight, FightObject effector, FightObject target) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(effector.getSkillId()));
        boolean attackCloaking = PassiveSkillUtils.isAttactCloaking(fight, effector, skill);
        return attackCloaking || target.getStatusValue(1011) <= 0;
    }
    
    public static boolean isMagicAttack(FightObject effector, FightObject target) {
        boolean isAttack = false;
        if ((target.getStatusValue(1011) > 0 && isAttackCloaking(effector, 7)) || target.getStatusValue(1011) <= 0) {
            isAttack = true;
        }
        return isAttack;
    }
    
    public static boolean isAttackCloaking(FightObject effector, int type) {
        boolean isAttackCloaking = false;
        Optional<RoleSkill> any = effector.getSkillIds().stream().filter(skill -> 83020 == (int)skill.getSkillId()).findAny();
        switch (type) {
            case 0: {
                isAttackCloaking = true;
                break;
            }
            case 7: {
                Map<Integer, SysPetSkill> sysPetSkillMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue());
                List<RoleSkill> skills = (List)effector.getSkillIds().stream().filter(skill -> 83020 == (int)skill.getSkillId() || 83036 == (int)skill.getSkillId()).collect(Collectors.toList());
                SysPetSkill petSkill = (SysPetSkill)sysPetSkillMap.get(Integer.valueOf(effector.getSkillId()));
                if (petSkill != null && (any.isPresent() || skills.size() > 0)) {
                    isAttackCloaking = true;
                    break;
                }
                break;
            }
        }
        return isAttackCloaking;
    }
    
    public static List<Integer> getTargetListNew(FightObject fightObject, int maxNum, Fight fight) {
        RoleSkill skill = fightObject.getSkillBy(Integer.valueOf(fightObject.getSkillId()));
        if (skill == null) {
            return Collections.emptyList();
        }
        if (SkillFunc.isAssistSkill(skill.getSkillId(), fightObject.getLogic())) {
            return getAssistTarget(fight, fightObject, maxNum);
        }
        return getRandomAttackTarget(fight, fightObject, maxNum);
    }
    
    public static List<Integer> getAssistTarget(Fight fight, FightObject effector, int maxTarget) {
        FightObject target = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        if (target != null && !isAttack(fight, effector, target)) {
            target = null;
        }
        List<FightObject> selfList = getFightObjects(fight, effector, false);
        ArrayList<Integer> resultList = new ArrayList<>();
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(effector.getSkillId()));
        if ((int)skill.getType() == SKILLATTR.SKILLATTR_XIXUE.getValue() || (int)skill.getType() == SKILLATTR.SKILLATTR_PAN.getValue()) {
            selfList = (List)selfList.stream().sorted(Comparator.comparing(FightObject::getSurplusLifePercent)).collect(Collectors.toList());
        }
        else if ((int)skill.getType() == SKILLATTR.SKILLATTR_ATTACK.getValue()) {
            selfList = SkillAi.certainProMax(selfList, 3);
        }
        else {
            selfList = SkillAi.certainProMax(selfList, 4);
        }
        boolean flag = false;
        if ((int)skill.getType() == SKILLATTR.SKILLATTR_ZHIYU.getValue() && effector.getStatusValue(1023) > 0) {
            flag = true;
        }
        if ((int)skill.getType() == SKILLATTR.SKILLATTR_XIXUE.getValue() || flag) {
            if (target != null && ((FightNewService)SpringUtils.getBean(FightNewService.class)).isSameTeam(effector, target) && whether(skill, target)) {
                resultList.add(target.getId());
            }
            if (resultList.size() >= maxTarget) {
                return resultList;
            }
            for (FightObject tempfightObject : selfList) {
                if (resultList.size() >= maxTarget) {
                    return resultList;
                }
                if (target != null && target.getId().equals(tempfightObject.getId())) {
                    continue;
                }
                if (whether(skill, tempfightObject) && !resultList.contains(tempfightObject.getId())) {
                    resultList.add(tempfightObject.getId());
                }
                else {
                    continue;
                }
            }
        }
        else {
            if (target != null && ((target.getCurrLife() > 0 && ((FightNewService)SpringUtils.getBean(FightNewService.class)).isSameTeam(effector, target)) || LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic()) && whether(skill, target)) {
                resultList.add(target.getId());
            }
            for (FightObject temp : selfList) {
                if (resultList.size() >= maxTarget) {
                    return resultList;
                }
                if (target != null && target.getId().equals(temp.getId())) {
                    continue;
                }
                if (whether(skill, temp) && temp.getCurrLife() > 0 && !resultList.contains(temp.getId())) {
                    resultList.add(temp.getId());
                }
                else {
                    continue;
                }
            }
        }
        return resultList;
    }
    
    public static boolean whether(RoleSkill skill, FightObject temp) {
        boolean flag = true;
        if (((int)skill.getSkillId() == 30037 || (int)skill.getSkillId() == 30038) && temp.getStatusMap().getInt(String.valueOf(1011)) > 0) {
            flag = false;
        }
        return flag;
    }
    
    public static FightObject getTargetBySkill(FightObject attacker, Fight fight) {
        RoleSkill skill = attacker.getSkillBy(Integer.valueOf(attacker.getSkillId()));
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
    
    private static FightObject getAutoTargetMagic(RoleSkill roleSkill, FightObject fightObject, Fight fight) {
        FightObject autoTarget = fight.queryFightObject(Integer.valueOf(fightObject.getAutoTarget()));
        if (fightObject.getAutoFightType() == 1) {
            return fightObject;
        }
        if (fightObject.getAutoFightType() == 2) {
            if (autoTarget != null && !autoTarget.isFallDown()) {
                if (autoTarget.isASide() != fightObject.isASide()) {
                    return autoTarget;
                }
                return fight.getAttackTarget(fightObject);
            }
            else {
                return fight.getAttackTarget(fightObject);
            }
        }
        else {
            if (autoTarget == null) {
                List<FightObject> targetSide = getFightObjects(fight, fightObject, true);
                if (targetSide != null && targetSide.size() > 0) {
                    autoTarget = (FightObject)targetSide.get(0);
                    return autoTarget;
                }
            }
            return null;
        }
    }
    
    public static List<Integer> getRowAttackTarget(Fight fight, FightObject effector, int maxTarget) {
        Set<Integer> targetSet = new HashSet();
        FightObject target = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        if (target != null && target.getCurrLife() > 0 && !fight.isSameSide(effector, target)) {
            targetSet.add(target.getId());
        }
        if (target == null) {
            target = ((FightNewService)SpringUtils.getBean(FightNewService.class)).getPhysicTarget(effector, fight);
            if (target == null) {
                return new ArrayList<>();
            }
        }
        effector.setTargetId((int)target.getId());
        Map<Integer, Integer> pos2Actors = new HashMap();
        for (FightObject obj : fight.getTargetSide((int)effector.getId())) {
            if (obj.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                continue;
            }
            pos2Actors.put(Integer.valueOf(obj.getPos()), obj.getId());
        }
        if (maxTarget > 1) {
            targetSet = multiTarget(targetSet, fight, effector, target, pos2Actors, maxTarget);
        }
        else {
            Set<Integer> parallel = new HashSet();
            parallel.add(Integer.valueOf(target.getPos()));
            targetSet = parallelGetTarget(targetSet, parallel, fight, effector, pos2Actors);
        }
        return new ArrayList(targetSet);
    }
    
    public static Set<Integer> multiTarget(Set<Integer> targetSet, Fight fight, FightObject effector, FightObject target, Map<Integer, Integer> pos2Actors, int maxTarget) {
        String joint = String.valueOf(target.getPos());
        int target_pos = target.getPos() % 10000 % 100;
        List<Integer> targets = xAxisTargets(target_pos, joint.substring(0, joint.length() - 1));
        targets = targets.subList(0, targets.size() - (5 - maxTarget));
        Set<Integer> parallel = new HashSet();
        for (Integer pos : targets) {
            int targetId = (int)pos2Actors.getOrDefault(pos, Integer.valueOf(0));
            if (targetId <= 0) {
                continue;
            }
            FightObject tmpObj = fight.queryFightObjectAnyWay(targetId);
            if (tmpObj == null || tmpObj.isFallDown()) {
                continue;
            }
            if (fight.isSameSide(effector, tmpObj)) {
                continue;
            }
            parallel.add(pos);
            targetSet.add(Integer.valueOf(targetId));
            if (targetSet.size() >= maxTarget) {
                break;
            }
            continue;
        }
        return parallelGetTarget(targetSet, parallel, fight, effector, pos2Actors);
    }
    
    public static Set<Integer> parallelGetTarget(Set<Integer> targetSet, Set<Integer> parallel, Fight fight, FightObject effector, Map<Integer, Integer> pos2Actors) {
        for (Integer pos : parallel) {
            int parallel_pos = (int)pos + 100;
            int targetId = (int)pos2Actors.getOrDefault(Integer.valueOf(parallel_pos), Integer.valueOf(0));
            if (targetId <= 0) {
                parallel_pos = (int)pos - 100;
                targetId = (int)pos2Actors.getOrDefault(Integer.valueOf(parallel_pos), Integer.valueOf(0));
                if (targetId <= 0) {
                    continue;
                }
            }
            FightObject tmpObj = fight.queryFightObjectAnyWay(targetId);
            if (tmpObj == null || tmpObj.isFallDown()) {
                continue;
            }
            if (fight.isSameSide(effector, tmpObj)) {
                continue;
            }
            targetSet.add(Integer.valueOf(targetId));
        }
        return targetSet;
    }
    
    public static List<Integer> xAxisTargets(int pos, String joint) {
        List<Integer> orders = new ArrayList<>();
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
            return orders;
        }
        return orders;
    }
    
    private static boolean inRange(int l, int r, int v) {
        return l <= v && v <= r;
    }
}
