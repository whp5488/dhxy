
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75303 extends AcsrySkillProcessor
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
        if (isHit == null || isHit) {
            return;
        }
        final int status = this.getStatus(skill.getType());
        if (status != 0 && target.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)target;
            final double percent = 0.25;
            final boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                final int round = 2;
                effecter.setStatus(status, round);
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(effecter.getId(), true);
                targetParam.getStatusMap().put(status, round);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, target.getId(), 75303);
                skillRecord.setT(effecter.getId());
            }
        }
    }
    
    private int getStatus(final int skillType) {
        if (SKILLATTR.SKILLATTR_ICE.getValue() == skillType) {
            return 1003;
        }
        if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == skillType) {
            return 1002;
        }
        if (SKILLATTR.SKILLATTR_SLEEP.getValue() == skillType) {
            return 1004;
        }
        if (SKILLATTR.SKILLATTR_YIWANG.getValue() == skillType) {
            return 1006;
        }
        return 0;
    }
}
