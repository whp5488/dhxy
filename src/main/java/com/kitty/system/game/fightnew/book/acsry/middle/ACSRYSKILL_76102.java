package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_76102 extends AcsrySkillProcessor
{
    public ACSRYSKILL_76102() {
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
        if (target.isPet() && target.isFallDown() && effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(76103))) {
                return;
            }
            double percent = 0.02;
            double maxPercent = 0.2;
            double attackAddition = playerFightObject.getAcsryPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.ATTACK_ADDITION.getDist()), 0.0);
            if (attackAddition >= maxPercent) {
                return;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 76102);
            attackAddition += percent;
            if (attackAddition > maxPercent) {
                attackAddition = maxPercent;
            }
            playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.ATTACK_ADDITION.getDist()), Double.valueOf(attackAddition));
            int oldHp = effecter.getCurrLife();
            int addHp = (int)((double)oldHp * percent);
            if (oldHp + addHp > effecter.getMaxLife()) {
                addHp = effecter.getMaxLife() - oldHp;
            }
            if (addHp > 0) {
                FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)effecter.getId(), true);
                PassiveSkillUtils.excuteChangeLifeAuto(fight, effecter, actionDetailParam, targetParam, addHp);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
}
