package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.Arrays;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_81006 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81006);
    SysPetSkill petSkill = (SysPetSkill)args[0];
    if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args))
      return null; 
    int count = 2;
    boolean trigger = AcsryPetSkillUtil.isTrigger(81006, effector);
    if (trigger)
      count++; 
    List<FightObject> units = fight.queryAnomalousObject(effector, count, Arrays.asList(new Integer[] { Integer.valueOf(LOGIC.LOGICTYPE_PET.getValue()), Integer.valueOf(LOGIC.LOGICTYPE_PARTNER.getValue()) }));
    if (units.size() <= 0)
      return null; 
    PetTriggerPassiveSkill.clearStatusParam(fight, units, effector, 81006);
    skillRecord.setT(((FightObject)units.get(0)).getId().intValue());
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
