package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.List;
import java.util.Map;

public class ACSRYSKILL_74303 extends AcsrySkillProcessor {
  public void beforeExecuteMagic(Fight fight, FightObject effecter, FightObject target, Object... args) {
    if (fight.getRound() > 1 && effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
      if (acsrySkillList.contains(Integer.valueOf(74303))) {
        RoleSkill skill = (RoleSkill)args[0];
        if (skill == null)
          return; 
        if (SKILLATTR.SKILLATTR_XIXUE.getValue() == skill.getType().intValue()) {
          Map<Integer, Integer> abnormalRoundMap = playerFightObject.getAbnormalRoundMap();
          int round = fight.getRound();
          round--;
          Integer status = abnormalRoundMap.get(Integer.valueOf(round));
          String skillRound = playerFightObject.getOtherMap().getString("acsrySkillRound_" + round, null);
          if (check(status, skillRound)) {
            String distStr = String.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist());
            playerFightObject.getAcsryPropertyMap().setv(distStr, Integer.valueOf(-1));
            playerFightObject.getAcsryPropertyMap().setv(distStr + "value", Double.valueOf(1.0D));
            String dictStr = String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist());
            playerFightObject.getAcsryPropertyMap().setv(dictStr, Integer.valueOf(-1));
            double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_SS.getDist());
            playerFightObject.getAcsryPropertyMap().setv(dictStr + "value", Double.valueOf(kx));
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 74303);
            skillRecord.setT(target.getId().intValue());
          } 
        } 
      } 
    } 
  }
  
  private boolean check(Integer status, String skillRound) {
    if (status != null && StatusConst.getControlStatus().contains(status))
      return false; 
    if (skillRound != null && skillRound.contains(String.valueOf(74303)))
      return false; 
    return true;
  }
}
