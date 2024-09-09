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
    public SKILL_81060() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        NutMap map = NutMap.NEW();
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 4.0 * ((double)addition.getRei() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(addition.getLv()), Integer.valueOf(addition.getRei()), addition.getExperience())) / 100.0));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist()), Double.valueOf(percent));
        return map;
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
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
