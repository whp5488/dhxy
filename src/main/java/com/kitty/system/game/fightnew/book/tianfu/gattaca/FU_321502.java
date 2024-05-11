
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import java.util.ArrayList;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321502 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return null;
        }
        if (!effector.isPlayer()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321502);
        if (tianFuObject == null) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321502);
        if (RACE.RACE_REN.getValue() == effector.getRace()) {
            if (SKILLATTR.SKILLATTR_POISON.getValue() != roleSkill.getType()) {
                return null;
            }
            final List<Integer> attrList = new ArrayList<Integer>();
            attrList.add(SKILLATTR.SKILLATTR_POISON.getValue());
            this.excutePercent(effector, target, fight.getRound(), tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList, roleSkill);
            skillRecord.setT(target.getId());
        }
        else if (RACE.RACE_XIAN.getValue() == effector.getRace()) {
            final List<Integer> attrList = new ArrayList<Integer>();
            attrList.add(SKILLATTR.SKILLATTR_FIRE.getValue());
            attrList.add(SKILLATTR.SKILLATTR_WIND.getValue());
            attrList.add(SKILLATTR.SKILLATTR_THUNDER.getValue());
            attrList.add(SKILLATTR.SKILLATTR_WATER.getValue());
            if (attrList.contains(roleSkill.getType())) {
                this.excutePercent(effector, target, fight.getRound(), tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList, roleSkill);
                skillRecord.setT(target.getId());
            }
        }
        else if (RACE.RACE_GUI.getValue() == effector.getRace()) {
            final List<Integer> attrList = new ArrayList<Integer>();
            attrList.add(SKILLATTR.SKILLATTR_XIXUE.getValue());
            attrList.add(SKILLATTR.SKILLATTR_AIHAO.getValue());
            if (attrList.contains(roleSkill.getType())) {
                this.excutePercent(effector, target, fight.getRound(), tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList, roleSkill);
                skillRecord.setT(target.getId());
            }
        }
        return null;
    }
    
    private void excutePercent(final FightObject effector, final FightObject target, final int fightRound, final byte tianFuLevel, final short dayInCeLevel, final List<Integer> attrList, final RoleSkill roleSkill) {
        double percenta = 0.7 * TianFuUtils.getTianFuCount(tianFuLevel, dayInCeLevel) / 100.0;
        final String dist = String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_COUNT.getDist());
        int distValue = effector.getBeAttackedMap().getInt(dist, 0);
        final int round1 = effector.getFightActionRecord().getInt(fightRound - 1 + "details");
        final SysSkill sysSkill = SkillDataPool.skillMap.get(round1);
        if (sysSkill != null && attrList.contains(sysSkill.getAttr())) {
            this.excuteProperty(effector, target, distValue, percenta, dist, roleSkill.getType());
        }
        else {
            final int round2 = effector.getFightActionRecord().getInt(fightRound - 2 + "details");
            final SysSkill sysSkill2 = SkillDataPool.skillMap.get(round2);
            if (sysSkill2 == null || !attrList.contains(sysSkill2.getAttr())) {
                if (percenta > 0.5) {
                    percenta = 0.5;
                }
                final String kx = this.getKx(roleSkill.getType());
                if (kx != null) {
                    final double k_dist = target.getPropertyMap().getDouble(kx);
                    if (k_dist > 0.0) {
                        effector.getBeAttackedMap().setv(dist, (Object)(++distValue));
                        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), (Object)(k_dist * percenta));
                    }
                    else {
                        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), 0);
                    }
                }
            }
            else {
                this.excuteProperty(effector, target, distValue, percenta, dist, roleSkill.getType());
            }
        }
    }
    
    private void excuteProperty(final FightObject effector, final FightObject target, int distValue, double percenta, final String dist, final int attr) {
        if (distValue > 0) {
            percenta += percenta * distValue;
        }
        if (percenta > 0.5) {
            percenta = 0.5;
        }
        final String kx = this.getKx(attr);
        if (kx != null) {
            final double k_dist = target.getPropertyMap().getDouble(kx);
            if (k_dist > 0.0) {
                effector.getBeAttackedMap().setv(dist, (Object)(++distValue));
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), (Object)(k_dist * percenta));
            }
            else {
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), 0);
            }
        }
    }
    
    private String getKx(final int attr) {
        final SKILLATTR attrEmun = SKILLATTR.getByValue(attr);
        int kx = 0;
        switch (attrEmun) {
            case SKILLATTR_POISON: {
                kx = PRO_DICT_TYPE.K_DU.getDist();
                break;
            }
            case SKILLATTR_XIXUE: {
                kx = PRO_DICT_TYPE.K_SS.getDist();
                break;
            }
            case SKILLATTR_AIHAO: {
                kx = PRO_DICT_TYPE.K_GH.getDist();
                break;
            }
            case SKILLATTR_FIRE: {
                kx = PRO_DICT_TYPE.K_H.getDist();
                break;
            }
            case SKILLATTR_WIND: {
                kx = PRO_DICT_TYPE.K_F.getDist();
                break;
            }
            case SKILLATTR_THUNDER: {
                kx = PRO_DICT_TYPE.K_L.getDist();
                break;
            }
            case SKILLATTR_WATER: {
                kx = PRO_DICT_TYPE.K_S.getDist();
                break;
            }
        }
        if (kx != 0) {
            return String.valueOf(kx);
        }
        return null;
    }
}
