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
import java.util.Random;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_62120 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percentc = (7.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
    percentc = HorseSkillUtils.getMathRoundPercent(percentc);
    if (percentc > 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(percentc));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(percentc));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(percentc));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(percentc));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(percentc));
      return map;
    } 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    RoleSkill skill = (RoleSkill)args[2];
    if (skill == null)
      return null; 
    if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_THUNDER
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_TM
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_QM
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_AIHAO
      .getValue() == skill.getType().intValue()) {
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(62120));
      if (horseSkillParam == null)
        return null; 
      Integer hurt = (Integer)args[0];
      if (hurt == null)
        return null; 
      double percenta = (50.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) * 10.0D;
      double percentb = (100.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) * 10.0D;
      percenta = HorseSkillUtils.getMathRoundPercent(percenta);
      percentb = HorseSkillUtils.getMathRoundPercent(percentb);
      double count = percenta + percentb;
      Random random = new Random();
      int i = random.nextInt((int)count);
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 62120);
      skillRecord.setT(effector.getId().intValue());
      if (i >= percenta) {
        double percent_b = percentb / 1000.0D;
        NutMap nutMap = NutMap.NEW();
        int j = (int)(hurt.intValue() * percent_b);
        nutMap.setv("alterHurt", Integer.valueOf(j));
        return nutMap;
      } 
      double percent_a = percenta / 1000.0D;
      NutMap map = NutMap.NEW();
      int alterHurt = (int)(hurt.intValue() * percent_a);
      map.setv("alterHurt", Integer.valueOf(-alterHurt));
      return map;
    } 
    return null;
  }
}
