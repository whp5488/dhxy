
package com.kitty.system.game.fightnew.book.acsry.middle;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71312 extends AcsrySkillProcessor
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
        if (SKILLATTR.SKILLATTR_POISON.getValue() == skill.getType() && effecter.isPlayer()) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 71312);
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(71313)) {
                return;
            }
            target.getStatusMap().setv("1005perpetrator", (Object)("71312_" + effecter.getId()));
            skillRecord.setT(target.getId());
        }
    }
    
    @Override
    public NutMap countRecoverBloodEnd(final Fight fight, final FightObject fightObject, final Object... args) {
        final String string = fightObject.getStatusMap().getString("1005perpetrator");
        if (string == null || !string.contains(String.valueOf(71312))) {
            return null;
        }
        int life = 0;
        try {
            life = (int)args[0];
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (life > 0) {
            final double percent = 0.3;
            final int reduceLife = (int)(life * percent);
            final NutMap map = NutMap.NEW();
            map.setv("changeLife", (Object)(-reduceLife));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap countRecoverMpEnd(final Fight fight, final FightObject fightObject, final Object... args) {
        final String string = fightObject.getStatusMap().getString("1005perpetrator");
        if (string == null || !string.contains(String.valueOf(71312))) {
            return null;
        }
        int mana = 0;
        try {
            mana = (int)args[0];
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (mana > 0) {
            final double percent = 0.3;
            final int reduceMana = (int)(mana * percent);
            final NutMap map = NutMap.NEW();
            map.setv("changeMana", (Object)(-reduceMana));
            return map;
        }
        return null;
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
        if (status == null || 1005 != status) {
            return;
        }
        final String perpetrator = fightObject.getStatusMap().getString("1005perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(71312))) {
            fightObject.getStatusMap().unset("1005perpetrator");
        }
    }
}
