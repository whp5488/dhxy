package com.kitty.system.game.fightnew.book.horse.three.senior;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_63118 extends HorseSkillProcessor
{
    public HORSESKILL_63118() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percentb = (7.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(percentb));
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
        double percenta = (10.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
}
