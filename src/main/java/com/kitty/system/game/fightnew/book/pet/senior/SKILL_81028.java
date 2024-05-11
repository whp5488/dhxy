
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81028 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81028);
        final SysPetSkill petSkill = (SysPetSkill)args[0];
        if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args)) {
            return null;
        }
        final double hurt = BigDecimalUtil.decimalScaleObjectDown(8, 3500.0 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose()));
        final ArrayList<FightObject> targetSide = fight.getAllFightObjects();
        int i = 0;
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 81028, 0);
        for (final FightObject targetObject : targetSide) {
            if (targetObject.isFallDown()) {
                continue;
            }
            if (LOGIC.LOGICTYPE_HERO.getValue() != targetObject.getLogic()) {
                continue;
            }
            int targethurt = (int)hurt;
            int surplus = targetObject.getCurrLife() - targethurt;
            if (surplus < 0) {
                targethurt = targetObject.getCurrLife();
            }
            ++i;
            FightObjectParam targetParam = ActionParamUtils.changeLifeForTarget(fight, targetObject, i, -targethurt, 2);
            actionDetailParam.getTargetParamList().add(targetParam);
            if (surplus >= 0) {
                continue;
            }
            final int petId = targetObject.getFightPetId();
            if (petId <= 0) {
                continue;
            }
            final FightObject petObject = fight.queryFightObject(petId);
            if (petObject == null || petObject.isFallDown()) {
                continue;
            }
            if (petObject.getCurrLife() + surplus < 0) {
                surplus = -petObject.getCurrLife();
            }
            ++i;
            targetParam = ActionParamUtils.changeLifeForTarget(fight, petObject, i, surplus, 2);
            actionDetailParam.getTargetParamList().add(targetParam);
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            skillRecord.setT(actionDetailParam.getTargetParamList().get(0).getObjId());
        }
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
