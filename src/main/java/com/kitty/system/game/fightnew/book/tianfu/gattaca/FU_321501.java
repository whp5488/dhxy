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
    public FU_321501() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill roleSkill = (RoleSkill)args[1];
        if (roleSkill == null || roleSkill.getSkillId() == null || (int)roleSkill.getSkillId() < 1) {
            return null;
        }
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(roleSkill.getSkillId());
        if (sysSkill == null) {
            return null;
        }
        double lifePercent = (double)(effector.getCurrLife() / effector.getMaxLife());
        if (lifePercent >= 1.0) {
            return null;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 321501);
        double percent = 1.0 - lifePercent;
        boolean flag = SkillAi.canAffectOnPro(percent);
        Double percenta = this.getPercent(effector);
        if (percenta == null) {
            return null;
        }
        if (flag && (30001 == (int)sysSkill.getSerial() || 30002 == (int)sysSkill.getSerial())) {
            this.installProperty(effector, PRO_DICT_TYPE.K_HL.getDist(), percenta);
            skillRecord.setT((long)(int)effector.getId());
        }
        else if (flag && (30003 == (int)sysSkill.getSerial() || 30004 == (int)sysSkill.getSerial())) {
            this.installProperty(effector, PRO_DICT_TYPE.K_HS.getDist(), percenta);
            skillRecord.setT((long)(int)effector.getId());
        }
        else if (flag && (30005 == (int)sysSkill.getSerial() || 30006 == (int)sysSkill.getSerial())) {
            this.installProperty(effector, PRO_DICT_TYPE.K_FY.getDist(), percenta);
            skillRecord.setT((long)(int)effector.getId());
        }
        else if (flag && (30029 == (int)sysSkill.getSerial() || 30030 == (int)sysSkill.getSerial())) {
            this.installProperty(effector, PRO_DICT_TYPE.K_YW.getDist(), percenta);
            skillRecord.setT((long)(int)effector.getId());
        }
        return null;
    }
    
    private Double getPercent(FightObject effector) {
        if (!effector.isPlayer()) {
            return null;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321501);
        if (tianFuObject == null) {
            return null;
        }
        double percent = 0.3 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        return Double.valueOf(percent);
    }
    
    private void installProperty(FightObject effector, int dist, Double percenta) {
        String distStr = String.valueOf(dist);
        double property = effector.getPropertyMap().getDouble(distStr);
        if (property > 0.0) {
            property += (double)percenta;
        }
        else {
            property = (double)percenta;
        }
        effector.getPropertyMap().setv(distStr, Double.valueOf(property));
    }
}
