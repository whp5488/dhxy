package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61010 extends HorseSkillProcessor
{
    public HORSESKILL_61010() {
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
        double percent = 0.2;
        NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(-percent));
        double percentb = (30.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percentb));
            return map;
        }
        return map;
    }
}
