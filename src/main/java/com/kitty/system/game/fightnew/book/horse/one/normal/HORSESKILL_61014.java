package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61014 extends HorseSkillProcessor
{
    public HORSESKILL_61014() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        if (effector.getType() != 0) {
            return null;
        }
        Integer hurt = (Integer)args[0];
        if (hurt == null) {
            return null;
        }
        double wx_h = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
        if (wx_h >= 50.0) {
            HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(61014));
            if (horseSkillParam == null) {
                return null;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 61014);
            double percenta = (40.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
            percenta = HorseSkillUtils.getMathRoundPercent(percenta);
            if (percenta != 0.0) {
                NutMap map = NutMap.NEW();
                int alterHurt = (int)((double)(int)hurt * percenta);
                map.setv("alterHurt", Integer.valueOf(alterHurt));
                skillRecord.setT((long)(int)target.getId());
                return map;
            }
        }
        return null;
    }
}
