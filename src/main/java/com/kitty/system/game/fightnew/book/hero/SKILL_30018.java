
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30018 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30018);
        if (skill != null && skill != null) {
            return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.3) * 8.0 / 100.0)));
        }
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(30018);
        final int speed = target.getSpeed();
        if (skill != null) {
            final Effect e = new Effect();
            if (!target.isFrozen()) {
                int time = this.time(effector, skill);
                if (target.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                    time = 0;
                }
                else {
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, (speed < 0) ? 0.0 : ((double)(int)(this.imbue(skill.getProficient()) * target.getSpeed())));
                }
                e.setTime(time);
                e.setType(1010);
            }
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(0, 75.0 * (Math.pow(skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0) * 1.0);
    }
    
    public int time(final FightObject effector, final RoleSkill skill) {
        int time = 3;
        if (skill != null) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.35) * 5.0 / 100.0));
        }
        return time;
    }
    
    public double imbue(final int proficient) {
        final SysSkill sysSkill = SkillDataPool.skillMap.get(30018);
        return BigDecimalUtil.decimalScaleDouble(2, (sysSkill.getPower()[0] + 8 * proficient / 25000.0) / 100.0);
    }
}
