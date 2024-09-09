package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74203 extends AcsrySkillProcessor
{
    public ACSRYSKILL_74203() {
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        RoleSkill skill = null;
        Boolean isHit = Boolean.valueOf(false);
        FightObject sideTarget = null;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
            sideTarget = (FightObject)args[6];
        }
        catch (Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !(boolean)isHit || sideTarget == null) {
            return;
        }
        if (SKILLATTR.SKILLATTR_XIXUE.getValue() == (int)skill.getType() && effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(74203))) {
                double percent = 0.1;
                percent += (double)((int)effecter.getRole().getGg() / 100) * 0.1;
                int addMp = (int)((double)sideTarget.getMaxMana() * percent);
                int oldMana = sideTarget.getCurrMana();
                if (addMp + oldMana > sideTarget.getMaxMana()) {
                    addMp = sideTarget.getMaxMana() - sideTarget.getCurrMana();
                }
                if (addMp > 0) {
                    FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)sideTarget.getId(), false);
                    if (targetParam != null) {
                        PassiveSkillUtils.excuteChangeManaAuto(fight, sideTarget, actionDetailParam, targetParam, addMp);
                        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 74203);
                        skillRecord.setT((long)(int)target.getId());
                    }
                }
            }
        }
        return;
    }
}
