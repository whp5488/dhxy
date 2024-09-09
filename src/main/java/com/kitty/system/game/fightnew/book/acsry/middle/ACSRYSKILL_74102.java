package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74102 extends AcsrySkillProcessor
{
    public ACSRYSKILL_74102() {
    }
    
    @Override
    public NutMap countRecoverBloodEndSS(Fight fight, FightObject effecter, FightObject target, Object... args) {
        if (effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(74103))) {
                return null;
            }
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(74102))) {
                int life = 0;
                RoleSkill skill = null;
                try {
                    life = (int)args[0];
                    skill = (RoleSkill)args[1];
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (skill == null) {
                    return null;
                }
                if (SKILLATTR.SKILLATTR_XIXUE.getValue() == (int)skill.getType()) {
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 74102);
                    skillRecord.setT((long)(int)effecter.getId());
                    if (life > 0) {
                        double percent = 1.0;
                        int addLife = (int)((double)life * percent);
                        NutMap map = NutMap.NEW();
                        map.setv("changeLife", Integer.valueOf(addLife));
                        skillRecord.setT((long)(int)effecter.getId());
                        return map;
                    }
                    return null;
                }
            }
        }
        return null;
    }
}
