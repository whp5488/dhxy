package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_81009 extends SkillProcessor {
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
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81009);
    List<FiedValue> list = PetPassiveSkillUtil.clearStatus(target, "abnormal");
    Effect effect = new Effect();
    effect.setType(100000);
    if (list != null && list.size() > 0) {
      effect.setList(list);
      skillRecord.setT(target.getId().intValue());
    } 
    return effect;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 15400;
  }
  
  public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
    effector.getSkillCoolMap().setv(String.valueOf(81009), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
    return true;
  }
  
  public List<Integer> getTargetIds(Fight fight, FightObject effector) {
    int count = 2;
    FightObject firstTarget = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
    List<Integer> targetIds = new ArrayList<>();
    if (firstTarget == null || firstTarget.isFallDown()) {
      count = 3;
    } else {
      targetIds.add(firstTarget.getId());
    } 
    boolean trigger = AcsryPetSkillUtil.isTrigger(81009, effector);
    if (trigger)
      count++; 
    for (FightObject obj : fight.getMySide(effector.getId().intValue())) {
      if (obj.getId().equals(Integer.valueOf(effector.getTargetId())))
        continue; 
      if (FightOtherUtil.isHaveStatus(obj, StatusConst.getAnomalousStatus())) {
        targetIds.add(obj.getId());
        count--;
        if (count <= 0)
          break; 
      } 
    } 
    return targetIds;
  }
}
