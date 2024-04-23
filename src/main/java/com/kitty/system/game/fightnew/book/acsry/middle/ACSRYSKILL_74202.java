package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.skill.domain.RoleSkill;

public class ACSRYSKILL_74202 extends AcsrySkillProcessor {
  public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    RoleSkill skill = null;
    Boolean isHit = Boolean.valueOf(false);
    FightObject sideTarget = null;
    try {
      skill = (RoleSkill)args[0];
      isHit = (Boolean)args[1];
      sideTarget = (FightObject)args[6];
    } catch (Exception e) {
      return;
    } 
    if (skill == null)
      return; 
    if (isHit == null || !isHit.booleanValue() || sideTarget == null)
      return; 
    if (SKILLATTR.SKILLATTR_XIXUE.getValue() == skill.getType().intValue() && 
      effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(74202))) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 74202);
        if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(74203)))
          return; 
        double percent = 0.1D;
        int addMp = (int)(sideTarget.getMaxMana() * percent);
        int oldMana = sideTarget.getCurrMana();
        if (addMp + oldMana > sideTarget.getMaxMana())
          addMp = sideTarget.getMaxMana() - sideTarget.getCurrMana(); 
        if (addMp > 0) {
          FightObjectParam targetParam = actionDetailParam.gainTargetParam(sideTarget.getId().intValue(), false);
          if (targetParam != null) {
            skillRecord.setT(effecter.getId().intValue());
            PassiveSkillUtils.excuteChangeManaAuto(fight, sideTarget, actionDetailParam, targetParam, addMp);
          } 
        } 
      } 
    } 
  }
}
