
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83007 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        final double percent = BigDecimalUtil.decimalScaleDouble(2, (0.1 + 4.0 * (addition.getRei() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(addition.getLv(), addition.getRei(), addition.getExperience())) / 100.0);
        double hitRatio = addition.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()));
        if (hitRatio > 0.0) {
            hitRatio += percent;
        }
        else {
            hitRatio = percent;
        }
        addition.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), (Object)hitRatio);
        double rageRatio = addition.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()));
        if (rageRatio > 0.0) {
            rageRatio += percent;
        }
        else {
            rageRatio = percent;
        }
        addition.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), (Object)rageRatio);
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
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
