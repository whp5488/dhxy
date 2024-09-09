package com.kitty.system.game.fightnew.book.pet.utils;

import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.book.vo.PetSkillAction;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import java.util.HashSet;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.common.utils.StringUtils;
import java.util.LinkedHashMap;
import java.util.Comparator;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.Set;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.Arrays;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import java.util.Iterator;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Map;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import com.kitty.system.init.domain.SysPetSkill;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class PetSkillEffectorUtil
{
    public PetSkillEffectorUtil() {
    }
    
    public static double getSuffix(Integer lv, Integer rei, long close) {
        return Math.pow((double)(int)lv, 0.5) / 10.0 + Math.pow((double)close, 0.16666666666666666) * 10.0 / (double)(100 + (int)rei * 20);
    }
    
    public static void sendMsg(Fight fight, FightObject effector, String msgStr) {
        IoSession session = effector.getSession();
        if (session != null) {
            RespMsg msg = new RespMsg();
            msg.setMsg(msgStr);
            MessagePusher.pushMessage(session, msg);
        }
    }
    
    public static SysPetSkill initiativeQueryCover(SysPetSkill sysPetSkill, List<RoleSkill> skills, Map<Integer, SysPetSkill> sysPetSkillMap) {
        List<RoleSkill> skillIds = (List)skills.stream().filter(obj -> -1 != (int)obj.getSkillId()).collect(Collectors.toList());
        Map<Integer, SysPetSkill> listPetSkillMap = new HashMap();
        for (RoleSkill skill : skillIds) {
            SysPetSkill petSkill = (SysPetSkill)sysPetSkillMap.get(skill.getSkillId());
            if (petSkill != null && petSkill.getSerial().equals(skill.getSkillId())) {
                listPetSkillMap.put(petSkill.getSerial(), petSkill);
            }
            else {
                continue;
            }
        }
        return queryCover(sysPetSkill, listPetSkillMap);
    }
    
    public static SysPetSkill queryCover(SysPetSkill sysPetSkill, Map<Integer, SysPetSkill> sysPetSkillMap) {
        String coverSkillSerials = sysPetSkill.getCoverSkillSerials();
        if (coverSkillSerials == null) {
            return sysPetSkill;
        }
        String[] skills;
        for (String skill : skills = coverSkillSerials.split(",")) {
            SysPetSkill petSkill = (SysPetSkill)sysPetSkillMap.get(Integer.valueOf(Integer.parseInt(skill)));
            if (petSkill != null) {
                sysPetSkill = petSkill;
                queryCover(sysPetSkill, sysPetSkillMap);
            }
        }
        return sysPetSkill;
    }
    
    public static FightObject getTargetForPos(List<FightObject> targets, int pos) {
        FightObject target = null;
        targets = (List)targets.stream().filter(obj -> obj.getPos() % 10000 == pos).collect(Collectors.toList());
        if (targets.size() > 0) {
            target = (FightObject)targets.get(0);
            return target;
        }
        return target;
    }
    
    public static void sendActionExcute(Fight fight, Integer petId, int skill, Integer targetId) {
        int[] skillStatus = (int[])Arrays.stream(SkillConst.skillStatus).filter(obj -> skill == obj[0]).findAny().get();
        ActionParamUtils.addStatus(fight, fight.queryFightObject(petId), fight.queryFightObject(targetId), 33, skill, 2, skillStatus[1], -3);
    }
    
    public static ActionDetailParam sendActionExcuteReturn(Integer petId, int skill, Integer targetId) {
        int[] skillStatus = (int[])Arrays.stream(SkillConst.skillStatus).filter(obj -> skill == obj[0]).findAny().get();
        ActionDetailParam actionDetailParam = ActionParamUtils.addStatusReturn((int)petId, (int)targetId, 33, skill, 2, skillStatus[1], -3);
        return actionDetailParam;
    }
    
    public static void sendCancelAction(Fight fight, FightObject actor) {
        BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(!actor.isASide()));
        sendCancelAction(fight, actor.getId(), beforeActionSkill, fight.getTargetSide((int)actor.getId()), false);
        BeforeActionSkill beforeActionSkillMySide = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(actor.isASide()));
        sendCancelAction(fight, actor.getId(), beforeActionSkillMySide, fight.getMySide((int)actor.getId()), false);
    }
    
    public static void sendCancelAction(Fight fight, Integer actorId, BeforeActionSkill beforeActionSkill, List<FightObject> objSide, boolean isNiuZhuanQianKun) {
        if (beforeActionSkill != null && beforeActionSkill.isPassiveAction() && beforeActionSkill.getRoleSkillMap() != null && beforeActionSkill.getRoleSkillMap().size() > 0) {
            List<Integer> skillIds = (List)beforeActionSkill.getRoleSkillMap().get(actorId);
            if (skillIds != null) {
                FightObject target = getTargetForPos(objSide, 3);
                if (target == null) {
                    return;
                }
                Iterator<Integer> iteratorSkill = skillIds.iterator();
                Integer skill = null;
                boolean flag = true;
                SysPetSkill petSkill = null;
                while (iteratorSkill.hasNext()) {
                    skill = (Integer)iteratorSkill.next();
                    petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skill);
                    if (!isNiuZhuanQianKun && petSkill != null && PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_FIRST_MAGIC.getValue().equals(petSkill.getTriggerType())) {
                        continue;
                    }
                    iteratorSkill.remove();
                    beforeActionSkill.getSkills().remove(skill);
                    if (beforeActionSkill.getSkills().contains(skill)) {
                        flag = false;
                    }
                    if (flag) {
                        if (Arrays.asList(SkillConst.beforeActionSkillOnlyOnce).contains(skill)) {
                            BeforeActionSkill beforeActionSkillReverse = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(!beforeActionSkill.isASide()));
                            if (beforeActionSkillReverse != null && beforeActionSkillReverse.isPassiveAction() && beforeActionSkillReverse.getSkills().contains(skill)) {
                                continue;
                            }
                        }
                        if (82007 == (int)skill) {
                            fight.getAstrictSkillMap().remove(Boolean.valueOf(beforeActionSkill.isASide()));
                        }
                        sendCancelActionExcute(fight, actorId, target.getId(), skill);
                    }
                    flag = true;
                }
                if (!isNiuZhuanQianKun && skillIds != null && skillIds.size() <= 0) {
                    beforeActionSkill.getRoleSkillMap().remove(actorId);
                }
                if (beforeActionSkill.getSkills().size() <= 0 && beforeActionSkill.getRoleSkillMap().size() <= 0) {
                    fight.getBeforeActionMap().remove(Boolean.valueOf(beforeActionSkill.isASide()));
                }
            }
        }
    }
    
    public static void sendCancelActionExcute(Fight fight, Integer actorId, Integer targetId, Integer skill) {
        int[] skillStatus = (int[])Arrays.stream(SkillConst.skillStatus).filter(obj -> (int)skill == obj[0]).findAny().get();
        ActionParamUtils.addStatus(fight, fight.queryFightObject(actorId), fight.queryFightObject(targetId), 34, (int)skill, 2, skillStatus[1], -3);
    }
    
    public static void enemyFirstMagicCancelAction(Fight fight, FightObject effector, FightObject target, BeforeActionSkill beforeActionSkill, Integer skillid) {
        beforeActionSkill.getSkills().remove(skillid);
        List<Integer> values = null;
        Set<Integer> keys = beforeActionSkill.getRoleSkillMap().keySet();
        boolean returnFlag = false;
        for (Integer key : keys) {
            values = (List)beforeActionSkill.getRoleSkillMap().get(key);
            for (Integer skill : values) {
                if (skillid.equals(skill)) {
                    values.remove(skill);
                    sendCancelActionExcute(fight, effector.getId(), target.getId(), skill);
                    returnFlag = true;
                    break;
                }
                continue;
            }
            if (values.size() <= 0) {
                beforeActionSkill.getRoleSkillMap().remove(key);
            }
            if (returnFlag) {
                break;
            }
            continue;
        }
    }
    
    public static void excuteFlashBeforeOne(Fight fight, FightObject host, Role role, Pet pet, int skillSerial) {
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        host.setFightPetId((int)pet.getPetId());
        ActionDetailParam actionDetailParam = new ActionDetailParam(host, 9, skillSerial, (int)pet.getPetId());
        RefreshPet refreshPet = new RefreshPet();
        ArrayList<RefreshPet> refreshPets = new ArrayList<>();
        refreshPets.add(refreshPet);
        refreshPet.setPetId((int)pet.getPetId());
        refreshPet.setType(1);
        actionDetailParam.setPetList(refreshPets);
        actionDetailParam.setRoleId((long)role.getId());
        FightExecutorNewService.PETID_ROLEID.put(pet.getPetId(), pet.getRoleId());
        FightExecutorNewService.FIGHT_ROOMS.put(pet.getPetId(), Integer.valueOf(fight.getId()));
        CombatAddObject combatAddObject = fightNewService.addPet2Fight((int)host.getId(), pet, fight, (int)SkillFunc.getPetPosByMasterPos(Integer.valueOf(host.getPos())));
        List<CombatAddObject> list = new ArrayList<>();
        list.add(combatAddObject);
        actionDetailParam.setCombatAddObjects(list);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        PetPassiveSkillUtil.otherEnterBattle(fight, pet.getPetId());
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
    
    private static void sendAction(Fight fight, FightObject target, Map<Integer, List<Integer>> mapAction) {
        Map<Integer, List<Integer>> map = new HashMap();
        BeforeActionSkill beforeActionSkillMy = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(!target.isASide()));
        BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(target.isASide()));
        List<Integer> skillIdList = new ArrayList<>();
        SysPetSkill petSkill = null;
        boolean flag = true;
        for (Map.Entry<Integer, List<Integer>> entry : mapAction.entrySet()) {
            List<Integer> skillIds = new ArrayList<>();
            Integer key = (Integer)entry.getKey();
            for (Integer skill : entry.getValue()) {
                petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skill);
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
                                sendCancelActionExcute(fight, petId, target.getId(), Integer.valueOf(81028));
                            }
                        }
                        if (beforeActionSkill != null && beforeActionSkill.isPassiveAction() && beforeActionSkill.getSkills().contains(Integer.valueOf(81028))) {
                            Integer petId = deleteAction(beforeActionSkill.getRoleSkillMap(), Integer.valueOf(81028));
                            if ((int)petId != 0) {
                                sendCancelActionExcute(fight, petId, target.getId(), Integer.valueOf(81028));
                            }
                        }
                        if (beforeActionSkillMy != null && beforeActionSkillMy.getSkills().contains(Integer.valueOf(81028))) {
                            Integer petId = deleteAction(beforeActionSkillMy.getRoleSkillMap(), Integer.valueOf(81028));
                            if ((int)petId != 0) {
                                FightObject myObject = getTargetForPos(fight.getTargetSide((int)target.getId()), 3);
                                sendCancelActionExcute(fight, petId, myObject.getId(), Integer.valueOf(81028));
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
                    sendActionExcute(fight, key, (int)skill, target.getId());
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
        saveBeforeActionSkill(fight, target, map, skillIdList);
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
    
    public static boolean checkCover(SysPetSkill petSkill, Fight fight, FightObject effector, FightObject target, boolean isExcute, Object... args) {
        boolean isCover = false;
        String coverSkillSerials = petSkill.getCoverSkillSerials();
        if (StringUtils.isNotEmpty(coverSkillSerials)) {
            String[] strs = coverSkillSerials.split(",");
            List<RoleSkill> skillIds = effector.getSkillIds();
            for (int i = 0; i < strs.length; ++i) {
                for (RoleSkill roleSkill : skillIds) {
                    if (strs[i].equals(roleSkill.getSkillId().toString())) {
                        if (isExcute) {
                            SkillProcessor skillProcessor = SkillProcessor.queryProcessor(roleSkill.getSkillId());
                            skillProcessor.effect(fight, effector, target, args);
                        }
                        isCover = true;
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        return isCover;
    }
    
    public static int computeTMFGQMXLHurt(FightObject effector, FightObject target, int hurt, int SkillId) {
        double kx = 0.0;
        double fk = 0.0;
        double extra = 0.0;
        double weak = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist())) - effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.SH_DK.getDist()));
        switch (SkillId) {
            case 80003:
            case 81010: {
                kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_TM.getDist());
                break;
            }
            case 80005:
            case 81011: {
                kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_FG.getDist());
                break;
            }
            case 80007:
            case 81012: {
                kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_QM.getDist());
                break;
            }
            case 80009:
            case 81013: {
                kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_XL.getDist());
                break;
            }
        }
        hurt = (int)SkillCompute.computePetSkillDamage((double)hurt, kx, fk, extra, weak, 1.0);
        return hurt;
    }
    
    public static List<Integer> queryCorrelationTargetIds(Fight fight, FightObject effector, int skillSerial) {
        Map<Integer, SysPetSkill> petSkillMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue());
        SysPetSkill petSkill = (SysPetSkill)petSkillMap.get(Integer.valueOf(skillSerial));
        if (petSkill != null && petSkill.getPassiveTriggerSkill() != null && !"".equals(petSkill.getPassiveTriggerSkill())) {
            String[] skillIds = petSkill.getPassiveTriggerSkill().split(",");
            List<RoleSkill> mySkills = (List)effector.getSkillIds().stream().filter(obj -> -1 != (int)obj.getSkillId()).collect(Collectors.toList());
            for (String skillIdStr : skillIds) {
                for (RoleSkill roleSkill : mySkills) {
                    if (roleSkill.getSkillId().equals(Integer.valueOf(Integer.parseInt(skillIdStr)))) {
                        SkillProcessor skillProcessor = SkillProcessor.queryProcessor(Integer.valueOf(Integer.parseInt(skillIdStr)));
                        if (skillProcessor != null) {
                            List<Integer> targetIds = skillProcessor.getTargetIdsForTFQX(fight, effector, Integer.valueOf(skillSerial));
                            return targetIds;
                        }
                        continue;
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    public static boolean isClash(SysPetSkill petSkill, Integer skillSerial) {
        if (skillSerial == null || (int)skillSerial <= 0) {
            return false;
        }
        if (skillSerial == petSkill.getSerial()) {
            return false;
        }
        boolean isClash = false;
        String clashSkillSerials = petSkill.getClashSkillSerials();
        if (clashSkillSerials != null && clashSkillSerials.contains(skillSerial.toString())) {
            isClash = true;
        }
        return isClash;
    }
    
    public static SysPetSkill queryInitiativeQueryCover(List<RoleSkill> skills, String type, int skillId) {
        SysPetSkill returnPetSkill = null;
        Map<Integer, SysPetSkill> sysPetSkillMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(type);
        SysPetSkill petSkill = (SysPetSkill)sysPetSkillMap.get(Integer.valueOf(skillId));
        if (petSkill != null) {
            SysPetSkill sysPetSkill = initiativeQueryCover(petSkill, skills, sysPetSkillMap);
            if (sysPetSkill != null && skillId != (int)sysPetSkill.getSerial()) {
                returnPetSkill = sysPetSkill;
            }
        }
        return returnPetSkill;
    }
    
    public static boolean addAttrObj(Fight fight, FightObject effector, double addK, double maxK, Integer type) {
        boolean isAdd = false;
        Map<Integer, Double> arrtMap = (Map)fight.getObjectAttributeMap().get(Boolean.valueOf(effector.isASide()));
        if (arrtMap != null) {
            if (arrtMap.get(type) != null) {
                if ((double)arrtMap.get(type) < maxK) {
                    arrtMap.put(type, Double.valueOf(BigDecimalUtil.additionDouble((double)arrtMap.get(type), addK)));
                    isAdd = true;
                }
            }
            else {
                arrtMap.put(type, Double.valueOf(addK));
                isAdd = true;
            }
        }
        else {
            arrtMap = new HashMap();
            arrtMap.put(type, Double.valueOf(addK));
            fight.getObjectAttributeMap().put(Boolean.valueOf(effector.isASide()), arrtMap);
            isAdd = true;
        }
        return isAdd;
    }
    
    public static int getExcuteLevel(Object... args) {
        int level = 1;
        if (args.length > 0 && args[0] instanceof FightObject) {
            FightObject effector = (FightObject)args[0];
            if (effector.getLevel() > 1) {
                level = effector.getLevel();
            }
        }
        return level;
    }
    
    public static void excuteEffect(boolean flag, Fight fight, FightObject effector, double percent, int proType, int skillSerial) {
        double newComboRatio = 0.0;
        double comboRatio = 0.0;
        if (flag) {
            List<FightObject> targetUnit2 = SkillAi.unit(fight, !effector.isASide());
            boolean isExcute = true;
            if (fight.getReduceRatioMap().get(Boolean.valueOf(!effector.isASide())) != null && ((Map)fight.getReduceRatioMap().get(Boolean.valueOf(!effector.isASide()))).get(Integer.valueOf(proType)) != null) {
                isExcute = false;
            }
            for (int i = 0; i < targetUnit2.size() && isExcute; ++i) {
                FightObject targetObject = (FightObject)targetUnit2.get(i);
                comboRatio = targetObject.getPropertyMap().getDouble(String.valueOf(proType));
                newComboRatio = Math.max(0.0, comboRatio * (1.0 - percent));
                targetObject.getPropertyMap().setv(String.valueOf(proType), Double.valueOf(newComboRatio));
            }
            if (fight.getReduceRatioMap().get(Boolean.valueOf(!effector.isASide())) != null) {
                if (((Map)fight.getReduceRatioMap().get(Boolean.valueOf(!effector.isASide()))).get(Integer.valueOf(proType)) == null) {
                    ((Map)fight.getReduceRatioMap().get(Boolean.valueOf(!effector.isASide()))).put(Integer.valueOf(proType), Integer.valueOf(0));
                }
            }
            else {
                Map<Integer, Integer> map = new HashMap();
                map.put(Integer.valueOf(proType), Integer.valueOf(0));
                fight.getReduceRatioMap().put(Boolean.valueOf(!effector.isASide()), map);
            }
        }
        else {
            boolean isSurvivalUnit = isSurvivalUnit(fight, effector.isASide(), skillSerial, LOGIC.LOGICTYPE_PET.getValue());
            if (isSurvivalUnit) {
                return;
            }
            List<FightObject> targetUnit = SkillAi.unit(fight, !effector.isASide());
            for (int i = 0; i < targetUnit.size(); ++i) {
                FightObject targetObject = (FightObject)targetUnit.get(i);
                comboRatio = targetObject.getPropertyMap().getDouble(String.valueOf(proType));
                newComboRatio = Math.max(0.0, comboRatio / (1.0 - percent));
                targetObject.getPropertyMap().setv(String.valueOf(proType), Double.valueOf(newComboRatio));
            }
            if (fight.getReduceRatioMap().get(Boolean.valueOf(!effector.isASide())) != null && ((Map)fight.getReduceRatioMap().get(Boolean.valueOf(!effector.isASide()))).get(Integer.valueOf(proType)) != null) {
                ((Map)fight.getReduceRatioMap().get(Boolean.valueOf(!effector.isASide()))).remove(Integer.valueOf(proType));
            }
        }
    }
    
    public static boolean isSurvivalUnit(Fight fight, boolean isASide, int skillSerial, int type) {
        boolean isSurvivalUnit = false;
        List<FightObject> effecterUnits = SkillAi.unit(fight, isASide);
        for (FightObject obj : effecterUnits) {
            if (type == obj.getLogic() && !obj.isFallDown()) {
                for (RoleSkill roleSkill : obj.getSkillIds()) {
                    if (roleSkill.getSkillId().equals(Integer.valueOf(skillSerial))) {
                        isSurvivalUnit = true;
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
        return isSurvivalUnit;
    }
    
    public static int wuXingFireCount(Fight fight, FightObject effector, FightObject target, double hurt) {
        int harm = 0;
        if (!target.isFrozen()) {
            double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_H.getDist());
            double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_H.getDist());
            double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_H.getDist());
            double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = (int)SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        return harm;
    }
    
    public static int wuXingWaterCount(Fight fight, FightObject effector, FightObject target, double hurt) {
        int harm = 0;
        if (!target.isFrozen()) {
            double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_S.getDist());
            double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_S.getDist());
            double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_S.getDist());
            double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = (int)SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        return harm;
    }
    
    public static int windCount(Fight fight, FightObject effector, FightObject target, double hurt) {
        int harm = 0;
        if (!target.isFrozen()) {
            double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_F.getDist());
            double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_F.getDist());
            double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_F.getDist());
            double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = (int)SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        return harm;
    }
    
    public static int thunderCount(Fight fight, FightObject effector, FightObject target, double hurt) {
        int harm = 0;
        if (!target.isFrozen()) {
            double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_L.getDist());
            double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_L.getDist());
            double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_L.getDist());
            double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = (int)SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        return harm;
    }
    
    public static FightObject horizontalLongitudinalTarget(boolean isHorizontal, Fight fight, FightObject effector, List<FightObject> targetSide, FightObject target) {
        FightObject newTarget = null;
        if (isHorizontal) {
            Set<Integer> targetSet = new HashSet();
            String targetPosStr = String.valueOf(target.getPos());
            String lastIndexStr = targetPosStr.substring(targetPosStr.length() - 1);
            List<Integer> posList = SkillTargetPicker.xAxisTargets(Integer.parseInt(lastIndexStr), targetPosStr.substring(0, targetPosStr.length() - 1));
            for (FightObject obj : targetSide) {
                for (Integer pos : posList) {
                    if (pos.equals(Integer.valueOf(obj.getPos()))) {
                        targetSet.add(obj.getId());
                        break;
                    }
                    continue;
                }
            }
            if (targetSet.size() > 0 && targetSet.iterator().hasNext()) {
                newTarget = fight.queryFightObject((Integer)targetSet.iterator().next());
            }
        }
        else {
            int target_pos = target.getPos() % 10000;
            int new_target_pos;
            if (target_pos > 5) {
                new_target_pos = target.getPos() - 100;
            }
            else {
                new_target_pos = target.getPos() + 100;
            }
            for (FightObject targetObj : targetSide) {
                if (targetObj.getPos() == new_target_pos) {
                    newTarget = targetObj;
                    break;
                }
                continue;
            }
        }
        return newTarget;
    }
    
    public static List<ActionDetailParam> checkActionForPrivate(Fight fight) {
        Map<Boolean, BeforeActionSkill> beforeActionMap = fight.getBeforeActionMap();
        if (beforeActionMap.size() > 0) {
            Integer petId = null;
            List<PetSkillAction> list = new ArrayList<>();
            List<Integer> publicList = new ArrayList<>();
            for (Map.Entry<Boolean, BeforeActionSkill> entry : beforeActionMap.entrySet()) {
                Boolean isA = (Boolean)entry.getKey();
                BeforeActionSkill beforeActionSkill = (BeforeActionSkill)entry.getValue();
                if (beforeActionSkill.getSkills().size() > 0) {
                    FightObject target = getTargetForPos(((boolean)isA) ? fight.getListA() : fight.getListB(), 3);
                    if (target == null) {
                        List<FightObject> objects = ((boolean)isA) ? fight.getListA() : fight.getListB();
                        if (objects != null && objects.size() > 0) {
                            target = (FightObject)objects.get(0);
                        }
                        else {
                            break;
                        }
                    }
                    for (Integer skillId : beforeActionSkill.getSkills()) {
                        for (Map.Entry<Integer, List<Integer>> map : beforeActionSkill.getRoleSkillMap().entrySet()) {
                            if (((List)map.getValue()).contains(skillId)) {
                                petId = (Integer)map.getKey();
                                break;
                            }
                            continue;
                        }
                        if (petId != null) {
                            PetSkillAction petSkillAction = new PetSkillAction((int)petId, (int)skillId, (int)target.getId());
                            list.add(petSkillAction);
                            petId = null;
                        }
                        else {
                            continue;
                        }
                    }
                    getPublicSkill(publicList, beforeActionSkill.getSkills());
                }
                else {
                    continue;
                }
            }
            if (list.size() > 0) {
                List<ActionDetailParam> actionDetailParams = new ArrayList<>();
                for (PetSkillAction skillAction : list) {
                    if (Arrays.asList(SkillConst.beforeActionSkillOnlyOnce).contains(Integer.valueOf(skillAction.getSkillId()))) {
                        if (!publicList.contains(Integer.valueOf(skillAction.getSkillId()))) {
                            continue;
                        }
                        ActionDetailParam actionDetailParam = sendActionExcuteReturn(Integer.valueOf(skillAction.getPetId()), skillAction.getSkillId(), Integer.valueOf(skillAction.getTargetId()));
                        actionDetailParams.add(actionDetailParam);
                        publicList.remove(publicList.indexOf(Integer.valueOf(skillAction.getSkillId())));
                    }
                    else {
                        ActionDetailParam actionDetailParam = sendActionExcuteReturn(Integer.valueOf(skillAction.getPetId()), skillAction.getSkillId(), Integer.valueOf(skillAction.getTargetId()));
                        actionDetailParams.add(actionDetailParam);
                    }
                }
                return actionDetailParams;
            }
        }
        return null;
    }
    
    public static void getPublicSkill(List<Integer> publicList, List<Integer> listSkill) {
        if (publicList == null) {
            publicList = new ArrayList<>();
        }
        if (listSkill == null) {
            return;
        }
        for (Integer skill : listSkill) {
            if (!publicList.contains(skill) && Arrays.asList(SkillConst.beforeActionSkillOnlyOnce).contains(skill)) {
                publicList.add(skill);
            }
            else {
                continue;
            }
        }
        if (publicList.contains(Integer.valueOf(82009)) && publicList.contains(Integer.valueOf(81028))) {
            publicList.remove(publicList.indexOf(Integer.valueOf(81028)));
        }
        return;
    }
    
    public static List<Integer> clearWuXin(FightObject fightObject) {
        List<Integer> wuXinDist = PRO_DICT_TYPE.getWuXinDist();
        for (Integer wuXin : wuXinDist) {
            double proValue = ImbueUtil.getProValue(fightObject.getImbueMap(), wuXin);
            if (proValue > 0.0) {
                ImbueUtil.removePro(fightObject.getImbueMap(), (int)wuXin);
            }
            else {
                continue;
            }
        }
        List<Integer> clearStatus = new ArrayList<>();
        for (Integer wxStatus : StatusConst.getWuxingStatus()) {
            if (fightObject.getStatusValue((int)wxStatus) > 0) {
                fightObject.removeStatus((int)wxStatus);
                clearStatus.add(wxStatus);
            }
            else {
                continue;
            }
        }
        if (clearStatus.size() > 0) {
            return clearStatus;
        }
        return null;
    }
    
    public static boolean isInFight(Role role, Pet pet) {
        int petLevel = (int)pet.getPetLv() + (int)pet.getRebirth() * 1000;
        int roleLevle = (int)role.getLevel() + (int)role.getRelive() * 1000;
        return petLevel - roleLevle <= 10;
    }
}
