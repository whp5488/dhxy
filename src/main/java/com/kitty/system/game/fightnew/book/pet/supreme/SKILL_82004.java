
package com.kitty.system.game.fightnew.book.pet.supreme;

import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_82004 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 82004);
        final int time = 3;
        final Effect effect = new Effect();
        effect.setType(1011);
        effect.setTime(time);
        skillRecord.setT(target.getId());
        return effect;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 2300;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(82004), 5);
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(final Fight fight, final FightObject effector) {
        final List<Integer> targetIds = new ArrayList<Integer>();
        if (effector.getId().equals(effector.getTargetId())) {
            final FightObject host = fight.queryFightObject(effector.getHostId());
            if (host != null && !host.isFallDown() && !host.isFrozen()) {
                targetIds.add(host.getId());
            }
            else {
                final List<FightObject> mySide = fight.getMySide(effector.getId()).stream().filter(obj -> !obj.getId().equals(effector.getId()) && !obj.isFallDown() && !obj.isFrozen()).collect(Collectors.toList());
                if (mySide != null && mySide.size() > 0) {
                    final Random random = new Random();
                    final int i = random.nextInt(mySide.size());
                    final FightObject fightObject = mySide.get(i);
                    if (fightObject != null && !fightObject.isFallDown()) {
                        targetIds.add(fightObject.getId());
                    }
                }
            }
        }
        else {
            final FightObject target = fight.queryFightObject(effector.getTargetId());
            if (target != null && !target.isFallDown() && !target.isFrozen()) {
                targetIds.add(target.getId());
            }
        }
        targetIds.add(effector.getId());
        if (targetIds != null && targetIds.size() > 0) {
            return targetIds;
        }
        return null;
    }
}
