
package com.kitty.system.common.formula;

import com.kitty.system.init.pool.ExpDataPool;
import com.kitty.system.init.domain.SysSkillUpGradeConsume;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.LOGIC;

public class FormulaHero
{
    public static long formulaHeroLvUpExp(final Integer nlv, final Integer tlv, final Integer zs) {
        final long needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), zs, nlv);
        return needExp;
    }
    
    public static long formula_petLvUpExp(final Integer limit, final Integer nlv, final Integer tlv, final Integer zs) {
        final long needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_PET.getValue(), zs, nlv);
        return needExp;
    }
    
    public static Integer formulaSkillUpMoney(final Integer hasExp, final Integer skillId) {
        Integer costMoney = 0;
        final SysSkill skill = SkillDataPool.skillMap.get(skillId);
        final SysSkillUpGradeConsume consume = ExpDataPool.skillUpGradeConsume.get(hasExp / 100);
        if (skill != null && consume != null) {
            costMoney = ((skill.getStep() == 4) ? consume.getAlone() : consume.getMore());
        }
        return costMoney;
    }
    
    public static int restorePorCount(final int rel, final int lv, final int logic) {
        if (rel <= 0) {
            return lv * 4;
        }
        if (logic == LOGIC.LOGICTYPE_HERO.getValue()) {
            return rel * 100 + lv * 4;
        }
        if (logic == LOGIC.LOGICTYPE_PET.getValue()) {
            return rel * 30 + lv * 4;
        }
        System.out.println("重置全部点数错误类型为 = " + logic);
        return -1;
    }
}
