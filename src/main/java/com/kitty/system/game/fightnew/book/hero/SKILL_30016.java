package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.enums.war.itempro.mind.MIND;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
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

public class SKILL_30016 extends SkillProcessor
{
    public SKILL_30016() {
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
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30016));
        if (skill != null && skill != null) {
            return Math.min(5, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0 * (1.0 + Math.pow((double)(int)skill.getProficient(), 0.3) * 5.0 / 100.0))));
        }
        return 0;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30016));
        if (skill != null) {
            Effect e = new Effect();
            int harm = 0;
            if (!target.isFrozen()) {
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30016));
                int proficient = (int)skill.getProficient();
                double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_H.getDist());
                double wsk = PassiveSkillUtils.getWsk(effector);
                kx -= wsk;
                double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_H.getDist());
                double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_H.getDist());
                double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                double hurt = sysSkill.getPower()[0] * (double)effector.getLevel() * (Math.pow((double)proficient, 0.4) * 2.8853998118144273 / 100.0 + 1.0);
                int chageBaseHurt = AcsrySkillUtil.getChageBaseHurt(effector);
                hurt += (double)chageBaseHurt;
                double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                harm = (int)SkillCompute.computeSkillDamage(hurt, kx, hkx, extra, weak, wx);
                harm -= (int)target.getSelfAllTempPro(MIND.XS_H.getDist());
                if (harm <= 0) {
                    harm = 1;
                }
            }
            e.setType(100000);
            e.setHurt(harm);
            return e;
        }
        else {
            return null;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(75.0 * (Math.pow((double)(int)skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0)));
    }
}
