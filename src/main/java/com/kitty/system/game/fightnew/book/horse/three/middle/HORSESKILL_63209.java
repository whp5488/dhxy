
package com.kitty.system.game.fightnew.book.horse.three.middle;

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

public class HORSESKILL_63209 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        if (effector.isPet()) {
            final Integer hurt = (Integer)args[0];
            if (hurt == null) {
                return null;
            }
            final double wx_h = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
            if (wx_h >= 50.0) {
                final HorseSkillParam horseSkillParam = effector.getHorseSkillMap().get(63209);
                if (horseSkillParam == null) {
                    return null;
                }
                double percenta = (30.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
                percenta = HorseSkillUtils.getMathRoundPercent(percenta);
                if (percenta != 0.0) {
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 63209);
                    skillRecord.setT(target.getId());
                    final NutMap map = NutMap.NEW();
                    final int alterHurt = (int)(hurt * percenta);
                    map.setv("alterHurt", (Object)(-alterHurt));
                    return map;
                }
            }
        }
        return null;
    }
}
