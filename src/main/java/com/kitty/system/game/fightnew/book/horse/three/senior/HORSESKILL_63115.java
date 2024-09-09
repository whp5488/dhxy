package com.kitty.system.game.fightnew.book.horse.three.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_63115 extends HorseSkillProcessor
{
    public HORSESKILL_63115() {
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
        double percenta = (12.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        double percentb = (5.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        NutMap map = NutMap.NEW();
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), Double.valueOf(percenta));
        }
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percentb));
        }
        if (map.size() > 0) {
            return map;
        }
        return null;
    }
}
