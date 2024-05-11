
package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_322504 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (!target.isPlayer()) {
            return;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322504);
        if (tianFuObject == null) {
            return;
        }
        boolean flag = false;
        Integer theStatus = -1;
        int attr = -1;
    Label_0186:
        for (final Integer status : StatusConst.getControlStatus()) {
            if (target.getStatusValue(status) > 0) {
                flag = true;
                theStatus = status;
                switch (theStatus) {
                    case 1002: {
                        attr = SKILLATTR.SKILLATTR_CONFUSE.getValue();
                        break Label_0186;
                    }
                    case 1003: {
                        attr = SKILLATTR.SKILLATTR_ICE.getValue();
                        break Label_0186;
                    }
                    case 1004: {
                        attr = SKILLATTR.SKILLATTR_SLEEP.getValue();
                        break Label_0186;
                    }
                    case 1006: {
                        attr = SKILLATTR.SKILLATTR_YIWANG.getValue();
                        break Label_0186;
                    }
                    default: {
                        break Label_0186;
                    }
                }
            }
        }
        if (!flag || attr == -1) {
            return;
        }
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (attr != roleSkill.getType()) {
            return;
        }
        final double percenta = 3.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        final boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
        if (flagTrigger) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 322504);
            target.getStatusMap().remove((Object)String.valueOf(theStatus));
            final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
            if (targetParam != null) {
                targetParam.addClearStatus(theStatus);
                skillRecord.setT(target.getId());
            }
        }
    }
}
