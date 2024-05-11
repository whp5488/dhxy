
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
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.init.domain.SysPartnerProperty;
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
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        final FightObject host = fight.queryFightObject(effector.getHostId());
        final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323201);
        if (tianFuObject == null) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 323201);
        if (effector.isPet() && 0 == effector.getType() && target.getStatusValue(1005) <= 0) {
            final double percenta = 0.7 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final boolean flag = SkillAi.canAffectOnPro(percenta);
            if (flag) {
                final Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = PartnerDataPool.partnerPropertyMap.get(41002);
                short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
                if (level > 3180) {
                    level = 3250;
                }
                final SysPartnerProperty partnerProperty = shortSysPartnerPropertyMap.get(level);
                if (partnerProperty == null) {
                    return;
                }
                final Integer proficient = partnerProperty.getSkillInfo().get(30008);
                if (proficient == null || proficient < 0) {
                    return;
                }
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30008);
                final double power = sysSkill.getPower()[0];
                final int maxLife = target.getMaxLife();
                final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                final double kdx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_DU.getDist());
                final double dqd = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_D.getDist());
                final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_D.getDist());
                final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                final double hurt = SkillCompute.computeSkillDamagePoison(power, kdx, dqd, proficient, 1, effector.getLevel(), maxLife, extra, weak, wx);
                final int time = this.time(effector, proficient, kdx, dqd);
                target.getStatusSubMap().setv("1005_power", (Object)power);
                target.getStatusSubMap().setv("1005_kdx", (Object)kdx);
                target.getStatusSubMap().setv("1005_dqd", (Object)dqd);
                target.getStatusSubMap().setv("1005_proficient", (Object)proficient);
                target.getStatusSubMap().setv("1005_round", (Object)1);
                target.getStatusSubMap().setv("1005_level", (Object)effector.getLevel());
                target.getStatusSubMap().setv("1005_maxLife", (Object)maxLife);
                target.getStatusSubMap().setv("1005_extra", (Object)extra);
                target.getStatusSubMap().setv("1005_wx", (Object)wx);
                int returnHurt = Math.max(1, (int)hurt);
                final int targetOldLife = target.getCurrLife();
                if (returnHurt > targetOldLife) {
                    returnHurt = targetOldLife;
                }
                target.getStatusMap().setv(String.valueOf(1005), (Object)time);
                final List<Integer> clearList = new ArrayList<Integer>();
                if (target.isSleep()) {
                    clearList.add(1004);
                    target.setStatus(1004, 0);
                }
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), true);
                final FightObjectParam targetParamC = PassiveSkillUtils.excuteChangeLifeAuto(fight, target, actionDetailParam, targetParam, -returnHurt);
                targetParamC.getStatusMap().put(1005, time);
                if (clearList.size() > 0) {
                    targetParamC.addClearStatus(clearList);
                }
                skillRecord.setT(target.getId());
            }
        }
    }
    
    public int time(final FightObject effector, final Integer proficient, final double kdx, final double dqd) {
        int time = 2;
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue()) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 2.0 * (1.0 + Math.pow(proficient, 0.34) * 4.0 / 100.0));
        }
        return Math.min(time, BigDecimalUtil.decimalScaleInteger(0, time * (1.0 - kdx / 2.0 + dqd)));
    }
}
