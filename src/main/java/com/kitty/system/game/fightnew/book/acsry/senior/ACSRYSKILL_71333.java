package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_71333 extends AcsrySkillProcessor {
  public NutMap executeReduceHp(Fight fight, FightObject effecter, FightObject target, Object... args) {
    if (effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
      if (acsrySkillList.contains(Integer.valueOf(71333))) {
        int hurt = 0;
        try {
          hurt = ((Integer)args[1]).intValue();
          if (hurt <= 0)
            return null; 
        } catch (Exception e) {
          return null;
        } 
        double percent = (playerFightObject.getRole().getGg().intValue() / 200) * 0.15D;
        if (percent > 0.0D) {
          int changeHurt = (int)(hurt * percent);
          if (changeHurt > 0) {
            target.getStatusMap().setv("1005perpetrator", "71333_" + effecter.getId());
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 71333);
            skillRecord.setT(target.getId().intValue());
            return NutMap.NEW().setv("changeHurt", Integer.valueOf(changeHurt));
          } 
        } 
      } 
    } 
    return null;
  }
  
  public NutMap poisonReduceHp(Fight fight, FightObject target, Object... args) {
    String perpetrator = target.getStatusMap().getString("1005perpetrator", null);
    if (perpetrator != null) {
      String[] splits = perpetrator.split("_");
      int skillSerial = 0;
      FightObject effecter = null;
      int hurt = 0;
      try {
        hurt = ((Integer)args[0]).intValue();
        skillSerial = Integer.parseInt(splits[0]);
        effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(splits[1])));
      } catch (NumberFormatException e) {
        return null;
      } 
      if (effecter == null || 71333 != skillSerial)
        return null; 
      if (hurt > 0) {
        double percent = (effecter.getRole().getGg().intValue() / 200) * 0.15D;
        int changeHurt = (int)(hurt * percent);
        if (changeHurt > 0) {
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, target.getId().intValue(), 71333);
          skillRecord.setT(target.getId().intValue());
          return NutMap.NEW().setv("changeHurt", Integer.valueOf(changeHurt));
        } 
      } 
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
    if (perpetrator != null && perpetrator.contains(String.valueOf(71333))) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 71333);
      skillRecord.setT(fightObject.getId().intValue());
      fightObject.getStatusMap().unset("1005perpetrator");
    } 
  }
}
