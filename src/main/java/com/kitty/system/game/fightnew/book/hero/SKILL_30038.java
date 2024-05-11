
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30038 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30038);
        if (skill != null) {
            final SysSkill skill2 = SkillDataPool.skillMap.get(30038);
            if (skill != null && SkillAi.canAffectOnPro(skill2.getHitRatio())) {
                return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.3) * 8.0 / 100.0)));
            }
        }
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(30038);
        if (skill != null) {
            final Effect e = new Effect();
            final SysSkill skill2 = SkillDataPool.skillMap.get(30038);
            if (!target.isFrozen()) {
                final int maxLife = target.getMaxLife();
                final double[] power = skill2.getPower();
                final int baseLife = (int)(power[0] + skill.getProficient() * 2.5);
                double addPct = power[1] + power[2] * skill.getProficient() / 25000.0;
                final double propertyAdd = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist()), 0.0);
                if (propertyAdd != 0.0) {
                    addPct += propertyAdd;
                }
                final int perLife = (int)(BigDecimalUtil.decimalScaleObjectDown(3, addPct) * maxLife);
                final int time = this.time(effector, skill);
                target.getStatusSubMap().setv(String.valueOf(1022) + "value", (Object)(baseLife + perLife));
                e.setTime(time);
                e.setType(1022);
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
        int time = 2;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && skill != null) {
            time += BigDecimalUtil.decimalScaleIntegerDown(0, Math.pow(skill.getProficient(), 0.35) * 5.0 / 100.0);
        }
        return time;
    }
}
