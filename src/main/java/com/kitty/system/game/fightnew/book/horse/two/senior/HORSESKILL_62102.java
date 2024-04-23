package com.kitty.system.game.fightnew.book.horse.two.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_62102 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percentb = (10.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    if (percentb != 0.0D) {
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
    RoleSkill skill = (RoleSkill)args[0];
    if (skill == null)
      return null; 
    if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_THUNDER
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_TM
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_QM
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_AIHAO
      .getValue() == skill.getType().intValue()) {
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(62102));
      if (horseSkillParam == null)
        return null; 
      double percent = (18.0D * HorseSkillUtils.getPercentBeforeMagicSenior(horseSkillParam) + 0.2D) / 100.0D;
      percent = HorseSkillUtils.getMathRoundPercent(percent);
      boolean flag = SkillAi.canAffectOnPro(percent);
      if (flag) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 62102);
        skillRecord.setT(effector.getId().intValue());
        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()), Integer.valueOf(2));
        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()), Integer.valueOf(1));
      } 
    } 
    return null;
  }
}
