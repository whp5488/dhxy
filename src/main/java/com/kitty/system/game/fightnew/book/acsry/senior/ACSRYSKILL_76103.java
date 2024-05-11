
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_76103 extends AcsrySkillProcessor
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
        if (target.isPet() && target.isFallDown() && effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final double percent = 0.04;
            final double maxPercent = 0.3;
            double attackAddition = playerFightObject.getAcsryPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.ATTACK_ADDITION.getDist()), 0.0);
            if (attackAddition >= maxPercent) {
                return;
            }
            attackAddition += percent;
            if (attackAddition > maxPercent) {
                attackAddition = maxPercent;
            }
            playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.ATTACK_ADDITION.getDist()), (Object)attackAddition);
            final int oldHp = effecter.getCurrLife();
            int addHp = (int)(oldHp * percent);
            if (oldHp + addHp > effecter.getMaxLife()) {
                addHp = effecter.getMaxLife() - oldHp;
            }
            if (addHp > 0) {
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(effecter.getId(), true);
                PassiveSkillUtils.excuteChangeLifeAuto(fight, effecter, actionDetailParam, targetParam, addHp);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 76103);
                skillRecord.setT(effecter.getId());
            }
        }
    }
}
