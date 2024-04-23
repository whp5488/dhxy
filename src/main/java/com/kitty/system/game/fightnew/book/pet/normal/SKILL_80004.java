package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.common.utils.BigDecimalUtil;
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
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_80004 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void normalAttack(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (actionDetailParam == null)
      return; 
    FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), true);
    if (!targetParam.isHit())
      return; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 80004);
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 1.5D * (effector.getRelive() * 0.4D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag && 
      !target.isFrozen()) {
      int round = 1;
      FightParamPool.addControlStatus(fight, target, 1003);
      target.getStatusMap().setv(String.valueOf(1003), Integer.valueOf(round));
      List<Integer> list = new ArrayList<>();
      if (target.isConfusion()) {
        list.add(Integer.valueOf(1002));
        target.setStatus(1002, 0);
      } 
      if (target.isForgotten()) {
        list.add(Integer.valueOf(1006));
        target.setStatus(1006, 0);
      } 
      if (target.isSleep()) {
        list.add(Integer.valueOf(1004));
        target.setStatus(1004, 0);
      } 
      if (target.getStatusMap().getInt(String.valueOf(1021)) > 0) {
        target.removeStatus(1021);
        list.add(Integer.valueOf(1021));
      } 
      targetParam.addDamageType(1003);
      targetParam.getStatusMap().put(Integer.valueOf(1003), Integer.valueOf(round));
      if (list.size() > 0)
        targetParam.addClearStatus(list); 
      skillRecord.setT(target.getId().intValue());
    } 
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
