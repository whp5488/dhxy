
package com.kitty.system.game.fightnew.book.pet.normal;

import java.util.Arrays;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import java.util.List;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_80010 extends SkillProcessor
{
    static final List<String> terms;
    
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 80010);
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 2.0 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            final FightObject unit = SkillAi.effectProBass(fight, effector, 2, SKILL_80010.terms, false);
            if (unit != null) {
                final double probability = BigDecimalUtil.decimalScaleObjectDown(3, unit.getCurrMana() / (double)unit.getMaxMana());
                if (probability >= 0.3) {
                    return null;
                }
                int addMana = (int)(unit.getMaxMana() * 0.5);
                if (addMana + unit.getCurrMana() > unit.getMaxMana()) {
                    addMana = unit.getMaxMana() - unit.getCurrMana();
                }
                ActionParamUtils.changeMana(fight, effector, unit, 7, 80010, 2, addMana);
                skillRecord.setT(unit.getId());
            }
        }
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
    
    static {
        terms = Arrays.asList(PET_SKILL_TERM_DICT.PRO_BASS_PET_N.getValue(), PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue());
    }
}
