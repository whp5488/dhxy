package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323304 extends TianFuProcessor
{
    public FU_323304() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (fight.getRound() > 5 || fight.isPvp() != 1 || !effector.isPet() || effector.getType() != 8) {
            return null;
        }
        FightObject theTarget = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        if (!theTarget.isPlayer() && !theTarget.isPartner()) {
            return null;
        }
        FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
        DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323304);
        if (tianFuObject == null) {
            return null;
        }
        FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 323304);
        double percenta = (45.0 + 2.5 * (double)tianFuObject.getLevel()) * (1.0 + (double)dayInCeFight.getLevel() * 0.002) / 100.0;
        double percentb = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_PERCENT.getDist()), Double.valueOf(percenta));
        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_CHANGE_PERCENT.getDist()), Double.valueOf(percentb));
        return null;
    }
}
