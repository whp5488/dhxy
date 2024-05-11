
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Arrays;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83001 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83001);
        final int round = 2;
        if (args.length > 0 && args[0] instanceof Integer) {
            double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 4.0 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            percent += AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31304, target);
            ImbueUtil.addRound(target.getImbueMap(), PRO_DICT_TYPE.RISE_SS.getDist(), round, -percent);
            ImbueUtil.addRound(target.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), round, -percent);
            final int hurt = (int)args[0];
            final int addHurt = (int)(hurt * percent);
            final Effect effect = new Effect(1012, round, hurt + addHurt, 0.0, Arrays.asList(new FiedValue[0]));
            effect.setTime(round);
            skillRecord.setT(target.getId());
            return effect;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
