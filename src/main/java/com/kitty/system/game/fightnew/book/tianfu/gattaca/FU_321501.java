
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321501 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill roleSkill = (RoleSkill)args[1];
        if (roleSkill == null || roleSkill.getSkillId() == null || roleSkill.getSkillId() < 1) {
            return null;
        }
        final SysSkill sysSkill = SkillDataPool.skillMap.get(roleSkill.getSkillId());
        if (sysSkill == null) {
            return null;
        }
        final double lifePercent = effector.getCurrLife() / effector.getMaxLife();
        if (lifePercent >= 1.0) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321501);
        final double percent = 1.0 - lifePercent;
        final boolean flag = SkillAi.canAffectOnPro(percent);
        final Double percenta = this.getPercent(effector);
        if (percenta == null) {
            return null;
        }
        if (flag && (30001 == sysSkill.getSerial() || 30002 == sysSkill.getSerial())) {
            this.installProperty(effector, PRO_DICT_TYPE.K_HL.getDist(), percenta);
            skillRecord.setT(effector.getId());
        }
        else if (flag && (30003 == sysSkill.getSerial() || 30004 == sysSkill.getSerial())) {
            this.installProperty(effector, PRO_DICT_TYPE.K_HS.getDist(), percenta);
            skillRecord.setT(effector.getId());
        }
        else if (flag && (30005 == sysSkill.getSerial() || 30006 == sysSkill.getSerial())) {
            this.installProperty(effector, PRO_DICT_TYPE.K_FY.getDist(), percenta);
            skillRecord.setT(effector.getId());
        }
        else if (flag && (30029 == sysSkill.getSerial() || 30030 == sysSkill.getSerial())) {
            this.installProperty(effector, PRO_DICT_TYPE.K_YW.getDist(), percenta);
            skillRecord.setT(effector.getId());
        }
        return null;
    }
    
    private Double getPercent(final FightObject effector) {
        if (!effector.isPlayer()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321501);
        if (tianFuObject == null) {
            return null;
        }
        final double percent = 0.3 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        return percent;
    }
    
    private void installProperty(final FightObject effector, final int dist, final Double percenta) {
        final String distStr = String.valueOf(dist);
        double property = effector.getPropertyMap().getDouble(distStr);
        if (property > 0.0) {
            property += percenta;
        }
        else {
            property = percenta;
        }
        effector.getPropertyMap().setv(distStr, (Object)property);
    }
}
