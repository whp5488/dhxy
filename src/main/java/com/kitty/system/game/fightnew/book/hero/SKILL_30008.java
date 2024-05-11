
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
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

public class SKILL_30008 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30008);
        if (skill != null && skill != null) {
            return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.35) * 5.0 / 100.0)));
        }
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(30008);
        if (skill != null) {
            final Effect e = new Effect();
            if (!target.isFrozen()) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30008);
                final double power = sysSkill.getPower()[0];
                final int proficient = skill.getProficient();
                final int maxLife = target.getMaxLife();
                final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                double kdx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_DU.getDist());
                final double wsk = PassiveSkillUtils.getWsk(effector);
                kdx -= wsk;
                final double dqd = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_D.getDist());
                final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_D.getDist());
                final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                final double hurt = SkillCompute.computeSkillDamagePoison(power, kdx, dqd, proficient, 1, effector.getLevel(), maxLife, extra, weak, wx);
                final int time = this.time(effector, skill, kdx, dqd);
                target.getStatusSubMap().setv("1005_power", (Object)power);
                target.getStatusSubMap().setv("1005_kdx", (Object)kdx);
                target.getStatusSubMap().setv("1005_weak", (Object)weak);
                target.getStatusSubMap().setv("1005_dqd", (Object)dqd);
                target.getStatusSubMap().setv("1005_proficient", (Object)proficient);
                target.getStatusSubMap().setv("1005_round", (Object)1);
                target.getStatusSubMap().setv("1005_level", (Object)effector.getLevel());
                target.getStatusSubMap().setv("1005_maxLife", (Object)maxLife);
                target.getStatusSubMap().setv("1005_extra", (Object)extra);
                target.getStatusSubMap().setv("1005_wx", (Object)wx);
                e.setHurt(Math.max(1, (int)hurt));
                e.setTime(time);
                e.setType(1005);
            }
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(0, 75.0 * (Math.pow(skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0));
    }
    
    public int time(final FightObject effector, final RoleSkill skill, final double kdx, final double dqd) {
        int time = 2;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && skill != null) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 2.0 * (1.0 + Math.pow(skill.getProficient(), 0.34) * 4.0 / 100.0));
        }
        return Math.min(time, BigDecimalUtil.decimalScaleInteger(0, time * (1.0 - kdx / 2.0 + dqd)));
    }
}
