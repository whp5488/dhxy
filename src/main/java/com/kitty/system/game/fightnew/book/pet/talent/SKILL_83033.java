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
    public SKILL_83033() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        if (args.length > 0 && args[0] instanceof SysPetSkill) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83033);
            int mana = Math.min(effector.getCurrMana() + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)effector.getMaxMana() * 0.8)), effector.getMaxMana());
            int change = mana - effector.getCurrMana();
            if (change <= 0) {
                return null;
            }
            ActionParamUtils.changeMana(fight, effector, effector, 7, 83033, 2, change);
            skillRecord.setT((long)(int)effector.getId());
        }
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
    
    @Override
    public List<Integer> getTargetIdsForTFQX(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83033);
        List<FightObject> targets = SkillTargetPicker.getFightObjects(fight, effector, true);
        if (targets != null && targets.size() > 0) {
            Iterator<FightObject> iterator = targets.iterator();
            while (iterator.hasNext()) {
            	FightObject obj = (FightObject)iterator.next();
                if (obj.getId().equals(Integer.valueOf(effector.getTargetId()))) {
                    targets.remove(obj);
                    break;
                }
                continue;
            }
            if (targets.size() > 0) {
                int length = 2;
                targets.sort(Comparator.comparing(obj -> Integer.valueOf(obj.getSpeed())));
                List<Integer> list = new ArrayList<>();
                for (FightObject target : targets) {
                    list.add(target.getId());
                    if (--length <= 0) {
                        break;
                    }
                    continue;
                }
                if (list.size() > 0) {
                    skillRecord.setT((long)(int)list.get(0));
                    return list;
                }
            }
        }
        return null;
    }
}
