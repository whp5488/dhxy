
package com.kitty.system.game.fightnew.utils;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.common.utils.RandomUtil;
import java.util.Collection;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import java.util.Optional;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.hero.utils.HeroSkillUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import java.util.List;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuSkillUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class PassiveSkillUtils
{
    public static void beforeDoAction(final Fight fight, final FightObject effector) {
        try {
            TianFuSkillUtils.beforeDoAction(fight, effector);
            AwakenSkillUtils.beforeDoAction(fight, effector);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void selfSellEnd(final Fight fight, final FightObject fightObject) {
        try {
            PetTriggerPassiveSkill.excutePassiveSkill(fight, fightObject, PET_PASSIVE_SKILL_TRIGGER_DICT.SELF_SELL_END.getValue(), new Object[0]);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int changeHurt(final Fight fight, final FightObject effector, final FightObject target, final int hurt, final boolean isFury, final RoleSkill skill, final List<Integer> clearStatusList) {
        int chageHurt = 0;
        try {
            chageHurt += TianFuSkillUtils.calculateHurt(fight, effector, target, hurt);
            chageHurt += HorseSkillUtils.reduceHurt(fight, effector, target, hurt, isFury, skill);
            chageHurt += AcsrySkillUtil.executeReduceHp(fight, effector, target, skill, hurt, clearStatusList);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return chageHurt;
    }
    
    public static void beforeMagic(final Fight fight, final FightObject effector, final RoleSkill skill, final List<Integer> targets) {
        try {
            TianFuSkillUtils.beforeDoMagic(fight, effector, skill);
            HorseSkillUtils.beforeMagicHurt(fight, effector, skill);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void beforeMagicOne(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill skill) {
        try {
            AcsrySkillUtil.beforeExecuteMagic(fight, effector, target, skill);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void beforeSkillEffect(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill roleSkill) {
        try {
            TianFuSkillUtils.beforeSkillEffect(fight, effector, target, roleSkill);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void statusOver(final Fight fight, final Map<Integer, List<Integer>> clearStatusMap) {
        for (final Map.Entry<Integer, List<Integer>> entry : clearStatusMap.entrySet()) {
            final FightObject obj = fight.queryFightObject(entry.getKey());
            if (obj != null) {
                final List<Integer> clearList = entry.getValue();
                for (final Integer status : clearList) {
                    AcsrySkillUtil.statusOver(fight, obj, status);
                    if (1006 == status) {
                        obj.getStatusMap().setv(String.valueOf(8008), 0);
                    }
                    PetTriggerPassiveSkill.statusClear(fight, obj, status);
                    AwakenSkillUtils.statusOver(fight, obj, status);
                }
            }
        }
    }
    
    public static void roundStart(final Fight fight) {
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            try {
                AcsrySkillUtil.roundStart(fight, fightObject);
                AwakenSkillUtils.roundStart(fight, fightObject);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void roundEnd(final Fight fight) {
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (fightObject.isPlayer()) {
                try {
                    AcsrySkillUtil.roundEnd(fight, fightObject);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void magicAttackEnd(final Fight fight, final FightObject effector, final List<MagicAttackParam> targets, final RoleSkill skill, final ActionDetailParam actionDetailParam) {
        try {
            TianFuSkillUtils.magicAttackEnd(fight, effector, targets, skill, actionDetailParam);
            for (final MagicAttackParam magicParam : targets) {
                final FightObject target = magicParam.getTarget();
                HeroSkillUtil.attackEnd(fight, effector, target, actionDetailParam);
                if (magicParam.isDodge()) {
                    HorseSkillUtils.targetHits(fight, effector, target, actionDetailParam);
                }
                AcsrySkillUtil.attackEnd(fight, effector, target, skill, magicParam.isDodge(), magicParam.getClearStatusList(), magicParam.getHurt(), magicParam.getTargetReduceMp(), magicParam.getSideTarget(), actionDetailParam);
                PetPassiveSkillUtil.magicOther(fight, effector, magicParam.getTarget(), skill, actionDetailParam);
            }
            AwakenSkillUtils.magicAttackEnd(fight, effector, skill, actionDetailParam, targets);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void afterPrintCommand(final Fight fight) {
        try {
            AcsrySkillUtil.afterPrintCommand(fight);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean isUnlock(final Fight fight, final FightObject fightObject, final Integer status) {
        final boolean flag = AcsrySkillUtil.isUnlock(fight, fightObject, status);
        return flag;
    }
    
    public static void excutePropertyChange(final FightObject fightObject) {
        try {
            AcsrySkillUtil.excutePropertyChange(fightObject);
            PetPassiveSkillUtil.checkControlledStatus(fightObject);
            TianFuSkillUtils.clearTianFuProperty(fightObject);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int countRecoverBloodEnd(final Fight fight, final FightObject fightObject, final int life, final RoleSkill skill) {
        int changeLife = 0;
        try {
            changeLife += AcsrySkillUtil.countRecoverBloodEnd(fight, fightObject, skill, life);
            AwakenSkillUtils.countRecoverBloodEnd(fight, fightObject, skill, life);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return changeLife;
    }
    
    public static int countRecoverMpEnd(final Fight fight, final FightObject fightObject, final int mana) {
        int changeMana = 0;
        try {
            changeMana += AcsrySkillUtil.countRecoverMpEnd(fight, fightObject, mana);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return changeMana;
    }
    
    public static int countRecoverBloodEndSS(final Fight fight, final FightObject effecter, final FightObject target, final RoleSkill skill, final int life) {
        int changeLife = 0;
        try {
            changeLife += AcsrySkillUtil.countRecoverBloodEndSS(fight, effecter, target, skill, life);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return changeLife;
    }
    
    public static int reduceMp(final Fight fight, final FightObject fightObject, final int mana) {
        int changeMana = 0;
        try {
            changeMana += AcsrySkillUtil.reduceMp(fight, fightObject, mana);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return changeMana;
    }
    
    public static int beforeReduceHurtMp(final Fight fight, final FightObject effecter, final FightObject target, final RoleSkill skill, final int mp) {
        int changeMp = 0;
        try {
            changeMp += AcsrySkillUtil.beforeReduceHurtMp(fight, effecter, target, skill, mp);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return changeMp;
    }
    
    public static int poisonReduceHp(final Fight fight, final FightObject target, final int hurt) {
        int change = 0;
        try {
            change += AcsrySkillUtil.poisonReduceHp(fight, target, hurt);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return change;
    }
    
    public static void phySicalLastHit(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill skill, final int hurt, final ActionDetailParam actionDetailParam) {
        try {
            if (skill == null && effector.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist())) > 0.0) {
                gsdn(fight, effector, target, hurt, actionDetailParam);
            }
            if (skill == null && effector.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT_NUM.getDist())) > 0) {
                flgj(fight, effector, target, actionDetailParam);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void excuteChangeLife(final Fight fight, final FightObject target, final FightObjectParam fightObjectParam, int changeLife) {
        changeLife = countChangeLife(fight, target, changeLife);
        target.setCurrLife(target.getCurrLife() + changeLife);
        fightObjectParam.setChangeLife(fightObjectParam.getChangeLife() + changeLife);
        fightObjectParam.setCurrentLife(target.getCurrLife());
    }
    
    private static int countChangeLife(final Fight fight, final FightObject target, int changeLife) {
        if (changeLife > 0) {
            final int cLife = countRecoverBloodEnd(fight, target, changeLife, null);
            changeLife += cLife;
        }
        if (target.getCurrLife() + changeLife > target.getMaxLife()) {
            changeLife = target.getMaxLife() - target.getCurrLife();
        }
        else if (target.getCurrLife() + changeLife < 0) {
            changeLife = -target.getCurrLife();
        }
        return changeLife;
    }
    
    public static FightObjectParam excuteChangeLifeAuto(final Fight fight, final FightObject target, final ActionDetailParam actionDetailParam, final FightObjectParam fightObjectParam, int changeLife) {
        changeLife = countChangeLife(fight, target, changeLife);
        target.setCurrLife(target.getCurrLife() + changeLife);
        if (fightObjectParam.getChangeLife() != 0) {
            final FightObjectParam targetParamC = new FightObjectParam();
            targetParamC.setObjId(target.getId());
            targetParamC.setOrder(1);
            targetParamC.setMissed(2);
            targetParamC.setChangeLife(changeLife);
            targetParamC.setCurrentLife(target.getCurrLife());
            actionDetailParam.getTargetParamList().add(targetParamC);
            return targetParamC;
        }
        fightObjectParam.setChangeLife(changeLife);
        fightObjectParam.setCurrentLife(target.getCurrLife());
        return fightObjectParam;
    }
    
    public static void excuteChangeMana(final Fight fight, final FightObject target, final FightObjectParam fightObjectParam, int changeMana) {
        if (target.isMonster()) {
            return;
        }
        if (changeMana > 0) {
            final int cMana = countRecoverMpEnd(fight, target, changeMana);
            changeMana += cMana;
        }
        if (target.getCurrMana() + changeMana > target.getMaxMana()) {
            changeMana = target.getMaxMana() - target.getCurrMana();
        }
        else if (target.getCurrMana() + changeMana < 0) {
            changeMana = target.getCurrMana();
        }
        target.setCurrMana(target.getCurrMana() + changeMana);
        fightObjectParam.setChangeMana(fightObjectParam.getChangeMana() + changeMana);
        fightObjectParam.setCurrentMana(target.getCurrMana());
    }
    
    public static void excuteChangeManaAuto(final Fight fight, final FightObject target, final ActionDetailParam actionDetailParam, final FightObjectParam fightObjectParam, int changeMana) {
        if (changeMana > 0) {
            final int cMana = countRecoverMpEnd(fight, target, changeMana);
            changeMana += cMana;
        }
        if (target.getCurrMana() + changeMana > target.getMaxMana()) {
            changeMana = target.getMaxMana() - target.getCurrMana();
        }
        else if (target.getCurrMana() + changeMana < 0) {
            changeMana = target.getCurrMana();
        }
        target.setCurrMana(target.getCurrMana() + changeMana);
        if (fightObjectParam.getChangeMana() != 0) {
            final FightObjectParam targetParamC = new FightObjectParam();
            targetParamC.setObjId(target.getId());
            targetParamC.setOrder(1);
            targetParamC.setMissed(2);
            targetParamC.setChangeMana(changeMana);
            targetParamC.setCurrentMana(target.getCurrMana());
            actionDetailParam.getTargetParamList().add(targetParamC);
        }
        else {
            fightObjectParam.setChangeMana(changeMana);
            fightObjectParam.setCurrentMana(target.getCurrMana());
        }
    }
    
    public static double getWsk(final FightObject effecter) {
        double wsk = 0.0;
        wsk += effecter.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), 0.0);
        wsk += effecter.getSelfAllTempPro(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist());
        return wsk;
    }
    
    public static void beforePhysicalAttack(final Fight fight, final FightObject attacker, final FightObject fightTarget) {
        try {
            TianFuSkillUtils.beforePhysicalAttack(fight, attacker, fightTarget);
            AcsrySkillUtil.beforePhysicalAttack(fight, attacker, fightTarget);
            AwakenSkillUtils.beforePhysicalAttack(fight, attacker, fightTarget);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void gsdn(final Fight fight, final FightObject effector, final FightObject target, final int hurt, final ActionDetailParam actionDetailParam) {
        final FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId(), target.getId());
        if (newTarget == null) {
            return;
        }
        final Effect effect = new Effect();
        effect.setHurt(hurt);
        final RoleSkill skillBy = new RoleSkill();
        skillBy.setType(0);
        skillBy.setSkillId(80011);
        final BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
        battleUnit.setEffect(effect);
        final ActionDetailParam actionDetailParamReturn = PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 80011);
        ActionParamUtils.setNormalAttack(newTarget.getId(), actionDetailParam, actionDetailParamReturn, 105);
    }
    
    public static void flgj(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam) {
        final FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId(), target.getId());
        if (newTarget == null) {
            return;
        }
        effector.getSkillCoolMap().setv(String.valueOf(80023), (Object)1);
        final RoleSkill skillBy = new RoleSkill();
        skillBy.setType(0);
        skillBy.setSkillId(80023);
        int num = effector.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT_NUM.getDist()), 0);
        if (--num <= 0) {
            effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT.getDist()));
            effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT_NUM.getDist()));
        }
        else {
            effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT_NUM.getDist()), (Object)num);
        }
        final BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
        battleUnit.countDoubleHit();
        final ActionDetailParam actionDetailParamReturn = PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 80023);
        ActionParamUtils.setNormalAttack(newTarget.getId(), actionDetailParam, actionDetailParamReturn, 116);
    }
    
    public static boolean isAttactCloaking(final Fight fight, final FightObject fightObject, final RoleSkill roleSkill) {
        boolean attactCloaking = false;
        try {
            attactCloaking = AcsrySkillUtil.magicSelectTarget(fight, fightObject, roleSkill);
            if (attactCloaking) {
                return attactCloaking;
            }
            attactCloaking = PetTriggerPassiveSkill.isAttackCloaking(fightObject, fightObject.getType());
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return attactCloaking;
    }
    
    public static int doubleHitExcute(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill skill, final BattleUnit battleUnit, final String skillTrigget, int doubleHit) {
        try {
            final Effect effectDoubleHit = PetPassiveSkillUtil.getPassiveSkill(fight, effector, target, skill, battleUnit, skillTrigget, doubleHit);
            if (effectDoubleHit != null) {
                final Optional<FiedValue> any = effectDoubleHit.getList().stream().filter(fiedValue -> "doubleHit".equals(fiedValue.getType())).findAny();
                if (any.isPresent()) {
                    doubleHit = Integer.valueOf(String.valueOf(any.get().getValue()));
                }
            }
        }
        catch (final NumberFormatException e) {
            e.printStackTrace();
        }
        return doubleHit;
    }
    
    public static Effect getPassiveSkill(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill skill, final BattleUnit battleUnit, final String skillTrigget, final int hurt) {
        try {
            final Effect effect = PetPassiveSkillUtil.getPassiveSkill(fight, effector, target, skill, battleUnit, skillTrigget, hurt);
            return effect;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void excutePetPKPetPassiveSkill(final Fight fight, final FightObject effector, final FightObject target, final Effect effect) {
        try {
            PetTriggerPassiveSkill.excutePetPKPetPassiveSkill(fight, effector, target, effect);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int addMagicEffect(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill roleSkill, final int hurt, final Effect effect, final int index) {
        int changeHurt = 0;
        try {
            changeHurt += TianFuSkillUtils.addMagicEffect(fight, effector, target, roleSkill, hurt, effect);
            changeHurt += AwakenSkillUtils.changeHurt(fight, effector, target, roleSkill, index);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return changeHurt;
    }
    
    public static void phyAttackEnd(final Fight fight, final FightObject effector, final BattleUnit battleUnit, final ActionDetailParam actionDetailParam) {
        try {
            TianFuSkillUtils.physicalAttack(fight, effector, battleUnit.getTarget(), actionDetailParam);
            TianFuSkillUtils.afterPhyAttack(fight, effector, battleUnit, actionDetailParam);
            HorseSkillUtils.targetHits(fight, effector, battleUnit.getTarget(), actionDetailParam);
            AcsrySkillUtil.attackEnd(fight, effector, battleUnit.getTarget(), null, battleUnit.isDodge(), battleUnit.getClearStatusList(), battleUnit.getPhyHurt(), battleUnit.getTargetReduceMp(), battleUnit.getSideTarget(), actionDetailParam);
            AwakenSkillUtils.phyAttackEnd(fight, effector, battleUnit, actionDetailParam);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void normalAttack(final Fight fight, final FightObject effector, final FightObject target, final BattleUnit battleUnit, final int hurt, final int skillSerial) {
        try {
            PetPassiveSkillUtil.normalAttackDoubleHit(fight, effector, target, battleUnit, hurt, skillSerial);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void normalAttackAfter(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam) {
        try {
            PetPassiveSkillUtil.normalAttackAfter(fight, effector, target, actionDetailParam);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void unitMagic(final Fight fight, final FightObject attacker, final SkillProcessor processor, final RoleSkill skill) {
        try {
            PetPassiveSkillUtil.unitMagic(fight, attacker, processor, skill);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int subtractMp(final Fight fight, final FightObject effector, final int mp) {
        int changeMp = 0;
        try {
            changeMp += TianFuSkillUtils.subtractMp(fight, effector, mp);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return changeMp;
    }
    
    public static boolean enemyFristMagic(final Fight fight, final FightObject attacker, final SkillProcessor processor, final RoleSkill skill) {
        boolean flag = false;
        try {
            flag = PetPassiveSkillUtil.enemyFristMagic(fight, attacker, processor, skill, fight.isPvp());
            if (flag) {
                return flag;
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    public static void magicMiss(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill roleSkill, final ActionDetailParam actionDetailParam) {
        try {
            TianFuSkillUtils.magicMiss(fight, effector, target, roleSkill, actionDetailParam);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void excutePassiveSkill(final Fight fight, final FightObject effector, final String type, final Object... args) {
        try {
            PetTriggerPassiveSkill.excutePassiveSkill(fight, effector, type, args);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void magicAttackEndOther(final Fight fight, final FightObject effector, final RoleSkill skill, final List<MagicAttackParam> magicTargets) {
        try {
            AwakenSkillUtils.magicAttackEndOhter(fight, effector, skill, magicTargets);
            if (magicTargets != null && magicTargets.size() > 0) {
                for (final MagicAttackParam magicAttackParam : magicTargets) {
                    final FightObject target = magicAttackParam.getTarget();
                    final int hurt = magicAttackParam.getHurt();
                    if (target != null) {
                        if (hurt <= 0) {
                            continue;
                        }
                        sufferHarm(fight, target, hurt, effector);
                    }
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void friendMagic(final Fight fight, final FightObject effector, final ActionDetailParam actionDetailParam) {
        try {
            PetPassiveSkillUtil.friendMagic(fight, effector, fight.isPvp(), actionDetailParam);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<FiedValue> useDrug(final Fight fight, final FightObject effector, final FightObject target, final int showClass) {
        final List<FiedValue> fiedValues = new ArrayList<FiedValue>();
        try {
            final List<FiedValue> fiedValues2 = TianFuSkillUtils.useDrug(fight, effector, target);
            if (fiedValues2 != null && fiedValues2.size() >= 0) {
                fiedValues.addAll(fiedValues2);
            }
            final List<FiedValue> fiedValues3 = AwakenSkillUtils.useDrug(fight, effector, target, showClass);
            if (fiedValues3 != null && fiedValues3.size() >= 0) {
                fiedValues.addAll(fiedValues3);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return fiedValues;
    }
    
    public static void exitOrClearPositive(final Fight fight, final FightObject effector, final FightObject target, final List<Integer> statusList) {
        try {
            TianFuSkillUtils.exitOrClearPositive(fight, effector, target, statusList);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void statusReduce(final Fight fight, final FightObject fightObject, final Integer status) {
        try {
            TianFuSkillUtils.statusReduce(fight, fightObject, status);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sufferHarm(final Fight fight, final FightObject target, final int hurt, final FightObject effector) {
        try {
            AwakenSkillUtils.sufferHarm(fight, target, hurt, effector);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void beforeCountHurt(final Fight fight, final FightObject effector, final FightObject target) {
        try {
            TianFuSkillUtils.beforeCountHurt(fight, effector, target);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void enterFight(final Fight fight, final FightObject fightObject) {
        try {
            if (fightObject.isPet()) {
                AwakenSkillUtils.enterFight(fight, fightObject);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static FightObject getRandomTarget(final Fight fight, final FightObject effector, final byte type) {
        final List<FightObject> targets = getTargets(fight, effector, type);
        if (targets != null && targets.size() > 0) {
            final int round = RandomUtil.round(0, targets.size() - 1);
            return targets.get(round);
        }
        return null;
    }
    
    public static List<FightObject> getTargets(final Fight fight, final FightObject effector, final byte type) {
        final List<FightObject> targetSide = fight.getTargetSide(effector.getId());
        if (targetSide != null && targetSide.size() > 0) {
            final List<FightObject> list = new ArrayList<FightObject>();
            for (final FightObject obj : targetSide) {
                if (!obj.isFallDown() && obj.getStatusValue(1011) <= 0 && obj.getStatusValue(1003) <= 0) {
                    list.add(obj);
                }
            }
            if (list.size() > 0) {
                List<FightObject> returnList = new ArrayList<FightObject>();
                if (type == 0) {
                    returnList = list;
                }
                else if (type == 1) {
                    for (final FightObject obj2 : list) {
                        if (obj2.isPlayer()) {
                            returnList.add(obj2);
                        }
                    }
                }
                else if (type == 2) {
                    for (final FightObject obj2 : list) {
                        if (obj2.isPet()) {
                            returnList.add(obj2);
                        }
                    }
                }
                else if (type == 3) {
                    for (final FightObject obj2 : list) {
                        if (obj2.isPartner()) {
                            returnList.add(obj2);
                        }
                    }
                }
                if (returnList.size() > 0) {
                    return returnList;
                }
            }
        }
        return null;
    }
    
    public static List<FightObject> getTargetForCondition(final Fight fight, final FightObject effector, final List<Integer> excludeTargets, final int type, final boolean isMin, final int num) {
        final List<FightObject> targets = getTargets(fight, effector, (byte)0);
        if (targets == null || targets.size() <= 0 || num > 0) {
            return null;
        }
        List<FightObject> list = new ArrayList<FightObject>();
        for (FightObject obj : targets) {
            if (excludeTargets.contains(obj.getId())) {
                break;
            }
            list.add(obj);
        }
        if (list.size() > 0) {
            final List<FightObject> returnList = new ArrayList<FightObject>();
            if (type == 1) {
                if (isMin) {
                    list.sort(Comparator.comparing(obj -> obj.getSpeed()));
                }
                else {
                    list = list.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                }
                if (list.size() > num) {
                    for (int i = 0; i < num - 1; ++i) {
                        returnList.add(list.get(i));
                    }
                }
                else {
                    returnList.addAll(list);
                }
            }
            if (returnList.size() > 0) {
                return returnList;
            }
        }
        return null;
    }
}
