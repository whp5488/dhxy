
package com.kitty.system.game.fightnew.book.acsry.senior;

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

public class ACSRYSKILL_73323 extends AcsrySkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 73323);
        if (flag && SKILLATTR.SKILLATTR_ATTACK.getValue() == skill.getType()) {
            if (effecter.isPlayer()) {
                skillRecord.setT(target.getId());
                target.getStatusMap().setv("1008perpetrator", (Object)("73323_" + effecter.getId()));
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
            if (acsrySkillList.contains(73323)) {
                final int oldMana = target.getCurrMana();
                if (oldMana <= 0) {
                    return;
                }
                int changeMana = 1;
                if (oldMana > 9) {
                    double percent = 0.2;
                    final String str = effecter.getStatusMap().getString("1008perpetrator", (String)null);
                    if (str != null) {
                        try {
                            final String[] splits = str.split("_");
                            final FightObject fightObject = fight.queryFightObject(Integer.parseInt(splits[1]));
                            percent += fightObject.getRole().getMj() / 50 * 0.01;
                        }
                        catch (final Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    changeMana = (int)(oldMana * percent);
                }
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
                if (targetParam != null) {
                    PassiveSkillUtils.excuteChangeManaAuto(fight, target, actionDetailParam, targetParam, -changeMana);
                    skillRecord.setT(target.getId());
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
            double percent = 0.1;
            final String str = effecter.getStatusMap().getString("1008perpetrator", (String)null);
            if (str != null) {
                try {
                    final String[] splits = str.split("_");
                    final FightObject fightObject = fight.queryFightObject(Integer.parseInt(splits[1]));
                    percent += fightObject.getRole().getMj() / 50 * 0.003;
                }
                catch (final Exception e2) {
                    e2.printStackTrace();
                }
            }
            final int changeHurt = (int)(hurt * percent);
            if (changeHurt > 0) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 73323);
                skillRecord.setT(target.getId());
                return NutMap.NEW().setv("changeHurt", (Object)changeHurt);
            }
        }
        return null;
    }
}
