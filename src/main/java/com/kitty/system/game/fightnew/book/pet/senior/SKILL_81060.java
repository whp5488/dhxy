
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

public class SKILL_81060 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        final NutMap map = NutMap.NEW();
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 4.0 * (addition.getRei() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(addition.getLv(), addition.getRei(), addition.getExperience())) / 100.0);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist()), (Object)percent);
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
