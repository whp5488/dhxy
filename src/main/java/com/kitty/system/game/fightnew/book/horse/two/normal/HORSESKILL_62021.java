package com.kitty.system.game.fightnew.book.horse.two.normal;

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

public class HORSESKILL_62021 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percentb = (7.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
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
    RoleSkill skill = (RoleSkill)args[2];
    if (skill == null)
      return null; 
    if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_THUNDER
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_TM
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_QM
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_AIHAO
      .getValue() == skill.getType().intValue()) {
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(62021));
      if (horseSkillParam == null)
        return null; 
      Integer hurt = (Integer)args[0];
      if (hurt == null)
        return null; 
      double percenta = (20.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
      percenta = HorseSkillUtils.getMathRoundPercent(percenta);
      int alterHurt = (int)(target.getCurrLife() * percenta);
      if (alterHurt != 0) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 62021);
        skillRecord.setT(target.getId().intValue());
        NutMap map = NutMap.NEW();
        map.setv("alterHurt", Integer.valueOf(alterHurt));
        return map;
      } 
    } 
    return null;
  }
}
