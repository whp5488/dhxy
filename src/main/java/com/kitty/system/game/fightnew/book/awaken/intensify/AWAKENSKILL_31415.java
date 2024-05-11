
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.Iterator;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import java.util.List;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31415 extends AwakenSkillProcessor
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
        boolean contain83020 = false;
        for (final RoleSkill roleSkill : effector.getSkillIds()) {
            if (83020 == roleSkill.getSkillId()) {
                contain83020 = true;
                break;
            }
        }
        if ((PETSKILLATTR.PETSKILLATTR_TM.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_QM.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_AIHAO.getValue() == skill.getType()) && contain83020) {
            final double percent = this.getPercent(effector);
            if (SkillAi.canAffectOnPro(percent)) {
                FightObject target = null;
                for (final MagicAttackParam magicAttackParam : targets) {
                    target = fight.queryFightObject(magicAttackParam.getTarget().getId());
                    if (target != null && !target.isFallDown() && target.getStatusValue(1011) > 0) {
                        final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), true);
                        target.removeStatus(1011);
                        final List<Integer> clearStatus = new ArrayList<Integer>();
                        clearStatus.add(1011);
                        targetParam.setClearStatus(clearStatus);
                    }
                }
                if (target != null) {
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, target.getId(), 31415);
                    skillRecord.setT(target.getId());
                }
            }
        }
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
