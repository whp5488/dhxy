
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30019 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30019);
        if (skill != null) {
            final Effect e = new Effect();
            if (!target.isFrozen()) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30019);
                int time = this.time(effector, skill);
                if (target.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                    time = 0;
                }
                else {
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1008, PRO_DICT_TYPE.HIT_RATIO.getDist(), time, sysSkill.getPower()[1]);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1008, PRO_DICT_TYPE.RISE_ATTACK.getDist(), time, this.imbue(skill.getProficient(), sysSkill));
                }
                e.setTime(time);
                e.setType(1008);
            }
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(0, 25.0 * (Math.pow(skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0) * 1.0);
    }
    
    public int time(final FightObject effector, final RoleSkill skill) {
        int time = 3;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && skill != null) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.35) * 5.0 / 100.0));
        }
        return time;
    }
    
    public double imbue(final int proficient, final SysSkill sysSkill) {
        return BigDecimalUtil.decimalScaleDouble(3, sysSkill.getPower()[0] * (Math.pow(proficient, 0.35) * 3.0 / 100.0 + 1.0));
    }
}
