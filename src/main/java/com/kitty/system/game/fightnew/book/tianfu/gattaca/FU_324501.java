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
    public FU_324501() {
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
        if (!effector.isPlayer()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324501);
        if (tianFuObject == null) {
            return;
        }
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return;
        }
        Optional<RoleSkill> optional = effector.getSkillIds().stream().filter(rs -> rs.getSkillId().equals(roleSkill.getSkillId())).findFirst();
        if (!optional.isPresent()) {
            return;
        }
        double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        boolean flag = SkillAi.canAffectOnPro(percenta);
        if (flag) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324501);
            FightObject newTarget = fight.getTargetNoCurrentTarget((int)effector.getId(), 0);
            if (newTarget == null) {
                return;
            }
            effector.setType(0);
            effector.setSkillId(0);
            PassiveSkillUtils.beforePhysicalAttack(fight, effector, newTarget);
            BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget);
            battleUnit.countDoubleHit();
            ActionDetailParam actionDetailParamReturn = PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 0);
            FightObjectParam targetParam = null;
            if (actionDetailParam.getTargetParamList().size() > 0) {
                targetParam = (FightObjectParam)actionDetailParam.getTargetParamList().get(0);
            }
            if (targetParam != null) {
                ActionParamUtils.setNormalAttackAll((int)newTarget.getId(), actionDetailParam, actionDetailParamReturn, Integer.valueOf(118));
                skillRecord.setT((long)(int)newTarget.getId());
            }
        }
        return;
    }
}
