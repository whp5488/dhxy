
package com.kitty.system.game.fightnew.book.horse.one.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61103 extends HorseSkillProcessor
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
        if (pet == null) {
            return null;
        }
        final NutMap map = NutMap.NEW();
        final int maxMana = pet.getMaxMana();
        final double percent = HorseSkillUtils.getPercentSenior(horseSkillParam);
        double percenta = (10.0 * percent + 0.2) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        final int value = BigDecimalUtil.decimalScaleInteger(0, maxMana * percenta);
        if (value != 0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist()), (Object)value);
            return map;
        }
        return null;
    }
}
