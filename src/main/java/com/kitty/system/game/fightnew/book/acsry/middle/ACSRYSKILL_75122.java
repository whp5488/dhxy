package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.List;

public class ACSRYSKILL_75122 extends AcsrySkillProcessor {
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
    if (SKILLATTR.SKILLATTR_WULI.getValue() == skill.getType().intValue() && 
      effecter.isPlayer()) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 75122);
      double percent = 0.1D;
      boolean flag = SkillAi.canAffectOnPro(percent);
      if (flag && 
        target.getStatusValue(1003) <= 0) {
        int round = 1;
        target.setStatus(1002, round);
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), false);
        if (targetParam != null)
          targetParam.getStatusMap().put(Integer.valueOf(1002), Integer.valueOf(round)); 
        List<Integer> list = clearStatus(target, skill.getType().intValue());
        if (list.size() > 0) {
          skillRecord.setT(target.getId().intValue());
          targetParam.addClearStatus(list);
        } 
      } 
    } 
  }
  
  public List<Integer> clearStatus(FightObject target, int skillType) {
    List<Integer> list = new ArrayList<>();
    if (target.getStatusValue(1006) > 0) {
      list.add(Integer.valueOf(1006));
      target.setStatus(1006, 0);
    } 
    if (target.getStatusValue(1004) > 0) {
      list.add(Integer.valueOf(1004));
      target.setStatus(1004, 0);
    } 
    return list;
  }
}
