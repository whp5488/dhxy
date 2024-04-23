package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_71313 extends AcsrySkillProcessor {
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
    if (SKILLATTR.SKILLATTR_POISON.getValue() == skill.getType().intValue() && 
      effecter.isPlayer()) {
      target.getStatusMap().setv("1005perpetrator", "71313_" + effecter.getId());
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 71313);
      skillRecord.setT(target.getId().intValue());
    } 
  }
  
  public NutMap countRecoverBloodEnd(Fight fight, FightObject fightObject, Object... args) {
    String string = fightObject.getStatusMap().getString("1005perpetrator");
    if (string == null || !string.contains(String.valueOf(71313)))
      return null; 
    FightObject effecter = null;
    int life = 0;
    try {
      life = ((Integer)args[0]).intValue();
      String[] splits = string.split("_");
      effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(splits[1])));
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (effecter == null)
      return null; 
    if (life > 0) {
      double percent = 0.3D;
      percent += (effecter.getRole().getGg().intValue() / 200) * 0.05D;
      if (percent > 0.5D)
        percent = 0.5D; 
      int reduceLife = (int)(life * percent);
      NutMap map = NutMap.NEW();
      map.setv("changeLife", Integer.valueOf(-reduceLife));
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 71313);
      skillRecord.setT(fightObject.getId().intValue());
      return map;
    } 
    return null;
  }
  
  public NutMap countRecoverMpEnd(Fight fight, FightObject fightObject, Object... args) {
    String string = fightObject.getStatusMap().getString("1005perpetrator");
    if (string == null || !string.contains(String.valueOf(71313)))
      return null; 
    FightObject effecter = null;
    int mana = 0;
    try {
      mana = ((Integer)args[0]).intValue();
      String[] splits = string.split("_");
      effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(splits[1])));
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (mana > 0) {
      double percent = 0.3D;
      percent += (effecter.getRole().getGg().intValue() / 200) * 0.05D;
      if (percent > 0.5D)
        percent = 0.5D; 
      int reduceMana = (int)(mana * percent);
      NutMap map = NutMap.NEW();
      map.setv("changeMana", Integer.valueOf(-reduceMana));
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 71313);
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
    if (status == null || 1005 != status.intValue())
      return; 
    String perpetrator = fightObject.getStatusMap().getString("1005perpetrator");
    if (perpetrator != null && perpetrator.contains(String.valueOf(71313))) {
      fightObject.getStatusMap().unset("1005perpetrator");
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 71313);
      skillRecord.setT(fightObject.getId().intValue());
    } 
  }
}
