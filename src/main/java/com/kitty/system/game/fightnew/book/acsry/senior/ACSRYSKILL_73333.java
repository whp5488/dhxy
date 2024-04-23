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

public class ACSRYSKILL_73333 extends AcsrySkillProcessor {
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
    if (SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType().intValue() && 
      effecter.isPlayer()) {
      target.getOtherMap().setv("skillRound", "73333_" + effecter.getId() + "_" + fight.getRound());
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 73333);
      skillRecord.setT(target.getId().intValue());
    } 
  }
  
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
    if (checkExcute(skill)) {
      Object obj = effecter.getOtherMap().get("skillRound", null);
      if (obj != null) {
        String str = obj.toString();
        String[] splits = str.split("_");
        if (splits.length >= 3) {
          String roundStr = splits[2];
          String objIdStr = splits[1];
          String skillIdStr = splits[0];
          if (roundStr.matches("^[0-9]+$") && objIdStr.matches("^[0-9]+$") && skillIdStr.matches("^[0-9]+$")) {
            int round = Integer.parseInt(roundStr);
            int objId = Integer.parseInt(objIdStr);
            int skillId = Integer.parseInt(skillIdStr);
            if (round == fight.getRound() && 73333 == skillId) {
              FightObject fightObject = fight.queryFightObject(Integer.valueOf(objId));
              if (fightObject != null) {
                double property = 0.15D;
                property += (fightObject.getRole().getMj().intValue() / 100) * 0.01D;
                if (property > 0.3D)
                  property = 0.3D; 
                int changeHurt = (int)(hurt * property);
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 73333);
                skillRecord.setT(effecter.getId().intValue());
                return NutMap.NEW().setv("changeHurt", Integer.valueOf(-changeHurt));
              } 
            } 
          } 
        } 
      } 
    } 
    return null;
  }
  
  private boolean checkExcute(RoleSkill skill) {
    if (SKILLATTR.SKILLATTR_FIRE.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_WIND.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_THUNDER
      .getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_WATER.getValue() == skill.getType().intValue())
      return true; 
    if (SKILLATTR.SKILLATTR_HENGSAO.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_POJIA.getValue() == skill.getType().intValue() || SKILLATTR.SKILLATTR_ZHENJI
      .getValue() == skill.getType().intValue())
      return true; 
    if (SKILLATTR.SKILLATTR_WULI.getValue() == skill.getType().intValue())
      return true; 
    if (SKILLATTR.SKILLATTR_AIHAO.getValue() == skill.getType().intValue())
      return true; 
    if (SKILLATTR.SKILLATTR_XIXUE.getValue() == skill.getType().intValue())
      return true; 
    if (SKILLATTR.SKILLATTR_POISON.getValue() == skill.getType().intValue())
      return true; 
    if (SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType().intValue())
      return true; 
    return false;
  }
}
