package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysPetSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_80021 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    SysPetSkill coverPetSkill = PetSkillEffectorUtil.queryInitiativeQueryCover(addition.getSkills(), PET_PASSIVE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue(), 80021);
    NutMap map = NutMap.NEW();
    if (coverPetSkill != null) {
      map.setv(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()), Integer.valueOf(0));
    } else {
      double add = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf(38.0D * (addition.getRei() * 0.3D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(addition.getLv()), Integer.valueOf(addition.getRei()), addition.getExperience())));
      map.setv(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()), Double.valueOf(add));
    } 
    return map;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
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
