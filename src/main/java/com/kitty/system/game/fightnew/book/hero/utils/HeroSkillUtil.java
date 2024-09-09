package com.kitty.system.game.fightnew.book.hero.utils;

import java.util.ArrayList;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.List;

public class HeroSkillUtil
{
    private static List<Integer> pjSkills;
    private static List<Integer> zjSkills;
    
    public HeroSkillUtil() {
    }
    
    public static void attackEnd(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam) {
        if (!HeroSkillUtil.zjSkills.contains(Integer.valueOf(effector.getSkillId())) && effector.getStatusValue(1020) > 0 && 7 == effector.getType()) {
            int status = 1020;
            int dict = PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist();
            if (7 == effector.getType() && effector.getStatusValue(status) > 0) {
                effector.setStatus(1020, 0);
                ImbueUtil.removeStatus(effector.getImbueMap(), dict, status);
                FightObjectParam attackerParam = actionDetailParam.gainAttackerParam();
                attackerParam.addClearStatus(1020);
            }
        }
    }
    
    public static void clearPj(FightObject target) {
        ImbueUtil.removeStatus(target.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), 1021);
    }
    
    static {
        HeroSkillUtil.pjSkills = new ArrayList<Integer>() {
            {
                this.add(Integer.valueOf(30033));
                this.add(Integer.valueOf(30034));
            }
        };
        HeroSkillUtil.zjSkills = new ArrayList<Integer>() {
            {
                this.add(Integer.valueOf(30039));
                this.add(Integer.valueOf(30040));
            }
        };
    }
}
