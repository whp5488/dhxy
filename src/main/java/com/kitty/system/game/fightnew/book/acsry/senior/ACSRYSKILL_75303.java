package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;

public class ACSRYSKILL_75303 extends AcsrySkillProcessor {
  public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    RoleSkill skill = null;
    Boolean isHit = Boolean.valueOf(false);
    try {
      skill = (RoleSkill)args[0];
      isHit = (Boolean)args[1];
    } catch (Exception e) {
      return;
    } 
    if (skill == null)
      return; 
    if (isHit == null || isHit.booleanValue())
      return; 
    int status = getStatus(skill.getType().intValue());
    if (status != 0 && 
      target.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)target;
      double percent = 0.25D;
      boolean flag = SkillAi.canAffectOnPro(percent);
      if (flag) {
        int round = 2;
        effecter.setStatus(status, round);
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(effecter.getId().intValue(), true);
        targetParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(round));
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, target.getId().intValue(), 75303);
        skillRecord.setT(effecter.getId().intValue());
      } 
    } 
  }
  
  private int getStatus(int skillType) {
    if (SKILLATTR.SKILLATTR_ICE.getValue() == skillType)
      return 1003; 
    if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == skillType)
      return 1002; 
    if (SKILLATTR.SKILLATTR_SLEEP.getValue() == skillType)
      return 1004; 
    if (SKILLATTR.SKILLATTR_YIWANG.getValue() == skillType)
      return 1006; 
    return 0;
  }
}
