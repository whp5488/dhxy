package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_74103 extends AcsrySkillProcessor {
  public NutMap countRecoverBloodEndSS(Fight fight, FightObject effecter, FightObject target, Object... args) {
    if (effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(74103))) {
        int life = 0;
        RoleSkill skill = null;
        try {
          life = ((Integer)args[0]).intValue();
          skill = (RoleSkill)args[1];
        } catch (Exception e) {
          e.printStackTrace();
        } 
        if (skill == null)
          return null; 
        if (SKILLATTR.SKILLATTR_XIXUE.getValue() == skill.getType().intValue()) {
          if (life > 0) {
            double percent = 1.0D;
            percent += (playerFightObject.getRole().getGg().intValue() / 100) * 0.1D;
            int addLife = (int)(life * percent);
            NutMap map = NutMap.NEW();
            map.setv("changeLife", Integer.valueOf(addLife));
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 74103);
            skillRecord.setT(target.getId().intValue());
            return map;
          } 
          return null;
        } 
      } 
    } 
    return null;
  }
}
