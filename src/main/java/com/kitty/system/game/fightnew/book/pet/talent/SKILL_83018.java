package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_83018 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83018);
    double percent = 0.5D;
    int oldLife = target.getCurrLife();
    int newLife = Math.min(target.getMaxLife(), oldLife + (int)(target.getMaxLife() * percent));
    int oldMana = target.getCurrMana();
    int newMana = Math.min(target.getMaxMana(), oldMana + (int)(target.getMaxMana() * percent));
    Effect effect = new Effect();
    effect.setType(100000);
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("targetChangeHp", (byte)3, Integer.valueOf(newLife - oldLife)));
    list.add(new FiedValue("targetChangeMp", (byte)3, Integer.valueOf(newMana - oldMana)));
    effect.setList(list);
    skillRecord.setT(target.getId().intValue());
    return effect;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 4300;
  }
  
  public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
    effector.getSkillCoolMap().setv(String.valueOf(83018), Integer.valueOf(3));
    return true;
  }
  
  public List<Integer> getTargetIds(Fight fight, FightObject effector) {
    int sum = 1;
    boolean isAddFirstTarget = true;
    FightObject firstTarget = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
    if (firstTarget == null) {
      sum = 2;
      isAddFirstTarget = false;
    } 
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> (obj.isFallDown() && !obj.getId().equals(firstTarget.getId()))).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).limit(sum).collect(Collectors.toList());
    if (isAddFirstTarget)
      mySide.add(firstTarget); 
    if (mySide != null && mySide.size() > 0) {
      List<Integer> targetIds = new ArrayList<>();
      for (FightObject target : mySide)
        targetIds.add(target.getId()); 
      return targetIds;
    } 
    return null;
  }
}
