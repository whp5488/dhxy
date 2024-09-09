package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import java.util.Map;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.List;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324417 extends TianFuProcessor
{
    public FU_324417() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
        DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324417);
        if (tianFuObject == null) {
            return null;
        }
        boolean statusFlag = false;
        List<Integer> statusList = (List)args[0];
        if (statusList.size() > 0 && statusList.contains(Integer.valueOf(1022)) && (target.getStatusValue(1022) <= 0 || target.isFallDown())) {
            statusFlag = true;
        }
        if (target.isPet() && statusFlag) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324417);
            double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean triggerFlag = SkillAi.canAffectOnPro(percenta);
            if (triggerFlag) {
                List<FightObject> list = TianFuUtils.getFightObject(fight, target, Integer.valueOf(1022));
                if (list == null || list.size() <= 0) {
                    return null;
                }
                FightObject myTarget = (FightObject)list.get(0);
                int time = target.getStatusValue(1022);
                Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map)PartnerDataPool.partnerPropertyMap.get(Integer.valueOf(41101));
                short level = (short)(target.getRelive() * 1000 + target.getLevel());
                SysPartnerProperty partnerProperty = (SysPartnerProperty)shortSysPartnerPropertyMap.get(Short.valueOf(level));
                if (partnerProperty == null) {
                    return null;
                }
                Integer proficient = (Integer)partnerProperty.getSkillInfo().get(Integer.valueOf(30037));
                if (proficient == null || (int)proficient < 0) {
                    return null;
                }
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30037));
                double[] power = sysSkill.getPower();
                int baseLife = (int)power[0] + (int)proficient * 14 / 5;
                int perLife = (int)(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[1] + power[2] * (double)(int)proficient / 25000.0)) * (double)myTarget.getCurrLife());
                myTarget.getStatusMap().setv(String.valueOf(1022), Integer.valueOf(time));
                target.getStatusSubMap().setv(String.valueOf(1022) + "value", Integer.valueOf(baseLife + perLife));
                ActionParamUtils.addStatus(fight, target, myTarget, 7, 300999, 2, 1022, time);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return null;
    }
}
