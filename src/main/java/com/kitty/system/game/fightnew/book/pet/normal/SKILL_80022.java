
package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_80022 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        final SysPetSkill coverPetSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(addition.getSkills(), PET_PASSIVE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue(), 80022);
        final NutMap map = NutMap.NEW();
        if (coverPetSkill != null) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), 0);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), 0);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), 0);
        }
        else {
            final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 2.0 * (addition.getRei() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(addition.getLv(), addition.getRei(), addition.getExperience())) / 100.0);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)percent);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)percent);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)percent);
        }
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
