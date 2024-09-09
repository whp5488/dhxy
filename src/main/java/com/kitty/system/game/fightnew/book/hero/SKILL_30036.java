package com.kitty.system.game.fightnew.book.hero;

import java.util.Collections;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30036 extends SkillProcessor
{
    public SKILL_30036() {
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
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30036));
        if (skill != null) {
            return (int)(1.2 * (1.0 + Math.pow((double)(int)skill.getProficient(), 0.35) * 5.0 / 100.0));
        }
        return 0;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30036));
        if (skill != null) {
            Effect e = new Effect();
            if (!target.isFrozen()) {
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30036));
                int atk = effector.getPhyPower() + (int)(effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * (double)effector.getPhyPower());
                double conquer = effector.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_RATIO.getDist());
                double conquer_extent = effector.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_EXTENT.getDist());
                double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                double[] power = sysSkill.getPower();
                atk += (int)(power[0] * (double)effector.getLevel());
                double mark = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(1), Double.valueOf(power[1] + power[2] * (1.0 + Math.pow((double)(int)skill.getProficient(), 0.35) * 2.0 / 100.0))) / 100.0));
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.HIT_RATIO.getDist(), mark);
                conquer += mark;
                conquer_extent += mark;
                if (weak < 0.0) {
                    weak = 0.0;
                }
                boolean flag = SkillAi.canAffectOnPro(conquer);
                Integer hurt = SkillCompute.computeDragonSkillDamage(fight, effector, target, atk, conquer_extent, flag, effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_QHS.getDist()), weak);
                e.setList(Collections.singletonList(new FiedValue("penetrate", (byte)3, Boolean.valueOf(flag))));
                e.setHurt((int)hurt);
                e.setType(1024);
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
}
