
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83010 extends SkillProcessor
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
        final List<FightObject> targetSide = fight.getTargetSide(effector.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        if (targetSide.size() < 2) {
            return targetSide.size();
        }
        return 2;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final Effect e = new Effect();
        final RoleSkill skill = effector.getSkillBy(83010);
        if (skill != null) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83010);
            int level = effector.getLevel();
            if (level <= 0) {
                level = 1;
            }
            final double hurt = BigDecimalUtil.decimalScaleObjectDown(0, 80 * level + effector.getMaxMana() / 100 * 6 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose()));
            final int harm = PetSkillEffectorUtil.windCount(fight, effector, target, hurt);
            e.setType(100000);
            e.setHurt(harm);
            skillRecord.setT(target.getId());
        }
        return e;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        final int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 32;
    }
}
