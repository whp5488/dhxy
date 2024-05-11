
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Optional;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324501 extends TianFuProcessor
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
        if (!effector.isPlayer()) {
            return;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324501);
        if (tianFuObject == null) {
            return;
        }
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return;
        }
        final Optional<RoleSkill> optional = effector.getSkillIds().stream().filter(rs -> rs.getSkillId().equals(roleSkill.getSkillId())).findFirst();
        if (!optional.isPresent()) {
            return;
        }
        final double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        final boolean flag = SkillAi.canAffectOnPro(percenta);
        if (flag) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324501);
            final FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId(), 0);
            if (newTarget == null) {
                return;
            }
            effector.setType(0);
            effector.setSkillId(0);
            PassiveSkillUtils.beforePhysicalAttack(fight, effector, newTarget);
            final BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget);
            battleUnit.countDoubleHit();
            final ActionDetailParam actionDetailParamReturn = PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 0);
            FightObjectParam targetParam = null;
            if (actionDetailParam.getTargetParamList().size() > 0) {
                targetParam = actionDetailParam.getTargetParamList().get(0);
            }
            if (targetParam != null) {
                ActionParamUtils.setNormalAttackAll(newTarget.getId(), actionDetailParam, actionDetailParamReturn, 118);
                skillRecord.setT(newTarget.getId());
            }
        }
    }
}
