package com.kitty.system.game.fightnew.book.horse.three.normal;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_63001 extends HorseSkillProcessor
{
    public HORSESKILL_63001() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        NutMap map = NutMap.NEW();
        double percentb = (10.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        double percentc = (20.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        percentc = HorseSkillUtils.getMathRoundPercent(percentc);
        if (percentc != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), Double.valueOf(percentc));
        }
        if (percentb != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percentb));
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
        double percenta = (12.5 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
}
