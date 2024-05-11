
package com.kitty.system.game.fightnew.book.pet.talent;

import java.util.Iterator;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Collection;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import java.util.Random;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83024 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83024);
        int num = effector.getSkillCoolMap().getInt("83024value");
        boolean isExcute = false;
        if (num == -1) {
            final RoleSkill skill = new RoleSkill();
            skill.setType(0);
            skill.setSkillId(83024);
            final BattleUnit battleUnit = new BattleUnit(fight, effector, target, skill);
            final ActionDetailParam actionDetailParam = PetPassiveSkillUtil.passivePhysics(fight, effector, target, battleUnit, null, 83024);
            final RespFightShouting fightShouting = SpringUtils.getBean(FightMessageNewService.class).fightShouting(effector, "法攻");
            final FightObjectParam attackerParam = actionDetailParam.gainAttackerParam();
            ActionParamUtils.updateObjectInfo(effector, 7, 84003, 2, fightShouting, attackerParam);
            if (args.length > 1) {
                List<ActionDetailParam> actionDetailParams = (List<ActionDetailParam>)args[1];
                if (actionDetailParams == null) {
                    actionDetailParams = new ArrayList<ActionDetailParam>();
                }
                actionDetailParams.add(actionDetailParam);
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
                if (targetParam != null && targetParam.isHit()) {
                    ++num;
                    effector.getSkillCoolMap().setv(String.valueOf(83024), (Object)1);
                    effector.getSkillCoolMap().setv("83024value", (Object)num);
                    this.effect(fight, effector, target, false, actionDetailParam);
                }
            }
            skillRecord.setT(target.getId());
            return null;
        }
        if (num == 0) {
            isExcute = true;
        }
        else if (num > 0 && num < 3) {
            final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (156.0 + (effector.getRelive() * 0.5 + 1.0) * (Math.pow(effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0)) / 10.0 / 100.0);
            final boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                isExcute = true;
            }
        }
        if (isExcute) {
            ActionDetailParam actionDetailParam2 = null;
            if (args.length > 1) {
                actionDetailParam2 = (ActionDetailParam)args[1];
            }
            if (actionDetailParam2 == null) {
                return null;
            }
            final double hitRate = BigDecimalUtil.decimalScaleObjectDown(3, (effector.getRelive() * 0.12 + 0.3) * (Math.pow(effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0) / 10.0 / 100.0);
            ImbueUtil.addRound(effector.getImbueMap(), PRO_DICT_TYPE.HIT_RATIO.getDist(), 1, hitRate);
            ImbueUtil.addRound(effector.getImbueMap(), PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), 1, hitRate);
            final Random rd = new Random();
            final boolean isHorizontal = rd.nextBoolean();
            FightObject newTarget = null;
            List<Integer> targetIds = effector.getSkillCoolMap().getList("83024value_targets", (Class)Integer.class);
            final List<FightObject> targetSide = SkillTargetPicker.getFightObjects(fight, effector, true);
            if (targetIds != null && targetIds.size() > 0) {
                final List<FightObject> removeList = new ArrayList<FightObject>();
                for (final Integer targetId : targetIds) {
                    final FightObject historyTarget = fight.queryFightObject(targetId);
                    if (historyTarget != null) {
                        removeList.add(historyTarget);
                    }
                }
                if (removeList.size() > 0) {
                    for (final FightObject fightObject : removeList) {
                        targetSide.remove(fightObject);
                    }
                }
            }
            if (targetSide.size() > 0) {
                newTarget = PetSkillEffectorUtil.horizontalLongitudinalTarget(isHorizontal, fight, effector, targetSide, target);
                if (newTarget == null) {
                    newTarget = PetSkillEffectorUtil.horizontalLongitudinalTarget(!isHorizontal, fight, effector, targetSide, target);
                }
                if (newTarget == null) {
                    return null;
                }
                if (targetIds == null) {
                    targetIds = new ArrayList<Integer>();
                }
                if (num == 0) {
                    targetIds.add(target.getId());
                }
                targetIds.add(newTarget.getId());
                final RoleSkill skillBy = effector.getSkillBy(83024);
                final BattleUnit battleUnit2 = new BattleUnit(fight, effector, newTarget, skillBy);
                final ActionDetailParam actionDetailParamT = PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit2, null, 83024);
                if (actionDetailParamT.getTargetParamList() != null) {
                    for (final FightObjectParam targetParam2 : actionDetailParamT.getTargetParamList()) {
                        targetParam2.addDamageType(105);
                    }
                }
                actionDetailParam2.getTargetParamList().addAll(actionDetailParamT.getTargetParamList());
                skillRecord.setT(newTarget.getId());
                ++num;
                effector.getSkillCoolMap().setv(String.valueOf(83024), (Object)1);
                effector.getSkillCoolMap().setv("83024value", (Object)num);
                effector.getSkillCoolMap().setv("83024value_targets", (Object)targetIds);
                this.effect(fight, effector, target, false, actionDetailParam2);
                return null;
            }
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
