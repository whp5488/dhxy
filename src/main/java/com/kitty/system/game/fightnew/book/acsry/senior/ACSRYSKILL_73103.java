package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73103 extends AcsrySkillProcessor
{
    public ACSRYSKILL_73103() {
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (fight.isPvp() != 1) {
            return;
        }
        RoleSkill skill = null;
        Boolean isHit = Boolean.valueOf(false);
        int targetHurt = 0;
        int targetMpHurt = 0;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
            targetHurt = (int)args[3];
            targetMpHurt = (int)args[4];
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
        if (SKILLATTR.SKILLATTR_ZHEN.getValue() == (int)skill.getType() && effecter.isPlayer()) {
            int addHp = 0;
            int addMp = 0;
            if (targetHurt > 0) {
                addHp = (int)((double)targetHurt * 0.02);
            }
            if (targetMpHurt > 0) {
                addMp = (int)((double)targetMpHurt * 0.1);
            }
            if (addHp == 0 && addMp == 0) {
                return;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 73103);
            FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)effecter.getId(), true);
            FightObjectParam targetParamC = null;
            if (addHp > 0) {
                int oldLife = effecter.getCurrLife();
                if (addHp + oldLife > effecter.getMaxLife()) {
                    addHp = effecter.getMaxLife() - effecter.getCurrLife();
                }
                targetParamC = PassiveSkillUtils.excuteChangeLifeAuto(fight, effecter, actionDetailParam, targetParam, addHp);
                skillRecord.setT((long)(int)effecter.getId());
            }
            if (addMp > 0) {
                int oldMana = effecter.getCurrMana();
                if (addMp + oldMana > effecter.getMaxMana()) {
                    addMp = effecter.getMaxMana() - effecter.getCurrMana();
                }
                PassiveSkillUtils.excuteChangeManaAuto(fight, effecter, actionDetailParam, targetParamC, addMp);
                skillRecord.setT((long)(int)effecter.getId());
            }
        }
        return;
    }
}
