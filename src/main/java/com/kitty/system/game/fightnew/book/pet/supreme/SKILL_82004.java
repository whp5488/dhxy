package com.kitty.system.game.fightnew.book.pet.supreme;

import java.util.Random;
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
    public SKILL_82004() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 82004);
        int time = 3;
        Effect effect = new Effect();
        effect.setType(1011);
        effect.setTime(time);
        skillRecord.setT((long)(int)target.getId());
        return effect;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 2300;
    }
    
    @Override
    public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(82004), Integer.valueOf(5));
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(Fight fight, FightObject effector) {
        List<Integer> targetIds = new ArrayList<>();
        if (effector.getId().equals(Integer.valueOf(effector.getTargetId()))) {
            FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
            if (host != null && !host.isFallDown() && !host.isFrozen()) {
                targetIds.add(host.getId());
            }
            else {
                List<FightObject> mySide = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> !obj.getId().equals(effector.getId()) && !obj.isFallDown() && !obj.isFrozen()).collect(Collectors.toList());
                if (mySide != null && mySide.size() > 0) {
                    Random random = new Random();
                    int i = random.nextInt(mySide.size());
                    FightObject fightObject = (FightObject)mySide.get(i);
                    if (fightObject != null && !fightObject.isFallDown()) {
                        targetIds.add(fightObject.getId());
                    }
                }
            }
        }
        else {
            FightObject target = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
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
