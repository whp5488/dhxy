
package com.kitty.system.game.fightnew.book.horse.two.normal;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_62019 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        double percentc = (13.5 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
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
        return null;
    }
    
    @Override
    public NutMap studyExcute(final Pet pet, final HorseSkillParam horseSkillParam, final Object... obj) {
        double percenta = ((2.0E-4 * Math.pow(horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * horseSkillParam.getLevel()) * (0.6 * horseSkillParam.getLevel() + 0.4 * horseSkillParam.getProficient() / 1000.0), 2.0) + 0.1 * (horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * horseSkillParam.getLevel()) * (0.6 * horseSkillParam.getLevel() + 0.4 * horseSkillParam.getProficient() / 1000.0))) * 0.6 + 0.1) / 100.0;
        double percentb = (10.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        final NutMap map = NutMap.NEW();
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), (Object)percenta);
        }
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), (Object)percentb);
        }
        if (map.size() > 0) {
            return map;
        }
        return null;
    }
}
