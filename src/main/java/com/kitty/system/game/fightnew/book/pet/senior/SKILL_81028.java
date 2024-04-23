package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;

public class SKILL_81028 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81028);
    SysPetSkill petSkill = (SysPetSkill)args[0];
    if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args))
      return null; 
    double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(8), Double.valueOf(3500.0D * (effector.getRelive() * 0.4D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
    ArrayList<FightObject> targetSide = fight.getAllFightObjects();
    int i = 0;
    ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 81028, 0);
    for (FightObject targetObject : targetSide) {
      if (targetObject.isFallDown())
        continue; 
      if (LOGIC.LOGICTYPE_HERO.getValue() != targetObject.getLogic())
        continue; 
      int targethurt = (int)hurt;
      int surplus = targetObject.getCurrLife() - targethurt;
      if (surplus < 0)
        targethurt = targetObject.getCurrLife(); 
      i++;
      FightObjectParam targetParam = ActionParamUtils.changeLifeForTarget(fight, targetObject, i, -targethurt, 2);
      actionDetailParam.getTargetParamList().add(targetParam);
      if (surplus < 0) {
        int petId = targetObject.getFightPetId();
        if (petId > 0) {
          FightObject petObject = fight.queryFightObject(Integer.valueOf(petId));
          if (petObject != null && !petObject.isFallDown()) {
            if (petObject.getCurrLife() + surplus < 0)
              surplus = -petObject.getCurrLife(); 
            i++;
            targetParam = ActionParamUtils.changeLifeForTarget(fight, petObject, i, surplus, 2);
            actionDetailParam.getTargetParamList().add(targetParam);
          } 
        } 
      } 
    } 
    if (actionDetailParam.getTargetParamList().size() > 0) {
      fight.getActionParam().getActionDetailParams().add(actionDetailParam);
      skillRecord.setT(((FightObjectParam)actionDetailParam.getTargetParamList().get(0)).getObjId());
    } 
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
