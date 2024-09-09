package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61020 extends HorseSkillProcessor
{
    public HORSESKILL_61020() {
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
        if (target.getStatusValue(1002) > 0 || target.getStatusValue(1004) > 0 || target.getStatusValue(1006) > 0) {
            Integer hurt = (Integer)args[0];
            if ((int)hurt <= 0) {
                return null;
            }
            HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(61020));
            if (horseSkillParam == null) {
                return null;
            }
            double percenta = (40.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
            percenta = HorseSkillUtils.getMathRoundPercent(percenta);
            if (percenta != 0.0) {
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 61020);
                skillRecord.setT((long)(int)target.getId());
                int alterHurt = (int)((double)(int)hurt * percenta);
                NutMap map = NutMap.NEW();
                map.setv("alterHurt", Integer.valueOf(alterHurt));
                return map;
            }
        }
        return null;
    }
}
