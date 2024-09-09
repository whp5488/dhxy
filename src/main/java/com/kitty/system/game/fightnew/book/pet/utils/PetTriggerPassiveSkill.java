package com.kitty.system.game.fightnew.book.pet.utils;

import java.util.LinkedHashMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Comparator;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import java.util.Optional;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import java.util.Iterator;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import java.util.ArrayList;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.Map;

public class PetTriggerPassiveSkill
{
    public static Map<String, Map<Integer, SysPetSkill>> data_pet_skill_maps;
    
    public PetTriggerPassiveSkill() {
    }
    
    public static List<SysPetSkill> roundStart(String roundNode, List<RoleSkill> roleSkills, int isPvp) {
        List<Integer> serials = getInegers(roleSkills);
        List<SysPetSkill> skills = new ArrayList<>();
        if (serials != null && serials.size() > 0) {
            if (PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START.getValue().equals(roundNode)) {
                getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START.getValue(), serials, skills);
            }
            if (PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE.getValue().equals(roundNode)) {
                getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE.getValue(), serials, skills);
            }
            if (PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF.getValue().equals(roundNode)) {
                getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF.getValue(), serials, skills);
            }
            if (PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF_ACTION.getValue().equals(roundNode)) {
                getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.ENTERING_SIDE_NF_ACTION.getValue(), serials, skills);
            }
            if (PET_PASSIVE_SKILL_TRIGGER_DICT.PET_BE.getValue().equals(roundNode)) {
                getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.PET_BE.getValue(), serials, skills);
            }
            if (PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_ENTERING_SIDE.getValue().equals(roundNode)) {
                getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_ENTERING_SIDE.getValue(), serials, skills);
            }
        }
        return skills;
    }
    
    public static List<SysPetSkill> normalAttack(String skillTrigget, List<RoleSkill> roleSkills, int isPvp) {
        List<Integer> serials = getInegers(roleSkills);
        List<SysPetSkill> normalAttackSkills = new ArrayList<>();
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, skillTrigget, serials, normalAttackSkills);
            return normalAttackSkills;
        }
        return normalAttackSkills;
    }
    
    public static boolean heroFallDown(Fight fight, FightObject target) {
        boolean flag = false;
        List<FightObject> mySidePets = (List)fight.getMySide((int)target.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
        if (mySidePets.size() == 1) {
            FightObject petObject = (FightObject)mySidePets.get(0);
            FightObject hostObject = fight.queryFightObject(Integer.valueOf(petObject.getHostId()));
            if (hostObject.isFallDown()) {
                List<RoleSkill> roleSkills = (List)petObject.getSkillIds().stream().filter(obj -> (int)obj.getSkillId() > 0).collect(Collectors.toList());
                List<Integer> serials = getInegers(roleSkills);
                List<SysPetSkill> heroFallDownSkills = new ArrayList<>();
                if (serials != null && serials.size() > 0) {
                    getSkills(fight.isPvp(), PET_PASSIVE_SKILL_TRIGGER_DICT.HERO_FALL_DOWN.getValue(), serials, heroFallDownSkills);
                }
                for (SysPetSkill petSkill : heroFallDownSkills) {
                    SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    processor.passive(fight, petObject, new Object[0]);
                    flag = true;
                }
            }
        }
        return flag;
    }
    
    public static List<SysPetSkill> petExit(String type, List<RoleSkill> roleSkills, int isPvp) {
        List<SysPetSkill> petExitSkills = new ArrayList<>();
        List<Integer> serials = getInegers(roleSkills);
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, type, serials, petExitSkills);
            return petExitSkills;
        }
        return petExitSkills;
    }
    
    public static List<SysPetSkill> attackPet(List<RoleSkill> roleSkills, int isPvp) {
        List<SysPetSkill> attackPetSkills = new ArrayList<>();
        List<Integer> serials = getInegers(roleSkills);
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.ATTACK_PET.getValue(), serials, attackPetSkills);
            return attackPetSkills;
        }
        return attackPetSkills;
    }
    
    public static List<SysPetSkill> currLifeLessThan40(List<RoleSkill> roleSkills, int isPvp) {
        List<SysPetSkill> currLifeLessThan40Skills = new ArrayList<>();
        List<Integer> serials = getInegers(roleSkills);
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.CURR_LIFE_LESS_THAN_40.getValue(), serials, currLifeLessThan40Skills);
            return currLifeLessThan40Skills;
        }
        return currLifeLessThan40Skills;
    }
    
    public static List<SysPetSkill> beforePetExit(List<RoleSkill> roleSkills, int isPvp) {
        List<SysPetSkill> otherSkills = new ArrayList<>();
        List<Integer> serials = getInegers(roleSkills);
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.BEFORE_PET_EXIT.getValue(), serials, otherSkills);
            return otherSkills;
        }
        return otherSkills;
    }
    
    public static void excutePassiveSkill(Fight fight, FightObject effector, String type, Object... args) {
        Map<Integer, SysPetSkill> petSkillMap = querySkills(type);
        if (petSkillMap.size() > 0) {
            List<RoleSkill> petRoleSkills = (List)effector.getSkillIds().stream().filter(skill -> -1 != (int)skill.getSkillId()).collect(Collectors.toList());
            for (RoleSkill roleSkill : petRoleSkills) {
                SysPetSkill petSkill = (SysPetSkill)petSkillMap.get(roleSkill.getSkillId());
                if (petSkill != null && (fight.isPvp() == 1 || petSkill.getIsPvp() == fight.isPvp())) {
                    SkillProcessor skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    skillProcessor.passive(fight, effector, args);
                }
                else {
                    continue;
                }
            }
        }
    }
    
    private static void getSkills(int isPvp, String opportunity, List<Integer> serials, List<SysPetSkill> skills) {
        Map<Integer, SysPetSkill> resourceMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(opportunity);
        for (int i = serials.size() - 1; i >= 0; --i) {
            SysPetSkill sysPetSkill = (SysPetSkill)resourceMap.get(serials.get(i));
            if (sysPetSkill != null) {
                if (isPvp == 1 || isPvp == sysPetSkill.getIsPvp()) {
                    skills.add(sysPetSkill);
                }
                serials.remove(i);
            }
        }
    }
    
    public static Map<Integer, SysPetSkill> querySkills(String opportunity) {
        Map<Integer, SysPetSkill> resourceMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(opportunity);
        return resourceMap;
    }
    
    public static List<Integer> getInegers(List<RoleSkill> roleSkills) {
        if (roleSkills != null) {
            List<Integer> serials = new ArrayList<>();
            roleSkills.forEach(roleSkill -> serials.add(roleSkill.getSkillId()));
            return serials;
        }
        return null;
    }
    
    public static boolean checkSkillIsUsable(Fight fight, FightObject effector, Integer skillId, String type) {
        Map<Integer, SysPetSkill> petSkillMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(type);
        SysPetSkill petSkill = (SysPetSkill)petSkillMap.get(skillId);
        int isPvp = fight.isPvp();
        boolean isPet = true;
        if (effector.isMonster()) {
            isPet = false;
        }
        if (petSkill != null) {
            if (isPvp == 0 && isPvp != petSkill.getIsPvp() && isPet) {
                PetSkillEffectorUtil.sendMsg(fight, effector, "此技能仅pvp生效！");
                return false;
            }
            Map<String, Double> astrictTypeMap = petSkill.getAstrictTypeMap();
            if (astrictTypeMap.size() > 0) {
                for (Map.Entry entry : astrictTypeMap.entrySet()) {
                    Double value = (Double)entry.getValue();
                    if ("after_round".equals(entry.getKey())) {
                        boolean isTrigger = AcsryPetSkillUtil.isTrigger((int)petSkill.getSerial(), effector);
                        int roundValue = value.intValue();
                        if (isTrigger) {
                            --roundValue;
                        }
                        if (fight.getRound() <= roundValue) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "前" + roundValue + "回合不可用！");
                            return false;
                        }
                    }
                    if ("gt_life".equals(entry.getKey()) && (double)effector.getCurrLife() <= (double)value) {
                        if (isPet) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "气血值大于" + value.intValue() + "才可执行！");
                        }
                        return false;
                    }
                    else if ("gt_mana".equals(entry.getKey()) && (double)effector.getCurrMana() <= (double)value) {
                        if (isPet) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "法力值大于" + value.intValue() + "才可执行！");
                        }
                        return false;
                    }
                    else if ("ge_mana".equals(entry.getKey()) && (double)effector.getCurrMana() < (double)value) {
                        if (isPet) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "法力值大于" + value.intValue() + "才可执行！");
                        }
                        return false;
                    }
                    else {
                        if ("gt_max_life".equals(entry.getKey()) && (double)value < 1.0) {
                            Double radio = Double.valueOf(Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(effector.getMaxLife()), String.valueOf(value))));
                            if ((double)effector.getCurrLife() < (double)radio) {
                                if (isPet) {
                                    PetSkillEffectorUtil.sendMsg(fight, effector, "气血值大于" + radio + "才可执行！");
                                }
                                return false;
                            }
                        }
                        if ("gt_max_mana".equals(entry.getKey()) && (double)value < 1.0) {
                            Double radio = Double.valueOf(Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(effector.getMaxMana()), String.valueOf(value))));
                            if ((double)effector.getCurrMana() < (double)radio) {
                                if (isPet) {
                                    PetSkillEffectorUtil.sendMsg(fight, effector, "法力值大于" + radio + "才可执行！");
                                }
                                return false;
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
            }
            String serialStr = String.valueOf(petSkill.getSerial());
            if ((serialStr.length() == 5 && effector.getSkillCoolMap().getInt(serialStr) > 0) || (serialStr.length() == 6 && effector.getSkillCoolMap().getInt(serialStr.substring(0, 5)) > 0)) {
                if (isPet) {
                    PetSkillEffectorUtil.sendMsg(fight, effector, "技能冷却中，本回合无法施放。");
                }
                return false;
            }
            else {
                return true;
            }
        }
        else {
            if (isPet) {
                PetSkillEffectorUtil.sendMsg(fight, effector, "未找到此技能！");
                return false;
            }
            return true;
        }
    }
    
    public static void excutePetPKPetPassiveSkill(Fight fight, FightObject effector, FightObject target, Effect effect) {
        if (effector.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && target.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
            List<SysPetSkill> sysPetSkills = attackPet((List)effector.getSkillIds().stream().filter(obj -> (int)obj.getSkillId() != -1).collect(Collectors.toList()), fight.isPvp());
            for (SysPetSkill petSkill : sysPetSkills) {
                SkillProcessor petProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                Effect effect2 = petProcessor.effect(fight, effector, target, Integer.valueOf(effect.getHurt()));
                if (effect2 != null && effect2.getHurt() > 0) {
                    effect.setHurt(effect2.getHurt());
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public static boolean isAttackCloaking(FightObject effector, int type) {
        boolean isAttackCloaking = false;
        Optional<RoleSkill> any = effector.getSkillIds().stream().filter(skill -> 83020 == (int)skill.getSkillId()).findAny();
        switch (type) {
            case 0: {
                if (any.isPresent()) {
                    isAttackCloaking = true;
                    break;
                }
                break;
            }
            case 7: {
                Map<Integer, SysPetSkill> sysPetSkillMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue());
                SysPetSkill petSkill = (SysPetSkill)sysPetSkillMap.get(Integer.valueOf(effector.getSkillId()));
                if ((petSkill != null && any.isPresent()) || effector.getSkillId() == 83020) {
                    isAttackCloaking = true;
                    break;
                }
                break;
            }
        }
        return isAttackCloaking;
    }
    
    public static boolean isMagicAttack(FightObject effector, FightObject target) {
        boolean isAttack = false;
        if ((target.getStatusValue(1011) > 0 && isAttackCloaking(effector, 7)) || target.getStatusValue(1011) <= 0) {
            isAttack = true;
        }
        return isAttack;
    }
    
    public static List<Integer> getTMTargetIds(Fight fight, FightObject effector, int skillSerial) {
        FightObject target = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        List<Integer> targets = SkillTargetPicker.getFightObjectIds(fight, effector, true);
        if (target != null && target.getStatusValue(1011) > 0 && !targets.contains(target.getId())) {
            target = null;
        }
        if ((target == null || target.isFallDown()) && targets != null && targets.size() > 0) {
            target = fight.queryFightObject((Integer)targets.get(0));
            effector.setTargetId((int)target.getId());
        }
        List<Integer> list = new ArrayList<>();
        if (target != null && !target.isFallDown()) {
            list.add(target.getId());
        }
        List<Integer> addList = PetSkillEffectorUtil.queryCorrelationTargetIds(fight, effector, skillSerial);
        if (addList != null && addList.size() > 0) {
            list.addAll(addList);
        }
        AwakenSkillUtils.skillIntensifyTM(fight, effector, skillSerial, list);
        if (list.size() > 0) {
            return list;
        }
        return null;
    }
    
    public static void clearStatusParam(Fight fight, List<FightObject> units, FightObject effector, int skillId) {
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, skillId, 0);
        for (int i = 0; i < units.size(); ++i) {
            FightObject obj = (FightObject)units.get(i);
            List<Integer> clearStatusList = obj.getStatusList(StatusConst.getAnomalousStatus());
            if (clearStatusList != null) {
                obj.clearStatus(clearStatusList);
                FightObjectParam targetParam = ActionParamUtils.clearStatusForTarget((int)obj.getId(), i + 1, clearStatusList, 2);
                actionDetailParam.getTargetParamList().add(targetParam);
            }
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
    }
    
    public static void statusClear(Fight fight, FightObject fightObject, int status) {
        if (81031 == status) {
            SkillProcessor skillProcessor = SkillProcessor.queryProcessor(Integer.valueOf(81031));
            if (skillProcessor != null) {
                skillProcessor.statusClear(fight, fightObject, status, new Object[0]);
            }
        }
    }
    
    public static List<Integer> getTFQXTargetIds(Fight fight, FightObject effector, int skillId) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), skillId);
        List<FightObject> targets = SkillTargetPicker.getFightObjects(fight, effector, true);
        if (targets != null && targets.size() > 0) {
            for (FightObject obj : targets) {
                if (obj.getId().equals(Integer.valueOf(effector.getTargetId()))) {
                    targets.remove(obj);
                    break;
                }
                continue;
            }
            if (targets.size() > 0) {
                FightObject target = (FightObject)targets.stream().min(Comparator.comparing(FightObject::getSpeed)).get();
                if (target != null) {
                    List<Integer> list = new ArrayList<>();
                    list.add(target.getId());
                    skillRecord.setT((long)(int)target.getId());
                    return list;
                }
            }
        }
        return null;
    }
    
    static {
        PetTriggerPassiveSkill.data_pet_skill_maps = new LinkedHashMap();
    }
}
