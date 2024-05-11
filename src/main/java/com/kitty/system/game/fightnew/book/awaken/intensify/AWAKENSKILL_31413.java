
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31413 extends AwakenSkillProcessor
{
    @Override
    public void magicAttackEnd(final Fight fight, final FightObject effector, final Object... args) {
        if (args == null && args.length < 3) {
            return;
        }
        final RoleSkill skill = (RoleSkill)args[0];
        final ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
        final List<MagicAttackParam> targets = (List<MagicAttackParam>)args[2];
        if (skill == null || actionDetailParam == null || targets == null) {
            return;
        }
        final List<Integer> targetIds = new ArrayList<Integer>();
        for (final MagicAttackParam magicAttackParam : targets) {
            if (magicAttackParam.getTarget() != null) {
                targetIds.add(magicAttackParam.getTarget().getId());
            }
        }
        final List<FightObject> mySide = fight.getMySide(effector.getId());
        if (mySide == null || mySide.size() <= 0) {
            return;
        }
        FightObject newTarget = null;
        double cpt = 1.0;
        for (final FightObject fightObject : mySide) {
            if (!targetIds.contains(fightObject.getId())) {
                final double tempCpt = fightObject.getCurrLife() / (double)fightObject.getMaxLife();
                if (tempCpt >= cpt) {
                    continue;
                }
                cpt = tempCpt;
                newTarget = fightObject;
            }
        }
        if (newTarget == null) {
            return;
        }
        final double percent = this.getPercent(effector);
        boolean flag = false;
        if (percent != 0.0) {
            flag = SkillAi.canAffectOnPro(percent);
        }
        if (!flag) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31413);
        final FightObjectParam targetParam = actionDetailParam.gainTargetParam(newTarget.getId(), true);
        final int addHp = (int)(newTarget.getMaxLife() * 0.5);
        PassiveSkillUtils.excuteChangeLifeAuto(fight, newTarget, actionDetailParam, targetParam, addHp);
        skillRecord.setT(newTarget.getId());
    }
    
    private double getPercent(final FightObject effector) {
        if (effector.isPet()) {
            final PetFightObject petFightObject = (PetFightObject)effector;
            final Role role = petFightObject.getRole();
            final Pet pet = petFightObject.getPet();
            final byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
            if (suitLevParam > 0) {
                double percent = 100 * suitLevParam / 20.0 / 100.0;
                if (pet.getAwaken() > 0) {
                    percent += 100 * pet.getAwaken() / 20.0 / 100.0;
                }
                return percent;
            }
        }
        return 0.0;
    }
}
