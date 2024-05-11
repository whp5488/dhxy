
package com.kitty.system.game.fightnew.book.pet;

import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.domain.Pet;
import java.util.Set;
import java.util.Optional;
import com.kitty.system.game.pet.domain.vo.AssistParam;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import java.util.Collections;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.model.BattleUnit;
import java.util.Collection;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.game.fightnew.bean.MonsterFightObject;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import java.util.Arrays;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.book.vo.StatusForSkillDispose;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuSkillUtils;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class PetPassiveSkillUtil
{
    public static void roundStartPassive(final Fight fight) {
        try {
            excuteRoundStartPassive(fight);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void excuteRoundStartPassive(final Fight fight) {
        final ArrayList<FightObject> allFightObjects = fight.getAllFightObjects();
        for (final FightObject effector : allFightObjects) {
            SkillProcessor skillProcessor = null;
            if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic() && !effector.isFallDown()) {
                final int isPvp = fight.isPvp();
                final List<RoleSkill> petRoleSkills = effector.getSkillIds().stream().filter(skill -> -1 != skill.getSkillId()).collect(Collectors.toList());
                final List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START.getValue(), petRoleSkills, isPvp);
                if (petSkillList.size() > 0) {
                    for (final SysPetSkill petSkill : petSkillList) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        skillProcessor.passive(fight, effector, petSkill);
                    }
                }
                if (fight.getRound() == 1) {
                    final List<FightObject> enemyPetSide = fight.getTargetSide(effector.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
                    for (final FightObject fightObject : enemyPetSide) {
                        final List<SysPetSkill> petSkillListEntering = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_ENTERING_SIDE.getValue(), (List<RoleSkill>)fightObject.getSkillIds().stream().filter(skill -> -1 != skill.getSkillId()).collect(Collectors.toList()), isPvp);
                        if (petSkillListEntering != null && petSkillListEntering.size() > 0) {
                            for (final SysPetSkill petSkill2 : petSkillListEntering) {
                                skillProcessor = SkillProcessor.queryProcessor(petSkill2.getSerial());
                                skillProcessor.passive(fight, fightObject, effector, petSkill2);
                            }
                        }
                    }
                }
                final Map<Integer, SysPetSkill> petSkillMapForN = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_CONTROL.getValue());
                if (petSkillMapForN.size() > 0) {
                    for (final RoleSkill roleSkill : petRoleSkills) {
                        final SysPetSkill petSkill = petSkillMapForN.get(roleSkill.getSkillId());
                        if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
                            skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                            skillProcessor.passive(fight, effector, new Object[0]);
                        }
                    }
                }
                if (effector.getCurrLife() / effector.getMaxLife() < 0.4) {
                    final List<SysPetSkill> petSkillList2 = PetTriggerPassiveSkill.currLifeLessThan40(petRoleSkills, isPvp);
                    if (petSkillList2 != null) {
                        for (final SysPetSkill petSkill3 : petSkillList2) {
                            skillProcessor = SkillProcessor.queryProcessor(petSkill3.getSerial());
                            skillProcessor.passive(fight, effector, new Object[0]);
                        }
                    }
                }
            }
            TianFuSkillUtils.enteringSide(fight, effector);
            TianFuSkillUtils.roundStart(fight, effector);
            if (fight.getRound() == 1) {
                PassiveSkillUtils.enterFight(fight, effector);
            }
        }
        roundStartAction(fight);
        deathExcute(fight);
    }
    
    private static void roundStartAction(final Fight fight) {
        final BeforeActionSkill beforeActionSkillA = fight.getBeforeActionMap().get(true);
        final BeforeActionSkill beforeActionSkillB = fight.getBeforeActionMap().get(false);
        roundStartActionExcute(fight, beforeActionSkillA, beforeActionSkillB, PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START_ACTION.getValue(), false);
    }
    
    public static void roundStartActionExcute(final Fight fight, final BeforeActionSkill beforeActionSkillM, final BeforeActionSkill beforeActionSkillT, final String type, final boolean isA) {
        final Map<Integer, SysPetSkill> resourceActionMap = PetTriggerPassiveSkill.querySkills(type);
        boolean flag = false;
        for (final Map.Entry<Integer, SysPetSkill> entry : resourceActionMap.entrySet()) {
            final SysPetSkill petSkill = entry.getValue();
            if (beforeActionSkillM != null && beforeActionSkillM.isPassiveAction()) {
                for (final Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkillM.getRoleSkillMap().entrySet()) {
                    if (entry2.getValue().contains(petSkill.getSerial())) {
                        final FightObject obj = fight.queryFightObject(entry2.getKey());
                        if (obj == null || obj.isFallDown()) {
                            return;
                        }
                        final SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(entry.getValue().getSerial());
                        passiveSkillProcessor.passive(fight, obj, petSkill);
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                flag = false;
                break;
            }
            if (beforeActionSkillT == null || !beforeActionSkillT.isPassiveAction()) {
                continue;
            }
            for (final Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkillT.getRoleSkillMap().entrySet()) {
                if (entry2.getValue().contains(petSkill.getSerial())) {
                    final FightObject obj = fight.queryFightObject(entry2.getKey());
                    if (obj == null || obj.isFallDown()) {
                        return;
                    }
                    final SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(entry.getValue().getSerial());
                    passiveSkillProcessor.passive(fight, obj, petSkill);
                    break;
                }
            }
        }
    }
    
    public static void enteringSideAction(final Fight fight, final FightObject petObject) {
        final Map<Integer, SysPetSkill> enteringSideSysPetSkillMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_ACTION.getValue());
        final List<RoleSkill> petRoleSkill = petObject.getSkillIds().stream().filter(skill -> skill.getSkillId() != -1).collect(Collectors.toList());
        if (petRoleSkill.size() > 0) {
            for (final Map.Entry<Integer, SysPetSkill> entry : enteringSideSysPetSkillMap.entrySet()) {
                for (final RoleSkill roleSkill : petRoleSkill) {
                    if (entry.getKey().equals(roleSkill.getSkillId())) {
                        final SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(roleSkill.getSkillId());
                        passiveSkillProcessor.passive(fight, petObject, new Object[0]);
                        break;
                    }
                }
            }
        }
    }
    
    public static double getSuffix(final Integer lv, final Integer rei, final long close) {
        return Math.pow(lv, 0.5) / 10.0 + Math.pow((double)close, 0.16666666666666666) * 10.0 / (100 + rei * 20);
    }
    
    public static List<FiedValue> clearStatus(final FightObject target, final String type) {
        if (target != null) {
            final StatusForSkillDispose statusForSkillDispose = new StatusForSkillDispose();
            List<Integer> statusList = new ArrayList<Integer>();
            final List<FiedValue> list = new ArrayList<FiedValue>();
            switch (type) {
                case "abnormal": {
                    statusList = statusForSkillDispose.disposeAbnormalStatus(target, true);
                    if (statusList.size() > 0) {
                        list.add(new FiedValue("targetClearStatus", (byte)3, statusList));
                        break;
                    }
                    break;
                }
                case "positive": {
                    if (target.isFrozen()) {
                        break;
                    }
                    for (final int status : StatusConst.getPositiveStatus()) {
                        if (target.getStatusMap().getInt(String.valueOf(status)) > 0) {
                            statusList.add(status);
                            statusForSkillDispose.disposePositiveStatus(target, status, true);
                            if (1023 != status) {
                                continue;
                            }
                            list.add(new FiedValue("targetMaxHp", (byte)3, target.getMaxLife()));
                            list.add(new FiedValue("targetMaxMp", (byte)3, target.getMaxMana()));
                            list.add(new FiedValue("targetCurrHp", (byte)3, target.getCurrLife()));
                            list.add(new FiedValue("targetCurrMp", (byte)3, target.getCurrMana()));
                        }
                    }
                    if (statusList.size() > 0) {
                        list.add(new FiedValue("targetClearStatus", (byte)3, statusList));
                        break;
                    }
                    break;
                }
                case "wuXinModification": {
                    if (target.isFrozen()) {
                        break;
                    }
                    statusList = statusForSkillDispose.disposeWuXinModification(target, true);
                    if (statusList.size() > 0) {
                        list.add(new FiedValue("targetClearStatus", (byte)3, statusList));
                        break;
                    }
                    break;
                }
            }
            return list;
        }
        return null;
    }
    
    public static void otherEnterBattle(final Fight fight, final Integer newPetId) {
        try {
            excuteOtherEnterBattle(fight, newPetId);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void excuteOtherEnterBattle(final Fight fight, final Integer newPetId) {
        final FightObject petObject = fight.queryFightObject(newPetId);
        final List<RoleSkill> petSkills = petObject.getSkillIds().stream().filter(skill -> -1 != skill.getSkillId()).collect(Collectors.toList());
        SkillProcessor skillProcessor = null;
        final List<SysPetSkill> petSkillListNF = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF.getValue(), petSkills, fight.isPvp());
        if (petSkillListNF.size() > 0) {
            for (final SysPetSkill petSkill : petSkillListNF) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                skillProcessor.passive(fight, petObject, null, petSkill);
            }
        }
        final List<FightObject> enemyPetSide = fight.getTargetSide(petObject.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
        for (final FightObject fightObject : enemyPetSide) {
            final List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_ENTERING_SIDE.getValue(), (List<RoleSkill>)fightObject.getSkillIds().stream().filter(skill -> -1 != skill.getSkillId()).collect(Collectors.toList()), fight.isPvp());
            if (petSkillList != null && petSkillList.size() > 0) {
                for (final SysPetSkill petSkill2 : petSkillList) {
                    skillProcessor = SkillProcessor.queryProcessor(petSkill2.getSerial());
                    skillProcessor.passive(fight, fightObject, petObject, petSkill2);
                }
            }
        }
        if (LOGIC.LOGICTYPE_PET.getValue() == petObject.getLogic()) {
            fight.excuteReduceRatio(petObject, true);
        }
        final Map<Integer, List<Integer>> petSkillMap = PetSkillEffectorUtil.getActionSkill(fight, Arrays.asList(petObject));
        if (petSkillMap != null && petSkillMap.size() > 0) {
            final List<FightObject> targets = fight.getTargetSide(petObject.getId());
            PetSkillEffectorUtil.excuteSendAction(fight, targets, petSkillMap);
        }
        enteringSideAction(fight, petObject);
        List<SysPetSkill> petSkillListEntering = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE.getValue(), petSkills, fight.isPvp());
        petSkillListEntering = petSkillListEntering.stream().sorted(Comparator.comparing(SysPetSkill::getExecutionSequence)).collect(Collectors.toList());
        if (petSkillListEntering.size() > 0) {
            for (final SysPetSkill petSkill2 : petSkillListEntering) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill2.getSerial());
                skillProcessor.passive(fight, petObject, petSkill2);
            }
        }
        final List<SysPetSkill> petSkillListNFAction = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF_ACTION.getValue(), petSkills, fight.isPvp());
        if (petSkillListNFAction.size() > 0) {
            for (final SysPetSkill petSkill3 : petSkillListNFAction) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill3.getSerial());
                skillProcessor.passive(fight, petObject, null, petSkill3);
            }
        }
        TianFuSkillUtils.enteringSide(fight, petObject);
        AwakenSkillUtils.enteringSide(fight, petObject);
        PassiveSkillUtils.enterFight(fight, petObject);
    }
    
    public static boolean isExcuteControlleSkill(final FightObject effector, final int count) {
        boolean returnFlag = false;
        final NutMap statusMap = effector.getStatusMap();
        final int anInt = statusMap.getInt("1999record");
        if (anInt >= count) {
            returnFlag = true;
        }
        return returnFlag;
    }
    
    public static boolean isAbnormalOrPositiveStatus(final NutMap map, final String type) {
        List<Integer> statuss = null;
        if ("abnormal".equals(type)) {
            statuss = StatusConst.getAnomalousStatus();
        }
        else if ("positive".equals(type)) {
            statuss = StatusConst.getPositiveStatus();
        }
        else if ("wuXinModification".equals(type)) {
            statuss = PRO_DICT_TYPE.getWuXinDist();
        }
        for (final int status : statuss) {
            if (map.getInt(String.valueOf(status)) > 0) {
                return true;
            }
        }
        return false;
    }
    
    public static void deathExcute(final Fight fight) {
        try {
            petDeathExcute(fight);
            monsterDeathExcute(fight);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void petDeathExcute(final Fight fight) {
        final List<FightObject> fallDownObjs = fight.getAllFightObjects().stream().filter(obj -> obj.isFallDown() && obj.isPet()).collect(Collectors.toList());
        if (fallDownObjs != null && fallDownObjs.size() > 0) {
            for (final FightObject target : fallDownObjs) {
                if (!fight.getDeadPetIds().contains(target.getId())) {
                    excuteDieAction(fight, target);
                }
            }
            petDeathExcute(fight);
        }
        else {
            final List<FightObject> fallDownPlayers = fight.getAllFightObjects().stream().filter(obj -> obj.isFallDown() && obj.isPlayer()).collect(Collectors.toList());
            final Iterator<FightObject> iterator2 = fallDownPlayers.iterator();
            while (iterator2.hasNext()) {
                FightObject obj = iterator2.next();
                final FightObject objPet = fight.queryFightObject(obj.getFightPetId());
                if (objPet != null) {
                    final boolean flag = PetTriggerPassiveSkill.heroFallDown(fight, objPet);
                    if (flag) {
                        petDeathExcute(fight);
                    }
                }
                if (obj.isFallDown() && obj.getDieRound() <= 0) {
                    obj.setDieRound(fight.getRound());
                }
            }
        }
    }
    
    public static void monsterDeathExcute(final Fight fight) {
        final List<FightObject> fallDownMonsters = fight.getAllFightObjects().stream().filter(obj -> obj.isFallDown() && obj.isMonster()).collect(Collectors.toList());
        if (fallDownMonsters != null && fallDownMonsters.size() > 0) {
            for (final FightObject monsterOjb : fallDownMonsters) {
                final int monsterType = ((MonsterFightObject)monsterOjb).getMonsterType();
                final SysDataMonster monster = FightDataPool.data_monster.get(monsterOjb.getSerial());
                if (monster != null && monster.getVanish() == 1) {
                    return;
                }
                if (Const.vanish.contains(monsterType)) {
                    continue;
                }
                sendDisappearData(fight, monsterOjb);
                fight.removeFightObject(monsterOjb);
            }
        }
    }
    
    public static void excuteDieAction(final Fight fight, final FightObject target) {
        if (target.isPet() || target.isPetMirror() || target.isMonster()) {
            if (target.isPet()) {
                final PetFightObject petFightObject = (PetFightObject)target;
                FightParamPool.addDeathPet(fight, target.getHostId(), petFightObject);
            }
            final boolean isRevive = petDieProcessing(fight, target);
            if (!isRevive) {
                AwakenSkillUtils.exitSide(fight, target);
                sendDisappearData(fight, target);
                triggerFlashBeforeOne(fight, target);
            }
        }
    }
    
    public static void sendDisappearData(final Fight fight, final FightObject target) {
        ActionParamUtils.excuteDisappear(fight, target, target.getId(), 46, 0, 1);
    }
    
    public static boolean petDieProcessing(final Fight fight, final FightObject target) {
        boolean isRevive = false;
        if (target.isPet() || target.isPetMirror()) {
            isRevive = excuteReviveSkill(fight, target);
            if (!isRevive) {
                isRevive = AwakenSkillUtils.revive(fight, target);
            }
            if (!isRevive) {
                fight.excuteExitPassive(target);
            }
        }
        if (!isRevive) {
            fight.petDie(target);
        }
        return isRevive;
    }
    
    public static void friendMagic(final Fight fight, final FightObject attacker, final int isPvp, final ActionDetailParam actionDetailParam) {
        final List<FightObject> objList = getSkillForPet(fight, attacker.isASide());
        final Map<Integer, SysPetSkill> resourceMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.FRIEND_MAGIC.getValue());
        for (final FightObject obj : objList) {
            final List<RoleSkill> roleSkills = obj.getSkillIds().stream().filter(roleSkill -> -1 != roleSkill.getSkillId()).collect(Collectors.toList());
            final Iterator<RoleSkill> iterator2 = roleSkills.iterator();
            while (iterator2.hasNext()) {
                RoleSkill roleSkill = iterator2.next();
                final SysPetSkill petSkill = resourceMap.get(roleSkill.getSkillId());
                if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
                    final SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    passiveSkillProcessor.normalAttack(fight, obj, attacker, actionDetailParam, new Object[0]);
                }
            }
        }
    }
    
    private static List<FightObject> getSkillForPet(final Fight fight, final boolean isASide) {
        List<FightObject> objList = null;
        if (isASide) {
            objList = fight.getListA().stream().filter(obj -> obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
        }
        else {
            objList = fight.getListB().stream().filter(obj -> obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
        }
        return objList;
    }
    
    public static boolean unitMagic(final Fight fight, final FightObject attacker, final SkillProcessor processor, final RoleSkill skill) {
        enemyMagic(fight, attacker, processor, skill, fight.isPvp());
        return false;
    }
    
    public static void enemyMagic(final Fight fight, final FightObject attacker, final SkillProcessor attackerProcessor, final RoleSkill skill, final int isPvp) {
        final List<FightObject> objList = getSkillForPet(fight, !attacker.isASide());
        final Map<Integer, SysPetSkill> resourceMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_MAGIC.getValue());
        excutePetSkill(fight, attacker, attackerProcessor, skill, resourceMap, objList, isPvp);
        final BeforeActionSkill beforeActionSkillM = fight.getBeforeActionMap().get(attacker.isASide());
        final BeforeActionSkill beforeActionSkillT = fight.getBeforeActionMap().get(!attacker.isASide());
        final Map<Integer, SysPetSkill> resourceActionMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.UNIT_MAGIC_ACTION.getValue());
        boolean flag = false;
        for (final Map.Entry<Integer, SysPetSkill> entry : resourceActionMap.entrySet()) {
            if (beforeActionSkillM != null && beforeActionSkillM.isPassiveAction()) {
                for (final Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkillM.getRoleSkillMap().entrySet()) {
                    if (entry2.getValue().contains(entry.getKey())) {
                        final SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(entry.getValue().getSerial());
                        passiveSkillProcessor.effect(fight, fight.queryFightObject(entry2.getKey()), attacker, attackerProcessor.costMana(skill, new Object[0]));
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                flag = false;
                break;
            }
            if (beforeActionSkillT == null || !beforeActionSkillT.isPassiveAction()) {
                continue;
            }
            for (final Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkillT.getRoleSkillMap().entrySet()) {
                if (entry2.getValue().contains(entry.getKey())) {
                    final SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(entry.getValue().getSerial());
                    passiveSkillProcessor.effect(fight, fight.queryFightObject(entry2.getKey()), attacker, attackerProcessor.costMana(skill, new Object[0]));
                    break;
                }
            }
        }
    }
    
    private static Effect excutePetSkill(final Fight fight, final FightObject attacker, final SkillProcessor attackerProcessor, final RoleSkill skill, final Map<Integer, SysPetSkill> resourceMap, final List<FightObject> objList, final int isPvp) {
        Effect effect = null;
        for (final FightObject obj : objList) {
            final List<RoleSkill> roleSkills = obj.getSkillIds().stream().filter(roleSkill -> -1 != roleSkill.getSkillId()).collect(Collectors.toList());
            final Iterator<RoleSkill> iterator2 = roleSkills.iterator();
            while (iterator2.hasNext()) {
                RoleSkill roleSkill = iterator2.next();
                final SysPetSkill petSkill = resourceMap.get(roleSkill.getSkillId());
                if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
                    final SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    effect = passiveSkillProcessor.effect(fight, obj, attacker, attackerProcessor.costMana(skill, new Object[0]), petSkill);
                }
            }
        }
        return effect;
    }
    
    public static boolean enemyFristMagic(final Fight fight, final FightObject attacker, final SkillProcessor attackerProcessor, final RoleSkill skill, final int isPvp) {
        final BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(attacker.isASide());
        if (beforeActionSkill == null || !beforeActionSkill.isPassiveAction()) {
            return false;
        }
        final boolean returnFlag = excuteEnemyMagic(fight, attacker, beforeActionSkill, PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_FIRST_MAGIC.getValue()), attackerProcessor.costMana(skill, new Object[0]));
        return returnFlag;
    }
    
    private static boolean excuteEnemyMagic(final Fight fight, final FightObject attacker, final BeforeActionSkill beforeActionSkill, final Map<Integer, SysPetSkill> petSkills, final int hurtParam) {
        boolean returnFlag = false;
        final List<Integer> keys = new ArrayList<Integer>(beforeActionSkill.getRoleSkillMap().keySet());
        for (int j = keys.size() - 1; j >= 0; --j) {
            final List<Integer> values = beforeActionSkill.getRoleSkillMap().get(keys.get(j));
            for (int i = values.size() - 1; i >= 0; --i) {
                final Integer skillId = values.get(i);
                final SysPetSkill petSkill = petSkills.get(skillId);
                if (petSkill != null) {
                    final SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(skillId);
                    FightObject pet = fight.queryFightObject(keys.get(j));
                    if (pet == null || pet.isFallDown()) {
                        pet = attacker;
                    }
                    final Effect effect = passiveSkillProcessor.effect(fight, pet, attacker, hurtParam, petSkill);
                    if (effect != null && 99 == effect.getType()) {
                        returnFlag = true;
                    }
                }
            }
        }
        return returnFlag;
    }
    
    public static void checkControlledStatus(final FightObject target) {
        if (target.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
            final NutMap statusMap = target.getStatusMap();
            int recordValue = 1;
            for (final Map.Entry<String, Object> entry : statusMap.entrySet()) {
                if (entry.getValue() instanceof Integer && (Integer)entry.getValue() > 0 && (entry.getKey().equals(String.valueOf(1002)) || entry.getKey().equals(String.valueOf(1003)) || entry.getKey().equals(String.valueOf(1004)) || entry.getKey().equals(String.valueOf(1006)) || entry.getKey().equals(String.valueOf(1019)))) {
                    if (statusMap.get("1999record") != null) {
                        recordValue = statusMap.getInt("1999record") + 1;
                    }
                    statusMap.put("1999record", (Object)recordValue);
                    break;
                }
            }
        }
    }
    
    public static ActionDetailParam passivePhysics(final Fight fight, final FightObject effector, final FightObject target, final BattleUnit battleUnit, final ActionDetailParam actionDetailParam, final int skillSerial) {
        createActionDetailParam(battleUnit, effector, target, actionDetailParam, skillSerial);
        if (effector.isPlayer()) {
            battleUnit.doPhyHit();
        }
        else if (effector.isPet()) {
            battleUnit.petDoPhyHit();
        }
        else {
            if (!effector.isMonster()) {
                return null;
            }
            if (fight.getSpecialMonsterType() == 1 && effector.getPos() == 10003 && effector.getName().equals("地飞星")) {
                battleUnit.doPhyHit();
            }
            else {
                if (skillSerial <= 80000 || skillSerial >= 90000) {
                    return null;
                }
                battleUnit.petDoPhyHit();
            }
        }
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        fightNewService.checkOrderTarget(fight, Collections.singletonList(target.getId()));
        if (target.isFallDown()) {
            effector.addFallDownList(target.getId());
            fightNewService.checkGuessSuccess(fight);
            fight.onActorDie(target);
        }
        if (effector.isFallDown()) {
            fight.onActorDie(effector);
        }
        return battleUnit.getActionDetailParam();
    }
    
    private static void createActionDetailParam(final BattleUnit battleUnit, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final int currSkill) {
        if (actionDetailParam != null && (80011 == currSkill || 81032 == currSkill || 80023 == currSkill || 81024 == currSkill || 81002 == currSkill || 83039 == currSkill)) {
            battleUnit.setActionDetailParamBac(actionDetailParam);
        }
        battleUnit.createActionDetailParam(effector, target, 7, currSkill, true);
    }
    
    public static void magicOther(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill skill, final ActionDetailParam actionDetailParam) {
        final Map<Integer, SysPetSkill> map = PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.MAGIC_ATTACK_END.getValue());
        if (skill != null && map.get(skill.getSkillId()) != null) {
            final SkillProcessor skillProcessor = SkillProcessor.queryProcessor(skill.getSkillId());
            skillProcessor.normalAttack(fight, effector, target, actionDetailParam, skill.getSkillId());
        }
    }
    
    private static void triggerFlashBeforeOne(final Fight fight, final FightObject actor) {
        final Optional<FightObject> mySideOpt = fight.getMySide(actor.getId()).stream().filter(obj -> !obj.isFallDown() && !obj.getId().equals(actor.getId())).findFirst();
        if (!mySideOpt.isPresent()) {
            return;
        }
        final FightObject host = fight.queryFightObject(actor.getHostId());
        final List<AssistParam> assistParams = ((PlayerFightObject)host).getAssistParams();
        final Set<Integer> deadPetIds = fight.getDeadPetIds();
        int petId = 0;
        for (final AssistParam assistParam : assistParams) {
            if (!deadPetIds.contains(assistParam.getPetId()) && !assistParam.isTrigger()) {
                petId = assistParam.getPetId();
                final boolean isExcuteSuccess = excuteFlash(fight, host, petId);
                if (isExcuteSuccess) {
                    assistParam.setTrigger(true);
                    break;
                }
                break;
            }
        }
    }
    
    private static boolean excuteFlash(final Fight fight, final FightObject host, final Integer petId) {
        final Map<Integer, SysPetSkill> petSkillMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.FLASH_BEFORE_ONE.getValue());
        final Role role = host.getRole();
        final Pet pet = SpringUtils.getPetService().selectPet(role).get(petId);
        if (pet == null) {
            return false;
        }
        final boolean inFight = PetSkillEffectorUtil.isInFight(role, pet);
        if (!inFight) {
            return false;
        }
        final List<PetSkill> petSkills = pet.getSkills().stream().filter(skill -> skill.getSerial() != -1).collect(Collectors.toList());
        List<SysPetSkill> newSysPetSkills = new ArrayList<SysPetSkill>();
        final Iterator<PetSkill> iterator = petSkills.iterator();
        while (iterator.hasNext()) {
            PetSkill skill = iterator.next();
            final SysPetSkill petSkill = petSkillMap.get(skill.getSerial());
            if (petSkill != null && skill.getSerial() == petSkill.getSerial()) {
                newSysPetSkills.add(petSkill);
            }
        }
        if (newSysPetSkills.size() <= 0) {
            return false;
        }
        newSysPetSkills = newSysPetSkills.stream().sorted(Comparator.comparing(SysPetSkill::getSerial)).collect(Collectors.toList());
        final Map<Integer, SysPetSkill> newSysPetSkillMap = newSysPetSkills.stream().collect(Collectors.toMap(SysPetSkill::getSerial, sysPetSkill -> sysPetSkill));
        SysPetSkill excuteSkill = newSysPetSkills.get(0);
        if (newSysPetSkills.size() > 1) {
            excuteSkill = PetSkillEffectorUtil.queryCover(excuteSkill, newSysPetSkillMap);
        }
        final SkillProcessor processor = SkillProcessor.queryProcessor(excuteSkill.getSerial());
        final NutMap passive = processor.passive(fight, host, role, pet);
        return passive != null;
    }
    
    public static boolean excuteReviveSkill(final Fight fight, final FightObject attacker) {
        final Map<Integer, SysPetSkill> reviveSkills = PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.REVIVE_SKILL.getValue());
        boolean isRevive = false;
        if (reviveSkills.size() > 0) {
            final List<Integer> skillIds = PetTriggerPassiveSkill.getInegers((List<RoleSkill>)attacker.getSkillIds().stream().filter(roleSkill -> roleSkill.getSkillId() > 1).collect(Collectors.toList()));
            for (final Map.Entry<Integer, SysPetSkill> entry : reviveSkills.entrySet()) {
                final Integer key = entry.getKey();
                if (skillIds.contains(key)) {
                    final SkillProcessor skillProcessor = SkillProcessor.queryProcessor(key);
                    final NutMap passive = skillProcessor.passive(fight, attacker, new Object[0]);
                    isRevive = passive.getBoolean("successful");
                }
            }
        }
        return isRevive;
    }
    
    public static Effect getPassiveSkill(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill skill, final BattleUnit battleUnit, final String skillTrigget, final int hurt) {
        SkillProcessor skillProcessor = null;
        Effect effectPart = null;
        if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic()) {
            final List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(skillTrigget, effector.getSkillIds(), fight.isPvp());
            if (petSkillList.size() > 0) {
                for (final SysPetSkill petSkill : petSkillList) {
                    if (skill == null) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        effectPart = skillProcessor.effect(fight, effector, target, hurt, petSkill, null, battleUnit);
                    }
                    else {
                        if (petSkill.getSerial().equals(skill.getSkillId())) {
                            continue;
                        }
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        effectPart = skillProcessor.effect(fight, effector, target, hurt, petSkill, skill.getSkillId(), battleUnit);
                    }
                }
            }
        }
        return effectPart;
    }
    
    public static void writeActionDetailParam(final BattleUnit battleUnit, final int currSkill) {
        if (80011 == currSkill || 81032 == currSkill || 80023 == currSkill || 81024 == currSkill || 81002 == currSkill || 83039 == currSkill) {
            final ActionDetailParam actionDetailParamBac = battleUnit.getActionDetailParamBac();
            if (actionDetailParamBac != null) {
                Integer status = null;
                if (80011 == currSkill || 81032 == currSkill) {
                    status = 105;
                }
                else if (80023 == currSkill || 81024 == currSkill) {
                    status = 116;
                }
                else if (81002 == currSkill) {
                    status = 117;
                }
                else if (83039 == currSkill) {
                    status = 120;
                }
                ActionParamUtils.setNormalAttack(battleUnit.getTarget().getId(), battleUnit.getActionDetailParamBac(), battleUnit.getActionDetailParam(), status);
                battleUnit.setActionDetailParam(battleUnit.getActionDetailParamBac());
            }
        }
    }
    
    public static void normalAttackDoubleHit(final Fight fight, final FightObject effector, final FightObject target, final BattleUnit battleUnit, final int hurt, final int skillSerial) {
        writeActionDetailParam(battleUnit, skillSerial);
        if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic() && hurt > 0) {
            SkillProcessor skillProcessor = null;
            final List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.NORMAL_ATTACK.getValue(), effector.getSkillIds(), fight.isPvp());
            if (petSkillList.size() > 0) {
                for (final SysPetSkill petSkill : petSkillList) {
                    if (isTriggerGFF(petSkill, skillSerial)) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        skillProcessor.normalAttack(fight, effector, target, battleUnit.getActionDetailParam(), hurt, skillSerial);
                    }
                }
            }
        }
    }
    
    private static boolean isTriggerGFF(final SysPetSkill petSkill, final int currSkill) {
        return petSkill != null && (currSkill <= 0 || (80011 != petSkill.getSerial() && 81032 != petSkill.getSerial() && 80023 != petSkill.getSerial() && 81024 != petSkill.getSerial() && 81002 != petSkill.getSerial()) || (80011 != currSkill && 81032 != currSkill && ((80023 != petSkill.getSerial() && 81024 != petSkill.getSerial()) || (80023 != currSkill && 81024 != currSkill)) && (81002 != petSkill.getSerial() || 81002 != currSkill)));
    }
    
    public static void normalAttackAfter(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam) {
        if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic()) {
            SkillProcessor skillProcessor = null;
            final List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.NORMAL_ATTACK_AFTER.getValue(), effector.getSkillIds(), fight.isPvp());
            if (petSkillList.size() > 0) {
                for (final SysPetSkill petSkill : petSkillList) {
                    if (isTriggerGFF(petSkill, actionDetailParam.getSkillSerial())) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        skillProcessor.normalAttack(fight, effector, target, actionDetailParam, new Object[0]);
                    }
                }
            }
        }
    }
    
    public static int reduceDoubleHit(final Fight fight, final FightObject effector) {
        try {
            final SkillProcessor skillProcessor = SkillProcessor.queryProcessor(81057);
            if (skillProcessor != null) {
                final NutMap passive = skillProcessor.passive(fight, effector, new Object[0]);
                final int num = passive.getInt("num");
                return num;
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
