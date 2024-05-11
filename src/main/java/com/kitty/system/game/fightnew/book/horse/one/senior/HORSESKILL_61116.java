
package com.kitty.system.game.fightnew.book.horse.one.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61116 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        if (effector.getType() != 0) {
            return null;
        }
        if (args != null && args.length > 2) {
            final Boolean isFury = (Boolean)args[1];
            if (isFury == null || !isFury) {
                return null;
            }
            final Integer hurt = (Integer)args[0];
            if (hurt == null) {
                return null;
            }
            final HorseSkillParam horseSkillParam = effector.getHorseSkillMap().get(61116);
            if (horseSkillParam == null) {
                return null;
            }
            double percenta = (50.0 * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2) / 100.0;
            percenta = HorseSkillUtils.getMathRoundPercent(percenta);
            if (percenta != 0.0) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 61116);
                skillRecord.setT(effector.getId());
                final NutMap map = NutMap.NEW();
                final int alterHurt = (int)(hurt * percenta);
                map.setv("alterHurt", (Object)alterHurt);
                return map;
            }
        }
        return null;
    }
}
