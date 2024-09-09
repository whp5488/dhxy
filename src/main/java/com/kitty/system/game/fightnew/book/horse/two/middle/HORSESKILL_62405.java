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

public class HORSESKILL_62405 extends HorseSkillProcessor
{
    public HORSESKILL_62405() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percentb = (15.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(percentb));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
        double percenta = (10.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
}
