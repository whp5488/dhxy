
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

public class HORSESKILL_62408 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        double percentb = (7.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb != 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), (Object)percentb);
            map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), (Object)percentb);
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
        final double percent = 0.2;
        final NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), (Object)(-percent));
        double percenta = (35.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta > 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), (Object)percenta);
        }
        return map;
    }
}
