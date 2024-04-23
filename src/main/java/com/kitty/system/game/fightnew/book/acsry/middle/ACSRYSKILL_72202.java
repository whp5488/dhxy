package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_72202 extends AcsrySkillProcessor {
  public NutMap executeReduceHp(Fight fight, FightObject effecter, FightObject target, Object... args) {
    RoleSkill skill = null;
    int hurt = 0;
    try {
      skill = (RoleSkill)args[0];
      hurt = ((Integer)args[1]).intValue();
    } catch (Exception e) {
      return null;
    } 
    if (skill == null)
      return null; 
    if (hurt <= 0)
      return null; 
    if (checkExcute(skill) && checkTarget(target) && 
      effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 72202);
      if (!playerFightObject.getAcsrySkillList().contains(Integer.valueOf(72203))) {
        int changeHurt = (int)(hurt * 0.8D);
        skillRecord.setT(target.getId().intValue());
        return NutMap.NEW().setv("changeHurt", Integer.valueOf(changeHurt));
      } 
    } 
    return null;
  }
  
  private boolean checkTarget(FightObject target) {
    if (target.getStatusValue(1006) > 0 || target.getStatusValue(1004) > 0)
      return true; 
    return false;
  }
  
  private boolean checkExcute(RoleSkill skill) {
    if (SKILLATTR.SKILLATTR_FIRE.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_WIND.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_THUNDER
      .getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_WATER.getValue() == skill.getType().intValue())
      return true; 
    if (SKILLATTR.SKILLATTR_AIHAO.getValue() == skill.getType().intValue())
      return true; 
    return false;
  }
}
