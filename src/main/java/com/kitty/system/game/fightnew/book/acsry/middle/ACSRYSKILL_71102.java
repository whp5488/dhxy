package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;

public class ACSRYSKILL_71102 extends AcsrySkillProcessor {
  public void beforeExecuteMagic(Fight fight, FightObject effecter, FightObject target, Object... args) {
    if (effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
      if (acsrySkillList.contains(Integer.valueOf(71102))) {
        RoleSkill skill = (RoleSkill)args[0];
        if (skill == null)
          return; 
        int property = getProperty(skill);
        if (property == 0)
          return; 
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 71102);
        double percent = 0.6D;
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
          String dictStr = String.valueOf(property);
          playerFightObject.getAcsryPropertyMap().setv(dictStr, Integer.valueOf(-1));
          playerFightObject.getAcsryPropertyMap().setv(dictStr + "value", Double.valueOf(0.03D));
          skillRecord.setT(target.getId().intValue());
        } 
      } 
    } 
  }
  
  private int getProperty(RoleSkill skill) {
    if (SKILLATTR.SKILLATTR_ICE.getValue() == skill.getType().intValue())
      return PRO_DICT_TYPE.OMIT_K_FY.getDist(); 
    if (SKILLATTR.SKILLATTR_SLEEP.getValue() == skill.getType().intValue())
      return PRO_DICT_TYPE.OMIT_K_HS.getDist(); 
    return 0;
  }
}
