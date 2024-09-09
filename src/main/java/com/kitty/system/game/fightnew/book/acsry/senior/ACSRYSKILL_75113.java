package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75113 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75113() {
    }
    
    @Override
    public NutMap beforeReduceHurtMp(Fight fight, FightObject effecter, FightObject target, Object... args) {
        if (target.isPlayer()) {
            RoleSkill skill = null;
            int mp = 0;
            try {
                skill = (RoleSkill)args[0];
                mp = (int)args[1];
                if (skill == null || mp <= 0) {
                    return null;
                }
            }
            catch (Exception e) {
                return null;
            }
            if (SKILLATTR.SKILLATTR_ZHEN.getValue() == (int)skill.getType()) {
                PlayerFightObject playerTarget = (PlayerFightObject)target;
                List<Integer> acsrySkillList = playerTarget.getAcsrySkillList();
                if (acsrySkillList.contains(Integer.valueOf(75113))) {
                    double percent = 0.3;
                    int changeMp = (int)((double)mp * percent);
                    if (changeMp > 0) {
                        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 75113);
                        skillRecord.setT((long)(int)target.getId());
                        return NutMap.NEW().setv("changeMana", Integer.valueOf(-changeMp));
                    }
                }
            }
        }
        return null;
    }
}
