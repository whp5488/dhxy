package com.kitty.system.game.fightnew.book.hero;

import com.kitty.common.utils.BigDecimalUtil;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.itempro.mind.MIND;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30027 extends SkillProcessor
{
    public SKILL_30027() {
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
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30027));
        if (skill != null) {
            Effect e = new Effect();
            int harm = 0;
            if (!target.isFrozen()) {
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30027));
                ArrayList<FightObject> objects = fight.getSideAllFightObjects(effector.isASide());
                int die = 0;
                for (FightObject object : objects) {
                    if (object.isFallDown()) {
                        ++die;
                    }
                    else {
                        continue;
                    }
                }
                double[] power = sysSkill.getPower();
                int proficient = (int)skill.getProficient();
                double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_GH.getDist());
                double wsk = PassiveSkillUtils.getWsk(effector);
                kx -= wsk;
                double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_GH.getDist());
                double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_GH.getDist());
                double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                harm = (int)SkillCompute.computeSkillDamageWildfire(effector, power, effector.getLevel(), proficient, kx, hkx, extra, weak, die, wx);
                harm -= (int)target.getSelfAllTempPro(MIND.XS_GH.getDist());
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
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(25.0 * (Math.pow((double)(int)skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0)));
    }
}
