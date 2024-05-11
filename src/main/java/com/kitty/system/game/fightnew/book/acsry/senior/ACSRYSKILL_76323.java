
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_76323 extends AcsrySkillProcessor
{
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (fight.isPvp() != 1) {
            return;
        }
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
        if (SKILLATTR.SKILLATTR_HENGSAO.getValue() == skill.getType() && effecter.isPlayer()) {
            final int round = 2;
            target.getOtherMap().setv("originalMaxLife", (Object)target.getMaxLife());
            target.getStatusMap().setv(String.valueOf(8011), (Object)round);
            target.getStatusMap().setv("8011perpetrator", (Object)("76323_" + effecter.getId()));
            double percent = 0.1;
            percent += effecter.getRole().getLl() / 100 * 0.1;
            if (percent > 0.3) {
                percent = 0.3;
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 76323);
            skillRecord.setT(target.getId());
            target.setMaxLife((int)(target.getMaxLife() * (1.0 - percent)));
            if (target.getMaxLife() < target.getCurrLife()) {
                final int reduceLife = target.getMaxLife() - target.getCurrLife();
                if (reduceLife < 0) {
                    final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
                    if (targetParam != null) {
                        final FightObjectParam targetC = ActionParamUtils.changeLifeForTarget(fight, target, 1, reduceLife, 2);
                        targetC.setMaxLife(target.getMaxLife());
                        targetC.addDamageType(300999);
                        actionDetailParam.getTargetParamList().add(targetC);
                    }
                }
            }
        }
    }
    
    @Override
    public void statusOver(final Fight fight, final FightObject fightObject, final Object... args) {
        Integer status = null;
        try {
            status = (Integer)args[0];
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (status == null || 8011 != status) {
            return;
        }
        final String perpetrator = fightObject.getStatusMap().getString("8011perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(76323))) {
            fightObject.getStatusMap().unset("8011perpetrator");
            final int maxLife = fightObject.getOtherMap().getInt("originalMaxLife", 0);
            if (maxLife > 0) {
                fightObject.setMaxLife(maxLife);
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 76323);
            skillRecord.setT(fightObject.getId());
        }
    }
}
