
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_72202 extends AcsrySkillProcessor
{
    @Override
    public NutMap executeReduceHp(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        RoleSkill skill = null;
        int hurt = 0;
        try {
            skill = (RoleSkill)args[0];
            hurt = (int)args[1];
        }
        catch (final Exception e) {
            return null;
        }
        if (skill == null) {
            return null;
        }
        if (hurt <= 0) {
            return null;
        }
        if (this.checkExcute(skill) && this.checkTarget(target) && effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 72202);
            if (!playerFightObject.getAcsrySkillList().contains(72203)) {
                final int changeHurt = (int)(hurt * 0.8);
                skillRecord.setT(target.getId());
                return NutMap.NEW().setv("changeHurt", (Object)changeHurt);
            }
        }
        return null;
    }
    
    private boolean checkTarget(final FightObject target) {
        return target.getStatusValue(1006) > 0 || target.getStatusValue(1004) > 0;
    }
    
    private boolean checkExcute(final RoleSkill skill) {
        return SKILLATTR.SKILLATTR_FIRE.getValue() == skill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == skill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == skill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == skill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == skill.getType();
    }
}
