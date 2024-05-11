
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
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

public class SKILL_30010 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30010);
        if (skill != null && skill != null) {
            return Math.min(5, BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.3) * 5.0 / 100.0)));
        }
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(30010);
        if (skill != null) {
            final Effect e = new Effect();
            int harm = 0;
            if (!target.isFrozen()) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30010);
                final int proficient = skill.getProficient();
                double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_F.getDist());
                final double wsk = PassiveSkillUtils.getWsk(effector);
                kx -= wsk;
                final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_F.getDist());
                final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_F.getDist());
                double hurt = sysSkill.getPower()[0] * effector.getLevel() * (Math.pow(proficient, 0.4) * 2.8853998118144273 / 100.0 + 1.0);
                final int chageBaseHurt = AcsrySkillUtil.getChageBaseHurt(effector);
                hurt += chageBaseHurt;
                final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                harm = SkillCompute.computeSkillDamage(hurt, kx, hkx, extra, weak, wx);
            }
            e.setType(100000);
            e.setHurt(harm);
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(0, 75.0 * (Math.pow(skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0));
    }
}
