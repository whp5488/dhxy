package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Collections;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.mind.MIND;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30021 extends SkillProcessor
{
    public SKILL_30021() {
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
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30021));
        if (skill != null) {
            Effect e = new Effect();
            int harmHp = 0;
            int harmMp = 0;
            if (!target.isFrozen()) {
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30021));
                int currLife = target.getCurrLife();
                int currMana = target.getCurrMana();
                int proficient = (int)skill.getProficient();
                double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_ZS.getDist());
                double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_ZS.getDist());
                double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ZS.getDist());
                double[] power = sysSkill.getPower();
                double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                int additional = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)effector.getLevel() * 3.0 * (1.0 - kx)));
                harmHp = (int)SkillCompute.computeSkillDamage(proficient, power[0], target.getMaxLife(), currLife, kx, hkx, extra, weak, wx) + additional;
                harmMp = (int)SkillCompute.computeSkillDamage(proficient, power[1], target.getMaxMana(), currMana, kx, hkx, extra, weak, wx);
                harmHp -= (int)target.getSelfAllTempPro(MIND.XS_ZS_X.getDist());
                if (harmHp <= 0) {
                    harmHp = 1;
                }
                harmMp -= (int)target.getSelfAllTempPro(MIND.XS_ZS_F.getDist());
                if (harmMp <= 0) {
                    harmMp = 1;
                }
            }
            e.setType(100000);
            e.setList(Collections.singletonList(new FiedValue("mana", (byte)4, String.valueOf(harmMp))));
            e.setHurt(harmHp);
            return e;
        }
        else {
            return null;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(25.0 * (Math.pow((double)(int)skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0) * 1.0));
    }
}
