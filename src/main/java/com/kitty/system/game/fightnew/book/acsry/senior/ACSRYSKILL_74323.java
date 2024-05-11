
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74323 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown() || fightObject.getCurrLife() <= 1) {
            return;
        }
        if (fight.isPvp() != 1) {
            return;
        }
        if (fightObject.getStatusValue(1007) > 0) {
            final String statusValue = fightObject.getStatusMap().getString("1007perpetrator");
            if (statusValue != null) {
                final String[] strs = statusValue.split("_");
                if (strs[0] == null || "".equals(strs[0]) || !String.valueOf(74323).equals(strs[0])) {
                    return;
                }
                if (strs[1] == null || "".equals(strs[1]) || !strs[1].matches("^[0-9]+$")) {
                    return;
                }
                final FightObject effecter = fight.queryFightObject(Integer.parseInt(strs[1]));
                if (effecter == null || effecter.isFallDown()) {
                    return;
                }
                double percent = 0.1;
                percent += ((PlayerFightObject)effecter).getRole().getMj() / 200 * 0.01;
                int reduceLife = (int)(fightObject.getMaxLife() * percent);
                if (reduceLife >= fightObject.getCurrLife()) {
                    reduceLife = fightObject.getCurrLife() - 1;
                }
                ActionParamUtils.changeLife(fight, effecter, fightObject, 7, 300999, 2, -reduceLife, false);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 74323);
                skillRecord.setT(fightObject.getId());
            }
        }
    }
    
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
        if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == skill.getType() && target.getStatusValue(1007) > 0) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 74323);
            skillRecord.setT(target.getId());
            target.getStatusMap().setv("1007perpetrator", (Object)("74323_" + effecter.getId()));
        }
    }
}
