package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;

public class ACSRYSKILL_74323 extends AcsrySkillProcessor {
  public void passive(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown() || fightObject.getCurrLife() <= 1)
      return; 
    if (fight.isPvp() != 1)
      return; 
    if (fightObject.getStatusValue(1007) > 0) {
      String statusValue = fightObject.getStatusMap().getString("1007perpetrator");
      if (statusValue != null) {
        String[] strs = statusValue.split("_");
        if (strs[0] == null || "".equals(strs[0]) || !String.valueOf(74323).equals(strs[0]))
          return; 
        if (strs[1] == null || "".equals(strs[1]) || !strs[1].matches("^[0-9]+$"))
          return; 
        FightObject effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(strs[1])));
        if (effecter == null || effecter.isFallDown())
          return; 
        double percent = 0.1D;
        percent += (((PlayerFightObject)effecter).getRole().getMj().intValue() / 200) * 0.01D;
        int reduceLife = (int)(fightObject.getMaxLife() * percent);
        if (reduceLife >= fightObject.getCurrLife())
          reduceLife = fightObject.getCurrLife() - 1; 
        ActionParamUtils.changeLife(fight, effecter, fightObject, 7, 300999, 2, -reduceLife, false);
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 74323);
        skillRecord.setT(fightObject.getId().intValue());
      } 
    } 
  }
  
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
    if (isHit == null || !isHit.booleanValue())
      return; 
    if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == skill.getType().intValue() && target.getStatusValue(1007) > 0) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 74323);
      skillRecord.setT(target.getId().intValue());
      target.getStatusMap().setv("1007perpetrator", "74323_" + effecter.getId());
    } 
  }
}
