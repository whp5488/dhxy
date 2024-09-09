package com.kitty.system.game.fightnew.book.pet.senior;

import java.util.Iterator;
import java.util.stream.Collectors;
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
    public SKILL_81008() {
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
        return 3;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81008);
        double percent = 0.5;
        SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81008);
        if (petSkill != null) {
            SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
            return processor.effect(fight, effector, target, args);
        }
        int oldLife = target.getCurrLife();
        int newLife = Math.min(target.getMaxLife(), oldLife + (int)((double)target.getMaxLife() * percent));
        int oldMana = target.getCurrMana();
        int newMana = Math.min(target.getMaxMana(), oldMana + (int)((double)target.getMaxMana() * percent));
        target.setCurrLife(newLife);
        target.setCurrMana(newMana);
        int changeLife = newLife - oldLife;
        int changeMana = newMana - oldMana;
        Effect effect = new Effect();
        effect.setType(100000);
        List<FiedValue> list = new ArrayList<>();
        list.add(new FiedValue("targetChangeHp", (byte)3, Integer.valueOf(changeLife)));
        list.add(new FiedValue("targetChangeMp", (byte)3, Integer.valueOf(changeMana)));
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
        effector.getSkillCoolMap().setv(String.valueOf(81008), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(Fight fight, FightObject effector) {
        SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81008);
        if (petSkill != null) {
            SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
            return processor.getTargetIds(fight, effector);
        }
        int sum = 2;
        FightObject firstTarget = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        if (firstTarget == null || firstTarget.isFallDown()) {
            sum = 3;
        }
        boolean trigger = AcsryPetSkillUtil.isTrigger(81008, effector);
        if (trigger) {
            ++sum;
        }
        List<FightObject> mySide = (List)fight.getMySide((int)effector.getId()).stream().sorted(Comparator.comparing(FightObject::getCurrLife)).filter(obj -> !obj.getId().equals(Integer.valueOf(effector.getTargetId()))).limit((long)sum).collect(Collectors.toList());
        if (firstTarget != null && !firstTarget.isFallDown()) {
            mySide.add(firstTarget);
        }
        List<Integer> targetIdList = new ArrayList<>();
        for (FightObject target : mySide) {
            targetIdList.add(target.getId());
        }
        return targetIdList;
    }
}
