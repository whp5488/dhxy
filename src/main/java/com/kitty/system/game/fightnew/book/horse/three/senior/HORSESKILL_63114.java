package com.kitty.system.game.fightnew.book.horse.three.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_63114 extends HorseSkillProcessor
{
    public HORSESKILL_63114() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        if (target.isPet()) {
            PetFightObject petTarget = (PetFightObject)target;
            Integer hurt = (Integer)args[0];
            if (hurt == null) {
                return null;
            }
            HorseSkillParam horseSkillParam = (HorseSkillParam)petTarget.getHorseSkillMap().get(Integer.valueOf(63114));
            if (horseSkillParam == null) {
                return null;
            }
            double percenta = 20.0 * this.getPercent(horseSkillParam) / 100.0;
            percenta = HorseSkillUtils.getMathRoundPercent(percenta);
            boolean flag = SkillAi.canAffectOnPro(percenta);
            if (flag) {
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 63114);
                skillRecord.setT((long)(int)target.getId());
                NutMap map = NutMap.NEW();
                int alterHurt = (int)hurt - 1;
                map.setv("alterHurt", Integer.valueOf(-alterHurt));
                return map;
            }
        }
        return null;
    }
    
    private double getPercent(HorseSkillParam horseSkillParam) {
        double percent = (Math.floor(((double)horseSkillParam.getGrowUp() + 10000.0) / 11265.0) * (-0.7 * (double)horseSkillParam.getGrowUp() + 885.5) + (double)horseSkillParam.getGrowUp()) / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(Math.floor(((double)horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.7 * (double)horseSkillParam.getLevel() + 56.0) + (double)horseSkillParam.getLevel(), 2.0) + 2.06 * (Math.floor(((double)horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.7 * (double)horseSkillParam.getLevel() + 56.0) + (double)horseSkillParam.getLevel())) / 130.0 + (-0.0067 * Math.pow(Math.floor(((double)horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.7 * (double)horseSkillParam.getProficient() + 56000.0) + (double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (Math.floor(((double)horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.7 * (double)horseSkillParam.getProficient() + 56000.0) + (double)horseSkillParam.getProficient())) / 325000.0 + 0.1) + 0.2;
        return percent;
    }
}
