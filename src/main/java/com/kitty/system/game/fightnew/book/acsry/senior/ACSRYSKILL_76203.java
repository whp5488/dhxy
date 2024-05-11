
package com.kitty.system.game.fightnew.book.acsry.senior;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_76203 extends AcsrySkillProcessor
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
        final int statusValue = target.getStatusValue(1022);
        if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == skill.getType() && statusValue > 0 && effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(76203)) {
                target.setStatus(8010, statusValue);
                double percent = 0.1;
                percent += playerFightObject.getRole().getGg() / 100 * 0.01;
                target.getStatusMap().setv("8010value", (Object)percent);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 76203);
                skillRecord.setT(target.getId());
            }
        }
    }
    
    @Override
    public NutMap countRecoverBloodEnd(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.getStatusValue(1022) > 0 && fightObject.getStatusValue(8010) > 0) {
            final double percent = fightObject.getStatusMap().getDouble("8010value", 0.0);
            if (percent > 0.0) {
                int addMp = (int)(fightObject.getMaxMana() * percent);
                if (fightObject.getCurrMana() + addMp > fightObject.getMaxMana()) {
                    addMp = fightObject.getMaxMana() - fightObject.getCurrMana();
                }
                if (addMp > 0) {
                    fightObject.setCurrMana(fightObject.getCurrMana() + addMp);
                    fightObject.getTempCache().put("recoverManaValue", addMp);
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 76203);
                    skillRecord.setT(fightObject.getId());
                }
            }
        }
        return null;
    }
}
