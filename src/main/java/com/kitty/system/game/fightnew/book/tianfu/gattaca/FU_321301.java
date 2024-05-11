
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321301 extends TianFuProcessor
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
        if (4 != sysSkill.getStep()) {
            return null;
        }
        if (!effector.isPlayer()) {
            return null;
        }
        final int isPvp = fight.isPvp();
        if (0 == isPvp) {
            final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321301);
            if (tianFuObject == null) {
                return null;
            }
            final double percentb = 2.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final int hurt = (int)args[0];
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321301);
            skillRecord.setT(effector.getId());
            return TianFuUtils.getHurtReturnMap(hurt, percentb);
        }
        else {
            if (1 != isPvp || fight.getRound() % 5 != 0) {
                return null;
            }
            final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321301);
            if (tianFuObject == null) {
                return null;
            }
            final double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final int hurt = (int)args[0];
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321301);
            skillRecord.setT(effector.getId());
            return TianFuUtils.getHurtReturnMap(hurt, percenta);
        }
    }
}
