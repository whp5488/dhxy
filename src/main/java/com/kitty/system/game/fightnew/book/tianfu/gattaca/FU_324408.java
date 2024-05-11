
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import java.util.List;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324408 extends TianFuProcessor
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
        if (fight.isPvp() != 1 || !effector.isPlayer()) {
            return;
        }
        if (!effector.isPlayer()) {
            return;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324408);
        if (tianFuObject == null) {
            return;
        }
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null || SKILLATTR.SKILLATTR_ZHEN.getValue() != roleSkill.getType()) {
            return;
        }
        final SysSkill sysSkill = SkillDataPool.skillMap.get(roleSkill.getSkillId());
        if (sysSkill == null || sysSkill.getStep() != 4) {
            return;
        }
        final Integer hurt = (Integer)args[1];
        if (hurt == null || hurt <= 0) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324408);
        final FightObject unit = SkillAi.effectProBass(fight, effector, 1, new ArrayList<String>());
        final double percenta = 10.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        int addLife = (int)(hurt * percenta);
        if (addLife + unit.getCurrLife() > unit.getMaxLife()) {
            addLife = unit.getMaxLife() - unit.getCurrLife();
        }
        if (addLife > 0) {
            final FightObjectParam targetParam = actionDetailParam.gainTargetParam(unit.getId(), true);
            PassiveSkillUtils.excuteChangeLifeAuto(fight, unit, actionDetailParam, targetParam, addLife);
            skillRecord.setT(target.getId());
        }
    }
}
