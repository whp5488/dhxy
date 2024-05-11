
package com.kitty.system.game.fightnew.book.pet.senior;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81051 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81051);
        final SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81051);
        if (petSkill != null) {
            final SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
            return processor.effect(fight, effector, target, args);
        }
        final List<FiedValue> list = PetPassiveSkillUtil.clearStatus(target, "positive");
        final Effect effect = new Effect();
        effect.setType(100000);
        if (list != null && list.size() > 0) {
            effect.setList(list);
        }
        skillRecord.setT(target.getId());
        return effect;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 15400;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(81051), (Object)WarMaxRound.WAR_MAX_ROUND.getValue());
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(final Fight fight, final FightObject effector) {
        final SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81051);
        if (petSkill != null) {
            final SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
            return processor.getTargetIds(fight, effector);
        }
        int sum = 2;
        boolean isAddFirstTarget = true;
        final FightObject firstTarget = fight.queryFightObject(effector.getTargetId());
        if (firstTarget == null || firstTarget.isFallDown()) {
            sum = 3;
            isAddFirstTarget = false;
        }
        final boolean trigger = AcsryPetSkillUtil.isTrigger(81051, effector);
        if (trigger) {
            ++sum;
        }
        final List<FightObject> targetSides = fight.getTargetSide(effector.getId()).stream().limit(sum).collect(Collectors.toList());
        if (isAddFirstTarget) {
            targetSides.add(firstTarget);
        }
        final List<Integer> targetIdList = new ArrayList<Integer>();
        for (final FightObject target : targetSides) {
            targetIdList.add(target.getId());
        }
        return targetIdList;
    }
}
