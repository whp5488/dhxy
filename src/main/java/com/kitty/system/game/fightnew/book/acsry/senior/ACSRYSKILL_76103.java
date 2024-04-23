package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
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

public class ACSRYSKILL_76103 extends AcsrySkillProcessor {
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
    if (target.isPet() && target.isFallDown() && 
      effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      double percent = 0.04D;
      double maxPercent = 0.3D;
      double attackAddition = playerFightObject.getAcsryPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.ATTACK_ADDITION.getDist()), 0.0D);
      if (attackAddition >= maxPercent)
        return; 
      attackAddition += percent;
      if (attackAddition > maxPercent)
        attackAddition = maxPercent; 
      playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.ATTACK_ADDITION.getDist()), Double.valueOf(attackAddition));
      int oldHp = effecter.getCurrLife();
      int addHp = (int)(oldHp * percent);
      if (oldHp + addHp > effecter.getMaxLife())
        addHp = effecter.getMaxLife() - oldHp; 
      if (addHp > 0) {
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(effecter.getId().intValue(), true);
        PassiveSkillUtils.excuteChangeLifeAuto(fight, effecter, actionDetailParam, targetParam, addHp);
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 76103);
        skillRecord.setT(effecter.getId().intValue());
      } 
    } 
  }
}
