package com.kitty.system.common.formula;

import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.SysSkillUpGradeConsume;
import com.kitty.system.init.pool.ExpDataPool;

public class FormulaHero {
  public static long formulaHeroLvUpExp(Integer nlv, Integer tlv, Integer zs) {
    long needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), zs.intValue(), nlv.intValue());
    return needExp;
  }
  
  public static long formula_petLvUpExp(Integer limit, Integer nlv, Integer tlv, Integer zs) {
    long needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_PET.getValue(), zs.intValue(), nlv.intValue());
    return needExp;
  }
  
  public static Integer formulaSkillUpMoney(Integer hasExp, Integer skillId) {
    Integer costMoney = Integer.valueOf(0);
    SysSkill skill = (SysSkill)SkillDataPool.skillMap.get(skillId);
    SysSkillUpGradeConsume consume = (SysSkillUpGradeConsume)ExpDataPool.skillUpGradeConsume.get(Integer.valueOf(hasExp.intValue() / 100));
    if (skill != null && consume != null)
      costMoney = (skill.getStep().intValue() == 4) ? consume.getAlone() : consume.getMore(); 
    return costMoney;
  }
  
  public static int restorePorCount(int rel, int lv, int logic) {
    if (rel <= 0)
      return lv * 4; 
    if (logic == LOGIC.LOGICTYPE_HERO.getValue())
      return rel * 100 + lv * 4; 
    if (logic == LOGIC.LOGICTYPE_PET.getValue())
      return rel * 30 + lv * 4; 
    System.out.println("重置全部点数错误类型为 = " + logic);
    return -1;
  }
}
