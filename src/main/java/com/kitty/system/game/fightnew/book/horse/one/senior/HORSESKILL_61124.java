package com.kitty.system.game.fightnew.book.horse.one.senior;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61124 extends HorseSkillProcessor
{
    public HORSESKILL_61124() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        NutMap map = NutMap.NEW();
        double percentb = (10.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        double percentc = (6.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        percentc = HorseSkillUtils.getMathRoundPercent(percentc);
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(percentb));
        }
        if (percentc != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(percentb));
        }
        if (map.size() > 0) {
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
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
}
