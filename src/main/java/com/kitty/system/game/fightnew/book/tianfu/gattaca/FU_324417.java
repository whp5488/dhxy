
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.init.domain.SysPartnerProperty;
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
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final FightObject host = fight.queryFightObject(target.getHostId());
        final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324417);
        if (tianFuObject == null) {
            return null;
        }
        boolean statusFlag = false;
        final List<Integer> statusList = (List<Integer>)args[0];
        if (statusList.size() > 0 && statusList.contains(1022) && (target.getStatusValue(1022) <= 0 || target.isFallDown())) {
            statusFlag = true;
        }
        if (target.isPet() && statusFlag) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324417);
            final double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final boolean triggerFlag = SkillAi.canAffectOnPro(percenta);
            if (triggerFlag) {
                final List<FightObject> list = TianFuUtils.getFightObject(fight, target, 1022);
                if (list == null || list.size() <= 0) {
                    return null;
                }
                final FightObject myTarget = list.get(0);
                final int time = target.getStatusValue(1022);
                final Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = PartnerDataPool.partnerPropertyMap.get(41101);
                short level = (short)(target.getRelive() * 1000 + target.getLevel());
                if (level > 3180) {
                    level = 3180;
                }
                final SysPartnerProperty partnerProperty = shortSysPartnerPropertyMap.get(level);
                if (partnerProperty == null) {
                    return null;
                }
                final Integer proficient = partnerProperty.getSkillInfo().get(30037);
                if (proficient == null || proficient < 0) {
                    return null;
                }
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30037);
                final double[] power = sysSkill.getPower();
                final int baseLife = (int)power[0] + proficient * 14 / 5;
                final int perLife = (int)(BigDecimalUtil.decimalScaleObjectDown(3, power[1] + power[2] * proficient / 25000.0) * myTarget.getCurrLife());
                myTarget.getStatusMap().setv(String.valueOf(1022), (Object)time);
                target.getStatusSubMap().setv(String.valueOf(1022) + "value", (Object)(baseLife + perLife));
                ActionParamUtils.addStatus(fight, target, myTarget, 7, 300999, 2, 1022, time);
                skillRecord.setT(target.getId());
            }
        }
        return null;
    }
}
