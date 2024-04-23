package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_83037 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    int status = 2087;
    if (fight.getRound() != 1)
      status = getRandomStatus(); 
    int time = 2;
    effector.setStatus(status, time);
    if (2087 == status)
      ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.H_KB_RATIO.getDist()), 1, 0.5D); 
    int oldStatus = getOldStatus(effector);
    if (oldStatus != status) {
      ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 43, 83037, effector.getId().intValue());
      FightObjectParam targetParam = ActionParamUtils.addStatusForTarget(effector, 2, status, time, 1);
      if (oldStatus != 0) {
        effector.removeStatus(oldStatus);
        targetParam.addClearStatus(oldStatus);
      } 
      actionDetailParam.getTargetParamList().add(targetParam);
      fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    } 
    return null;
  }
  
  private int getRandomStatus() {
    int[] statuss = { 2087, 2088, 2089 };
    int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(statuss.length - 1));
    return statuss[random];
  }
  
  private int getOldStatus(FightObject fightObject) {
    if (fightObject.getStatusValue(2087) > 0)
      return 2087; 
    if (fightObject.getStatusValue(2088) > 0)
      return 2088; 
    if (fightObject.getStatusValue(2089) > 0)
      return 2089; 
    return 0;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 2;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(83037));
    if (skill != null && !target.isFrozen()) {
      double hurt = CountUtils.getPhyAttack(effector);
      Effect e = new Effect();
      int harm = PetSkillEffectorUtil.wuXingFireCount(fight, effector, target, hurt);
      e.setType(100000);
      e.setHurt(harm);
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
  
  public List<Integer> getTargetIdsForTFQX(Fight fight, FightObject effector, Object... args) {
    if (args != null && args.length > 0) {
      int status = getStatus(effector);
      if (status > 0) {
        int skillSerial = ((Integer)args[0]).intValue();
        if (status == 2088 && (skillSerial == 80007 || skillSerial == 81012))
          return PetTriggerPassiveSkill.getTFQXTargetIds(fight, effector, 83037); 
        if (status == 2089 && (skillSerial == 80009 || skillSerial == 81013))
          return PetTriggerPassiveSkill.getTFQXTargetIds(fight, effector, 83037); 
      } 
    } 
    return null;
  }
  
  private int getStatus(FightObject fightObject) {
    if (fightObject.getStatusValue(2088) > 0)
      return 2088; 
    if (fightObject.getStatusValue(2089) > 0)
      return 2089; 
    return 0;
  }
}
