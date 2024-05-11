
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
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        RoleSkill skill = null;
        Boolean isHit = false;
        boolean flag = true;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
            flag = (boolean)args[5];
        }
        catch (final Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !isHit) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 73322);
        if (flag && SKILLATTR.SKILLATTR_ATTACK.getValue() == skill.getType()) {
            if (effecter.isPlayer()) {
                if (((PlayerFightObject)effecter).getAcsrySkillList().contains(73323)) {
                    return;
                }
                target.getStatusMap().setv("1008perpetrator", (Object)("73322_" + effecter.getId()));
                skillRecord.setT(target.getId());
            }
        }
        else if (!flag && SKILLATTR.SKILLATTR_WULI.getValue() == skill.getType() && effecter.getStatusValue(1008) > 0) {
            final int unitId = effecter.getStatusSubMap().getInt("1008target");
            if (unitId <= 0) {
                return;
            }
            final FightObject unit = fight.queryFightObject(unitId);
            if (unit == null || !unit.isPlayer()) {
                return;
            }
            final List<Integer> acsrySkillList = ((PlayerFightObject)unit).getAcsrySkillList();
            if (acsrySkillList.contains(73322)) {
                final int oldMana = target.getCurrMana();
                if (oldMana <= 0) {
                    return;
                }
                int changeMana = 1;
                if (oldMana > 9) {
                    changeMana = (int)(oldMana * 0.2);
                }
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
                if (targetParam != null) {
                    skillRecord.setT(target.getId());
                    PassiveSkillUtils.excuteChangeManaAuto(fight, target, actionDetailParam, targetParam, -changeMana);
                }
            }
        }
    }
    
    @Override
    public NutMap executeReduceHp(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        int hurt = 0;
        try {
            hurt = (int)args[1];
        }
        catch (final Exception e) {
            return null;
        }
        if (hurt > 0) {
            final int changeHurt = (int)(hurt * 0.1);
            if (changeHurt > 0) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 73322);
                skillRecord.setT(target.getId());
                return NutMap.NEW().setv("changeHurt", (Object)changeHurt);
            }
        }
        return null;
    }
}
