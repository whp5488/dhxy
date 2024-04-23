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

public class ACSRYSKILL_76313 extends AcsrySkillProcessor {
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
    if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == skill.getType().intValue() && 
      effecter.isPlayer()) {
      target.getStatusMap().setv("1022perpetrator", "76313_" + effecter.getId());
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 76313);
      skillRecord.setT(target.getId().intValue());
    } 
  }
  
  public NutMap countRecoverBloodEnd(Fight fight, FightObject fightObject, Object... args) {
    String string = fightObject.getStatusMap().getString("1022perpetrator");
    if (string == null || !string.contains(String.valueOf(76313)))
      return null; 
    int life = 0;
    FightObject effecter = null;
    try {
      life = ((Integer)args[0]).intValue();
      effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(string.split("_")[1])));
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (effecter == null || life <= 0)
      return null; 
    if (effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      double percent = 0.04D;
      percent += (playerFightObject.getRole().getGg().intValue() / 300) * 0.04D;
      int addLife = (int)(life * percent);
      NutMap map = NutMap.NEW();
      map.setv("changeLife", Integer.valueOf(addLife));
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 76313);
      skillRecord.setT(fightObject.getId().intValue());
      return map;
    } 
    return null;
  }
  
  public void statusOver(Fight fight, FightObject fightObject, Object... args) {
    Integer status = null;
    try {
      status = (Integer)args[0];
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (status == null || 1022 != status.intValue())
      return; 
    String perpetrator = fightObject.getStatusMap().getString("1022perpetrator");
    if (perpetrator != null && perpetrator.contains(String.valueOf(76313))) {
      fightObject.getStatusMap().unset("1022perpetrator");
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 76313);
      skillRecord.setT(fightObject.getId().intValue());
    } 
  }
}
