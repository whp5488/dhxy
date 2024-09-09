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

public class HORSESKILL_61107 extends HorseSkillProcessor
{
    public HORSESKILL_61107() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percent = HorseSkillUtils.getPercentSenior(horseSkillParam);
        double percentb = (10.0 * percent + 0.2) / 100.0;
        double percentc = (8.0 * percent + 0.2) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        percentc = HorseSkillUtils.getMathRoundPercent(percentc);
        NutMap map = NutMap.NEW();
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), Double.valueOf(percentb));
        }
        if (percentc != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), Double.valueOf(percentc));
        }
        if (map.size() <= 0) {
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
        double percent = HorseSkillUtils.getPercentSenior(horseSkillParam);
        double percenta = (7.5 * percent + 0.2) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
}
