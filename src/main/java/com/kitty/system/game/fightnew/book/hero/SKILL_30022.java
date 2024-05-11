
package com.kitty.system.game.fightnew.book.hero;

import java.util.Collections;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30022 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30022);
        if (skill != null && skill != null) {
            return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.35) * 5.0 / 100.0)));
        }
        return 3;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(30022);
        if (skill != null) {
            final Effect e = new Effect();
            int harmHp = 0;
            int harmMp = 0;
            if (!target.isFrozen()) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30022);
                final int currLife = target.getCurrLife();
                final int currMana = target.getCurrMana();
                final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                final int proficient = skill.getProficient();
                final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_ZS.getDist());
                final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_ZS.getDist());
                final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ZS.getDist());
                final double[] power = sysSkill.getPower();
                final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                final int additional = BigDecimalUtil.decimalScaleIntegerDown(0, effector.getLevel() * 3.0 * (1.0 - kx));
                harmHp = SkillCompute.computeSkillDamage(proficient, power[0], target.getMaxLife(), currLife, kx, hkx, extra, weak, wx) + additional;
                harmMp = SkillCompute.computeSkillDamage(proficient, power[1], target.getMaxMana(), currMana, kx, hkx, extra, weak, wx);
            }
            e.setType(100000);
            if (harmMp > 0) {
                e.setList(Collections.singletonList(new FiedValue("mana", (byte)4, String.valueOf(harmMp))));
            }
            e.setHurt(harmHp);
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(0, 75.0 * (Math.pow(skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0) * 1.0);
    }
}
