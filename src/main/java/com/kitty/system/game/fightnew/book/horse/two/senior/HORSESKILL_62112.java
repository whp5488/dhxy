package com.kitty.system.game.fightnew.book.horse.two.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_62112 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percentb = (7.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    if (percentb > 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(percentb));
      return map;
    } 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    Integer hurt = (Integer)args[0];
    if (hurt == null)
      return null; 
    double wx_s = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_S.getDist());
    if (wx_s >= 50.0D) {
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(62112));
      if (horseSkillParam == null)
        return null; 
      RoleSkill skill = (RoleSkill)args[2];
      if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_THUNDER
        .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_TM
        .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_QM
        .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_AIHAO
        .getValue() == skill.getType().intValue()) {
        double percenta = (40.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0D) {
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 62112);
          skillRecord.setT(target.getId().intValue());
          NutMap map = NutMap.NEW();
          int alterHurt = (int)(hurt.intValue() * percenta);
          map.setv("alterHurt", Integer.valueOf(alterHurt));
          return map;
        } 
      } 
    } 
    return null;
  }
}
