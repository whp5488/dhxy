
package com.kitty.system.game.fightnew.book.horse.two.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
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

public class HORSESKILL_62121 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        double percentb = (7.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb > 0.0) {
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
        final RoleSkill skill = (RoleSkill)args[2];
        if (skill == null) {
            return null;
        }
        if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_THUNDER.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_TM.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_QM.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_AIHAO.getValue() == skill.getType()) {
            final HorseSkillParam horseSkillParam = effector.getHorseSkillMap().get(62121);
            if (horseSkillParam == null) {
                return null;
            }
            final Integer hurt = (Integer)args[0];
            if (hurt == null) {
                return null;
            }
            double percenta = (20.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
            percenta = HorseSkillUtils.getMathRoundPercent(percenta);
            final int alterHurt = (int)(target.getCurrLife() * percenta);
            if (alterHurt != 0) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 62121);
                skillRecord.setT(target.getId());
                final NutMap map = NutMap.NEW();
                map.setv("alterHurt", (Object)alterHurt);
                return map;
            }
        }
        return null;
    }
}
