package com.kitty.system.game.fightnew.book.pet.senior;

import java.util.Iterator;
import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import java.util.ArrayList;
import com.kitty.system.enums.war.wardef.WarMaxRound;
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

public class SKILL_81009 extends SkillProcessor
{
    public SKILL_81009() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81009);
        List<FiedValue> list = PetPassiveSkillUtil.clearStatus(target, "abnormal");
        Effect effect = new Effect();
        effect.setType(100000);
        if (list != null && list.size() > 0) {
            effect.setList(list);
            skillRecord.setT((long)(int)target.getId());
            return effect;
        }
        return effect;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 15400;
    }
    
    @Override
    public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(81009), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(Fight fight, FightObject effector) {
        int count = 2;
        FightObject firstTarget = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        List<Integer> targetIds = new ArrayList<>();
        if (firstTarget == null || firstTarget.isFallDown()) {
            count = 3;
        }
        else {
            targetIds.add(firstTarget.getId());
        }
        boolean trigger = AcsryPetSkillUtil.isTrigger(81009, effector);
        if (trigger) {
            ++count;
        }
        for (FightObject obj : fight.getMySide((int)effector.getId())) {
            if (obj.getId().equals(Integer.valueOf(effector.getTargetId()))) {
                continue;
            }
            if (FightOtherUtil.isHaveStatus(obj, StatusConst.getAnomalousStatus())) {
                targetIds.add(obj.getId());
                if (--count <= 0) {
                    break;
                }
                continue;
            }
            else {
                continue;
            }
        }
        return targetIds;
    }
}
