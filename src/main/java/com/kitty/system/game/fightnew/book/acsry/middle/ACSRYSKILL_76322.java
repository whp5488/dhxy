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
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;

public class ACSRYSKILL_76322 extends AcsrySkillProcessor {
  public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (fight.isPvp() != 1)
      return; 
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
    if (SKILLATTR.SKILLATTR_HENGSAO.getValue() == skill.getType().intValue() && 
      effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(76323)))
        return; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 76322);
      int round = 2;
      target.getOtherMap().setv("originalMaxLife", Integer.valueOf(target.getMaxLife()));
      target.getStatusMap().setv(String.valueOf(8011), Integer.valueOf(round));
      target.getStatusMap().setv("8011perpetrator", "76322_" + effecter.getId());
      target.setMaxLife((int)(target.getMaxLife() * 0.9D));
      if (target.getMaxLife() < target.getCurrLife()) {
        int reduceLife = target.getMaxLife() - target.getCurrLife();
        if (reduceLife < 0) {
          FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), false);
          if (targetParam != null) {
            FightObjectParam targetC = ActionParamUtils.changeLifeForTarget(fight, target, 1, reduceLife, 2);
            targetC.setMaxLife(target.getMaxLife());
            targetC.addDamageType(300999);
            actionDetailParam.getTargetParamList().add(targetC);
          } 
        } 
      } 
      skillRecord.setT(target.getId().intValue());
    } 
  }
  
  public void statusOver(Fight fight, FightObject fightObject, Object... args) {
    Integer status = null;
    try {
      status = (Integer)args[0];
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (status == null || 8011 != status.intValue())
      return; 
    String perpetrator = fightObject.getStatusMap().getString("8011perpetrator");
    if (perpetrator != null && perpetrator.contains(String.valueOf(76322))) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 76322);
      fightObject.getStatusMap().unset("8011perpetrator");
      int maxLife = fightObject.getOtherMap().getInt("originalMaxLife", 0);
      if (maxLife > 0) {
        fightObject.setMaxLife(maxLife);
        skillRecord.setT(fightObject.getId().intValue());
      } 
    } 
  }
}
