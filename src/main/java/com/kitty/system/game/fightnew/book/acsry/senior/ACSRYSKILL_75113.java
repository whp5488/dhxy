package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_75113 extends AcsrySkillProcessor {
  public NutMap beforeReduceHurtMp(Fight fight, FightObject effecter, FightObject target, Object... args) {
    if (target.isPlayer()) {
      RoleSkill skill = null;
      int mp = 0;
      try {
        skill = (RoleSkill)args[0];
        mp = ((Integer)args[1]).intValue();
        if (skill == null || mp <= 0)
          return null; 
      } catch (Exception e) {
        return null;
      } 
      if (SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType().intValue()) {
        PlayerFightObject playerTarget = (PlayerFightObject)target;
        List<Integer> acsrySkillList = playerTarget.getAcsrySkillList();
        if (acsrySkillList.contains(Integer.valueOf(75113))) {
          double percent = 0.3D;
          int changeMp = (int)(mp * percent);
          if (changeMp > 0) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 75113);
            skillRecord.setT(target.getId().intValue());
            return NutMap.NEW().setv("changeMana", Integer.valueOf(-changeMp));
          } 
        } 
      } 
    } 
    return null;
  }
}
