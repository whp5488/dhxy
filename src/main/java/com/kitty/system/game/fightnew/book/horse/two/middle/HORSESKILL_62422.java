
package com.kitty.system.game.fightnew.book.horse.two.middle;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_62422 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        double percentb = (16.8 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        double percentc = (8.4 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        double percentd = (7.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        double percente = (29.25 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        percentc = HorseSkillUtils.getMathRoundPercent(percentc);
        percentd = HorseSkillUtils.getMathRoundPercent(percentd);
        percente = HorseSkillUtils.getMathRoundPercent(percente);
        final NutMap map = NutMap.NEW();
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), (Object)percentb);
        }
        if (percentc != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), (Object)percentb);
        }
        if (percentd != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), (Object)percentb);
        }
        if (percente != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist()), (Object)percente);
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
        double percenta = (18.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), (Object)percenta);
            return map;
        }
        return null;
    }
}
