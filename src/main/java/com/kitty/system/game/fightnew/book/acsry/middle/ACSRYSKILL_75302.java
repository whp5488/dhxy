package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75302 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75302() {
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        RoleSkill skill = null;
        Boolean isHit = Boolean.valueOf(false);
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
        }
        catch (Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || (boolean)isHit) {
            return;
        }
        int status = this.getStatus((int)skill.getType());
        if (status != 0 && target.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)target;
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(75303))) {
                return;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 75302);
            double percent = 0.15;
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                int round = 2;
                effecter.setStatus(status, round);
                FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)effecter.getId(), true);
                targetParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(round));
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
    
    private int getStatus(int skillType) {
        if (SKILLATTR.SKILLATTR_ICE.getValue() == skillType) {
            return 1003;
        }
        if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == skillType) {
            return 1002;
        }
        if (SKILLATTR.SKILLATTR_SLEEP.getValue() == skillType) {
            return 1004;
        }
        if (SKILLATTR.SKILLATTR_YIWANG.getValue() == skillType) {
            return 1006;
        }
        return 0;
    }
}
