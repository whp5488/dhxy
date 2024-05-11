
package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61008 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        double percenta = (15.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        final NutMap map = NutMap.NEW();
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), (Object)percenta);
        }
        final int add = (int)(2.0 * (horseSkillParam.getGrowUp() / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(horseSkillParam.getLevel(), 2.0) + 2.06 * horseSkillParam.getLevel()) / 130.0 + (-0.0067 * Math.pow((double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * horseSkillParam.getProficient()) / 325000.0 + 0.1)) + 1.0);
        if (add > 0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist()), (Object)add);
        }
        if (map.size() <= 0) {
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
        final double percent = HorseSkillUtils.getPercent(horseSkillParam);
        double percentb = (10.0 * percent + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb != 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), (Object)percentb);
            return map;
        }
        return null;
    }
}
