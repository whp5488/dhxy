package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_76202 extends AcsrySkillProcessor {
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
    int statusValue = target.getStatusValue(1022);
    if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == skill.getType().intValue() && statusValue > 0 && 
      effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(76202))) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 76202);
        if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(76203)))
          return; 
        target.setStatus(8010, statusValue);
        double percent = 0.1D;
        target.getStatusMap().setv("8010value", Double.valueOf(percent));
        skillRecord.setT(target.getId().intValue());
      } 
    } 
  }
  
  public NutMap countRecoverBloodEnd(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.getStatusValue(1022) > 0 && fightObject.getStatusValue(8010) > 0) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 76202);
      double percent = fightObject.getStatusMap().getDouble("8010value", 0.0D);
      if (percent > 0.0D) {
        int addMp = (int)(fightObject.getMaxMana() * percent);
        if (fightObject.getCurrMana() + addMp > fightObject.getMaxMana())
          addMp = fightObject.getMaxMana() - fightObject.getCurrMana(); 
        if (addMp > 0) {
          fightObject.setCurrMana(fightObject.getCurrMana() + addMp);
          fightObject.getTempCache().put("recoverManaValue", Integer.valueOf(addMp));
        } 
        skillRecord.setT(fightObject.getId().intValue());
      } 
    } 
    return null;
  }
}
