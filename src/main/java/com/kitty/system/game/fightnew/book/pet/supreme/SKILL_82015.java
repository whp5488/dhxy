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
    public SKILL_82015() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 82015);
        double percent = 0.6;
        int oldLife = target.getCurrLife();
        int newLife = Math.min(target.getMaxLife(), oldLife + (int)((double)target.getMaxLife() * percent));
        int oldMana = target.getCurrMana();
        int newMana = Math.min(target.getMaxMana(), oldMana + (int)((double)target.getMaxMana() * percent));
        target.setCurrLife(newLife);
        target.setCurrMana(newMana);
        boolean down = target.isFallDown();
        boolean isRevive = false;
        if (down) {
            isRevive = true;
        }
        Effect effect = new Effect();
        effect.setType(100000);
        List<FiedValue> list = new ArrayList<>();
        list.add(new FiedValue("targetChangeHp", (byte)3, Integer.valueOf(newLife - oldLife)));
        list.add(new FiedValue("targetChangeMp", (byte)3, Integer.valueOf(newMana - oldMana)));
        effect.setList(list);
        skillRecord.setT((long)(int)target.getId());
        return effect;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 6300;
    }
    
    @Override
    public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(82015), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(Fight fight, FightObject effector) {
        List<FightObject> mySide = fight.getMySide((int)effector.getId());
        if (mySide != null && mySide.size() > 0) {
            List<Integer> targetList = new ArrayList<>();
            for (FightObject target : mySide) {
                targetList.add(target.getId());
            }
            return targetList;
        }
        return null;
    }
}
