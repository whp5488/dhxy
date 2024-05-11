
package com.kitty.system.game.fightnew.book.horse.two.middle;

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

public class HORSESKILL_62401 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        double percentb = (10.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb != 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), (Object)percentb);
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = (RoleSkill)args[0];
        if (skill == null) {
            return null;
        }
        if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_THUNDER.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_TM.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_QM.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_AIHAO.getValue() == skill.getType()) {
            final HorseSkillParam horseSkillParam = effector.getHorseSkillMap().get(62401);
            if (horseSkillParam == null) {
                return null;
            }
            double percent = (35.0 * HorseSkillUtils.getPercentBeforeMagicMiddle(horseSkillParam) + 0.1) / 100.0;
            percent = HorseSkillUtils.getMathRoundPercent(percent);
            final boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 62401);
                skillRecord.setT(effector.getId());
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()), (Object)1);
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()), (Object)1);
            }
        }
        return null;
    }
}
