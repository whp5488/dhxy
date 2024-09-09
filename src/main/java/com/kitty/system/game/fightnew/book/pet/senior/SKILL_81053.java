package com.kitty.system.game.fightnew.book.pet.senior;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81053 extends SkillProcessor
{
    public SKILL_81053() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81053);
        List<FiedValue> list = PetPassiveSkillUtil.clearStatus(target, "wuXinModification");
        Effect effect = new Effect();
        effect.setType(100000);
        if (list != null && list.size() > 0) {
            effect.setList(list);
        }
        skillRecord.setT((long)(int)target.getId());
        return effect;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 21;
    }
    
    @Override
    public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(81053), Integer.valueOf(5));
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(Fight fight, FightObject effector) {
        List<FightObject> mySide = (List)fight.getMySide((int)effector.getId()).stream().collect(Collectors.toList());
        if (mySide != null && mySide.size() > 0) {
            List<Integer> targetIds = new ArrayList<>();
            for (FightObject target : mySide) {
                targetIds.add(target.getId());
            }
            return targetIds;
        }
        return null;
    }
}
