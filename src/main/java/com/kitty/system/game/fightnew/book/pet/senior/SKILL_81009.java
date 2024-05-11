
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81009);
        final List<FiedValue> list = PetPassiveSkillUtil.clearStatus(target, "abnormal");
        final Effect effect = new Effect();
        effect.setType(100000);
        if (list != null && list.size() > 0) {
            effect.setList(list);
            skillRecord.setT(target.getId());
        }
        return effect;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 15400;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(81009), (Object)WarMaxRound.WAR_MAX_ROUND.getValue());
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(final Fight fight, final FightObject effector) {
        int count = 2;
        final FightObject firstTarget = fight.queryFightObject(effector.getTargetId());
        final List<Integer> targetIds = new ArrayList<Integer>();
        if (firstTarget == null || firstTarget.isFallDown()) {
            count = 3;
        }
        else {
            targetIds.add(firstTarget.getId());
        }
        final boolean trigger = AcsryPetSkillUtil.isTrigger(81009, effector);
        if (trigger) {
            ++count;
        }
        for (final FightObject obj : fight.getMySide(effector.getId())) {
            if (obj.getId().equals(effector.getTargetId())) {
                continue;
            }
            if (!FightOtherUtil.isHaveStatus(obj, StatusConst.getAnomalousStatus())) {
                continue;
            }
            targetIds.add(obj.getId());
            if (--count <= 0) {
                break;
            }
        }
        return targetIds;
    }
}
