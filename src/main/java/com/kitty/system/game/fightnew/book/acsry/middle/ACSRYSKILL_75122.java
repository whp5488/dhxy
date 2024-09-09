package com.kitty.system.game.fightnew.book.acsry.middle;

import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75122 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75122() {
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
        if (isHit == null || !(boolean)isHit) {
            return;
        }
        if (SKILLATTR.SKILLATTR_WULI.getValue() == (int)skill.getType() && effecter.isPlayer()) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 75122);
            double percent = 0.1;
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag && target.getStatusValue(1003) <= 0) {
                int round = 1;
                target.setStatus(1002, round);
                FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), false);
                if (targetParam != null) {
                    targetParam.getStatusMap().put(Integer.valueOf(1002), Integer.valueOf(round));
                }
                List<Integer> list = this.clearStatus(target, (int)skill.getType());
                if (list.size() > 0) {
                    skillRecord.setT((long)(int)target.getId());
                    targetParam.addClearStatus(list);
                }
            }
        }
        return;
    }
    
    public List<Integer> clearStatus(FightObject target, int skillType) {
        List<Integer> list = new ArrayList<>();
        if (target.getStatusValue(1006) > 0) {
            list.add(Integer.valueOf(1006));
            target.setStatus(1006, 0);
        }
        if (target.getStatusValue(1004) > 0) {
            list.add(Integer.valueOf(1004));
            target.setStatus(1004, 0);
            return list;
        }
        return list;
    }
}
