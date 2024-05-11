
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74202 extends AcsrySkillProcessor
{
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        RoleSkill skill = null;
        Boolean isHit = false;
        FightObject sideTarget = null;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
            sideTarget = (FightObject)args[6];
        }
        catch (final Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !isHit || sideTarget == null) {
            return;
        }
        if (SKILLATTR.SKILLATTR_XIXUE.getValue() == skill.getType() && effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(74202)) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 74202);
                if (playerFightObject.getAcsrySkillList().contains(74203)) {
                    return;
                }
                final double percent = 0.1;
                int addMp = (int)(sideTarget.getMaxMana() * percent);
                final int oldMana = sideTarget.getCurrMana();
                if (addMp + oldMana > sideTarget.getMaxMana()) {
                    addMp = sideTarget.getMaxMana() - sideTarget.getCurrMana();
                }
                if (addMp > 0) {
                    final FightObjectParam targetParam = actionDetailParam.gainTargetParam(sideTarget.getId(), false);
                    if (targetParam != null) {
                        skillRecord.setT(effecter.getId());
                        PassiveSkillUtils.excuteChangeManaAuto(fight, sideTarget, actionDetailParam, targetParam, addMp);
                    }
                }
            }
        }
    }
}
