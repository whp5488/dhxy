
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.common.utils.BigDecimalUtil;
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

public class SKILL_81032 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81032);
        final SysPetSkill petSkill = SkillDataPool.petSkillMap.get(81032);
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
        double percent = 0.35;
        final double pval = AcsryPetSkillUtil.getPval(80011, effector);
        if (pval > 0.0) {
            percent += pval;
        }
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            final Object oldHurt = args[0];
            final Integer hurt = Integer.parseInt(oldHurt.toString());
            final Effect effect = new Effect();
            effect.setHurt(BigDecimalUtil.decimalScaleIntegerDown(0, hurt * 0.5));
            final FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId(), target.getId());
            if (newTarget == null) {
                return;
            }
            final RoleSkill skillBy = effector.getSkillBy(81032);
            final BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
            battleUnit.setEffect(effect);
            PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 81032);
            skillRecord.setT(newTarget.getId());
        }
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
