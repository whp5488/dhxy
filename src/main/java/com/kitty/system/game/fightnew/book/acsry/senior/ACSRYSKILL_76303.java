package com.kitty.system.game.fightnew.book.acsry.senior;

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

public class ACSRYSKILL_76303 extends AcsrySkillProcessor {
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
    int status = getStatus(skill);
    if ((1020 == status || 1021 == status) && 
      effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(76303))) {
        target.getStatusMap().setv(status + "perpetrator", "76303_" + effecter.getId());
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 76303);
        skillRecord.setT(target.getId().intValue());
      } 
    } 
  }
  
  public void statusOver(Fight fight, FightObject fightObject, Object... args) {
    Integer status = null;
    try {
      status = (Integer)args[0];
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (1021 == status.intValue() || 1020 == status.intValue()) {
      String perpetrator = fightObject.getStatusMap().getString(status + "perpetrator");
      if (perpetrator != null && perpetrator.contains(String.valueOf(76303))) {
        fightObject.getStatusMap().unset(status + "perpetrator");
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 76303);
        skillRecord.setT(fightObject.getId().intValue());
      } 
    } 
  }
  
  public NutMap reduceMp(Fight fight, FightObject fightObject, Object... args) {
    int mana = 0;
    try {
      mana = ((Integer)args[0]).intValue();
    } catch (Exception e) {
      return null;
    } 
    if (mana <= 0)
      return null; 
    int status = getStatusExcute(fightObject);
    if (status != 0) {
      String perpetrator = fightObject.getStatusMap().getString(status + "perpetrator");
      if (perpetrator != null && perpetrator.contains(String.valueOf(76303))) {
        double percent = 0.4D;
        try {
          FightObject effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(perpetrator.split("_")[1])));
          percent += (effecter.getRole().getLl().intValue() / 100) * 0.04D;
        } catch (NumberFormatException e) {
          return null;
        } 
        int changeMp = (int)(mana * percent);
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 76303);
        skillRecord.setT(fightObject.getId().intValue());
        return NutMap.NEW().setv("changeMana", Integer.valueOf(changeMp));
      } 
    } 
    return null;
  }
  
  private int getStatus(RoleSkill skill) {
    if (SKILLATTR.SKILLATTR_POJIA.getValue() == skill.getType().intValue())
      return 1021; 
    if (SKILLATTR.SKILLATTR_ZHENJI.getValue() == skill.getType().intValue())
      return 1020; 
    return 0;
  }
  
  private int getStatusExcute(FightObject fightObject) {
    if (fightObject.getStatusValue(1020) > 0)
      return 1020; 
    if (fightObject.getStatusValue(1021) > 0)
      return 1021; 
    return 0;
  }
}
