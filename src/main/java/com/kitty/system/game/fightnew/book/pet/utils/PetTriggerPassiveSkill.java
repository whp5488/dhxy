
package com.kitty.system.game.fightnew.book.pet.utils;

import java.util.LinkedHashMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import java.util.Collection;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import java.util.Optional;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import java.util.Iterator;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import java.util.stream.Collector;
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
    
    public static List<SysPetSkill> roundStart(final String roundNode, final List<RoleSkill> roleSkills, final int isPvp) {
        final List<Integer> serials = getInegers(roleSkills);
        final List<SysPetSkill> skills = new ArrayList<SysPetSkill>();
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
    
    public static List<SysPetSkill> normalAttack(final String skillTrigget, final List<RoleSkill> roleSkills, final int isPvp) {
        final List<Integer> serials = getInegers(roleSkills);
        final List<SysPetSkill> normalAttackSkills = new ArrayList<SysPetSkill>();
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, skillTrigget, serials, normalAttackSkills);
        }
        return normalAttackSkills;
    }
    
    public static boolean heroFallDown(final Fight fight, final FightObject target) {
        boolean flag = false;
        final List<FightObject> mySidePets = fight.getMySide(target.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toList());
        if (mySidePets.size() == 1) {
            final FightObject petObject = mySidePets.get(0);
            final FightObject hostObject = fight.queryFightObject(petObject.getHostId());
            if (hostObject.isFallDown()) {
                final List<RoleSkill> roleSkills = petObject.getSkillIds().stream().filter(obj -> obj.getSkillId() > 0).collect(Collectors.toList());
                final List<Integer> serials = getInegers(roleSkills);
                final List<SysPetSkill> heroFallDownSkills = new ArrayList<SysPetSkill>();
                if (serials != null && serials.size() > 0) {
                    getSkills(fight.isPvp(), PET_PASSIVE_SKILL_TRIGGER_DICT.HERO_FALL_DOWN.getValue(), serials, heroFallDownSkills);
                }
                for (final SysPetSkill petSkill : heroFallDownSkills) {
                    final SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    processor.passive(fight, petObject);
                    flag = true;
                }
            }
        }
        return flag;
    }
    
    public static List<SysPetSkill> petExit(final String type, final List<RoleSkill> roleSkills, final int isPvp) {
        final List<SysPetSkill> petExitSkills = new ArrayList<SysPetSkill>();
        final List<Integer> serials = getInegers(roleSkills);
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, type, serials, petExitSkills);
        }
        return petExitSkills;
    }
    
    public static List<SysPetSkill> attackPet(final List<RoleSkill> roleSkills, final int isPvp) {
        final List<SysPetSkill> attackPetSkills = new ArrayList<SysPetSkill>();
        final List<Integer> serials = getInegers(roleSkills);
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.ATTACK_PET.getValue(), serials, attackPetSkills);
        }
        return attackPetSkills;
    }
    
    public static List<SysPetSkill> currLifeLessThan40(final List<RoleSkill> roleSkills, final int isPvp) {
        final List<SysPetSkill> currLifeLessThan40Skills = new ArrayList<SysPetSkill>();
        final List<Integer> serials = getInegers(roleSkills);
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.CURR_LIFE_LESS_THAN_40.getValue(), serials, currLifeLessThan40Skills);
        }
        return currLifeLessThan40Skills;
    }
    
    public static List<SysPetSkill> beforePetExit(final List<RoleSkill> roleSkills, final int isPvp) {
        final List<SysPetSkill> otherSkills = new ArrayList<SysPetSkill>();
        final List<Integer> serials = getInegers(roleSkills);
        if (serials != null && serials.size() > 0) {
            getSkills(isPvp, PET_PASSIVE_SKILL_TRIGGER_DICT.BEFORE_PET_EXIT.getValue(), serials, otherSkills);
        }
        return otherSkills;
    }
    
    public static void excutePassiveSkill(final Fight fight, final FightObject effector, final String type, final Object... args) {
        final Map<Integer, SysPetSkill> petSkillMap = querySkills(type);
        if (petSkillMap.size() > 0) {
            final List<RoleSkill> petRoleSkills = effector.getSkillIds().stream().filter(skill -> -1 != skill.getSkillId()).collect(Collectors.toList());
            for (final RoleSkill roleSkill : petRoleSkills) {
                final SysPetSkill petSkill = petSkillMap.get(roleSkill.getSkillId());
                if (petSkill != null && (fight.isPvp() == 1 || petSkill.getIsPvp() == fight.isPvp())) {
                    final SkillProcessor skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    skillProcessor.passive(fight, effector, args);
                }
            }
        }
    }
    
    private static void getSkills(final int isPvp, final String opportunity, final List<Integer> serials, final List<SysPetSkill> skills) {
        final Map<Integer, SysPetSkill> resourceMap = PetTriggerPassiveSkill.data_pet_skill_maps.get(opportunity);
        for (int i = serials.size() - 1; i >= 0; --i) {
            final SysPetSkill sysPetSkill = resourceMap.get(serials.get(i));
            if (sysPetSkill != null) {
                if (isPvp == 1 || isPvp == sysPetSkill.getIsPvp()) {
                    skills.add(sysPetSkill);
                }
                serials.remove(i);
            }
        }
    }
    
    public static Map<Integer, SysPetSkill> querySkills(final String opportunity) {
        final Map<Integer, SysPetSkill> resourceMap = PetTriggerPassiveSkill.data_pet_skill_maps.get(opportunity);
        return resourceMap;
    }
    
    public static List<Integer> getInegers(final List<RoleSkill> roleSkills) {
        if (roleSkills != null) {
            final List<Integer> serials = new ArrayList<Integer>();
            roleSkills.forEach(roleSkill -> serials.add(roleSkill.getSkillId()));
            return serials;
        }
        return null;
    }
    
    public static boolean checkSkillIsUsable(final Fight fight, final FightObject effector, final Integer skillId, final String type) {
        final Map<Integer, SysPetSkill> petSkillMap = PetTriggerPassiveSkill.data_pet_skill_maps.get(type);
        final SysPetSkill petSkill = petSkillMap.get(skillId);
        final int isPvp = fight.isPvp();
        boolean isPet = true;
        if (effector.isMonster()) {
            isPet = false;
        }
        if (petSkill != null) {
            if (isPvp == 0 && isPvp != petSkill.getIsPvp() && isPet) {
                PetSkillEffectorUtil.sendMsg(fight, effector, "此技能仅pvp生效！");
                return false;
            }
            final Map<String, Double> astrictTypeMap = petSkill.getAstrictTypeMap();
            if (astrictTypeMap.size() > 0) {
                for (final Map.Entry entry : astrictTypeMap.entrySet()) {
                    final Double value = (Double)entry.getValue();
                    if ("after_round".equals(entry.getKey())) {
                        final boolean isTrigger = AcsryPetSkillUtil.isTrigger(petSkill.getSerial(), effector);
                        int roundValue = value.intValue();
                        if (isTrigger) {
                            --roundValue;
                        }
                        if (fight.getRound() <= roundValue) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "前" + roundValue + "回合不可用！");
                            return false;
                        }
                    }
                    if ("gt_life".equals(entry.getKey()) && effector.getCurrLife() <= value) {
                        if (isPet) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "气血值大于" + value.intValue() + "才可执行！");
                        }
                        return false;
                    }
                    if ("gt_mana".equals(entry.getKey()) && effector.getCurrMana() <= value) {
                        if (isPet) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "法力值大于" + value.intValue() + "才可执行！");
                        }
                        return false;
                    }
                    if ("ge_mana".equals(entry.getKey()) && effector.getCurrMana() < value) {
                        if (isPet) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "法力值大于" + value.intValue() + "才可执行！");
                        }
                        return false;
                    }
                    if ("gt_max_life".equals(entry.getKey()) && value < 1.0) {
                        final Double radio = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(effector.getMaxLife()), String.valueOf(value)));
                        if (effector.getCurrLife() < radio) {
                            if (isPet) {
                                PetSkillEffectorUtil.sendMsg(fight, effector, "气血值大于" + radio + "才可执行！");
                            }
                            return false;
                        }
                    }
                    if (!"gt_max_mana".equals(entry.getKey()) || value >= 1.0) {
                        continue;
                    }
                    final Double radio = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(effector.getMaxMana()), String.valueOf(value)));
                    if (effector.getCurrMana() < radio) {
                        if (isPet) {
                            PetSkillEffectorUtil.sendMsg(fight, effector, "法力值大于" + radio + "才可执行！");
                        }
                        return false;
                    }
                }
            }
            final String serialStr = String.valueOf(petSkill.getSerial());
            if ((serialStr.length() == 5 && effector.getSkillCoolMap().getInt(serialStr) > 0) || (serialStr.length() == 6 && effector.getSkillCoolMap().getInt(serialStr.substring(0, 5)) > 0)) {
                if (isPet) {
                    PetSkillEffectorUtil.sendMsg(fight, effector, "技能冷却中，本回合无法施放。");
                }
                return false;
            }
            return true;
        }
        else {
            if (isPet) {
                PetSkillEffectorUtil.sendMsg(fight, effector, "未找到此技能！");
                return false;
            }
            return true;
        }
    }
    
    public static void excutePetPKPetPassiveSkill(final Fight fight, final FightObject effector, final FightObject target, final Effect effect) {
        if (effector.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && target.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
            final List<SysPetSkill> sysPetSkills = attackPet((List<RoleSkill>)effector.getSkillIds().stream().filter(obj -> obj.getSkillId() != -1).collect(Collectors.toList()), fight.isPvp());
            for (final SysPetSkill petSkill : sysPetSkills) {
                final SkillProcessor petProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                final Effect effect2 = petProcessor.effect(fight, effector, target, effect.getHurt());
                if (effect2 != null && effect2.getHurt() > 0) {
                    effect.setHurt(effect2.getHurt());
                }
            }
        }
    }
    
    public static boolean isAttackCloaking(final FightObject effector, final int type) {
        boolean isAttackCloaking = false;
        final Optional<RoleSkill> any = effector.getSkillIds().stream().filter(skill -> 83020 == skill.getSkillId()).findAny();
        switch (type) {
            case 0: {
                if (any.isPresent()) {
                    isAttackCloaking = true;
                    break;
                }
                break;
            }
            case 7: {
                final Map<Integer, SysPetSkill> sysPetSkillMap = PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue());
                final SysPetSkill petSkill = sysPetSkillMap.get(effector.getSkillId());
                if ((petSkill != null && any.isPresent()) || effector.getSkillId() == 83020) {
                    isAttackCloaking = true;
                    break;
                }
                break;
            }
        }
        return isAttackCloaking;
    }
    
    public static boolean isMagicAttack(final FightObject effector, final FightObject target) {
        boolean isAttack = false;
        if ((target.getStatusValue(1011) > 0 && isAttackCloaking(effector, 7)) || target.getStatusValue(1011) <= 0) {
            isAttack = true;
        }
        return isAttack;
    }
    
    public static List<Integer> getTMTargetIds(final Fight fight, final FightObject effector, final int skillSerial) {
        FightObject target = fight.queryFightObject(effector.getTargetId());
        final List<Integer> targets = SkillTargetPicker.getFightObjectIds(fight, effector, true);
        if (target != null && target.getStatusValue(1011) > 0 && !targets.contains(target.getId())) {
            target = null;
        }
        if ((target == null || target.isFallDown()) && targets != null && targets.size() > 0) {
            target = fight.queryFightObject(targets.get(0));
            effector.setTargetId(target.getId());
        }
        final List<Integer> list = new ArrayList<Integer>();
        if (target != null && !target.isFallDown()) {
            list.add(target.getId());
        }
        final List<Integer> addList = PetSkillEffectorUtil.queryCorrelationTargetIds(fight, effector, skillSerial);
        if (addList != null && addList.size() > 0) {
            list.addAll(addList);
        }
        AwakenSkillUtils.skillIntensifyTM(fight, effector, skillSerial, list);
        if (list.size() > 0) {
            return list;
        }
        return null;
    }
    
    public static void clearStatusParam(final Fight fight, final List<FightObject> units, final FightObject effector, final int skillId) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, skillId, 0);
        for (int i = 0; i < units.size(); ++i) {
            final FightObject obj = units.get(i);
            final List<Integer> clearStatusList = obj.getStatusList(StatusConst.getAnomalousStatus());
            if (clearStatusList != null) {
                obj.clearStatus(clearStatusList);
                final FightObjectParam targetParam = ActionParamUtils.clearStatusForTarget(obj.getId(), i + 1, clearStatusList, 2);
                actionDetailParam.getTargetParamList().add(targetParam);
            }
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
    }
    
    public static void statusClear(final Fight fight, final FightObject fightObject, final int status) {
        if (81031 == status) {
            final SkillProcessor skillProcessor = SkillProcessor.queryProcessor(81031);
            if (skillProcessor != null) {
                skillProcessor.statusClear(fight, fightObject, status);
            }
        }
    }
    
    public static List<Integer> getTFQXTargetIds(final Fight fight, final FightObject effector, final int skillId) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), skillId);
        final List<FightObject> targets = SkillTargetPicker.getFightObjects(fight, effector, true);
        if (targets != null && targets.size() > 0) {
            for (final FightObject obj : targets) {
                if (obj.getId().equals(effector.getTargetId())) {
                    targets.remove(obj);
                    break;
                }
            }
            if (targets.size() > 0) {
                final FightObject target = targets.stream().min(Comparator.comparing(FightObject::getSpeed)).get();
                if (target != null) {
                    final List<Integer> list = new ArrayList<Integer>();
                    list.add(target.getId());
                    skillRecord.setT(target.getId());
                    return list;
                }
            }
        }
        return null;
    }
    
    static {
        PetTriggerPassiveSkill.data_pet_skill_maps = new LinkedHashMap<String, Map<Integer, SysPetSkill>>();
    }
}
