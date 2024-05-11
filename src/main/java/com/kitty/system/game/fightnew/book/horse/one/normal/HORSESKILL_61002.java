
package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61002 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        final NutMap map = NutMap.NEW();
        final double percent = HorseSkillUtils.getPercent(horseSkillParam);
        double percenta = (5.0 * percent + 0.1) / 100.0;
        double percentb = (35.0 * percent + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), (Object)percenta);
        }
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), (Object)percenta);
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
}
