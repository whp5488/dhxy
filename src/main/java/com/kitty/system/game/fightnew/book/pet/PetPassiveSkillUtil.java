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
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class PetPassiveSkillUtil
{
    public PetPassiveSkillUtil() {
    }
    
    public static void roundStartPassive(Fight fight) {
        try {
            excuteRoundStartPassive(fight);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void excuteRoundStartPassive(Fight fight) {
        ArrayList<FightObject> allFightObjects = fight.getAllFightObjects();
        for (FightObject effector : allFightObjects) {
            SkillProcessor skillProcessor = null;
            if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic() && !effector.isFallDown()) {
                int isPvp = fight.isPvp();
                List<RoleSkill> petRoleSkills = (List)effector.getSkillIds().stream().filter(skill -> -1 != (int)skill.getSkillId()).collect(Collectors.toList());
                List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START.getValue(), petRoleSkills, isPvp);
                if (petSkillList.size() > 0) {
                    for (SysPetSkill petSkill : petSkillList) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        skillProcessor.passive(fight, effector, petSkill);
                    }
                }
                if (fight.getRound() == 1) {
                    List<FightObject> enemyPetSide = (List)fight.getTargetSide((int)effector.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
                    for (FightObject fightObject : enemyPetSide) {
                        List<SysPetSkill> petSkillListEntering = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_ENTERING_SIDE.getValue(), (List)fightObject.getSkillIds().stream().filter(skill -> -1 != (int)skill.getSkillId()).collect(Collectors.toList()), isPvp);
                        if (petSkillListEntering != null && petSkillListEntering.size() > 0) {
                            for (SysPetSkill petSkill2 : petSkillListEntering) {
                                skillProcessor = SkillProcessor.queryProcessor(petSkill2.getSerial());
                                skillProcessor.passive(fight, fightObject, effector, petSkill2);
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
                Map<Integer, SysPetSkill> petSkillMapForN = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_CONTROL.getValue());
                if (petSkillMapForN.size() > 0) {
                    for (RoleSkill roleSkill : petRoleSkills) {
                        SysPetSkill petSkill = (SysPetSkill)petSkillMapForN.get(roleSkill.getSkillId());
                        if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
                            skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                            skillProcessor.passive(fight, effector, new Object[0]);
                        }
                        else {
                            continue;
                        }
                    }
                }
                if ((double)(effector.getCurrLife() / effector.getMaxLife()) < 0.4) {
                    List<SysPetSkill> petSkillList2 = PetTriggerPassiveSkill.currLifeLessThan40(petRoleSkills, isPvp);
                    if (petSkillList2 != null) {
                        for (SysPetSkill petSkill3 : petSkillList2) {
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
            else {
                continue;
            }
        }
        roundStartAction(fight);
        deathExcute(fight);
    }
    
    private static void roundStartAction(Fight fight) {
        BeforeActionSkill beforeActionSkillA = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(true));
        BeforeActionSkill beforeActionSkillB = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(false));
        roundStartActionExcute(fight, beforeActionSkillA, beforeActionSkillB, PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START_ACTION.getValue(), false);
    }
    
    public static void roundStartActionExcute(Fight fight, BeforeActionSkill beforeActionSkillM, BeforeActionSkill beforeActionSkillT, String type, boolean isA) {
        Map<Integer, SysPetSkill> resourceActionMap = PetTriggerPassiveSkill.querySkills(type);
        boolean flag = false;
        for (Map.Entry<Integer, SysPetSkill> entry : resourceActionMap.entrySet()) {
            SysPetSkill petSkill = (SysPetSkill)entry.getValue();
            if (beforeActionSkillM != null && beforeActionSkillM.isPassiveAction()) {
                for (Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkillM.getRoleSkillMap().entrySet()) {
                    if (((List)entry2.getValue()).contains(petSkill.getSerial())) {
                        FightObject obj = fight.queryFightObject((Integer)entry2.getKey());
                        if (obj == null || obj.isFallDown()) {
                            return;
                        }
                        SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(((SysPetSkill)entry.getValue()).getSerial());
                        passiveSkillProcessor.passive(fight, obj, petSkill);
                        flag = true;
                        break;
                    }
                    else {
                        continue;
                    }
                }
            }
            if (flag) {
                flag = false;
                break;
            }
            if (beforeActionSkillT != null && beforeActionSkillT.isPassiveAction()) {
                for (Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkillT.getRoleSkillMap().entrySet()) {
                    if (((List)entry2.getValue()).contains(petSkill.getSerial())) {
                        FightObject obj = fight.queryFightObject((Integer)entry2.getKey());
                        if (obj == null || obj.isFallDown()) {
                            return;
                        }
                        SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(((SysPetSkill)entry.getValue()).getSerial());
                        passiveSkillProcessor.passive(fight, obj, petSkill);
                        break;
                    }
                    else {
                        continue;
                    }
                }
            }
            else {
                continue;
            }
        }
    }
    
    public static void enteringSideAction(Fight fight, FightObject petObject) {
        Map<Integer, SysPetSkill> enteringSideSysPetSkillMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_ACTION.getValue());
        List<RoleSkill> petRoleSkill = (List)petObject.getSkillIds().stream().filter(skill -> (int)skill.getSkillId() != -1).collect(Collectors.toList());
        if (petRoleSkill.size() > 0) {
            for (Map.Entry<Integer, SysPetSkill> entry : enteringSideSysPetSkillMap.entrySet()) {
                for (RoleSkill roleSkill : petRoleSkill) {
                    if (((Integer)entry.getKey()).equals(roleSkill.getSkillId())) {
                        SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(roleSkill.getSkillId());
                        passiveSkillProcessor.passive(fight, petObject, new Object[0]);
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    public static double getSuffix(Integer lv, Integer rei, long close) {
        return Math.pow((double)(int)lv, 0.5) / 10.0 + Math.pow((double)close, 0.16666666666666666) * 10.0 / (double)(100 + (int)rei * 20);
    }
    
    public static List<FiedValue> clearStatus(FightObject target, String type) {
        if (target != null) {
            StatusForSkillDispose statusForSkillDispose = new StatusForSkillDispose();
            List<Integer> statusList = new ArrayList<>();
            List<FiedValue> list = new ArrayList<>();
            int n = -1;
            switch (type.hashCode()) {
                case 1585146952: {
                    if (type.equals("abnormal")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 747805177: {
                    if (type.equals("positive")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case 2068759323: {
                    if (type.equals("wuXinModification")) {
                        n = 2;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    statusList = statusForSkillDispose.disposeAbnormalStatus(target, true);
                    if (statusList.size() > 0) {
                        list.add(new FiedValue("targetClearStatus", (byte)3, statusList));
                        break;
                    }
                    break;
                }
                case 1: {
                    if (!target.isFrozen()) {
                        for (int status : StatusConst.getPositiveStatus()) {
                            if (target.getStatusMap().getInt(String.valueOf(status)) > 0) {
                                statusList.add(Integer.valueOf(status));
                                statusForSkillDispose.disposePositiveStatus(target, status, true);
                                if (1023 == status) {
                                    list.add(new FiedValue("targetMaxHp", (byte)3, Integer.valueOf(target.getMaxLife())));
                                    list.add(new FiedValue("targetMaxMp", (byte)3, Integer.valueOf(target.getMaxMana())));
                                    list.add(new FiedValue("targetCurrHp", (byte)3, Integer.valueOf(target.getCurrLife())));
                                    list.add(new FiedValue("targetCurrMp", (byte)3, Integer.valueOf(target.getCurrMana())));
                                }
                                else {
                                    continue;
                                }
                            }
                            else {
                                continue;
                            }
                        }
                        if (statusList.size() > 0) {
                            list.add(new FiedValue("targetClearStatus", (byte)3, statusList));
                            break;
                        }
                        break;
                    }
                    else {
                        break;
                    }
                }
                case 2: {
                    if (!target.isFrozen()) {
                        statusList = statusForSkillDispose.disposeWuXinModification(target, true);
                        if (statusList.size() > 0) {
                            list.add(new FiedValue("targetClearStatus", (byte)3, statusList));
                            break;
                        }
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
            return list;
        }
        return null;
    }
    
    public static void otherEnterBattle(Fight fight, Integer newPetId) {
        try {
            excuteOtherEnterBattle(fight, newPetId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void excuteOtherEnterBattle(Fight fight, Integer newPetId) {
        FightObject petObject = fight.queryFightObject(newPetId);
        List<RoleSkill> petSkills = (List)petObject.getSkillIds().stream().filter(skill -> -1 != (int)skill.getSkillId()).collect(Collectors.toList());
        SkillProcessor skillProcessor = null;
        List<SysPetSkill> petSkillListNF = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF.getValue(), petSkills, fight.isPvp());
        if (petSkillListNF.size() > 0) {
            for (SysPetSkill petSkill : petSkillListNF) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                skillProcessor.passive(fight, petObject, (Object[])null, petSkill);
            }
        }
        List<FightObject> enemyPetSide = (List)fight.getTargetSide((int)petObject.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
        for (FightObject fightObject : enemyPetSide) {
            List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_ENTERING_SIDE.getValue(), (List)fightObject.getSkillIds().stream().filter(skill -> -1 != (int)skill.getSkillId()).collect(Collectors.toList()), fight.isPvp());
            if (petSkillList != null && petSkillList.size() > 0) {
                for (SysPetSkill petSkill2 : petSkillList) {
                    skillProcessor = SkillProcessor.queryProcessor(petSkill2.getSerial());
                    skillProcessor.passive(fight, fightObject, petObject, petSkill2);
                }
            }
            else {
                continue;
            }
        }
        if (LOGIC.LOGICTYPE_PET.getValue() == petObject.getLogic()) {
            fight.excuteReduceRatio(petObject, true);
        }
        Map<Integer, List<Integer>> petSkillMap = PetSkillEffectorUtil.getActionSkill(fight, Arrays.asList(petObject));
        if (petSkillMap != null && petSkillMap.size() > 0) {
            List<FightObject> targets = fight.getTargetSide((int)petObject.getId());
            PetSkillEffectorUtil.excuteSendAction(fight, targets, petSkillMap);
        }
        enteringSideAction(fight, petObject);
        List<SysPetSkill> petSkillListEntering = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE.getValue(), petSkills, fight.isPvp());
        petSkillListEntering = (List)petSkillListEntering.stream().sorted(Comparator.comparing(SysPetSkill::getExecutionSequence)).collect(Collectors.toList());
        if (petSkillListEntering.size() > 0) {
            for (SysPetSkill petSkill2 : petSkillListEntering) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill2.getSerial());
                skillProcessor.passive(fight, petObject, petSkill2);
            }
        }
        List<SysPetSkill> petSkillListNFAction = PetTriggerPassiveSkill.roundStart(PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF_ACTION.getValue(), petSkills, fight.isPvp());
        if (petSkillListNFAction.size() > 0) {
            for (SysPetSkill petSkill3 : petSkillListNFAction) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill3.getSerial());
                skillProcessor.passive(fight, petObject, (Object[])null, petSkill3);
            }
        }
        TianFuSkillUtils.enteringSide(fight, petObject);
        AwakenSkillUtils.enteringSide(fight, petObject);
        PassiveSkillUtils.enterFight(fight, petObject);
    }
    
    public static boolean isExcuteControlleSkill(FightObject effector, int count) {
        boolean returnFlag = false;
        NutMap statusMap = effector.getStatusMap();
        int anInt = statusMap.getInt("1999record");
        if (anInt >= count) {
            returnFlag = true;
        }
        return returnFlag;
    }
    
    public static boolean isAbnormalOrPositiveStatus(NutMap map, String type) {
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
        for (int status : statuss) {
            if (map.getInt(String.valueOf(status)) > 0) {
                return true;
            }
            continue;
        }
        return false;
    }
    
    public static void deathExcute(Fight fight) {
        try {
            petDeathExcute(fight);
            monsterDeathExcute(fight);
            otherExcute(fight);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void petDeathExcute(Fight fight) {
        List<FightObject> fallDownObjs = (List)fight.getAllFightObjects().stream().filter(obj -> obj.isFallDown() && obj.isPet()).collect(Collectors.toList());
        if (fallDownObjs != null && fallDownObjs.size() > 0) {
            for (FightObject target : fallDownObjs) {
                if (!fight.getDeadPetIds().contains(target.getId())) {
                    excuteDieAction(fight, target);
                }
                else {
                    continue;
                }
            }
            petDeathExcute(fight);
        }
        else {
            List<FightObject> fallDownPlayers = (List)fight.getAllFightObjects().stream().filter(obj -> obj.isFallDown() && obj.isPlayer()).collect(Collectors.toList());
            Iterator<FightObject> iterator = fallDownPlayers.iterator();
            while (iterator.hasNext()) {
            	FightObject obj = (FightObject)iterator.next();
                FightObject objPet = fight.queryFightObject(Integer.valueOf(obj.getFightPetId()));
                if (objPet != null) {
                    boolean flag = PetTriggerPassiveSkill.heroFallDown(fight, objPet);
                    if (flag) {
                        petDeathExcute(fight);
                    }
                }
                if (obj.isFallDown() && obj.getDieRound() <= 0) {
                    obj.setDieRound(fight.getRound());
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public static void monsterDeathExcute(Fight fight) {
        List<FightObject> fallDownMonsters = (List)fight.getAllFightObjects().stream().filter(obj -> obj.isFallDown() && obj.isMonster()).collect(Collectors.toList());
        if (fallDownMonsters != null && fallDownMonsters.size() > 0) {
            for (FightObject monsterOjb : fallDownMonsters) {
                int monsterType = ((MonsterFightObject)monsterOjb).getMonsterType();
                SysDataMonster monster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(monsterOjb.getSerial()));
                if (monster != null && monster.getVanish() == 1) {
                    return;
                }
                if (!Const.vanish.contains(Integer.valueOf(monsterType))) {
                    sendDisappearData(fight, monsterOjb);
                    fight.removeFightObject(monsterOjb);
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public static void otherExcute(Fight fight) {
        boolean isTriggerSupportPet = false;
        for (FightObject obj : fight.getAllFightObjects()) {
            if (obj.isPlayer()) {
                isTriggerSupportPet = obj.getOtherMap().getBoolean("isTriggerSupportPet", false);
                if (isTriggerSupportPet) {
                    triggerFlashBeforeOne(fight, obj);
                    isTriggerSupportPet = false;
                    obj.getOtherMap().setv("isTriggerSupportPet", Boolean.valueOf(false));
                }
                else {
                    continue;
                }
            }
            else {
                continue;
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
        ActionParamUtils.excuteDisappear(fight, target, (int)target.getId(), 46, 0, 1);
    }
    
    public static boolean petDieProcessing(Fight fight, FightObject target) {
        boolean isRevive = false;
        boolean isCoerceExit = target.getOtherMap().getBoolean("isCoerceExit", false);
        if ((target.isPet() || target.isPetMirror()) && !isCoerceExit) {
            isRevive = excuteReviveSkill(fight, target);
            if (!isRevive) {
                isRevive = AwakenSkillUtils.revive(fight, target);
            }
            if (!isRevive) {
                fight.excuteExitPassive(target);
            }
        }
        target.getOtherMap().setv("isCoerceExit", Boolean.valueOf(false));
        if (!isRevive) {
            fight.petDie(target);
        }
        return isRevive;
    }
    
    public static void friendMagic(Fight fight, FightObject attacker, int isPvp, ActionDetailParam actionDetailParam) {
        List<FightObject> objList = getSkillForPet(fight, attacker.isASide());
        Map<Integer, SysPetSkill> resourceMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.FRIEND_MAGIC.getValue());
        for (FightObject obj : objList) {
            List<RoleSkill> roleSkills = (List)obj.getSkillIds().stream().filter(roleSkill -> -1 != (int)roleSkill.getSkillId()).collect(Collectors.toList());
            Iterator<RoleSkill> iterator2 = roleSkills.iterator();
            while (iterator2.hasNext()) {
            	RoleSkill roleSkill = (RoleSkill)iterator2.next();
                SysPetSkill petSkill = (SysPetSkill)resourceMap.get(roleSkill.getSkillId());
                if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
                    SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    passiveSkillProcessor.normalAttack(fight, obj, attacker, actionDetailParam, new Object[0]);
                }
                else {
                    continue;
                }
            }
        }
    }
    
    private static List<FightObject> getSkillForPet(Fight fight, boolean isASide) {
        List<FightObject> objList = null;
        if (isASide) {
            objList = (List)fight.getListA().stream().filter(obj -> obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
            return objList;
        }
        objList = (List)fight.getListB().stream().filter(obj -> obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
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
        BeforeActionSkill beforeActionSkillM = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(attacker.isASide()));
        BeforeActionSkill beforeActionSkillT = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(!attacker.isASide()));
        Map<Integer, SysPetSkill> resourceActionMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.UNIT_MAGIC_ACTION.getValue());
        boolean flag = false;
        for (Map.Entry<Integer, SysPetSkill> entry : resourceActionMap.entrySet()) {
            if (beforeActionSkillM != null && beforeActionSkillM.isPassiveAction()) {
                for (Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkillM.getRoleSkillMap().entrySet()) {
                    if (((List)entry2.getValue()).contains(entry.getKey())) {
                        SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(((SysPetSkill)entry.getValue()).getSerial());
                        passiveSkillProcessor.effect(fight, fight.queryFightObject((Integer)entry2.getKey()), attacker, Integer.valueOf(attackerProcessor.costMana(skill, new Object[0])));
                        flag = true;
                        break;
                    }
                    continue;
                }
            }
            if (flag) {
                flag = false;
                break;
            }
            if (beforeActionSkillT != null && beforeActionSkillT.isPassiveAction()) {
                for (Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkillT.getRoleSkillMap().entrySet()) {
                    if (((List)entry2.getValue()).contains(entry.getKey())) {
                        SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(((SysPetSkill)entry.getValue()).getSerial());
                        passiveSkillProcessor.effect(fight, fight.queryFightObject((Integer)entry2.getKey()), attacker, Integer.valueOf(attackerProcessor.costMana(skill, new Object[0])));
                        break;
                    }
                    continue;
                }
            }
            else {
                continue;
            }
        }
    }
    
    private static Effect excutePetSkill(Fight fight, FightObject attacker, SkillProcessor attackerProcessor, RoleSkill skill, Map<Integer, SysPetSkill> resourceMap, List<FightObject> objList, int isPvp) {
        Effect effect = null;
        for (FightObject obj : objList) {
            List<RoleSkill> roleSkills = (List)obj.getSkillIds().stream().filter(roleSkill -> -1 != (int)roleSkill.getSkillId()).collect(Collectors.toList());
            Iterator<RoleSkill> iterator2 = roleSkills.iterator();
            while (iterator2.hasNext()) {
            	RoleSkill roleSkill = (RoleSkill)iterator2.next();
                SysPetSkill petSkill = (SysPetSkill)resourceMap.get(roleSkill.getSkillId());
                if (petSkill != null && (isPvp == 1 || petSkill.getIsPvp() == isPvp)) {
                    SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    effect = passiveSkillProcessor.effect(fight, obj, attacker, Integer.valueOf(attackerProcessor.costMana(skill, new Object[0])), petSkill);
                }
                else {
                    continue;
                }
            }
        }
        return effect;
    }
    
    public static boolean enemyFristMagic(Fight fight, FightObject attacker, SkillProcessor attackerProcessor, RoleSkill skill, int isPvp) {
        BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(attacker.isASide()));
        if (beforeActionSkill == null || !beforeActionSkill.isPassiveAction()) {
            return false;
        }
        boolean returnFlag = excuteEnemyMagic(fight, attacker, beforeActionSkill, (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_FIRST_MAGIC.getValue()), attackerProcessor.costMana(skill, new Object[0]));
        return returnFlag;
    }
    
    private static boolean excuteEnemyMagic(Fight fight, FightObject attacker, BeforeActionSkill beforeActionSkill, Map<Integer, SysPetSkill> petSkills, int hurtParam) {
        boolean returnFlag = false;
        List<Integer> keys = new ArrayList(beforeActionSkill.getRoleSkillMap().keySet());
        for (int j = keys.size() - 1; j >= 0; --j) {
            List<Integer> values = (List)beforeActionSkill.getRoleSkillMap().get(keys.get(j));
            for (int i = values.size() - 1; i >= 0; --i) {
                Integer skillId = (Integer)values.get(i);
                SysPetSkill petSkill = (SysPetSkill)petSkills.get(skillId);
                if (petSkill != null) {
                    SkillProcessor passiveSkillProcessor = SkillProcessor.queryProcessor(skillId);
                    FightObject pet = fight.queryFightObject((Integer)keys.get(j));
                    if (pet == null || pet.isFallDown()) {
                        pet = attacker;
                    }
                    Effect effect = passiveSkillProcessor.effect(fight, pet, attacker, Integer.valueOf(hurtParam), petSkill);
                    if (effect != null && 99 == effect.getType()) {
                        returnFlag = true;
                    }
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
                if (entry.getValue() instanceof Integer && (int)entry.getValue() > 0 && (((String)entry.getKey()).equals(String.valueOf(1002)) || ((String)entry.getKey()).equals(String.valueOf(1003)) || ((String)entry.getKey()).equals(String.valueOf(1004)) || ((String)entry.getKey()).equals(String.valueOf(1006)) || ((String)entry.getKey()).equals(String.valueOf(1019)))) {
                    if (statusMap.get("1999record") != null) {
                        recordValue = statusMap.getInt("1999record") + 1;
                    }
                    statusMap.put("1999record", Integer.valueOf(recordValue));
                    break;
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public static ActionDetailParam passivePhysics(Fight fight, FightObject effector, FightObject target, BattleUnit battleUnit, ActionDetailParam actionDetailParam, int skillSerial) {
        createActionDetailParam(battleUnit, effector, target, actionDetailParam, skillSerial);
        if (effector.isPlayer()) {
            battleUnit.doPhyHit();
        }
        else if (effector.isPet()) {
            battleUnit.petDoPhyHit();
        }
        else if (effector.isMonster()) {
            if (fight.getSpecialMonsterType() == 1 && effector.getPos() == 10003 && effector.getName().equals("地飞星")) {
                battleUnit.doPhyHit();
            }
            else if (skillSerial > 80000 && skillSerial < 90000) {
                battleUnit.petDoPhyHit();
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        fightNewService.checkOrderTarget(fight, Collections.singletonList(target.getId()));
        if (target.isFallDown()) {
            effector.addFallDownList((int)target.getId());
            fightNewService.checkGuessSuccess(fight);
            fight.onActorDie(target);
        }
        if (effector.isFallDown()) {
            fight.onActorDie(effector);
        }
        return battleUnit.getActionDetailParam();
    }
    
    private static void createActionDetailParam(BattleUnit battleUnit, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, int currSkill) {
        if (actionDetailParam != null && (80011 == currSkill || 81032 == currSkill || 80023 == currSkill || 81024 == currSkill || 81002 == currSkill || 83039 == currSkill)) {
            battleUnit.setActionDetailParamBac(actionDetailParam);
        }
        battleUnit.createActionDetailParam(effector, target, 7, currSkill, true);
    }
    
    public static void magicOther(Fight fight, FightObject effector, FightObject target, RoleSkill skill, ActionDetailParam actionDetailParam) {
        Map<Integer, SysPetSkill> map = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.MAGIC_ATTACK_END.getValue());
        if (skill != null && map.get(skill.getSkillId()) != null) {
            SkillProcessor skillProcessor = SkillProcessor.queryProcessor(skill.getSkillId());
            skillProcessor.normalAttack(fight, effector, target, actionDetailParam, skill.getSkillId());
        }
    }
    
    private static void triggerFlashBeforeOne(Fight fight, FightObject actor) {
        List<AssistParam> assistParams = null;
        FightObject host = null;
        if (actor.isPet()) {
            Optional<FightObject> mySideOpt = fight.getMySide((int)actor.getId()).stream().filter(obj -> !obj.isFallDown() && !obj.getId().equals(actor.getId())).findFirst();
            if (!mySideOpt.isPresent()) {
                return;
            }
            host = fight.queryFightObject(Integer.valueOf(actor.getHostId()));
        }
        else if (actor.isPlayer() && !actor.isFallDown()) {
            boolean flag = actor.getOtherMap().getBoolean("isTriggerSupportPet", false);
            if (flag) {
                host = actor;
            }
        }
        assistParams = ((PlayerFightObject)host).getAssistParams();
        Set<Integer> deadPetIds = fight.getDeadPetIds();
        int petId = 0;
        for (AssistParam assistParam : assistParams) {
            if (!deadPetIds.contains(assistParam.getPetId()) && !assistParam.isTrigger()) {
                petId = (int)assistParam.getPetId();
                boolean isExcuteSuccess = excuteFlash(fight, host, Integer.valueOf(petId));
                if (isExcuteSuccess) {
                    assistParam.setTrigger(true);
                    break;
                }
                break;
            }
            else {
                continue;
            }
        }
    }
    
    private static boolean excuteFlash(Fight fight, FightObject host, Integer petId) {
        Map<Integer, SysPetSkill> petSkillMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.FLASH_BEFORE_ONE.getValue());
        Role role = host.getRole();
        Pet pet = (Pet)SpringUtils.getPetService().selectPet(role).get(petId);
        if (pet == null) {
            return false;
        }
        boolean inFight = PetSkillEffectorUtil.isInFight(role, pet);
        if (!inFight) {
            return false;
        }
        List<PetSkill> petSkills = (List)pet.getSkills().stream().filter(skill -> skill.getSerial() != -1).collect(Collectors.toList());
        List<SysPetSkill> newSysPetSkills = new ArrayList<>();
        Iterator<PetSkill> iterator = petSkills.iterator();
        while (iterator.hasNext()) {
        	PetSkill skill = (PetSkill)iterator.next();
            SysPetSkill petSkill = (SysPetSkill)petSkillMap.get(Integer.valueOf(skill.getSerial()));
            if (petSkill != null && skill.getSerial() == (int)petSkill.getSerial()) {
                newSysPetSkills.add(petSkill);
            }
            else {
                continue;
            }
        }
        if (newSysPetSkills.size() <= 0) {
            return false;
        }
        newSysPetSkills = (List)newSysPetSkills.stream().sorted(Comparator.comparing(SysPetSkill::getSerial)).collect(Collectors.toList());
        Map<Integer, SysPetSkill> newSysPetSkillMap = (Map)newSysPetSkills.stream().collect(Collectors.toMap(SysPetSkill::getSerial, sysPetSkill -> sysPetSkill));
        SysPetSkill excuteSkill = (SysPetSkill)newSysPetSkills.get(0);
        if (newSysPetSkills.size() > 1) {
            excuteSkill = PetSkillEffectorUtil.queryCover(excuteSkill, newSysPetSkillMap);
        }
        SkillProcessor processor = SkillProcessor.queryProcessor(excuteSkill.getSerial());
        NutMap passive = processor.passive(fight, host, role, pet);
        return passive != null;
    }
    
    public static boolean excuteReviveSkill(Fight fight, FightObject attacker) {
        Map<Integer, SysPetSkill> reviveSkills = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.REVIVE_SKILL.getValue());
        boolean isRevive = false;
        if (reviveSkills.size() > 0) {
            List<Integer> skillIds = PetTriggerPassiveSkill.getInegers((List)attacker.getSkillIds().stream().filter(roleSkill -> (int)roleSkill.getSkillId() > 1).collect(Collectors.toList()));
            for (Map.Entry<Integer, SysPetSkill> entry : reviveSkills.entrySet()) {
                Integer key = (Integer)entry.getKey();
                if (skillIds.contains(key)) {
                    SkillProcessor skillProcessor = SkillProcessor.queryProcessor(key);
                    NutMap passive = skillProcessor.passive(fight, attacker, new Object[0]);
                    isRevive = passive.getBoolean("successful");
                }
                else {
                    continue;
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
            if (petSkillList.size() > 0) {
                for (SysPetSkill petSkill : petSkillList) {
                    if (skill == null) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        effectPart = skillProcessor.effect(fight, effector, target, Integer.valueOf(hurt), petSkill, null, battleUnit);
                    }
                    else if (!petSkill.getSerial().equals(skill.getSkillId())) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        effectPart = skillProcessor.effect(fight, effector, target, Integer.valueOf(hurt), petSkill, skill.getSkillId(), battleUnit);
                    }
                    else {
                        continue;
                    }
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
                    status = Integer.valueOf(105);
                }
                else if (80023 == currSkill || 81024 == currSkill) {
                    status = Integer.valueOf(116);
                }
                else if (81002 == currSkill) {
                    status = Integer.valueOf(117);
                }
                else if (83039 == currSkill) {
                    status = Integer.valueOf(120);
                }
                ActionParamUtils.setNormalAttack((int)battleUnit.getTarget().getId(), battleUnit.getActionDetailParamBac(), battleUnit.getActionDetailParam(), status);
                battleUnit.setActionDetailParam(battleUnit.getActionDetailParamBac());
            }
        }
    }
    
    public static void normalAttackDoubleHit(Fight fight, FightObject effector, FightObject target, BattleUnit battleUnit, int hurt, int skillSerial) {
        writeActionDetailParam(battleUnit, skillSerial);
        if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic() && hurt > 0) {
            SkillProcessor skillProcessor = null;
            List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.NORMAL_ATTACK.getValue(), effector.getSkillIds(), fight.isPvp());
            if (petSkillList.size() > 0) {
                for (SysPetSkill petSkill : petSkillList) {
                    if (isTriggerGFF(petSkill, skillSerial)) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        skillProcessor.normalAttack(fight, effector, target, battleUnit.getActionDetailParam(), Integer.valueOf(hurt), Integer.valueOf(skillSerial));
                    }
                    else {
                        continue;
                    }
                }
            }
        }
    }
    
    private static boolean isTriggerGFF(SysPetSkill petSkill, int currSkill) {
        return petSkill != null && (currSkill <= 0 || (80011 != (int)petSkill.getSerial() && 81032 != (int)petSkill.getSerial() && 80023 != (int)petSkill.getSerial() && 81024 != (int)petSkill.getSerial() && 81002 != (int)petSkill.getSerial()) || (80011 != currSkill && 81032 != currSkill && ((80023 != (int)petSkill.getSerial() && 81024 != (int)petSkill.getSerial()) || (80023 != currSkill && 81024 != currSkill)) && (81002 != (int)petSkill.getSerial() || 81002 != currSkill)));
    }
    
    public static void normalAttackAfter(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam) {
        if (LOGIC.LOGICTYPE_PET.getValue() == effector.getLogic()) {
            SkillProcessor skillProcessor = null;
            List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.NORMAL_ATTACK_AFTER.getValue(), effector.getSkillIds(), fight.isPvp());
            if (petSkillList.size() > 0) {
                for (SysPetSkill petSkill : petSkillList) {
                    if (isTriggerGFF(petSkill, actionDetailParam.getSkillSerial())) {
                        skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                        skillProcessor.normalAttack(fight, effector, target, actionDetailParam, new Object[0]);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
    }
    
    public static int reduceDoubleHit(Fight fight, FightObject effector) {
        try {
            SkillProcessor skillProcessor = SkillProcessor.queryProcessor(Integer.valueOf(81057));
            if (skillProcessor != null) {
                NutMap passive = skillProcessor.passive(fight, effector, new Object[0]);
                int num = passive.getInt("num");
                return num;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
