package com.kitty.system.game.fightnew.book.horse.two.normal;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_62001 extends HorseSkillProcessor
{
    public HORSESKILL_62001() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percentb = (10.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb != 0.0) {
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
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        RoleSkill skill = (RoleSkill)args[0];
        if (skill == null) {
            return null;
        }
        if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_THUNDER.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_TM.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_QM.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_AIHAO.getValue() == (int)skill.getType()) {
            HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(62001));
            if (horseSkillParam == null) {
                return null;
            }
            double percent = (35.0 * HorseSkillUtils.getPercentBeforeMagic(horseSkillParam) + 0.1) / 100.0;
            percent = HorseSkillUtils.getMathRoundPercent(percent);
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 62001);
                skillRecord.setT((long)(int)effector.getId());
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()), Integer.valueOf(1));
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()), Integer.valueOf(1));
            }
        }
        return null;
    }
}
