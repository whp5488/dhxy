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
    public SKILL_30019() {
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
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30019));
        if (skill != null) {
            Effect e = new Effect();
            if (!target.isFrozen()) {
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30019));
                int time = this.time(effector, skill);
                if (target.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                    time = 0;
                }
                else {
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1008, PRO_DICT_TYPE.HIT_RATIO.getDist(), time, sysSkill.getPower()[1]);
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1008, PRO_DICT_TYPE.RISE_ATTACK.getDist(), time, this.imbue((int)skill.getProficient(), sysSkill));
                }
                e.setTime(time);
                e.setType(1008);
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
        int time = 3;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && skill != null) {
            time = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0 * (1.0 + Math.pow((double)(int)skill.getProficient(), 0.35) * 5.0 / 100.0)));
        }
        return time;
    }
    
    public double imbue(int proficient, SysSkill sysSkill) {
        return BigDecimalUtil.decimalScaleDouble(Integer.valueOf(3), Double.valueOf(sysSkill.getPower()[0] * (Math.pow((double)proficient, 0.35) * 3.0 / 100.0 + 1.0)));
    }
}
