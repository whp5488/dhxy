
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324409 extends TianFuProcessor
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
        if (sysSkill == null || 4 != sysSkill.getStep() || SKILLATTR.SKILLATTR_XIXUE.getValue() != roleSkill.getType()) {
            return null;
        }
        if (!effector.isPlayer()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324409);
        if (tianFuObject == null) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324409);
        final int isPvp = fight.isPvp();
        if (0 == isPvp) {
            final double percentb = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final int hurt = (int)args[0];
            skillRecord.setT(target.getId());
            return TianFuUtils.getHurtReturnMap(hurt, percentb);
        }
        if (1 == isPvp && fight.getRound() % 2 == 0) {
            final double percenta = 10.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final int hurt = (int)args[0];
            skillRecord.setT(target.getId());
            return TianFuUtils.getHurtReturnMap(hurt, percenta);
        }
        return null;
    }
}
