package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321503 extends TianFuProcessor
{
    public FU_321503() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (effector.isPlayer() && effector.getType() == 0) {
            DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321503);
            if (tianFuObject == null) {
                return null;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 321503);
            double percenta = 2.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flagA = SkillAi.canAffectOnPro(percenta);
            if (flagA) {
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FURY.getDist()), Integer.valueOf(3));
            }
            else {
                effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FURY.getDist()));
            }
            double percentb = 3.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flagB = SkillAi.canAffectOnPro(percentb);
            if (flagB) {
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist()), Double.valueOf(1.2));
            }
            else {
                effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist()));
            }
            skillRecord.setT((long)(int)target.getId());
        }
        return null;
    }
}
