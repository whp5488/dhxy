
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324504 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (fight.isPvp() == 1) {
            final RoleSkill roleSkill = (RoleSkill)args[1];
            if (roleSkill == null || roleSkill.getSkillId() == null || roleSkill.getSkillId() < 1) {
                return null;
            }
            if (SKILLATTR.SKILLATTR_ZHEN.getValue() == roleSkill.getType()) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324504);
                final Double percent = this.getPercent(effector);
                if (percent == null) {
                    return null;
                }
                final ImbueParam imbueParam = ImbueUtil.gainProForSkill(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_ZS.getDist(), 324504, true);
                double value = imbueParam.getValue() + percent;
                if (value > 0.3) {
                    value = 0.3;
                }
                imbueParam.setValue(value);
                imbueParam.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
                skillRecord.setT(target.getId());
            }
        }
        return null;
    }
    
    private Double getPercent(final FightObject effector) {
        if (!effector.isPlayer()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324504);
        if (tianFuObject == null) {
            return null;
        }
        final double percent = 0.3 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        return percent;
    }
}
