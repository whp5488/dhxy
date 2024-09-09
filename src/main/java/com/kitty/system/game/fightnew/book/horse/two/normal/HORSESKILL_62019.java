package com.kitty.system.game.fightnew.book.horse.two.normal;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_62019 extends HorseSkillProcessor
{
    public HORSESKILL_62019() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percentc = (13.5 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percentc = HorseSkillUtils.getMathRoundPercent(percentc);
        if (percentc > 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(percentc));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(percentc));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(percentc));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(percentc));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(percentc));
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
        double percenta = ((2.0E-4 * Math.pow((double)horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * (double)horseSkillParam.getLevel()) * (0.6 * (double)horseSkillParam.getLevel() + 0.4 * (double)horseSkillParam.getProficient() / 1000.0), 2.0) + 0.1 * ((double)horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * (double)horseSkillParam.getLevel()) * (0.6 * (double)horseSkillParam.getLevel() + 0.4 * (double)horseSkillParam.getProficient() / 1000.0))) * 0.6 + 0.1) / 100.0;
        double percentb = (10.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        NutMap map = NutMap.NEW();
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(percenta));
        }
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), Double.valueOf(percentb));
        }
        if (map.size() > 0) {
            return map;
        }
        return null;
    }
}
