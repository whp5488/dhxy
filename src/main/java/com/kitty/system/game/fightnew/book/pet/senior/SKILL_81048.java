
package com.kitty.system.game.fightnew.book.pet.senior;

import java.util.Arrays;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.BigDecimalUtil;
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

public class SKILL_81048 extends SkillProcessor
{
    static final List<String> terms;
    
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81048);
        int count = 1;
        final boolean trigger = AcsryPetSkillUtil.isTrigger(80002, effector);
        if (trigger) {
            ++count;
        }
        for (int i = 0; i < count; ++i) {
            final FightObject unit = SkillAi.effectProBass(fight, effector, 2, SKILL_81048.terms);
            if (unit != null) {
                this.excute(fight, effector, unit);
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
    
    private void excute(final Fight fight, final FightObject effector, final FightObject unit) {
        if (unit != null) {
            final int mana = unit.getMaxMana();
            final int life = Math.min(unit.getCurrLife() + BigDecimalUtil.decimalScaleIntegerDown(0, unit.getMaxLife() * 0.2), unit.getMaxLife());
            final int oldLife = unit.getCurrLife();
            final int oldMana = unit.getCurrMana();
            ActionParamUtils.changeHpMp(fight, effector, unit, 7, 81048, 2, life - oldLife, mana - oldMana, false);
        }
    }
    
    static {
        terms = Arrays.asList(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue());
    }
}
