
package com.kitty.system.game.fightnew.book.pet.supreme;

import java.util.Iterator;
import com.kitty.system.enums.war.wardef.WarMaxRound;
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

public class SKILL_82015 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 82015);
        final double percent = 0.6;
        final int oldLife = target.getCurrLife();
        final int newLife = Math.min(target.getMaxLife(), oldLife + (int)(target.getMaxLife() * percent));
        final int oldMana = target.getCurrMana();
        final int newMana = Math.min(target.getMaxMana(), oldMana + (int)(target.getMaxMana() * percent));
        target.setCurrLife(newLife);
        target.setCurrMana(newMana);
        final boolean down = target.isFallDown();
        boolean isRevive = false;
        if (down) {
            isRevive = true;
        }
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
        return 6300;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(82015), (Object)WarMaxRound.WAR_MAX_ROUND.getValue());
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(final Fight fight, final FightObject effector) {
        final List<FightObject> mySide = fight.getMySide(effector.getId());
        if (mySide != null && mySide.size() > 0) {
            final List<Integer> targetList = new ArrayList<Integer>();
            for (final FightObject target : mySide) {
                targetList.add(target.getId());
            }
            return targetList;
        }
        return null;
    }
}
