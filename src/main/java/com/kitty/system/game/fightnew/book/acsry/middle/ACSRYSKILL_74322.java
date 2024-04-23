package com.kitty.system.game.fightnew.book.acsry.middle;

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

public class ACSRYSKILL_74322 extends AcsrySkillProcessor {
  public void passive(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown() || fightObject.getCurrLife() <= 1)
      return; 
    if (fight.isPvp() != 1)
      return; 
    if (fightObject.getStatusValue(1007) > 0) {
      String statusValue = fightObject.getStatusMap().getString("1007perpetrator");
      if (statusValue != null) {
        String[] strs = statusValue.split("_");
        if (strs[0] == null || "".equals(strs[0]) || !String.valueOf(74322).equals(strs[0]))
          return; 
        if (strs[1] == null || "".equals(strs[1]) || !strs[1].matches("^[0-9]+$"))
          return; 
        FightObject effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(strs[1])));
        if (effecter == null || effecter.isFallDown())
          return; 
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 74322);
        int reduceLife = (int)(fightObject.getMaxLife() * 0.1D);
        if (reduceLife >= fightObject.getCurrLife())
          reduceLife = fightObject.getCurrLife() - 1; 
        skillRecord.setT(fightObject.getId().intValue());
        ActionParamUtils.changeLife(fight, effecter, fightObject, 7, 300999, 2, -reduceLife, false);
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
    if (effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(74323)))
        return; 
      if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == skill.getType().intValue() && target.getStatusValue(1007) > 0) {
        target.getStatusMap().setv("1007perpetrator", "74322_" + effecter.getId());
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 74322);
        skillRecord.setT(target.getId().intValue());
      } 
    } 
  }
}
