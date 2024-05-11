
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83008 extends SkillProcessor
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
        final int time = 3;
        final RoleSkill skill = effector.getSkillBy(83008);
        final Effect e = new Effect();
        if (skill != null) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83008);
            if (!target.isFrozen()) {
                final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 5.0 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
                final int speed = target.getSpeed();
                final int result = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(speed), String.valueOf(percent)));
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1016, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, -result);
                e.setTime(time);
                e.setType(1016);
            }
            skillRecord.setT(target.getId());
        }
        return e;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        final int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 22;
    }
}
