package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_80001 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    Role role = (Role)args[0];
    Pet pet = (Pet)args[1];
    double percent = 0.25D;
    double pval = AcsryPetSkillUtil.getPval(80001, effector);
    if (pval > 0.0D)
      percent += pval; 
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      PetSkillEffectorUtil.excuteFlashBeforeOne(fight, effector, role, pet, 80001);
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, pet.getPetId().intValue(), 80001);
      skillRecord.setT(pet.getPetId().intValue());
      return NutMap.NEW();
    } 
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
