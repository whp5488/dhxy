package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30037 extends SkillProcessor
{
    public SKILL_30037() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30037));
        if (skill != null) {
            Effect e = new Effect();
            SysSkill skill2 = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30037));
            if (!target.isFrozen()) {
                int maxLife = target.getMaxLife();
                double[] power = skill2.getPower();
                int baseLife = (int)power[0] + (int)skill.getProficient() * 14 / 5;
                double addPct = power[1] + power[2] * (double)(int)skill.getProficient() / 25000.0;
                double propertyAdd = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist()), 0.0);
                if (propertyAdd != 0.0) {
                    addPct += propertyAdd;
                }
                int perLife = (int)(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(addPct)) * (double)maxLife);
                int time = this.time(effector, skill);
                target.getStatusSubMap().setv(String.valueOf(1022) + "value", Integer.valueOf(baseLife + perLife));
                e.setTime(time);
                e.setType(1022);
                return e;
            }
            else {
                return e;
            }
        }
        else {
            return null;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(25.0 * (Math.pow((double)(int)skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0) * 1.0));
    }
    
    public int time(FightObject effector, RoleSkill skill) {
        int time = 2;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && skill != null) {
            time += BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(Math.pow((double)(int)skill.getProficient(), 0.35) * 5.0 / 100.0));
        }
        return time;
    }
}
