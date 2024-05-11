
package com.kitty.system.game.fightnew.book.pet.talent;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Comparator;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import java.util.List;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83033 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        if (args.length > 0 && args[0] instanceof SysPetSkill) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83033);
            final int mana = Math.min(effector.getCurrMana() + BigDecimalUtil.decimalScaleIntegerDown(0, effector.getMaxMana() * 0.8), effector.getMaxMana());
            final int change = mana - effector.getCurrMana();
            if (change <= 0) {
                return null;
            }
            ActionParamUtils.changeMana(fight, effector, effector, 7, 83033, 2, change);
            skillRecord.setT(effector.getId());
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
    
    @Override
    public List<Integer> getTargetIdsForTFQX(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83033);
        final List<FightObject> targets = SkillTargetPicker.getFightObjects(fight, effector, true);
        if (targets != null && targets.size() > 0) {
            final Iterator<FightObject> iterator = targets.iterator();
            while (iterator.hasNext()) {
                FightObject obj = iterator.next();
                if (obj.getId().equals(effector.getTargetId())) {
                    targets.remove(obj);
                    break;
                }
            }
            if (targets.size() > 0) {
                int length = 2;
                targets.sort(Comparator.comparing(obj -> obj.getSpeed()));
                final List<Integer> list = new ArrayList<Integer>();
                for (final FightObject target : targets) {
                    list.add(target.getId());
                    if (--length <= 0) {
                        break;
                    }
                }
                if (list.size() > 0) {
                    skillRecord.setT(list.get(0));
                    return list;
                }
            }
        }
        return null;
    }
}
