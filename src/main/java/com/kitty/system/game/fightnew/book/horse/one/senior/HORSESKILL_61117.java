
package com.kitty.system.game.fightnew.book.horse.one.senior;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61117 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        final NutMap map = NutMap.NEW();
        final double percent = HorseSkillUtils.getPercentSenior(horseSkillParam);
        double percenta = (8.0 * percent + 0.2) / 100.0;
        double percentb = (10.0 * percent + 0.2) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), (Object)percenta);
        }
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), (Object)percentb);
        }
        if (map.size() > 0) {
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap studyExcute(final Pet pet, final HorseSkillParam horseSkillParam, final Object... obj) {
        final double pow = Math.pow(horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * horseSkillParam.getLevel()) * (0.6 * horseSkillParam.getLevel() + 0.4 * horseSkillParam.getProficient() / 1000.0), 2.0);
        double percenta = ((2.0E-4 * pow + 0.1 * (horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * horseSkillParam.getLevel()) * (0.6 * horseSkillParam.getLevel() + 0.4 * horseSkillParam.getProficient() / 1000.0))) * 0.6 + 0.2) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), (Object)percenta);
            return map;
        }
        return null;
    }
}
