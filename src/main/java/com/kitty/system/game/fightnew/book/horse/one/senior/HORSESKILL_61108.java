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

public class HORSESKILL_61108 extends HorseSkillProcessor
{
    public HORSESKILL_61108() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percenta = (15.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        NutMap map = NutMap.NEW();
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), Double.valueOf(percenta));
        }
        int add = (int)(4.0 * ((double)horseSkillParam.getGrowUp() / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow((double)horseSkillParam.getLevel(), 2.0) + 2.06 * (double)horseSkillParam.getLevel()) / 130.0 + (-0.0067 * Math.pow((double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (double)horseSkillParam.getProficient()) / 325000.0 + 0.1)) + 1.0);
        if (add > 0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist()), Integer.valueOf(add));
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
        double percentb = (10.0 * percent + 0.2) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percentb));
            return map;
        }
        return null;
    }
}
