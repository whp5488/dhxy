
package com.kitty.system.game.fightnew.book.horse.two.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.Random;
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

public class HORSESKILL_62120 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        double percentc = (7.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        percentc = HorseSkillUtils.getMathRoundPercent(percentc);
        if (percentc > 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), (Object)percentc);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), (Object)percentc);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), (Object)percentc);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), (Object)percentc);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), (Object)percentc);
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
        if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() != skill.getType() && PETSKILLATTR.PETSKILLATTR_WIND.getValue() != skill.getType() && PETSKILLATTR.PETSKILLATTR_THUNDER.getValue() != skill.getType() && PETSKILLATTR.PETSKILLATTR_WATER.getValue() != skill.getType() && PETSKILLATTR.PETSKILLATTR_TM.getValue() != skill.getType() && PETSKILLATTR.PETSKILLATTR_FG.getValue() != skill.getType() && PETSKILLATTR.PETSKILLATTR_QM.getValue() != skill.getType() && PETSKILLATTR.PETSKILLATTR_XL.getValue() != skill.getType() && PETSKILLATTR.PETSKILLATTR_AIHAO.getValue() != skill.getType()) {
            return null;
        }
        final HorseSkillParam horseSkillParam = effector.getHorseSkillMap().get(62120);
        if (horseSkillParam == null) {
            return null;
        }
        final Integer hurt = (Integer)args[0];
        if (hurt == null) {
            return null;
        }
        double percenta = (50.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) * 10.0;
        double percentb = (100.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) * 10.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        final double count = percenta + percentb;
        final Random random = new Random();
        final int i = random.nextInt((int)count);
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 62120);
        skillRecord.setT(effector.getId());
        if (i >= percenta) {
            final double percent_b = percentb / 1000.0;
            final NutMap map = NutMap.NEW();
            final int alterHurt = (int)(hurt * percent_b);
            map.setv("alterHurt", (Object)alterHurt);
            return map;
        }
        final double percent_a = percenta / 1000.0;
        final NutMap map = NutMap.NEW();
        final int alterHurt = (int)(hurt * percent_a);
        map.setv("alterHurt", (Object)(-alterHurt));
        return map;
    }
}
