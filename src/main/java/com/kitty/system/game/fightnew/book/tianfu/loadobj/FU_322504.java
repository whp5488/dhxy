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
    public FU_322504() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (!target.isPlayer()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322504);
        if (tianFuObject == null) {
            return;
        }
        boolean flag = false;
        Integer theStatus = Integer.valueOf(-1);
        int attr = -1;
        for (Integer status : StatusConst.getControlStatus()) {
            if (target.getStatusValue((int)status) > 0) {
                flag = true;
                theStatus = status;
                switch ((int)theStatus) {
                    case 1002: {
                        attr = SKILLATTR.SKILLATTR_CONFUSE.getValue();
                        break;
                    }
                    case 1003: {
                        attr = SKILLATTR.SKILLATTR_ICE.getValue();
                        break;
                    }
                    case 1004: {
                        attr = SKILLATTR.SKILLATTR_SLEEP.getValue();
                        break;
                    }
                    case 1006: {
                        attr = SKILLATTR.SKILLATTR_YIWANG.getValue();
                        break;
                    }
                }
                break;
            }
            else {
                continue;
            }
        }
        if (!flag || attr == -1) {
            return;
        }
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (attr != (int)roleSkill.getType()) {
            return;
        }
        double percenta = 3.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
        if (flagTrigger) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 322504);
            target.getStatusMap().remove(String.valueOf(theStatus));
            FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), false);
            if (targetParam != null) {
                targetParam.addClearStatus((int)theStatus);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
}
