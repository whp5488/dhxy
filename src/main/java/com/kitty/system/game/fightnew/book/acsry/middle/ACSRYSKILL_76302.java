
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_76302 extends AcsrySkillProcessor
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
        final int status = this.getStatus(skill);
        if ((1020 == status || 1021 == status) && effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(76302)) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 76302);
                if (playerFightObject.getAcsrySkillList().contains(76303)) {
                    return;
                }
                target.getStatusMap().setv(status + "perpetrator", (Object)("76302_" + effecter.getId()));
                skillRecord.setT(target.getId());
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
        if (1021 == status || 1020 == status) {
            final String perpetrator = fightObject.getStatusMap().getString(status + "perpetrator");
            if (perpetrator != null && perpetrator.contains(String.valueOf(76302))) {
                fightObject.getStatusMap().unset(status + "perpetrator");
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 76302);
                skillRecord.setT(fightObject.getId());
            }
        }
    }
    
    @Override
    public NutMap reduceMp(final Fight fight, final FightObject fightObject, final Object... args) {
        int mana = 0;
        try {
            mana = (int)args[0];
        }
        catch (final Exception e) {
            return null;
        }
        if (mana <= 0) {
            return null;
        }
        final int status = this.getStatusExcute(fightObject);
        if (status != 0) {
            final String perpetrator = fightObject.getStatusMap().getString(status + "perpetrator");
            if (perpetrator != null && perpetrator.contains(String.valueOf(76302))) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 76302);
                skillRecord.setT(fightObject.getId());
                final double percent = 0.4;
                final int changeMp = (int)(mana * percent);
                return NutMap.NEW().setv("changeMana", (Object)changeMp);
            }
        }
        return null;
    }
    
    private int getStatus(final RoleSkill skill) {
        if (SKILLATTR.SKILLATTR_POJIA.getValue() == skill.getType()) {
            return 1021;
        }
        if (SKILLATTR.SKILLATTR_ZHENJI.getValue() == skill.getType()) {
            return 1020;
        }
        return 0;
    }
    
    private int getStatusExcute(final FightObject fightObject) {
        if (fightObject.getStatusValue(1020) > 0) {
            return 1020;
        }
        if (fightObject.getStatusValue(1021) > 0) {
            return 1021;
        }
        return 0;
    }
}
