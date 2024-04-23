package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.skill.domain.RoleSkill;

public class ACSRYSKILL_73103 extends AcsrySkillProcessor {
  public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (fight.isPvp() != 1)
      return; 
    RoleSkill skill = null;
    Boolean isHit = Boolean.valueOf(false);
    int targetHurt = 0;
    int targetMpHurt = 0;
    try {
      skill = (RoleSkill)args[0];
      isHit = (Boolean)args[1];
      targetHurt = ((Integer)args[3]).intValue();
      targetMpHurt = ((Integer)args[4]).intValue();
    } catch (Exception e) {
      return;
    } 
    if (skill == null)
      return; 
    if (isHit == null || !isHit.booleanValue())
      return; 
    if (SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType().intValue() && 
      effecter.isPlayer()) {
      int addHp = 0;
      int addMp = 0;
      if (targetHurt > 0)
        addHp = (int)(targetHurt * 0.02D); 
      if (targetMpHurt > 0)
        addMp = (int)(targetMpHurt * 0.1D); 
      if (addHp == 0 && addMp == 0)
        return; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 73103);
      FightObjectParam targetParam = actionDetailParam.gainTargetParam(effecter.getId().intValue(), true);
      FightObjectParam targetParamC = null;
      if (addHp > 0) {
        int oldLife = effecter.getCurrLife();
        if (addHp + oldLife > effecter.getMaxLife())
          addHp = effecter.getMaxLife() - effecter.getCurrLife(); 
        targetParamC = PassiveSkillUtils.excuteChangeLifeAuto(fight, effecter, actionDetailParam, targetParam, addHp);
        skillRecord.setT(effecter.getId().intValue());
      } 
      if (addMp > 0) {
        int oldMana = effecter.getCurrMana();
        if (addMp + oldMana > effecter.getMaxMana())
          addMp = effecter.getMaxMana() - effecter.getCurrMana(); 
        PassiveSkillUtils.excuteChangeManaAuto(fight, effecter, actionDetailParam, targetParamC, addMp);
        skillRecord.setT(effecter.getId().intValue());
      } 
    } 
  }
}
