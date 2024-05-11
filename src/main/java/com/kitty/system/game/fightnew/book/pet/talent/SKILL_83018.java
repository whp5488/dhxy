
package com.kitty.system.game.fightnew.book.pet.talent;

import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83018 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83018);
        final double percent = 0.5;
        final int oldLife = target.getCurrLife();
        final int newLife = Math.min(target.getMaxLife(), oldLife + (int)(target.getMaxLife() * percent));
        final int oldMana = target.getCurrMana();
        final int newMana = Math.min(target.getMaxMana(), oldMana + (int)(target.getMaxMana() * percent));
        final Effect effect = new Effect();
        effect.setType(100000);
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("targetChangeHp", (byte)3, newLife - oldLife));
        list.add(new FiedValue("targetChangeMp", (byte)3, newMana - oldMana));
        effect.setList(list);
        skillRecord.setT(target.getId());
        return effect;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 4300;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(83018), (Object)3);
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(final Fight fight, final FightObject effector) {
        int sum = 1;
        boolean isAddFirstTarget = true;
        final FightObject firstTarget = fight.queryFightObject(effector.getTargetId());
        if (firstTarget == null) {
            sum = 2;
            isAddFirstTarget = false;
        }
        final List<FightObject> mySide = fight.getMySide(effector.getId()).stream().filter(obj -> obj.isFallDown() && !obj.getId().equals(firstTarget.getId())).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).limit(sum).collect(Collectors.toList());
        if (isAddFirstTarget) {
            mySide.add(firstTarget);
        }
        if (mySide != null && mySide.size() > 0) {
            final List<Integer> targetIds = new ArrayList<Integer>();
            for (final FightObject target : mySide) {
                targetIds.add(target.getId());
            }
            return targetIds;
        }
        return null;
    }
}
