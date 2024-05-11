
package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
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

public class SKILL_80027 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 80027);
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 2.0 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        final int hurt = (int)args[0];
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            final FightObject master = fight.queryFightObject(effector.getHostId());
            if (master.isFallDown()) {
                return;
            }
            final int addBlood = (int)((hurt * 0.15 >= 1.0) ? (hurt * 0.15) : 1.0);
            final int mana = Math.min(master.getCurrMana() + addBlood, master.getMaxMana());
            final int addMana = mana - master.getCurrMana();
            final FightObjectParam newTargetParam = ActionParamUtils.changeManaForTarget(fight, master, 1, addMana, 2);
            newTargetParam.addDamageType(2051);
            actionDetailParam.getTargetParamList().add(newTargetParam);
            skillRecord.setT(master.getId());
        }
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
