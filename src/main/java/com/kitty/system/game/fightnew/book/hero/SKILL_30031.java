
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
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

public class SKILL_30031 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30031);
        if (skill != null) {
            final Effect e = new Effect();
            if (!target.isFrozen()) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30031);
                final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_MH.getDist());
                final int proficient = skill.getProficient();
                final double[] power = sysSkill.getPower();
                final double Debuff = BigDecimalUtil.decimalScaleObjectDown(2, BigDecimalUtil.decimalScaleObjectDown(2, power[0] * (Math.pow(proficient, 0.35) * 20.0 / 100.0 + 1.0)) * (1.0 + extra));
                final double buff = BigDecimalUtil.decimalScaleObjectDown(3, BigDecimalUtil.decimalScaleObjectDown(3, power[1] * (Math.pow(proficient, 0.35) * 2.0 / 100.0 + 1.0)) * (1.0 + extra));
                final double add = BigDecimalUtil.decimalScaleObjectDown(3, BigDecimalUtil.decimalScaleObjectDown(3, power[2] * (Math.pow(proficient, 0.35) * 2.0 / 100.0 + 1.0)) * (1.0 + extra));
                final int time = this.time(effector, skill);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.K_FY.getDist(), time, -Debuff);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.K_HS.getDist(), time, -Debuff);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.K_HL.getDist(), time, -Debuff);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.K_YW.getDist(), time, -Debuff);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.K_F.getDist(), time, -buff);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.K_L.getDist(), time, -buff);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.K_S.getDist(), time, -buff);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.K_H.getDist(), time, -buff);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1007, PRO_DICT_TYPE.WL_XS.getDist(), time, -add);
                e.setTime(time);
                e.setType(1007);
            }
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(0, 25.0 * (Math.pow(skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0));
    }
    
    public int time(final FightObject effector, final RoleSkill skill) {
        int time = 3;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && skill != null) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.35) * 5.0 / 100.0));
        }
        return time;
    }
}
