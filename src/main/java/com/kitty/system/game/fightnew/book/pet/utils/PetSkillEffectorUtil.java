
package com.kitty.system.game.fightnew.book.pet.utils;

import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.book.vo.PetSkillAction;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import java.util.HashSet;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.common.utils.StringUtils;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Map;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import com.kitty.system.init.domain.SysPetSkill;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class PetSkillEffectorUtil
{
    public static double getSuffix(final Integer lv, final Integer rei, final long close) {
        return Math.pow(lv, 0.5) / 10.0 + Math.pow((double)close, 0.16666666666666666) * 10.0 / (100 + rei * 20);
    }
    
    public static void sendMsg(final Fight fight, final FightObject effector, final String msgStr) {
        final IoSession session = effector.getSession();
        if (session != null) {
            final RespMsg msg = new RespMsg();
            msg.setMsg(msgStr);
            MessagePusher.pushMessage(session, msg);
        }
    }
    
    public static SysPetSkill initiativeQueryCover(final SysPetSkill sysPetSkill, final List<RoleSkill> skills, final Map<Integer, SysPetSkill> sysPetSkillMap) {
        final List<RoleSkill> skillIds = skills.stream().filter(obj -> -1 != obj.getSkillId()).collect(Collectors.toList());
        final Map<Integer, SysPetSkill> listPetSkillMap = new HashMap<Integer, SysPetSkill>();
        for (final RoleSkill skill : skillIds) {
            final SysPetSkill petSkill = sysPetSkillMap.get(skill.getSkillId());
            if (petSkill != null && petSkill.getSerial().equals(skill.getSkillId())) {
                listPetSkillMap.put(petSkill.getSerial(), petSkill);
            }
        }
        return queryCover(sysPetSkill, listPetSkillMap);
    }
    
    public static SysPetSkill queryCover(SysPetSkill sysPetSkill, final Map<Integer, SysPetSkill> sysPetSkillMap) {
        final String coverSkillSerials = sysPetSkill.getCoverSkillSerials();
        if (coverSkillSerials == null) {
            return sysPetSkill;
        }
        final String[] split;
        final String[] skills = split = coverSkillSerials.split(",");
        for (final String skill : split) {
            final SysPetSkill petSkill = sysPetSkillMap.get(Integer.parseInt(skill));
            if (petSkill != null) {
                sysPetSkill = petSkill;
                queryCover(sysPetSkill, sysPetSkillMap);
            }
        }
        return sysPetSkill;
    }
    
    public static FightObject getTargetForPos(List<FightObject> targets, final int pos) {
        FightObject target = null;
        targets = targets.stream().filter(obj -> obj.getPos() % 10000 == pos).collect(Collectors.toList());
        if (targets.size() > 0) {
            target = targets.get(0);
        }
        return target;
    }
    
    public static void sendActionExcute(final Fight fight, final Integer petId, final int skill, final Integer targetId) {
        final int[] skillStatus = Arrays.stream(SkillConst.skillStatus).filter(obj -> skill == obj[0]).findAny().get();
        ActionParamUtils.addStatus(fight, fight.queryFightObject(petId), fight.queryFightObject(targetId), 33, skill, 2, skillStatus[1], -3);
    }
    
    public static ActionDetailParam sendActionExcuteReturn(final Integer petId, final int skill, final Integer targetId) {
        final int[] skillStatus = Arrays.stream(SkillConst.skillStatus).filter(obj -> skill == obj[0]).findAny().get();
        final ActionDetailParam actionDetailParam = ActionParamUtils.addStatusReturn(petId, targetId, 33, skill, 2, skillStatus[1], -3);
        return actionDetailParam;
    }
    
    public static void sendCancelAction(final Fight fight, final FightObject actor) {
        final BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(!actor.isASide());
        sendCancelAction(fight, actor.getId(), beforeActionSkill, fight.getTargetSide(actor.getId()), false);
        final BeforeActionSkill beforeActionSkillMySide = fight.getBeforeActionMap().get(actor.isASide());
        sendCancelAction(fight, actor.getId(), beforeActionSkillMySide, fight.getMySide(actor.getId()), false);
    }
    
    public static void sendCancelAction(final Fight fight, final Integer actorId, final BeforeActionSkill beforeActionSkill, final List<FightObject> objSide, final boolean isNiuZhuanQianKun) {
        if (beforeActionSkill != null && beforeActionSkill.isPassiveAction() && beforeActionSkill.getRoleSkillMap() != null && beforeActionSkill.getRoleSkillMap().size() > 0) {
            final List<Integer> skillIds = beforeActionSkill.getRoleSkillMap().get(actorId);
            if (skillIds != null) {
                final FightObject target = getTargetForPos(objSide, 3);
                if (target == null) {
                    return;
                }
                final Iterator<Integer> iteratorSkill = skillIds.iterator();
                Integer skill = null;
                boolean flag = true;
                SysPetSkill petSkill = null;
                while (iteratorSkill.hasNext()) {
                    skill = iteratorSkill.next();
                    petSkill = SkillDataPool.petSkillMap.get(skill);
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
                            final BeforeActionSkill beforeActionSkillReverse = fight.getBeforeActionMap().get(!beforeActionSkill.isASide());
                            if (beforeActionSkillReverse != null && beforeActionSkillReverse.isPassiveAction() && beforeActionSkillReverse.getSkills().contains(skill)) {
                                continue;
                            }
                        }
                        if (82007 == skill) {
                            fight.getAstrictSkillMap().remove(beforeActionSkill.isASide());
                        }
                        sendCancelActionExcute(fight, actorId, target.getId(), skill);
                    }
                    flag = true;
                }
                if (!isNiuZhuanQianKun && skillIds != null && skillIds.size() <= 0) {
                    beforeActionSkill.getRoleSkillMap().remove(actorId);
                }
                if (beforeActionSkill.getSkills().size() <= 0 && beforeActionSkill.getRoleSkillMap().size() <= 0) {
                    fight.getBeforeActionMap().remove(beforeActionSkill.isASide());
                }
            }
        }
    }
    
    public static void sendCancelActionExcute(final Fight fight, final Integer actorId, final Integer targetId, final Integer skill) {
        final int[] skillStatus = Arrays.stream(SkillConst.skillStatus).filter(obj -> skill == obj[0]).findAny().get();
        ActionParamUtils.addStatus(fight, fight.queryFightObject(actorId), fight.queryFightObject(targetId), 34, skill, 2, skillStatus[1], -3);
    }
    
    public static void enemyFirstMagicCancelAction(final Fight fight, final FightObject effector, final FightObject target, final BeforeActionSkill beforeActionSkill, final Integer skillid) {
        beforeActionSkill.getSkills().remove(skillid);
        List<Integer> values = null;
        final Set<Integer> keys = beforeActionSkill.getRoleSkillMap().keySet();
        boolean returnFlag = false;
        for (final Integer key : keys) {
            values = beforeActionSkill.getRoleSkillMap().get(key);
            for (final Integer skill : values) {
                if (skillid.equals(skill)) {
                    values.remove(skill);
                    sendCancelActionExcute(fight, effector.getId(), target.getId(), skill);
                    returnFlag = true;
                    break;
                }
            }
            if (values.size() <= 0) {
                beforeActionSkill.getRoleSkillMap().remove(key);
            }
            if (returnFlag) {
                break;
            }
        }
    }
    
    public static void excuteFlashBeforeOne(final Fight fight, final FightObject host, final Role role, final Pet pet, final int skillSerial) {
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        host.setFightPetId(pet.getPetId());
        final ActionDetailParam actionDetailParam = new ActionDetailParam(host, 9, skillSerial, pet.getPetId());
        final RefreshPet refreshPet = new RefreshPet();
        final ArrayList<RefreshPet> refreshPets = new ArrayList<RefreshPet>();
        refreshPets.add(refreshPet);
        refreshPet.setPetId(pet.getPetId());
        refreshPet.setType(1);
        actionDetailParam.setPetList(refreshPets);
        actionDetailParam.setRoleId(role.getId());
        FightExecutorNewService.PETID_ROLEID.put(pet.getPetId(), pet.getRoleId());
        FightExecutorNewService.FIGHT_ROOMS.put(pet.getPetId(), fight.getId());
        final CombatAddObject combatAddObject = fightNewService.addPet2Fight(host.getId(), pet, fight, SkillFunc.getPetPosByMasterPos(host.getPos()));
        final List<CombatAddObject> list = new ArrayList<CombatAddObject>();
        list.add(combatAddObject);
        actionDetailParam.setCombatAddObjects(list);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        PetPassiveSkillUtil.otherEnterBattle(fight, pet.getPetId());
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
    
    private static void sendAction(final Fight fight, final FightObject target, final Map<Integer, List<Integer>> mapAction) {
        final Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        final BeforeActionSkill beforeActionSkillMy = fight.getBeforeActionMap().get(!target.isASide());
        final BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(target.isASide());
        final List<Integer> skillIdList = new ArrayList<Integer>();
        SysPetSkill petSkill = null;
        boolean flag = true;
        for (final Map.Entry<Integer, List<Integer>> entry : mapAction.entrySet()) {
            final List<Integer> skillIds = new ArrayList<Integer>();
            final Integer key = entry.getKey();
            for (final Integer skill : entry.getValue()) {
                petSkill = SkillDataPool.petSkillMap.get(skill);
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
                                sendCancelActionExcute(fight, petId, target.getId(), 81028);
                            }
                        }
                        if (beforeActionSkill != null && beforeActionSkill.isPassiveAction() && beforeActionSkill.getSkills().contains(81028)) {
                            final Integer petId = deleteAction(beforeActionSkill.getRoleSkillMap(), 81028);
                            if (petId != 0) {
                                sendCancelActionExcute(fight, petId, target.getId(), 81028);
                            }
                        }
                        if (beforeActionSkillMy != null && beforeActionSkillMy.getSkills().contains(81028)) {
                            final Integer petId = deleteAction(beforeActionSkillMy.getRoleSkillMap(), 81028);
                            if (petId != 0) {
                                final FightObject myObject = getTargetForPos(fight.getTargetSide(target.getId()), 3);
                                sendCancelActionExcute(fight, petId, myObject.getId(), 81028);
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
                sendActionExcute(fight, key, skill, target.getId());
            }
            if (skillIds.size() > 0) {
                map.put(key, skillIds);
            }
        }
        saveBeforeActionSkill(fight, target, map, skillIdList);
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
    
    public static boolean checkCover(final SysPetSkill petSkill, final Fight fight, final FightObject effector, final FightObject target, final boolean isExcute, final Object... args) {
        boolean isCover = false;
        final String coverSkillSerials = petSkill.getCoverSkillSerials();
        if (StringUtils.isNotEmpty(coverSkillSerials)) {
            final String[] strs = coverSkillSerials.split(",");
            final List<RoleSkill> skillIds = effector.getSkillIds();
            for (int i = 0; i < strs.length; ++i) {
                for (final RoleSkill roleSkill : skillIds) {
                    if (strs[i].equals(roleSkill.getSkillId().toString())) {
                        if (isExcute) {
                            final SkillProcessor skillProcessor = SkillProcessor.queryProcessor(roleSkill.getSkillId());
                            skillProcessor.effect(fight, effector, target, args);
                        }
                        isCover = true;
                    }
                }
            }
        }
        return isCover;
    }
    
    public static int computeTMFGQMXLHurt(final FightObject effector, final FightObject target, int hurt, final int SkillId) {
        double kx = 0.0;
        final double fk = 0.0;
        final double extra = 0.0;
        final double weak = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist())) - effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.SH_DK.getDist()));
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
        hurt = SkillCompute.computePetSkillDamage(hurt, kx, fk, extra, weak, 1.0);
        return hurt;
    }
    
    public static List<Integer> queryCorrelationTargetIds(final Fight fight, final FightObject effector, final int skillSerial) {
        final Map<Integer, SysPetSkill> petSkillMap = PetTriggerPassiveSkill.querySkills(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue());
        final SysPetSkill petSkill = petSkillMap.get(skillSerial);
        if (petSkill != null && petSkill.getPassiveTriggerSkill() != null && !"".equals(petSkill.getPassiveTriggerSkill())) {
            final String[] skillIds = petSkill.getPassiveTriggerSkill().split(",");
            final List<RoleSkill> mySkills = effector.getSkillIds().stream().filter(obj -> -1 != obj.getSkillId()).collect(Collectors.toList());
            for (final String skillIdStr : skillIds) {
                for (final RoleSkill roleSkill : mySkills) {
                    if (roleSkill.getSkillId().equals(Integer.parseInt(skillIdStr))) {
                        final SkillProcessor skillProcessor = SkillProcessor.queryProcessor(Integer.parseInt(skillIdStr));
                        if (skillProcessor != null) {
                            final List<Integer> targetIds = skillProcessor.getTargetIdsForTFQX(fight, effector, skillSerial);
                            return targetIds;
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    public static boolean isClash(final SysPetSkill petSkill, final Integer skillSerial) {
        if (skillSerial == null || skillSerial <= 0) {
            return false;
        }
        if (skillSerial == petSkill.getSerial()) {
            return false;
        }
        boolean isClash = false;
        final String clashSkillSerials = petSkill.getClashSkillSerials();
        if (clashSkillSerials != null && clashSkillSerials.contains(skillSerial.toString())) {
            isClash = true;
        }
        return isClash;
    }
    
    public static SysPetSkill queryInitiativeQueryCover(final List<RoleSkill> skills, final String type, final int skillId) {
        SysPetSkill returnPetSkill = null;
        final Map<Integer, SysPetSkill> sysPetSkillMap = PetTriggerPassiveSkill.data_pet_skill_maps.get(type);
        final SysPetSkill petSkill = sysPetSkillMap.get(skillId);
        if (petSkill != null) {
            final SysPetSkill sysPetSkill = initiativeQueryCover(petSkill, skills, sysPetSkillMap);
            if (sysPetSkill != null && skillId != sysPetSkill.getSerial()) {
                returnPetSkill = sysPetSkill;
            }
        }
        return returnPetSkill;
    }
    
    public static boolean addAttrObj(final Fight fight, final FightObject effector, final double addK, final double maxK, final Integer type) {
        boolean isAdd = false;
        Map<Integer, Double> arrtMap = fight.getObjectAttributeMap().get(effector.isASide());
        if (arrtMap != null) {
            if (arrtMap.get(type) != null) {
                if (arrtMap.get(type) < maxK) {
                    arrtMap.put(type, BigDecimalUtil.additionDouble(arrtMap.get(type), addK));
                    isAdd = true;
                }
            }
            else {
                arrtMap.put(type, addK);
                isAdd = true;
            }
        }
        else {
            arrtMap = new HashMap<Integer, Double>();
            arrtMap.put(type, addK);
            fight.getObjectAttributeMap().put(effector.isASide(), arrtMap);
            isAdd = true;
        }
        return isAdd;
    }
    
    public static int getExcuteLevel(final Object... args) {
        int level = 1;
        if (args.length > 0 && args[0] instanceof FightObject) {
            final FightObject effector = (FightObject)args[0];
            if (effector.getLevel() > 1) {
                level = effector.getLevel();
            }
        }
        return level;
    }
    
    public static void excuteEffect(final boolean flag, final Fight fight, final FightObject effector, final double percent, final int proType, final int skillSerial) {
        double newComboRatio = 0.0;
        double comboRatio = 0.0;
        if (flag) {
            final List<FightObject> targetUnit = SkillAi.unit(fight, !effector.isASide());
            boolean isExcute = true;
            if (fight.getReduceRatioMap().get(!effector.isASide()) != null && fight.getReduceRatioMap().get(!effector.isASide()).get(proType) != null) {
                isExcute = false;
            }
            for (int i = 0; i < targetUnit.size() && isExcute; ++i) {
                final FightObject targetObject = targetUnit.get(i);
                comboRatio = targetObject.getPropertyMap().getDouble(String.valueOf(proType));
                newComboRatio = Math.max(0.0, comboRatio * (1.0 - percent));
                targetObject.getPropertyMap().setv(String.valueOf(proType), (Object)newComboRatio);
            }
            if (fight.getReduceRatioMap().get(!effector.isASide()) != null) {
                if (fight.getReduceRatioMap().get(!effector.isASide()).get(proType) == null) {
                    fight.getReduceRatioMap().get(!effector.isASide()).put(proType, 0);
                }
            }
            else {
                final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                map.put(proType, 0);
                fight.getReduceRatioMap().put(!effector.isASide(), map);
            }
        }
        else {
            final boolean isSurvivalUnit = isSurvivalUnit(fight, effector.isASide(), skillSerial, LOGIC.LOGICTYPE_PET.getValue());
            if (isSurvivalUnit) {
                return;
            }
            final List<FightObject> targetUnit2 = SkillAi.unit(fight, !effector.isASide());
            for (int i = 0; i < targetUnit2.size(); ++i) {
                final FightObject targetObject = targetUnit2.get(i);
                comboRatio = targetObject.getPropertyMap().getDouble(String.valueOf(proType));
                newComboRatio = Math.max(0.0, comboRatio / (1.0 - percent));
                targetObject.getPropertyMap().setv(String.valueOf(proType), (Object)newComboRatio);
            }
            if (fight.getReduceRatioMap().get(!effector.isASide()) != null && fight.getReduceRatioMap().get(!effector.isASide()).get(proType) != null) {
                fight.getReduceRatioMap().get(!effector.isASide()).remove(proType);
            }
        }
    }
    
    public static boolean isSurvivalUnit(final Fight fight, final boolean isASide, final int skillSerial, final int type) {
        boolean isSurvivalUnit = false;
        final List<FightObject> effecterUnits = SkillAi.unit(fight, isASide);
        for (final FightObject obj : effecterUnits) {
            if (type == obj.getLogic() && !obj.isFallDown()) {
                for (final RoleSkill roleSkill : obj.getSkillIds()) {
                    if (roleSkill.getSkillId().equals(skillSerial)) {
                        isSurvivalUnit = true;
                    }
                }
            }
        }
        return isSurvivalUnit;
    }
    
    public static int wuXingFireCount(final Fight fight, final FightObject effector, final FightObject target, final double hurt) {
        int harm = 0;
        if (!target.isFrozen()) {
            final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_H.getDist());
            final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_H.getDist());
            final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_H.getDist());
            final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        return harm;
    }
    
    public static int wuXingWaterCount(final Fight fight, final FightObject effector, final FightObject target, final double hurt) {
        int harm = 0;
        if (!target.isFrozen()) {
            final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_S.getDist());
            final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_S.getDist());
            final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_S.getDist());
            final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        return harm;
    }
    
    public static int windCount(final Fight fight, final FightObject effector, final FightObject target, final double hurt) {
        int harm = 0;
        if (!target.isFrozen()) {
            final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_F.getDist());
            final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_F.getDist());
            final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_F.getDist());
            final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        return harm;
    }
    
    public static int thunderCount(final Fight fight, final FightObject effector, final FightObject target, final double hurt) {
        int harm = 0;
        if (!target.isFrozen()) {
            final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_L.getDist());
            final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_L.getDist());
            final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_L.getDist());
            final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        return harm;
    }
    
    public static FightObject horizontalLongitudinalTarget(final boolean isHorizontal, final Fight fight, final FightObject effector, final List<FightObject> targetSide, final FightObject target) {
        FightObject newTarget = null;
        if (isHorizontal) {
            final Set<Integer> targetSet = new HashSet<Integer>();
            final String targetPosStr = String.valueOf(target.getPos());
            final String lastIndexStr = targetPosStr.substring(targetPosStr.length() - 1);
            final List<Integer> posList = SkillTargetPicker.xAxisTargets(Integer.parseInt(lastIndexStr), targetPosStr.substring(0, targetPosStr.length() - 1));
            for (final FightObject obj : targetSide) {
                for (final Integer pos : posList) {
                    if (pos.equals(obj.getPos())) {
                        targetSet.add(obj.getId());
                        break;
                    }
                }
            }
            if (targetSet.size() > 0 && targetSet.iterator().hasNext()) {
                newTarget = fight.queryFightObject(targetSet.iterator().next());
            }
        }
        else {
            final int target_pos = target.getPos() % 10000;
            int new_target_pos;
            if (target_pos > 5) {
                new_target_pos = target.getPos() - 100;
            }
            else {
                new_target_pos = target.getPos() + 100;
            }
            for (final FightObject targetObj : targetSide) {
                if (targetObj.getPos() == new_target_pos) {
                    newTarget = targetObj;
                    break;
                }
            }
        }
        return newTarget;
    }
    
    public static List<ActionDetailParam> checkActionForPrivate(final Fight fight) {
        final Map<Boolean, BeforeActionSkill> beforeActionMap = fight.getBeforeActionMap();
        if (beforeActionMap.size() > 0) {
            Integer petId = null;
            final List<PetSkillAction> list = new ArrayList<PetSkillAction>();
            final List<Integer> publicList = new ArrayList<Integer>();
            for (final Map.Entry<Boolean, BeforeActionSkill> entry : beforeActionMap.entrySet()) {
                final Boolean isA = entry.getKey();
                final BeforeActionSkill beforeActionSkill = entry.getValue();
                if (beforeActionSkill.getSkills().size() > 0) {
                    FightObject target = getTargetForPos(((boolean)isA) ? fight.getListA() : fight.getListB(), 3);
                    if (target == null) {
                        final List<FightObject> objects = isA ? fight.getListA() : fight.getListB();
                        if (objects == null || objects.size() <= 0) {
                            break;
                        }
                        target = objects.get(0);
                    }
                    for (final Integer skillId : beforeActionSkill.getSkills()) {
                        for (final Map.Entry<Integer, List<Integer>> map : beforeActionSkill.getRoleSkillMap().entrySet()) {
                            if (map.getValue().contains(skillId)) {
                                petId = map.getKey();
                                break;
                            }
                        }
                        if (petId != null) {
                            final PetSkillAction petSkillAction = new PetSkillAction(petId, skillId, target.getId());
                            list.add(petSkillAction);
                            petId = null;
                        }
                    }
                    getPublicSkill(publicList, beforeActionSkill.getSkills());
                }
            }
            if (list.size() > 0) {
                final List<ActionDetailParam> actionDetailParams = new ArrayList<ActionDetailParam>();
                for (final PetSkillAction skillAction : list) {
                    if (Arrays.asList(SkillConst.beforeActionSkillOnlyOnce).contains(skillAction.getSkillId())) {
                        if (!publicList.contains(skillAction.getSkillId())) {
                            continue;
                        }
                        final ActionDetailParam actionDetailParam = sendActionExcuteReturn(skillAction.getPetId(), skillAction.getSkillId(), skillAction.getTargetId());
                        actionDetailParams.add(actionDetailParam);
                        publicList.remove(publicList.indexOf(skillAction.getSkillId()));
                    }
                    else {
                        final ActionDetailParam actionDetailParam = sendActionExcuteReturn(skillAction.getPetId(), skillAction.getSkillId(), skillAction.getTargetId());
                        actionDetailParams.add(actionDetailParam);
                    }
                }
                return actionDetailParams;
            }
        }
        return null;
    }
    
    public static void getPublicSkill(List<Integer> publicList, final List<Integer> listSkill) {
        if (publicList == null) {
            publicList = new ArrayList<Integer>();
        }
        if (listSkill == null) {
            return;
        }
        for (final Integer skill : listSkill) {
            if (!publicList.contains(skill) && Arrays.asList(SkillConst.beforeActionSkillOnlyOnce).contains(skill)) {
                publicList.add(skill);
            }
        }
        if (publicList.contains(82009) && publicList.contains(81028)) {
            publicList.remove(publicList.indexOf(81028));
        }
    }
    
    public static List<Integer> clearWuXin(final FightObject fightObject) {
        final List<Integer> wuXinDist = PRO_DICT_TYPE.getWuXinDist();
        for (final Integer wuXin : wuXinDist) {
            final double proValue = ImbueUtil.getProValue(fightObject.getImbueMap(), wuXin);
            if (proValue > 0.0) {
                ImbueUtil.removePro(fightObject.getImbueMap(), wuXin);
            }
        }
        final List<Integer> clearStatus = new ArrayList<Integer>();
        for (final Integer wxStatus : StatusConst.getWuxingStatus()) {
            if (fightObject.getStatusValue(wxStatus) > 0) {
                fightObject.removeStatus(wxStatus);
                clearStatus.add(wxStatus);
            }
        }
        if (clearStatus.size() > 0) {
            return clearStatus;
        }
        return null;
    }
    
    public static boolean isInFight(final Role role, final Pet pet) {
        final int petLevel = pet.getPetLv() + pet.getRebirth() * 1000;
        final int roleLevle = role.getLevel() + role.getRelive() * 1000;
        return petLevel - roleLevle <= 10;
    }
}
