package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
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

public class SKILL_30024 extends SkillProcessor
{
    public SKILL_30024() {
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
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30024));
        if (skill != null && skill != null) {
            return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0 * (1.0 + Math.pow((double)(int)skill.getProficient(), 0.3) * 8.0 / 100.0))));
        }
        return 3;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30024));
        if (skill != null) {
            Effect e = new Effect();
            if (!target.isFrozen()) {
                int time = this.time(effector, skill);
                if (target.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                    time = 0;
                }
                else {
                    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30024));
                    int proficient = (int)skill.getProficient();
                    double[] power = sysSkill.getPower();
                    double up = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(power[0] * (Math.pow((double)proficient, 0.35) * 20.0 / 100.0 + 1.0)));
                    double buff = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[1] * (Math.pow((double)proficient, 0.35) * 2.0 / 100.0 + 1.0)));
                    double add = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[2] * (Math.pow((double)proficient, 0.35) * 2.0 / 100.0 + 1.0)));
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_FY.getDist(), time, up);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_HS.getDist(), time, up);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_HL.getDist(), time, up);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_YW.getDist(), time, up);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_F.getDist(), time, buff);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_L.getDist(), time, buff);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_S.getDist(), time, buff);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_H.getDist(), time, buff);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_SS.getDist(), time, buff);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_GH.getDist(), time, buff);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_DU.getDist(), time, buff);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.WL_XS.getDist(), time, add);
                }
                e.setTime(time);
                e.setType(1009);
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
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(75.0 * (Math.pow((double)(int)skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0) * 1.0));
    }
    
    public int time(FightObject effector, RoleSkill skill) {
        int time = 3;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && skill != null) {
            time = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0 * (1.0 + Math.pow((double)(int)skill.getProficient(), 0.35) * 5.0 / 100.0)));
        }
        return time;
    }
}
