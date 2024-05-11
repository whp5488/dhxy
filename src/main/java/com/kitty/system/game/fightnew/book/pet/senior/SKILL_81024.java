
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81024 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81024);
        final SysPetSkill petSkill = SkillDataPool.petSkillMap.get(81024);
        if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, target, false, args)) {
            return;
        }
        if (args.length < 2) {
            return;
        }
        final Integer skillSerial = (Integer)args[1];
        if (skillSerial != null && PetSkillEffectorUtil.isClash(petSkill, skillSerial)) {
            return;
        }
        double percent = 0.3;
        final double pval = AcsryPetSkillUtil.getPval(80023, effector);
        if (pval > 0.0) {
            percent += pval;
        }
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            if (effector.getSkillCoolMap().get((Object)String.valueOf(81024)) != null && (int)effector.getSkillCoolMap().get((Object)String.valueOf(81024)) >= 1) {
                return;
            }
            if (effector.getSkillCoolMap().get((Object)String.valueOf(81002)) != null && (int)effector.getSkillCoolMap().get((Object)String.valueOf(81002)) >= 1) {
                return;
            }
            final FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId(), target.getId());
            if (newTarget == null) {
                return;
            }
            effector.getSkillCoolMap().setv(String.valueOf(81024), (Object)1);
            final RoleSkill skillBy = effector.getSkillBy(81024);
            final BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
            battleUnit.countDoubleHit();
            int doubleHit = 1;
            if (SkillAi.checkDoubleHit(effector)) {
                final int num = BigDecimalUtil.decimalScaleIntegerDown(0, effector.getPropertyMap().get((Object)String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())));
                doubleHit += num;
            }
            final boolean flagZ = SkillAi.checkPetIsSkill(fight, effector.isASide(), 81057);
            if (flagZ) {
                final int num2 = PetPassiveSkillUtil.reduceDoubleHit(fight, effector);
                doubleHit = Math.max(1, doubleHit - num2);
            }
            battleUnit.setDoubleHit(doubleHit);
            PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 81024);
            skillRecord.setT(newTarget.getId());
        }
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
