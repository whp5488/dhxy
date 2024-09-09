package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import java.util.ArrayList;
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
    public FU_321502() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return null;
        }
        if (!effector.isPlayer()) {
            return null;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321502);
        if (tianFuObject == null) {
            return null;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 321502);
        if (RACE.RACE_REN.getValue() == effector.getRace()) {
            if (SKILLATTR.SKILLATTR_POISON.getValue() != (int)roleSkill.getType()) {
                return null;
            }
            List<Integer> attrList = new ArrayList<>();
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_POISON.getValue()));
            this.excutePercent(effector, target, fight.getRound(), tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList, roleSkill);
            skillRecord.setT((long)(int)target.getId());
        }
        else if (RACE.RACE_XIAN.getValue() == effector.getRace()) {
            List<Integer> attrList = new ArrayList<>();
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_FIRE.getValue()));
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_WIND.getValue()));
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_THUNDER.getValue()));
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_WATER.getValue()));
            if (attrList.contains(roleSkill.getType())) {
                this.excutePercent(effector, target, fight.getRound(), tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList, roleSkill);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        else if (RACE.RACE_GUI.getValue() == effector.getRace()) {
            List<Integer> attrList = new ArrayList<>();
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_XIXUE.getValue()));
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_AIHAO.getValue()));
            if (attrList.contains(roleSkill.getType())) {
                this.excutePercent(effector, target, fight.getRound(), tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList, roleSkill);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return null;
    }
    
    private void excutePercent(FightObject effector, FightObject target, int fightRound, byte tianFuLevel, short dayInCeLevel, List<Integer> attrList, RoleSkill roleSkill) {
        double percenta = 0.7 * TianFuUtils.getTianFuCount(tianFuLevel, dayInCeLevel) / 100.0;
        String dist = String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_COUNT.getDist());
        int distValue = effector.getBeAttackedMap().getInt(dist, 0);
        int round1 = effector.getFightActionRecord().getInt(fightRound - 1 + "details");
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(round1));
        if (sysSkill != null && attrList.contains(sysSkill.getAttr())) {
            this.excuteProperty(effector, target, distValue, percenta, dist, (int)roleSkill.getType());
        }
        else {
            int round2 = effector.getFightActionRecord().getInt(fightRound - 2 + "details");
            SysSkill sysSkill2 = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(round2));
            if (sysSkill2 == null || !attrList.contains(sysSkill2.getAttr())) {
                if (percenta > 0.5) {
                    percenta = 0.5;
                }
                String kx = this.getKx((int)roleSkill.getType());
                if (kx != null) {
                    double k_dist = target.getPropertyMap().getDouble(kx);
                    if (k_dist > 0.0) {
                        effector.getBeAttackedMap().setv(dist, Integer.valueOf(++distValue));
                        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), Double.valueOf(k_dist * percenta));
                    }
                    else {
                        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), Integer.valueOf(0));
                    }
                }
            }
            else {
                this.excuteProperty(effector, target, distValue, percenta, dist, (int)roleSkill.getType());
            }
        }
    }
    
    private void excuteProperty(FightObject effector, FightObject target, int distValue, double percenta, String dist, int attr) {
        if (distValue > 0) {
            percenta += percenta * (double)distValue;
        }
        if (percenta > 0.5) {
            percenta = 0.5;
        }
        String kx = this.getKx(attr);
        if (kx != null) {
            double k_dist = target.getPropertyMap().getDouble(kx);
            if (k_dist > 0.0) {
                effector.getBeAttackedMap().setv(dist, Integer.valueOf(++distValue));
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), Double.valueOf(k_dist * percenta));
            }
            else {
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), Integer.valueOf(0));
            }
        }
    }
    
    private String getKx(int attr) {
        SKILLATTR attrEmun = SKILLATTR.getByValue(attr);
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
