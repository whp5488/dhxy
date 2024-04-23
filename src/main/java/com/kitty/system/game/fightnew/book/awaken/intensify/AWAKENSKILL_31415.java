package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.List;

public class AWAKENSKILL_31415 extends AwakenSkillProcessor {
  public void magicAttackEnd(Fight fight, FightObject effector, Object... args) {
    if (args == null && args.length < 3)
      return; 
    RoleSkill skill = (RoleSkill)args[0];
    ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
    List<MagicAttackParam> targets = (List<MagicAttackParam>)args[2];
    if (skill == null || actionDetailParam == null || targets == null)
      return; 
    boolean contain83020 = false;
    for (RoleSkill roleSkill : effector.getSkillIds()) {
      if (83020 == roleSkill.getSkillId().intValue()) {
        contain83020 = true;
        break;
      } 
    } 
    if ((PETSKILLATTR.PETSKILLATTR_TM.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_QM
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_AIHAO
      .getValue() == skill.getType().intValue()) && contain83020) {
      double percent = getPercent(effector);
      if (SkillAi.canAffectOnPro(percent)) {
        FightObject target = null;
        for (MagicAttackParam magicAttackParam : targets) {
          target = fight.queryFightObject(magicAttackParam.getTarget().getId());
          if (target != null && !target.isFallDown() && target.getStatusValue(1011) > 0) {
            FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), true);
            target.removeStatus(1011);
            List<Integer> clearStatus = new ArrayList<>();
            clearStatus.add(Integer.valueOf(1011));
            targetParam.setClearStatus(clearStatus);
          } 
        } 
        if (target != null) {
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, target.getId().intValue(), 31415);
          skillRecord.setT(target.getId().intValue());
        } 
      } 
    } 
  }
  
  private double getPercent(FightObject effector) {
    if (effector.isPet()) {
      PetFightObject petFightObject = (PetFightObject)effector;
      Role role = petFightObject.getRole();
      Pet pet = petFightObject.getPet();
      byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
      if (suitLevParam > 0) {
        double percent = (100 * suitLevParam) / 20.0D / 100.0D;
        if (pet.getAwaken() > 0)
          percent += (100 * pet.getAwaken()) / 20.0D / 100.0D; 
        return percent;
      } 
    } 
    return 0.0D;
  }
}
