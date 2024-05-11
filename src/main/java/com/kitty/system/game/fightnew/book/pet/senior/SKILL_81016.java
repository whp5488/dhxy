
package com.kitty.system.game.fightnew.book.pet.senior;

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

public class SKILL_81016 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 2.7 * (addition.getRei() * 0.5 + 1.0) * PetSkillEffectorUtil.getSuffix(addition.getLv(), addition.getRei(), addition.getExperience())) / 100.0);
        final NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)percent);
        return map;
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
