package com.kitty.system.game.fightnew.book.horse.three.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_63109 extends HorseSkillProcessor
{
    public HORSESKILL_63109() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        if (effector.isPet()) {
            Integer hurt = (Integer)args[0];
            if (hurt == null) {
                return null;
            }
            double wx_h = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
            if (wx_h >= 50.0) {
                HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(63109));
                if (horseSkillParam == null) {
                    return null;
                }
                double percenta = (30.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
                percenta = HorseSkillUtils.getMathRoundPercent(percenta);
                if (percenta != 0.0) {
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 63109);
                    skillRecord.setT((long)(int)target.getId());
                    NutMap map = NutMap.NEW();
                    int alterHurt = (int)((double)(int)hurt * percenta);
                    map.setv("alterHurt", Integer.valueOf(-alterHurt));
                    return map;
                }
            }
        }
        return null;
    }
}
