
package com.kitty.system.game.fightnew.book.horse.four.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_64303 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap studyExcute(final Pet pet, final HorseSkillParam horseSkillParam, final Object... obj) {
        double percenta = this.getPercenta(horseSkillParam) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), (Object)percenta);
            return map;
        }
        return null;
    }
    
    private double getPercenta(final HorseSkillParam horseSkillParam) {
        final double percent = (2.0E-4 * Math.pow(horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * horseSkillParam.getLevel()) * (0.6 * horseSkillParam.getLevel() + 0.4 * horseSkillParam.getProficient() / 1000.0), 2.0) + 0.1 * (horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * horseSkillParam.getLevel()) * (0.6 * horseSkillParam.getLevel() + 0.4 * horseSkillParam.getProficient() / 1000.0))) * 0.8 + 0.1;
        return percent;
    }
}
