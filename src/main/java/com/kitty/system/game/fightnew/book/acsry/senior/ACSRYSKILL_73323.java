package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
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
import java.util.List;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_73323 extends AcsrySkillProcessor {
  public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    RoleSkill skill = null;
    Boolean isHit = Boolean.valueOf(false);
    boolean flag = true;
    try {
      skill = (RoleSkill)args[0];
      isHit = (Boolean)args[1];
      flag = ((Boolean)args[5]).booleanValue();
    } catch (Exception e) {
      return;
    } 
    if (skill == null)
      return; 
    if (isHit == null || !isHit.booleanValue())
      return; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 73323);
    if (flag && SKILLATTR.SKILLATTR_ATTACK.getValue() == skill.getType().intValue()) {
      if (effecter.isPlayer()) {
        skillRecord.setT(target.getId().intValue());
        target.getStatusMap().setv("1008perpetrator", "73323_" + effecter.getId());
      } 
    } else if (!flag && SKILLATTR.SKILLATTR_WULI.getValue() == skill.getType().intValue() && 
      effecter.getStatusValue(1008) > 0) {
      int unitId = effecter.getStatusSubMap().getInt("1008target");
      if (unitId <= 0)
        return; 
      FightObject unit = fight.queryFightObject(Integer.valueOf(unitId));
      if (unit == null || !unit.isPlayer())
        return; 
      List<Integer> acsrySkillList = ((PlayerFightObject)unit).getAcsrySkillList();
      if (acsrySkillList.contains(Integer.valueOf(73323))) {
        int oldMana = target.getCurrMana();
        if (oldMana <= 0)
          return; 
        int changeMana = 1;
        if (oldMana > 9) {
          double percent = 0.2D;
          String str = effecter.getStatusMap().getString("1008perpetrator", null);
          if (str != null)
            try {
              String[] splits = str.split("_");
              FightObject fightObject = fight.queryFightObject(Integer.valueOf(Integer.parseInt(splits[1])));
              percent += (fightObject.getRole().getMj().intValue() / 50) * 0.01D;
            } catch (Exception e) {
              e.printStackTrace();
            }  
          changeMana = (int)(oldMana * percent);
        } 
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), false);
        if (targetParam != null) {
          PassiveSkillUtils.excuteChangeManaAuto(fight, target, actionDetailParam, targetParam, -changeMana);
          skillRecord.setT(target.getId().intValue());
        } 
      } 
    } 
  }
  
  public NutMap executeReduceHp(Fight fight, FightObject effecter, FightObject target, Object... args) {
    int hurt = 0;
    try {
      hurt = ((Integer)args[1]).intValue();
    } catch (Exception e) {
      return null;
    } 
    if (hurt > 0) {
      double percent = 0.1D;
      String str = effecter.getStatusMap().getString("1008perpetrator", null);
      if (str != null)
        try {
          String[] splits = str.split("_");
          FightObject fightObject = fight.queryFightObject(Integer.valueOf(Integer.parseInt(splits[1])));
          percent += (fightObject.getRole().getMj().intValue() / 50) * 0.003D;
        } catch (Exception e) {
          e.printStackTrace();
        }  
      int changeHurt = (int)(hurt * percent);
      if (changeHurt > 0) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 73323);
        skillRecord.setT(target.getId().intValue());
        return NutMap.NEW().setv("changeHurt", Integer.valueOf(changeHurt));
      } 
    } 
    return null;
  }
}
