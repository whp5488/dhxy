package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import java.util.Map;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323201 extends TianFuProcessor
{
    public FU_323201() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
        DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323201);
        if (tianFuObject == null) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 323201);
        if (effector.isPet() && 0 == effector.getType() && target.getStatusValue(1005) <= 0) {
            double percenta = 0.7 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flag = SkillAi.canAffectOnPro(percenta);
            if (flag) {
                Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map)PartnerDataPool.partnerPropertyMap.get(Integer.valueOf(41002));
                short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
                SysPartnerProperty partnerProperty = (SysPartnerProperty)shortSysPartnerPropertyMap.get(Short.valueOf(level));
                if (partnerProperty == null) {
                    return;
                }
                Integer proficient = (Integer)partnerProperty.getSkillInfo().get(Integer.valueOf(30008));
                if (proficient == null || (int)proficient < 0) {
                    return;
                }
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30008));
                double power = sysSkill.getPower()[0];
                int maxLife = target.getMaxLife();
                double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                double kdx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_DU.getDist());
                double dqd = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_D.getDist());
                double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_D.getDist());
                double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                double hurt = SkillCompute.computeSkillDamagePoison(power, kdx, dqd, proficient, Integer.valueOf(1), Integer.valueOf(effector.getLevel()), Integer.valueOf(maxLife), extra, weak, wx);
                int time = this.time(effector, proficient, kdx, dqd);
                target.getStatusSubMap().setv("1005_power", Double.valueOf(power));
                target.getStatusSubMap().setv("1005_kdx", Double.valueOf(kdx));
                target.getStatusSubMap().setv("1005_dqd", Double.valueOf(dqd));
                target.getStatusSubMap().setv("1005_proficient", proficient);
                target.getStatusSubMap().setv("1005_round", Integer.valueOf(1));
                target.getStatusSubMap().setv("1005_level", Integer.valueOf(effector.getLevel()));
                target.getStatusSubMap().setv("1005_maxLife", Integer.valueOf(maxLife));
                target.getStatusSubMap().setv("1005_extra", Double.valueOf(extra));
                target.getStatusSubMap().setv("1005_wx", Double.valueOf(wx));
                int returnHurt = Math.max(1, (int)hurt);
                int targetOldLife = target.getCurrLife();
                if (returnHurt > targetOldLife) {
                    returnHurt = targetOldLife;
                }
                target.getStatusMap().setv(String.valueOf(1005), Integer.valueOf(time));
                List<Integer> clearList = new ArrayList<>();
                if (target.isSleep()) {
                    clearList.add(Integer.valueOf(1004));
                    target.setStatus(1004, 0);
                }
                FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), true);
                FightObjectParam targetParamC = PassiveSkillUtils.excuteChangeLifeAuto(fight, target, actionDetailParam, targetParam, -returnHurt);
                targetParamC.getStatusMap().put(Integer.valueOf(1005), Integer.valueOf(time));
                if (clearList.size() > 0) {
                    targetParamC.addClearStatus(clearList);
                }
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
    
    public int time(FightObject effector, Integer proficient, double kdx, double dqd) {
        int time = 2;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue()) {
            time = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(2.0 * (1.0 + Math.pow((double)(int)proficient, 0.34) * 4.0 / 100.0)));
        }
        return Math.min(time, BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf((double)time * (1.0 - kdx / 2.0 + dqd))));
    }
}
