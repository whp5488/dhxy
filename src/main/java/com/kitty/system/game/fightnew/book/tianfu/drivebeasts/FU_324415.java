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
    public FU_324415() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (effector.isPet() && fight.getRound() - effector.getComeRound() > 0) {
            FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
            DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
            TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324415);
            if (tianFuObject == null) {
                return null;
            }
            double percent = 0.2 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            double k_hl = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
            if (k_hl > 0.0) {
                k_hl = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hl), String.valueOf(1.0 + percent)));
            }
            else {
                k_hl = percent;
            }
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(k_hl));
            double k_fy = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
            if (k_fy > 0.0) {
                k_fy = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_fy), String.valueOf(1.0 + percent)));
            }
            else {
                k_fy = percent;
            }
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(k_fy));
            double k_hs = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
            if (k_hs > 0.0) {
                k_hs = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hs), String.valueOf(1.0 + percent)));
            }
            else {
                k_hs = percent;
            }
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(k_hs));
            double k_yw = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()));
            if (k_yw > 0.0) {
                k_yw = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_yw), String.valueOf(1.0 + percent)));
            }
            else {
                k_yw = percent;
            }
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(k_yw));
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324415);
            skillRecord.setT((long)(int)effector.getId());
        }
        return null;
    }
}
