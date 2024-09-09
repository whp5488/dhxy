package com.kitty.system.game.fightnew.book.pet.senior;

import java.util.Iterator;
import java.util.ArrayList;
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
    public SKILL_81051() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81051);
        SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81051);
        if (petSkill != null) {
            SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
            return processor.effect(fight, effector, target, args);
        }
        List<FiedValue> list = PetPassiveSkillUtil.clearStatus(target, "positive");
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
        return 15400;
    }
    
    @Override
    public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(81051), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(Fight fight, FightObject effector) {
        SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81051);
        if (petSkill != null) {
            SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
            return processor.getTargetIds(fight, effector);
        }
        int sum = 2;
        boolean isAddFirstTarget = true;
        FightObject firstTarget = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        if (firstTarget == null || firstTarget.isFallDown()) {
            sum = 3;
            isAddFirstTarget = false;
        }
        boolean trigger = AcsryPetSkillUtil.isTrigger(81051, effector);
        if (trigger) {
            ++sum;
        }
        List<FightObject> targetSides = (List)fight.getTargetSide((int)effector.getId()).stream().limit((long)sum).collect(Collectors.toList());
        if (isAddFirstTarget) {
            targetSides.add(firstTarget);
        }
        List<Integer> targetIdList = new ArrayList<>();
        for (FightObject target : targetSides) {
            targetIdList.add(target.getId());
        }
        return targetIdList;
    }
}
