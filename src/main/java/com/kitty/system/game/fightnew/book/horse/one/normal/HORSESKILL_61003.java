package com.kitty.system.game.fightnew.book.horse.one.normal;

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

public class HORSESKILL_61003 extends HorseSkillProcessor
{
    public HORSESKILL_61003() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
        if (pet == null) {
            return null;
        }
        NutMap map = NutMap.NEW();
        int maxMana = (int)pet.getMaxMana();
        double percent = HorseSkillUtils.getPercent(horseSkillParam);
        double percenta = (10.0 * percent + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        int value = BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf((double)maxMana * percenta));
        if (value != 0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist()), Integer.valueOf(value));
            return map;
        }
        return null;
    }
}
