package com.kitty.system.game.fightnew.book.acsry.middle;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73322 extends AcsrySkillProcessor
{
    public ACSRYSKILL_73322() {
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        RoleSkill skill = null;
        Boolean isHit = Boolean.valueOf(false);
        boolean flag = true;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
            flag = (boolean)args[5];
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 73322);
        if (flag && SKILLATTR.SKILLATTR_ATTACK.getValue() == (int)skill.getType()) {
            if (effecter.isPlayer()) {
                if (((PlayerFightObject)effecter).getAcsrySkillList().contains(Integer.valueOf(73323))) {
                    return;
                }
                target.getStatusMap().setv("1008perpetrator", "73322_" + effecter.getId());
                skillRecord.setT((long)(int)target.getId());
            }
        }
        else if (!flag && SKILLATTR.SKILLATTR_WULI.getValue() == (int)skill.getType() && effecter.getStatusValue(1008) > 0) {
            int unitId = effecter.getStatusSubMap().getInt("1008target");
            if (unitId <= 0) {
                return;
            }
            FightObject unit = fight.queryFightObject(Integer.valueOf(unitId));
            if (unit == null || !unit.isPlayer()) {
                return;
            }
            List<Integer> acsrySkillList = ((PlayerFightObject)unit).getAcsrySkillList();
            if (acsrySkillList.contains(Integer.valueOf(73322))) {
                int oldMana = target.getCurrMana();
                if (oldMana <= 0) {
                    return;
                }
                int changeMana = 1;
                if (oldMana > 9) {
                    changeMana = (int)((double)oldMana * 0.2);
                }
                FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), false);
                if (targetParam != null) {
                    skillRecord.setT((long)(int)target.getId());
                    PassiveSkillUtils.excuteChangeManaAuto(fight, target, actionDetailParam, targetParam, -changeMana);
                }
            }
        }
        return;
    }
    
    @Override
    public NutMap executeReduceHp(Fight fight, FightObject effecter, FightObject target, Object... args) {
        int hurt = 0;
        try {
            hurt = (int)args[1];
        }
        catch (Exception e) {
            return null;
        }
        if (hurt > 0) {
            int changeHurt = (int)((double)hurt * 0.1);
            if (changeHurt > 0) {
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 73322);
                skillRecord.setT((long)(int)target.getId());
                return NutMap.NEW().setv("changeHurt", Integer.valueOf(changeHurt));
            }
        }
        return null;
    }
}
