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

public class HORSESKILL_61209 extends HorseSkillProcessor
{
    public HORSESKILL_61209() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percent = HorseSkillUtils.getPercentMiddle(horseSkillParam);
        double percentb = (7.0 * percent + 0.1) / 100.0;
        double percentd = (15.0 * percent + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        percentd = HorseSkillUtils.getMathRoundPercent(percentd);
        NutMap map = NutMap.NEW();
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), Double.valueOf(percentb));
        }
        if (percentd != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), Double.valueOf(percentd));
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
        double percent = HorseSkillUtils.getPercentMiddle(horseSkillParam);
        double percenta = (8.0 * percent + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
}
