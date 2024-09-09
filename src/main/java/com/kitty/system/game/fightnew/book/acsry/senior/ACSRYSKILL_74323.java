package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74323 extends AcsrySkillProcessor
{
    public ACSRYSKILL_74323() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isFallDown() || fightObject.getCurrLife() <= 1) {
            return;
        }
        if (fight.isPvp() != 1) {
            return;
        }
        if (fightObject.getStatusValue(1007) > 0) {
            String statusValue = fightObject.getStatusMap().getString("1007perpetrator");
            if (statusValue != null) {
                String[] strs = statusValue.split("_");
                if (strs[0] == null || "".equals(strs[0]) || !String.valueOf(74323).equals(strs[0])) {
                    return;
                }
                if (strs[1] == null || "".equals(strs[1]) || !strs[1].matches("^[0-9]+$")) {
                    return;
                }
                FightObject effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(strs[1])));
                if (effecter == null || effecter.isFallDown()) {
                    return;
                }
                double percent = 0.1;
                percent += (double)((int)((PlayerFightObject)effecter).getRole().getMj() / 200) * 0.01;
                int reduceLife = (int)((double)fightObject.getMaxLife() * percent);
                if (reduceLife >= fightObject.getCurrLife()) {
                    reduceLife = fightObject.getCurrLife() - 1;
                }
                ActionParamUtils.changeLife(fight, effecter, fightObject, 7, 300999, 2, -reduceLife, false);
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 74323);
                skillRecord.setT((long)(int)fightObject.getId());
            }
        }
        return;
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
        if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == (int)skill.getType() && target.getStatusValue(1007) > 0) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 74323);
            skillRecord.setT((long)(int)target.getId());
            target.getStatusMap().setv("1007perpetrator", "74323_" + effecter.getId());
        }
        return;
    }
}
