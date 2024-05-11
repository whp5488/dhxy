
package com.kitty.system.game.fightnew.book.acsry.middle;

import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75122 extends AcsrySkillProcessor
{
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        RoleSkill skill = null;
        Boolean isHit = false;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
        }
        catch (final Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !isHit) {
            return;
        }
        if (SKILLATTR.SKILLATTR_WULI.getValue() == skill.getType() && effecter.isPlayer()) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 75122);
            final double percent = 0.1;
            final boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag && target.getStatusValue(1003) <= 0) {
                final int round = 1;
                target.setStatus(1002, round);
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
                if (targetParam != null) {
                    targetParam.getStatusMap().put(1002, round);
                }
                final List<Integer> list = this.clearStatus(target, skill.getType());
                if (list.size() > 0) {
                    skillRecord.setT(target.getId());
                    targetParam.addClearStatus(list);
                }
            }
        }
    }
    
    public List<Integer> clearStatus(final FightObject target, final int skillType) {
        final List<Integer> list = new ArrayList<Integer>();
        if (target.getStatusValue(1006) > 0) {
            list.add(1006);
            target.setStatus(1006, 0);
        }
        if (target.getStatusValue(1004) > 0) {
            list.add(1004);
            target.setStatus(1004, 0);
        }
        return list;
    }
}
