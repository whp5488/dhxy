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
    public ACSRYSKILL_72202() {
    }
    
    @Override
    public NutMap executeReduceHp(Fight fight, FightObject effecter, FightObject target, Object... args) {
        RoleSkill skill = null;
        int hurt = 0;
        try {
            skill = (RoleSkill)args[0];
            hurt = (int)args[1];
        }
        catch (Exception e) {
            return null;
        }
        if (skill == null) {
            return null;
        }
        if (hurt <= 0) {
            return null;
        }
        if (this.checkExcute(skill) && this.checkTarget(target) && effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 72202);
            if (!playerFightObject.getAcsrySkillList().contains(Integer.valueOf(72203))) {
                int changeHurt = (int)((double)hurt * 0.8);
                skillRecord.setT((long)(int)target.getId());
                return NutMap.NEW().setv("changeHurt", Integer.valueOf(changeHurt));
            }
        }
        return null;
    }
    
    private boolean checkTarget(FightObject target) {
        return target.getStatusValue(1006) > 0 || target.getStatusValue(1004) > 0;
    }
    
    private boolean checkExcute(RoleSkill skill) {
        return SKILLATTR.SKILLATTR_FIRE.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == (int)skill.getType();
    }
}
