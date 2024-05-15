
package com.kitty.system.game.fightnew.book.awaken.utils;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightnew.book.awaken.other.AWAKENSKILL_31516;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.Iterator;
import java.util.Map;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillConst;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class AwakenSkillUtils
{
    public static void roundStart(final Fight fight, final FightObject fightObject) {
        if (fightObject != null && fightObject.isPet()) {
            final PetFightObject petFightObject = (PetFightObject)fightObject;
            final Role role = petFightObject.getRole();
            final Pet pet = petFightObject.getPet();
            final List<PetEquip> wearAcsrys = AcsryPetSkillUtil.getWearAcsrys(role, pet);
            if (wearAcsrys.size() >= 3) {
                final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
                if (sysPet != null) {
                    final Integer jxSkill = sysPet.getJxSkill();
                    if (jxSkill != null && AwakenSkillConst.getRoundStartSkills().contains(jxSkill)) {
                        final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
                        if (awakenSkillProcessor != null) {
                            awakenSkillProcessor.roundStart(fight, petFightObject);
                        }
                    }
                }
            }
        }
    }
    
    public static NutMap addition(final Role role, final Pet pet) {
        NutMap nutMap = additionSuit(role, pet);
        if (nutMap == null) {
            nutMap = NutMap.NEW();
        }
        final NutMap nutMap2 = additionAwaken(role, pet);
        if (nutMap2 != null) {
            for (final Map.Entry<String, Object> entry : nutMap2.entrySet()) {
                if (nutMap.getDouble(entry.getKey()) != 0.0) {
                    nutMap.setv(entry.getKey(), nutMap.getDouble(entry.getKey() + entry.getValue()));
                }
                else {
                    nutMap.setv(entry.getKey(), entry.getValue());
                }
            }
        }
        if (nutMap.size() > 0) {
            return nutMap;
        }
        return null;
    }
    
    public static NutMap additionSuit(final Role role, final Pet pet) {
        final List<PetEquip> wearAcsrys = AcsryPetSkillUtil.getWearAcsrys(role, pet);
        if (wearAcsrys.size() >= 3) {
            final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
            if (sysPet != null) {
                final Integer jxSkill = sysPet.getJxSkill();
                if (jxSkill != null && AwakenSkillConst.getAdditionSkills().contains(jxSkill)) {
                    final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
                    if (awakenSkillProcessor != null) {
                        final NutMap nutMap = awakenSkillProcessor.additionSuit(role, pet);
                        return nutMap;
                    }
                }
            }
        }
        return null;
    }
    
    public static NutMap additionAwaken(final Role role, final Pet pet) {
        final List<PetEquip> wearAcsrys = AcsryPetSkillUtil.getWearAcsrys(role, pet);
        if (wearAcsrys.size() >= 3) {
            final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
            if (sysPet != null) {
                final Integer jxSkill = sysPet.getJxSkill();
                if (jxSkill != null && AwakenSkillConst.getAdditionSkills().contains(jxSkill)) {
                    final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
                    if (awakenSkillProcessor != null && pet.getAwaken() > 0) {
                        final NutMap nutMap = awakenSkillProcessor.additionAwaken(role, pet);
                        return nutMap;
                    }
                }
            }
        }
        return null;
    }
    
    public static double skillIntensifyDouble(final Fight fight, final FightObject effector, final int skillSerial, final FightObject target) {
        if (effector.isPet()) {
            final Integer jxSkill = getJxSkill(effector);
            if (jxSkill == null || skillSerial != jxSkill) {
                return 0.0;
            }
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(skillSerial);
            if (awakenSkillProcessor != null) {
                final NutMap nutMap = awakenSkillProcessor.skillIntensify(fight, (PetFightObject)effector, target);
                if (nutMap != null) {
                    final double value = nutMap.getDouble("percent");
                    if (value != 0.0) {
                        return value;
                    }
                }
            }
        }
        return 0.0;
    }
    
    public static boolean skillIntensifyBoolean(final Fight fight, final FightObject effector, final int skillSerial) {
        if (effector.isPet()) {
            final Integer jxSkill = getJxSkill(effector);
            if (jxSkill == null || skillSerial != jxSkill) {
                return false;
            }
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(skillSerial);
            if (awakenSkillProcessor != null) {
                final NutMap nutMap = awakenSkillProcessor.skillIntensify(fight, (PetFightObject)effector);
                if (nutMap != null) {
                    final double value = nutMap.getDouble("percent");
                    if (value != 0.0) {
                        return SkillAi.canAffectOnPro(value);
                    }
                }
            }
        }
        return false;
    }
    
    public static void skillIntensifyTM(final Fight fight, final FightObject effector, final int skillId, final List<Integer> excludeTargets) {
        if (effector.isPet()) {
            final Integer jxSkill = getJxSkill(effector);
            if (jxSkill == null || !AwakenSkillConst.getSkillIntensifyTMSkills().contains(skillId)) {
                return;
            }
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.skillIntensifyTM(fight, (PetFightObject)effector, skillId, excludeTargets);
            }
        }
    }
    
    public static void magicAttackEnd(final Fight fight, final FightObject effector, final RoleSkill skill, final ActionDetailParam actionDetailParam, final List<MagicAttackParam> targets) {
        if (skill != null) {
            final Integer jxSkill = getJxSkill(effector);
            if (jxSkill != null && AwakenSkillConst.getMagicAttackEndSkills().contains(jxSkill)) {
                final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
                if (awakenSkillProcessor != null) {
                    awakenSkillProcessor.magicAttackEnd(fight, effector, skill, actionDetailParam, targets);
                }
            }
        }
    }
    
    public static void magicAttackEndOhter(final Fight fight, final FightObject effector, final RoleSkill skill, final List<MagicAttackParam> magicTargets) {
        final Integer jxSkill = getJxSkill(effector);
        if (jxSkill != null && AwakenSkillConst.getMagicAttackEndOtherSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.magicAttackEndOther(fight, effector, skill, magicTargets);
            }
        }
    }
    
    public static void beforePhysicalAttack(final Fight fight, final FightObject effector, final FightObject target) {
        final Integer jxSkill = getJxSkill(effector);
        if (jxSkill != null && AwakenSkillConst.getBeforePhysicalAttackSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.beforePhysicalAttack(fight, effector);
            }
        }
    }
    
    public static void phyAttackEnd(final Fight fight, final FightObject effector, final BattleUnit battleUnit, final ActionDetailParam actionDetailParam) {
        final Integer jxSkill = getJxSkill(effector);
        if (jxSkill != null && AwakenSkillConst.getPhyAttackEndSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.phyAttackEnd(fight, effector, battleUnit, actionDetailParam);
            }
        }
    }
    
    public static void beforeDoAction(final Fight fight, final FightObject effector) {
        final Integer jxSkill = getJxSkill(effector);
        if (jxSkill != null && AwakenSkillConst.getBeforeDoActionSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.beforeDoAction(fight, effector);
            }
        }
        AWAKENSKILL_31516.clearStatus(fight, effector);
    }
    
    public static boolean revive(final Fight fight, final FightObject effector) {
        final Integer jxSkill = getJxSkill(effector);
        if (jxSkill != null && AwakenSkillConst.getReviveSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                final NutMap map = awakenSkillProcessor.revive(fight, effector);
                if (map != null && map.getBoolean("successful", false)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static NutMap isGuard(final Fight fight, final FightObject fightObject) {
        if (fightObject.isPlayer()) {
            final List<FightObject> mySide = fight.getMySide(fightObject.getId());
            FightObject petFightObject = null;
            for (final FightObject obj : mySide) {
                if (obj.isPet() && obj.getHostId() == fightObject.getId()) {
                    petFightObject = obj;
                    break;
                }
            }
            if (petFightObject != null) {
                final Integer jxSkill = getJxSkill(petFightObject);
                if (jxSkill != null && AwakenSkillConst.getIsGuardSkills().contains(jxSkill)) {
                    final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
                    if (awakenSkillProcessor != null) {
                        final double percent = awakenSkillProcessor.isGuard(fight, petFightObject);
                        if (percent > 0.0) {
                            return NutMap.NEW().setv("percent", percent).setv("petFightObjectId", petFightObject.getId());
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static NutMap countRecoverBloodEnd(final Fight fight, final FightObject fightObject, final RoleSkill skill, final int addLife) {
        if (addLife > 0) {
            final Integer jxSkill = getJxSkill(fightObject);
            if (jxSkill != null && AwakenSkillConst.getCountRecoverBloodEndSkills().contains(jxSkill)) {
                final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
                if (awakenSkillProcessor != null) {
                    awakenSkillProcessor.countRecoverBloodEnd(fight, fightObject, skill, addLife);
                }
            }
        }
        return null;
    }
    
    public static void enteringSide(final Fight fight, final FightObject fightObject) {
        final Integer jxSkill = getJxSkill(fightObject);
        if (jxSkill != null && jxSkill != null && AwakenSkillConst.getEnteringSideSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.enteringSide(fight, fightObject);
            }
        }
    }
    
    public static void sufferHarm(final Fight fight, final FightObject fightObject, final int hurt, final FightObject effector) {
        if (fightObject == null || fightObject.isFallDown() || hurt <= 0) {
            return;
        }
        final Integer jxSkill = getJxSkill(fightObject);
        if (jxSkill != null && AwakenSkillConst.getSufferHarmSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.sufferHarm(fight, fightObject, hurt, effector);
            }
        }
    }
    
    public static void enterFight(final Fight fight, final FightObject fightObject) {
        final Integer jxSkill = getJxSkill(fightObject);
        if (jxSkill != null && AwakenSkillConst.getEnterFightSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.enterFight(fight, fightObject);
            }
        }
    }
    
    public static void exitSide(final Fight fight, final FightObject fightObject) {
        final Integer jxSkill = getJxSkill(fightObject);
        if (jxSkill != null && AwakenSkillConst.getExitSideSkills().contains(jxSkill)) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.exitSide(fight, fightObject);
            }
        }
    }
    
    public static void statusOver(final Fight fight, final FightObject fightObject, final int status) {
        if (2169 == status) {
            final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(31514);
            if (awakenSkillProcessor != null) {
                awakenSkillProcessor.statusOver(fight, fightObject, status);
            }
        }
    }
    
    public static int changeHurt(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill skill, final int index) {
        if (effector.isPet() && (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_THUNDER.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == skill.getType())) {
            final Integer jxSkill = getJxSkill(effector);
            if (jxSkill != null && AwakenSkillConst.getChangeHurtSkills().contains(jxSkill)) {
                final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
                if (awakenSkillProcessor != null) {
                    return awakenSkillProcessor.changeHurt(fight, effector, target, index);
                }
            }
        }
        return 0;
    }
    
    public static List<FiedValue> useDrug(final Fight fight, final FightObject effector, final FightObject target, final int showClass) {
        final List<FightObject> mySide = fight.getMySide(target.getId());
        if (mySide != null && mySide.size() > 0) {
            for (final FightObject obj : mySide) {
                if (obj.isPet() && !obj.getId().equals(target.getId())) {
                    final Integer jxSkill = getJxSkill(obj);
                    if (jxSkill == null || !AwakenSkillConst.getUseDrugSkills().contains(jxSkill)) {
                        continue;
                    }
                    final AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
                    if (awakenSkillProcessor != null) {
                        return awakenSkillProcessor.useDrug(fight, obj, target, showClass);
                    }
                    continue;
                }
            }
        }
        return null;
    }
    
    public static int getAwakenSpeed(final Pet pet) {
        final Role role = SpringUtils.getRoleService().getPlayer(pet.getRoleId());
        if (role == null) {
            return 0;
        }
        final NutMap nutMap = additionAwaken(role, pet);
        if (nutMap != null) {
            final int suitSpeed = nutMap.getInt(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
            if (suitSpeed > 0) {
                return suitSpeed;
            }
        }
        return 0;
    }
    
    private static Integer getJxSkill(final FightObject effector) {
        if (!effector.isPet()) {
            return null;
        }
        final Pet pet = effector.getPet();
        if (pet == null) {
            return null;
        }
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        if (sysPet == null) {
            return null;
        }
        final Integer jxSkill = sysPet.getJxSkill();
        if (jxSkill == null || jxSkill <= 0) {
            return null;
        }
        return jxSkill;
    }
}
