
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81002 extends SkillProcessor
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
        return null;
    }
    
    @Override
    public void normalAttack(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (target == null || !target.isFallDown()) {
            return;
        }
        final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), true);
        if (!targetParam.isHit()) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81002);
        int num = effector.getSkillCoolMap().getInt("81002value");
        if (num < 2) {
            double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 7.0 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            if (num >= 0) {
                percent = 1.0;
            }
            else {
                final double pval = AcsryPetSkillUtil.getPval(81002, effector);
                percent += pval;
            }
            final boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                if (effector.getSkillCoolMap().getInt(String.valueOf(81002)) > 0 && effector.getSkillCoolMap().getInt("81002value") >= 2) {
                    return;
                }
                effector.getSkillCoolMap().setv(String.valueOf(81002), (Object)1);
                ++num;
                effector.getSkillCoolMap().setv("81002value", (Object)num);
                final FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId(), target.getId());
                if (newTarget == null) {
                    return;
                }
                final RoleSkill skillBy = effector.getSkillBy(81002);
                final BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
                battleUnit.countDoubleHit();
                int doubleHit = 1;
                if (SkillAi.checkDoubleHit(effector)) {
                    final int numZ = BigDecimalUtil.decimalScaleIntegerDown(0, effector.getPropertyMap().get((Object)String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())));
                    doubleHit += numZ;
                }
                final boolean flagZ = SkillAi.checkPetIsSkill(fight, effector.isASide(), 81057);
                if (flagZ) {
                    final int reduceNum = PetPassiveSkillUtil.reduceDoubleHit(fight, effector);
                    doubleHit = Math.max(1, doubleHit - reduceNum);
                }
                battleUnit.setDoubleHit(doubleHit);
                PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 81002);
                skillRecord.setT(newTarget.getId());
                if (newTarget.isFallDown()) {
                    this.normalAttack(fight, effector, newTarget, actionDetailParam, new Object[0]);
                }
            }
        }
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
