
package com.kitty.system.game.fightnew.book.pet.normal;

import java.util.Arrays;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import java.util.List;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_80013 extends SkillProcessor
{
    static final List<String> terms;
    
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 80013);
        final int max2Mana = effector.getMaxMana();
        int count = 1;
        final boolean trigger = AcsryPetSkillUtil.isTrigger(80013, effector);
        if (trigger) {
            ++count;
        }
        for (int i = 0; i < count; ++i) {
            final FightObject unit = SkillAi.effectProBass(fight, effector, 2, SKILL_80013.terms);
            if (unit != null) {
                this.excute(fight, effector, unit, max2Mana);
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
    
    private void excute(final Fight fight, final FightObject effector, final FightObject unit, final int max2Mana) {
        if (unit != null) {
            final int mana = Math.min(unit.getMaxMana(), unit.getCurrMana() + max2Mana);
            final int addMana = mana - unit.getCurrMana();
            ActionParamUtils.changeMana(fight, effector, unit, 7, 80013, 2, addMana);
        }
    }
    
    static {
        terms = Arrays.asList(PET_SKILL_TERM_DICT.PRO_BASS_PET_N.getValue(), PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue());
    }
}
