
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324415 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (effector.isPet() && fight.getRound() - effector.getComeRound() > 0) {
            final FightObject host = fight.queryFightObject(effector.getHostId());
            final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
            final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324415);
            if (tianFuObject == null) {
                return null;
            }
            final double percent = 0.2 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            double k_hl = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
            if (k_hl > 0.0) {
                k_hl = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hl), String.valueOf(1.0 + percent)));
            }
            else {
                k_hl = percent;
            }
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)k_hl);
            double k_fy = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
            if (k_fy > 0.0) {
                k_fy = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_fy), String.valueOf(1.0 + percent)));
            }
            else {
                k_fy = percent;
            }
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)k_fy);
            double k_hs = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
            if (k_hs > 0.0) {
                k_hs = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hs), String.valueOf(1.0 + percent)));
            }
            else {
                k_hs = percent;
            }
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), (Object)k_hs);
            double k_yw = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()));
            if (k_yw > 0.0) {
                k_yw = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_yw), String.valueOf(1.0 + percent)));
            }
            else {
                k_yw = percent;
            }
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)k_yw);
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324415);
            skillRecord.setT(effector.getId());
        }
        return null;
    }
}
