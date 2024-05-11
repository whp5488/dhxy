
package com.kitty.system.game.fightnew.book.pet.senior;

import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81008 extends SkillProcessor
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
        return 3;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81008);
        final double percent = 0.5;
        final SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81008);
        if (petSkill != null) {
            final SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
            return processor.effect(fight, effector, target, args);
        }
        final int oldLife = target.getCurrLife();
        final int newLife = Math.min(target.getMaxLife(), oldLife + (int)(target.getMaxLife() * percent));
        final int oldMana = target.getCurrMana();
        final int newMana = Math.min(target.getMaxMana(), oldMana + (int)(target.getMaxMana() * percent));
        target.setCurrLife(newLife);
        target.setCurrMana(newMana);
        final int changeLife = newLife - oldLife;
        final int changeMana = newMana - oldMana;
        final Effect effect = new Effect();
        effect.setType(100000);
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("targetChangeHp", (byte)3, changeLife));
        list.add(new FiedValue("targetChangeMp", (byte)3, changeMana));
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
        effector.getSkillCoolMap().setv(String.valueOf(81008), (Object)WarMaxRound.WAR_MAX_ROUND.getValue());
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(final Fight fight, final FightObject effector) {
        final SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81008);
        if (petSkill != null) {
            final SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
            return processor.getTargetIds(fight, effector);
        }
        int sum = 2;
        final FightObject firstTarget = fight.queryFightObject(effector.getTargetId());
        if (firstTarget == null || firstTarget.isFallDown()) {
            sum = 3;
        }
        final boolean trigger = AcsryPetSkillUtil.isTrigger(81008, effector);
        if (trigger) {
            ++sum;
        }
        final List<FightObject> mySide = fight.getMySide(effector.getId()).stream().sorted(Comparator.comparing(FightObject::getCurrLife)).filter(obj -> !obj.getId().equals(effector.getTargetId())).limit(sum).collect(Collectors.toList());
        if (firstTarget != null && !firstTarget.isFallDown()) {
            mySide.add(firstTarget);
        }
        final List<Integer> targetIdList = new ArrayList<Integer>();
        for (final FightObject target : mySide) {
            targetIdList.add(target.getId());
        }
        return targetIdList;
    }
}
