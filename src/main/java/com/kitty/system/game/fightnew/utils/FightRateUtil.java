package com.kitty.system.game.fightnew.utils;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;

public class FightRateUtil {
  private static Integer[] magicRate = new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_WIND.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_THUNDER.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_WATER.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_FIRE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_XIXUE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_AIHAO.getValue()), 
      Integer.valueOf(PETSKILLATTR.PETSKILLATTR_TM.getValue()), Integer.valueOf(PETSKILLATTR.PETSKILLATTR_FG.getValue()), Integer.valueOf(PETSKILLATTR.PETSKILLATTR_QM.getValue()), Integer.valueOf(PETSKILLATTR.PETSKILLATTR_XL.getValue()) };
  
  public static int getRandHurt(int power) {
    Random random = new Random();
    int per = random.nextInt(10) + 1;
    int value = (new Double((power * per) * 0.0055D)).intValue();
    if (value <= 0)
      return 1; 
    return random.nextInt(value);
  }
  
  public static boolean isHit(int score, int maxScore) {
    Random random = new Random();
    int value = random.nextInt(maxScore);
    return (value <= score);
  }
  
  public static boolean petCanRevive(FightObject fightObject) {
    if (fightObject == null)
      return false; 
    return false;
  }
  
  public static int getZhangAiRate(Fight fight, FightObject attacker, FightObject target) {
    return 100;
  }
  
  public static boolean calcMagicStuntRate(FightObject fightObject, RoleSkill skill) {
    Random random = new Random();
    if (ArrayUtils.contains((Object[])magicRate, skill.getType())) {
      if (fightObject.isPlayer() || fightObject.isPet())
        return triggerRate(fightObject, skill.getType().intValue()); 
      if (random.nextInt(100) + 1 <= 5)
        return true; 
    } 
    return false;
  }
  
  public static boolean triggerRate(FightObject fightObject, int type) {
    double pro = 0.0D;
    if (type == SKILLATTR.SKILLATTR_WIND.getValue()) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.F_KB_RATIO.getDist());
    } else if (type == SKILLATTR.SKILLATTR_THUNDER.getValue()) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.L_KB_RATIO.getDist());
    } else if (type == SKILLATTR.SKILLATTR_WATER.getValue()) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.S_KB_RATIO.getDist());
    } else if (type == SKILLATTR.SKILLATTR_FIRE.getValue()) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.H_KB_RATIO.getDist());
    } else if (type == SKILLATTR.SKILLATTR_XIXUE.getValue()) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.SS_KB_RATIO.getDist());
    } else if (type == SKILLATTR.SKILLATTR_AIHAO.getValue()) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.GH_KB_RATIO.getDist());
    } else if (type == SKILLATTR.SKILLATTR_HENGSAO.getValue() || type == SKILLATTR.SKILLATTR_POJIA.getValue() || type == SKILLATTR.SKILLATTR_ZHENJI.getValue()) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
    } else if (fightObject.isPet() && PETSKILLATTR.PETSKILLATTR_TM.getValue() == type) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.TM_KB_RATIO.getDist());
    } else if (fightObject.isPet() && PETSKILLATTR.PETSKILLATTR_FG.getValue() == type) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.FG_KB_RATIO.getDist());
    } else if (fightObject.isPet() && PETSKILLATTR.PETSKILLATTR_QM.getValue() == type) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.QM_KB_RATIO.getDist());
    } else if (fightObject.isPet() && PETSKILLATTR.PETSKILLATTR_XL.getValue() == type) {
      pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.XL_KB_RATIO.getDist());
    } 
    return SkillAi.canAffectOnPro(pro);
  }
  
  public static boolean isPetSkillHit(FightObject effector, FightObject target) {
    return isHit(10, 100);
  }
}
