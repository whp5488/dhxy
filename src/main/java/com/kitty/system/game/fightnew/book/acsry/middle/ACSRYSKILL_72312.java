package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;

public class ACSRYSKILL_72312 extends AcsrySkillProcessor {
  public boolean magicSelectTarget(Fight fight, PlayerFightObject playerFightObject, Object... args) {
    RoleSkill skill = (RoleSkill)args[0];
    if (skill == null)
      return false; 
    if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_POJIA.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_ZHENJI
      .getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_FIRE.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_WIND
      .getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_THUNDER.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_WATER
      .getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_AIHAO.getValue() == skill.getType().intValue()) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, playerFightObject.getId().intValue(), 72312);
      double percent = 0.5D;
      boolean flag = SkillAi.canAffectOnPro(percent);
      if (flag) {
        skillRecord.setT(playerFightObject.getId().intValue());
        return true;
      } 
    } 
    return false;
  }
}
