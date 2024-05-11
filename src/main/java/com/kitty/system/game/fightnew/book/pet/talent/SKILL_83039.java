
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83039 extends SkillProcessor
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
    public void normalAttack(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (target.isFallDown() || !effector.isPet()) {
            return;
        }
        final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
        if (targetParam == null || !targetParam.isHit()) {
            return;
        }
        final int skillCool = effector.getSkillCoolMap().getInt(String.valueOf(83039));
        if (skillCool > 0) {
            return;
        }
        final boolean flag = SkillAi.canAffectOnPro(this.getPercent(effector));
        if (flag) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83039);
            effector.getSkillCoolMap().setv(String.valueOf(83039), 0);
            effector.setType(0);
            effector.setSkillId(0);
            final RoleSkill skill = new RoleSkill();
            skill.setType(0);
            skill.setSkillId(83039);
            AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31427, target);
            final BattleUnit battleUnit = new BattleUnit(fight, effector, target, skill);
            battleUnit.countDoubleHit();
            PetPassiveSkillUtil.passivePhysics(fight, effector, target, battleUnit, actionDetailParam, 83039);
            skillRecord.setT(target.getId());
        }
    }
    
    private double getPercent(final FightObject effector) {
        final double param1 = 4.0 * (effector.getRelive() * 0.8 + 1.0);
        final double param2 = Math.pow(effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0 / (100 + effector.getRelive() * 20);
        return Math.min(50.0, 5.0 + param1 * param2) / 100.0;
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
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
