
package com.kitty.system.game.fightnew.utils;

import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import org.apache.commons.lang3.ArrayUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.FightObject;
import java.util.Random;

public class FightRateUtil
{
    private static Integer[] magicRate;
    
    public static int getRandHurt(final int power) {
        final Random random = new Random();
        final int per = random.nextInt(10) + 1;
        final int value = new Double(power * per * 0.0055).intValue();
        if (value <= 0) {
            return 1;
        }
        return random.nextInt(value);
    }
    
    public static boolean isHit(final int score, final int maxScore) {
        final Random random = new Random();
        final int value = random.nextInt(maxScore);
        return value <= score;
    }
    
    public static boolean petCanRevive(final FightObject fightObject) {
        return fightObject == null && false;
    }
    
    public static int getZhangAiRate(final Fight fight, final FightObject attacker, final FightObject target) {
        return 100;
    }
    
    public static boolean calcMagicStuntRate(final FightObject fightObject, final RoleSkill skill) {
        final Random random = new Random();
        if (ArrayUtils.contains((Object[])FightRateUtil.magicRate, (Object)skill.getType())) {
            if (fightObject.isPlayer() || fightObject.isPet()) {
                return triggerRate(fightObject, skill.getType());
            }
            if (random.nextInt(100) + 1 <= 5) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean triggerRate(final FightObject fightObject, final int type) {
        double pro = 0.0;
        if (type == SKILLATTR.SKILLATTR_WIND.getValue()) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.F_KB_RATIO.getDist());
        }
        else if (type == SKILLATTR.SKILLATTR_THUNDER.getValue()) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.L_KB_RATIO.getDist());
        }
        else if (type == SKILLATTR.SKILLATTR_WATER.getValue()) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.S_KB_RATIO.getDist());
        }
        else if (type == SKILLATTR.SKILLATTR_FIRE.getValue()) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.H_KB_RATIO.getDist());
        }
        else if (type == SKILLATTR.SKILLATTR_XIXUE.getValue()) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.SS_KB_RATIO.getDist());
        }
        else if (type == SKILLATTR.SKILLATTR_AIHAO.getValue()) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.GH_KB_RATIO.getDist());
        }
        else if (type == SKILLATTR.SKILLATTR_HENGSAO.getValue() || type == SKILLATTR.SKILLATTR_POJIA.getValue() || type == SKILLATTR.SKILLATTR_ZHENJI.getValue()) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
        }
        else if (fightObject.isPet() && PETSKILLATTR.PETSKILLATTR_TM.getValue() == type) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.TM_KB_RATIO.getDist());
        }
        else if (fightObject.isPet() && PETSKILLATTR.PETSKILLATTR_FG.getValue() == type) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.FG_KB_RATIO.getDist());
        }
        else if (fightObject.isPet() && PETSKILLATTR.PETSKILLATTR_QM.getValue() == type) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.QM_KB_RATIO.getDist());
        }
        else if (fightObject.isPet() && PETSKILLATTR.PETSKILLATTR_XL.getValue() == type) {
            pro = fightObject.getSelfAllTempPro(PRO_DICT_TYPE.XL_KB_RATIO.getDist());
        }
        return SkillAi.canAffectOnPro(pro);
    }
    
    public static boolean isPetSkillHit(final FightObject effector, final FightObject target) {
        return isHit(10, 100);
    }
    
    static {
        FightRateUtil.magicRate = new Integer[] { SKILLATTR.SKILLATTR_WIND.getValue(), SKILLATTR.SKILLATTR_THUNDER.getValue(), SKILLATTR.SKILLATTR_WATER.getValue(), SKILLATTR.SKILLATTR_FIRE.getValue(), SKILLATTR.SKILLATTR_XIXUE.getValue(), SKILLATTR.SKILLATTR_AIHAO.getValue(), PETSKILLATTR.PETSKILLATTR_TM.getValue(), PETSKILLATTR.PETSKILLATTR_FG.getValue(), PETSKILLATTR.PETSKILLATTR_QM.getValue(), PETSKILLATTR.PETSKILLATTR_XL.getValue() };
    }
}
