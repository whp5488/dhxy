package com.kitty.system.game.fightnew.book.awaken.intensify;

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
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.List;

public class AWAKENSKILL_31413 extends AwakenSkillProcessor {
  public void magicAttackEnd(Fight fight, FightObject effector, Object... args) {
    if (args == null && args.length < 3)
      return; 
    RoleSkill skill = (RoleSkill)args[0];
    ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
    List<MagicAttackParam> targets = (List<MagicAttackParam>)args[2];
    if (skill == null || actionDetailParam == null || targets == null)
      return; 
    List<Integer> targetIds = new ArrayList<>();
    for (MagicAttackParam magicAttackParam : targets) {
      if (magicAttackParam.getTarget() != null)
        targetIds.add(magicAttackParam.getTarget().getId()); 
    } 
    List<FightObject> mySide = fight.getMySide(effector.getId().intValue());
    if (mySide == null || mySide.size() <= 0)
      return; 
    FightObject newTarget = null;
    double cpt = 1.0D;
    for (FightObject fightObject : mySide) {
      if (!targetIds.contains(fightObject.getId())) {
        double tempCpt = fightObject.getCurrLife() / fightObject.getMaxLife();
        if (tempCpt < cpt) {
          cpt = tempCpt;
          newTarget = fightObject;
        } 
      } 
    } 
    if (newTarget == null)
      return; 
    double percent = getPercent(effector);
    boolean flag = false;
    if (percent != 0.0D)
      flag = SkillAi.canAffectOnPro(percent); 
    if (!flag)
      return; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31413);
    FightObjectParam targetParam = actionDetailParam.gainTargetParam(newTarget.getId().intValue(), true);
    int addHp = (int)(newTarget.getMaxLife() * 0.5D);
    PassiveSkillUtils.excuteChangeLifeAuto(fight, newTarget, actionDetailParam, targetParam, addHp);
    skillRecord.setT(newTarget.getId().intValue());
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
