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
    public AWAKENSKILL_31415() {
    }
    
    @Override
    public void magicAttackEnd(Fight fight, FightObject effector, Object... args) {
        if (args == null && args.length < 3) {
            return;
        }
        RoleSkill skill = (RoleSkill)args[0];
        ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
        List<MagicAttackParam> targets = (List)args[2];
        if (skill == null || actionDetailParam == null || targets == null) {
            return;
        }
        boolean contain83020 = false;
        for (RoleSkill roleSkill : effector.getSkillIds()) {
            if (83020 == (int)roleSkill.getSkillId()) {
                contain83020 = true;
                break;
            }
            continue;
        }
        if ((PETSKILLATTR.PETSKILLATTR_TM.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_QM.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_AIHAO.getValue() == (int)skill.getType()) && contain83020) {
            double percent = this.getPercent(effector);
            if (SkillAi.canAffectOnPro(percent)) {
                FightObject target = null;
                for (MagicAttackParam magicAttackParam : targets) {
                    target = fight.queryFightObject(magicAttackParam.getTarget().getId());
                    if (target != null && !target.isFallDown() && target.getStatusValue(1011) > 0) {
                        FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), true);
                        target.removeStatus(1011);
                        List<Integer> clearStatus = new ArrayList<>();
                        clearStatus.add(Integer.valueOf(1011));
                        targetParam.setClearStatus(clearStatus);
                    }
                    else {
                        continue;
                    }
                }
                if (target != null) {
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)target.getId(), 31415);
                    skillRecord.setT((long)(int)target.getId());
                }
            }
        }
        return;
    }
    
    private double getPercent(FightObject effector) {
        if (effector.isPet()) {
            PetFightObject petFightObject = (PetFightObject)effector;
            Role role = petFightObject.getRole();
            Pet pet = petFightObject.getPet();
            byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
            if (suitLevParam > 0) {
                double percent = (double)(100 * suitLevParam) / 20.0 / 100.0;
                if (pet.getAwaken() > 0) {
                    percent += (double)(100 * pet.getAwaken()) / 20.0 / 100.0;
                }
                return percent;
            }
        }
        return 0.0;
    }
}
