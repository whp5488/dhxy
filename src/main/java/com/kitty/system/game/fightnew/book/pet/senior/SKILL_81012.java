
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81012 extends SkillProcessor
{
    private int reduce;
    
    public SKILL_81012() {
        this.reduce = 0;
    }
    
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81012);
        final double percent = BigDecimalUtil.decimalScaleObjectDown(8, (22.1 + 4.0 * (effector.getRelive() * 0.5 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        int lastHurt = 0;
        final int hurt = (int)(this.reduce * percent);
        if (!target.isFrozen()) {
            lastHurt = PetSkillEffectorUtil.computeTMFGQMXLHurt(effector, target, hurt, 81012);
            final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            lastHurt += (int)(lastHurt * weak);
            skillRecord.setT(target.getId());
        }
        final Effect effect = new Effect(100000, 0, lastHurt, -1.0, new ArrayList<FiedValue>());
        final List<FiedValue> list = effect.getOrCreateList();
        return effect;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        if (args != null && args.length > 0) {
            final Object arg = args[0];
            try {
                final FightObject effector = (FightObject)arg;
                if (effector != null && effector.getCurrMana() >= 10000) {
                    final int hp = BigDecimalUtil.decimalScaleIntegerDown(0, effector.getCurrMana() * 0.95);
                    return this.reduce = hp;
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return 10000;
    }
    
    @Override
    public List<Integer> getTargetIds(final Fight fight, final FightObject effector) {
        final List<Integer> list = PetTriggerPassiveSkill.getTMTargetIds(fight, effector, 81012);
        return list;
    }
    
    @Override
    public void changeVariate(final Object... args) {
        if (args != null && args.length > 0) {
            final Object arg = args[0];
            try {
                final int value = Integer.parseInt(String.valueOf(arg));
                this.reduce = value;
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
