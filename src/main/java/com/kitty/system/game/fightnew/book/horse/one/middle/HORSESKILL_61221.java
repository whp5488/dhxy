package com.kitty.system.game.fightnew.book.horse.one.middle;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61221 extends HorseSkillProcessor
{
    public HORSESKILL_61221() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percentc = (16.8 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        double percentd = (8.4 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percentc = HorseSkillUtils.getMathRoundPercent(percentc);
        percentd = HorseSkillUtils.getMathRoundPercent(percentd);
        NutMap map = NutMap.NEW();
        if (percentc != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), Double.valueOf(percentc));
        }
        if (percentd != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percentd));
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
        NutMap map = NutMap.NEW();
        double percenta = (4.2 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        double percentb = (12.5 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), Double.valueOf(percenta));
        }
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percentb));
        }
        if (map.size() > 0) {
            return map;
        }
        return null;
    }
}
