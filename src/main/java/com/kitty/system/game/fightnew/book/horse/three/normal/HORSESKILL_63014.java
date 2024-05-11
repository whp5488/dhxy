
package com.kitty.system.game.fightnew.book.horse.three.normal;

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

public class HORSESKILL_63014 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        if (target.isPet()) {
            final PetFightObject petTarget = (PetFightObject)target;
            final Integer hurt = (Integer)args[0];
            if (hurt == null) {
                return null;
            }
            final HorseSkillParam horseSkillParam = petTarget.getHorseSkillMap().get(63014);
            if (horseSkillParam == null) {
                return null;
            }
            double percenta = (20.0 * this.getPercent(horseSkillParam) + 0.1) / 100.0;
            percenta = HorseSkillUtils.getMathRoundPercent(percenta);
            final boolean flag = SkillAi.canAffectOnPro(percenta);
            if (flag) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 63014);
                skillRecord.setT(target.getId());
                final NutMap map = NutMap.NEW();
                final int alterHurt = hurt - 1;
                map.setv("alterHurt", (Object)(-alterHurt));
                return map;
            }
        }
        return null;
    }
    
    private double getPercent(final HorseSkillParam horseSkillParam) {
        final double percent = (Math.floor((horseSkillParam.getGrowUp() + 10000.0) / 11265.0) * (-0.7 * horseSkillParam.getGrowUp() + 885.5) + horseSkillParam.getGrowUp()) / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(Math.floor((horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.7 * horseSkillParam.getLevel() + 56.0) + horseSkillParam.getLevel(), 2.0) + 2.06 * (Math.floor((horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.7 * horseSkillParam.getLevel() + 56.0) + horseSkillParam.getLevel())) / 130.0 + (-0.0067 * Math.pow(Math.floor((horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.7 * horseSkillParam.getProficient() + 56000.0) + horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (Math.floor((horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.7 * horseSkillParam.getProficient() + 56000.0) + horseSkillParam.getProficient())) / 325000.0 + 0.1) * 0.6;
        return percent;
    }
}
