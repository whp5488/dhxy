
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.skill.domain.RoleSkill;
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
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30035 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30035);
        if (skill != null) {
            final Effect e = new Effect();
            if (!target.isFrozen()) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30035);
                int atk = effector.getPhyPower() + (int)(effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * effector.getPhyPower());
                double conquer = effector.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_RATIO.getDist());
                double conquer_extent = effector.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_EXTENT.getDist());
                double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                final double[] power = sysSkill.getPower();
                atk += (int)(power[0] * effector.getLevel());
                final double mark = BigDecimalUtil.decimalScaleObjectDown(3, BigDecimalUtil.decimalScaleObjectDown(1, power[1] + power[2] * (1.0 + Math.pow(skill.getProficient(), 0.35) * 2.0 / 100.0)) / 100.0);
                ImbueUtil.addRound(effector.getImbueMap(), PRO_DICT_TYPE.HIT_RATIO.getDist(), 1, mark);
                conquer += mark;
                conquer_extent += mark;
                if (weak < 0.0) {
                    weak = 0.0;
                }
                final boolean flag = SkillAi.canAffectOnPro(conquer);
                final Integer hurt = SkillCompute.computeDragonSkillDamage(fight, effector, target, atk, conquer_extent, flag, effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_QHS.getDist()), weak);
                e.setList(Collections.singletonList(new FiedValue("penetrate", (byte)3, flag)));
                e.setHurt(hurt);
                e.setType(1024);
            }
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(0, 25.0 * (Math.pow(skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0) * 1.0);
    }
}
