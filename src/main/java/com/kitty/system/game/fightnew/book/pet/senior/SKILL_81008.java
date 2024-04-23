package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_81008 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 3;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81008);
    double percent = 0.5D;
    SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81008);
    if (petSkill != null) {
      SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
      return processor.effect(fight, effector, target, args);
    } 
    int oldLife = target.getCurrLife();
    int newLife = Math.min(target.getMaxLife(), oldLife + (int)(target.getMaxLife() * percent));
    int oldMana = target.getCurrMana();
    int newMana = Math.min(target.getMaxMana(), oldMana + (int)(target.getMaxMana() * percent));
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
    skillRecord.setT(target.getId().intValue());
    return effect;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 6300;
  }
  
  public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
    effector.getSkillCoolMap().setv(String.valueOf(81008), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
    return true;
  }
  
  public List<Integer> getTargetIds(Fight fight, FightObject effector) {
    SysPetSkill petSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(effector.getSkillIds(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), 81008);
    if (petSkill != null) {
      SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
      return processor.getTargetIds(fight, effector);
    } 
    int sum = 2;
    FightObject firstTarget = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
    if (firstTarget == null || firstTarget.isFallDown())
      sum = 3; 
    boolean trigger = AcsryPetSkillUtil.isTrigger(81008, effector);
    if (trigger)
      sum++; 
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().sorted(Comparator.comparing(FightObject::getCurrLife)).filter(obj -> !obj.getId().equals(Integer.valueOf(effector.getTargetId()))).limit(sum).collect(Collectors.toList());
    if (firstTarget != null && !firstTarget.isFallDown())
      mySide.add(firstTarget); 
    List<Integer> targetIdList = new ArrayList<>();
    for (FightObject target : mySide)
      targetIdList.add(target.getId()); 
    return targetIdList;
  }
}
