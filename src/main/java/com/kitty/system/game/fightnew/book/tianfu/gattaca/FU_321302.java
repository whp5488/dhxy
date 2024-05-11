
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuSkillUtils;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321302 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill roleSkill = (RoleSkill)args[1];
        final Effect effect = (Effect)args[2];
        if (!effector.isPlayer() || effect == null || roleSkill == null) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321302);
        if (tianFuObject == null) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321302);
        if (SKILLATTR.SKILLATTR_POISON.getValue() == roleSkill.getType() && target.getStatusValue(1005) > 0) {
            final int hurt = (int)args[0];
            double percentb = 0.6 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final double addPercent = this.addPercent(target, percentb, String.valueOf(OTHER_SKILL_DICT_TYPE.D_SUPERPOSITION_COUNT.getDist()));
            percentb += addPercent;
            skillRecord.setT(target.getId());
            return TianFuUtils.getHurtReturnMap(hurt, percentb);
        }
        if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == roleSkill.getType() && target.getStatusValue(1007) > 0) {
            double percenta = 0.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final double addPercent2 = this.addPercent(target, percenta, String.valueOf(OTHER_SKILL_DICT_TYPE.MH_SUPERPOSITION_COUNT.getDist()));
            percenta += addPercent2;
            TianFuSkillUtils.mhChangePro(target, 1007, effect.getTime(), percenta);
            skillRecord.setT(target.getId());
        }
        return null;
    }
    
    private double addPercent(final FightObject target, final double percent, final String superpositionStr) {
        final int maxSuerpositionCount = 4;
        int dSuerposition = target.getBeAttackedMap().getInt(superpositionStr, 0);
        if (dSuerposition > 0) {
            if (dSuerposition > maxSuerpositionCount) {
                dSuerposition = maxSuerpositionCount;
            }
            target.getBeAttackedMap().setv(superpositionStr, (Object)(++dSuerposition));
            return dSuerposition * percent;
        }
        target.getBeAttackedMap().setv(superpositionStr, (Object)(++dSuerposition));
        return percent;
    }
}
