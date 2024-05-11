
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73103 extends AcsrySkillProcessor
{
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (fight.isPvp() != 1) {
            return;
        }
        RoleSkill skill = null;
        Boolean isHit = false;
        int targetHurt = 0;
        int targetMpHurt = 0;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
            targetHurt = (int)args[3];
            targetMpHurt = (int)args[4];
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
        if (SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType() && effecter.isPlayer()) {
            int addHp = 0;
            int addMp = 0;
            if (targetHurt > 0) {
                addHp = (int)(targetHurt * 0.02);
            }
            if (targetMpHurt > 0) {
                addMp = (int)(targetMpHurt * 0.1);
            }
            if (addHp == 0 && addMp == 0) {
                return;
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 73103);
            final FightObjectParam targetParam = actionDetailParam.gainTargetParam(effecter.getId(), true);
            FightObjectParam targetParamC = null;
            if (addHp > 0) {
                final int oldLife = effecter.getCurrLife();
                if (addHp + oldLife > effecter.getMaxLife()) {
                    addHp = effecter.getMaxLife() - effecter.getCurrLife();
                }
                targetParamC = PassiveSkillUtils.excuteChangeLifeAuto(fight, effecter, actionDetailParam, targetParam, addHp);
                skillRecord.setT(effecter.getId());
            }
            if (addMp > 0) {
                final int oldMana = effecter.getCurrMana();
                if (addMp + oldMana > effecter.getMaxMana()) {
                    addMp = effecter.getMaxMana() - effecter.getCurrMana();
                }
                PassiveSkillUtils.excuteChangeManaAuto(fight, effecter, actionDetailParam, targetParamC, addMp);
                skillRecord.setT(effecter.getId());
            }
        }
    }
}
