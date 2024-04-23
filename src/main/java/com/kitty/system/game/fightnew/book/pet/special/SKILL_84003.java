package com.kitty.system.game.fightnew.book.pet.special;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_84003 extends SkillProcessor {
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
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 84003);
    double oriparam = 2.5D;
    Effect effect = CountUtils.countHurt(fight, effector, target, 0.0D, oriparam);
    effect.setType(7);
    int doubleHit = 1;
    if (SkillAi.checkDoubleHit(effector)) {
      int num = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), effector.getPropertyMap().get(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())));
      doubleHit += num;
    } 
    boolean flag = SkillAi.checkPetIsSkill(fight, effector.isASide(), Integer.valueOf(81057));
    if (flag) {
      int num = PetPassiveSkillUtil.reduceDoubleHit(fight, effector);
      doubleHit = Math.max(1, doubleHit - num);
    } 
    RoleSkill skillBy = effector.getSkillBy(Integer.valueOf(84003));
    BattleUnit battleUnit = new BattleUnit(fight, effector, target, skillBy);
    battleUnit.setEffect(effect);
    battleUnit.setDoubleHit(doubleHit);
    ActionDetailParam actionDetailParam = PetPassiveSkillUtil.passivePhysics(fight, effector, target, battleUnit, null, 84003);
    RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(effector, "法攻");
    FightObjectParam attackerParam = actionDetailParam.gainAttackerParam();
    ActionParamUtils.updateObjectInfo(effector, 7, 84003, 2, fightShouting, attackerParam);
    if (args.length > 1) {
      List<ActionDetailParam> actionDetailParams = (List<ActionDetailParam>)args[1];
      if (actionDetailParams == null)
        actionDetailParams = new ArrayList<>(); 
      actionDetailParams.add(actionDetailParam);
    } 
    skillRecord.setT(target.getId().intValue());
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    if (args.length > 0 && args[0] instanceof FightObject) {
      FightObject effector = (FightObject)args[0];
      return (int)(effector.getMaxMana() * 0.19D);
    } 
    return 0;
  }
  
  public int costLife(FightObject effector) {
    double value = effector.getMaxLife() * 0.49D;
    return (int)value;
  }
  
  public boolean isExcute(Fight fight, FightObject effector, Object... args) {
    double lifeRatio = Double.parseDouble(BigDecimalUtil.divide(String.valueOf(effector.getCurrLife()), String.valueOf(effector.getMaxLife())));
    double manaRatio = Double.parseDouble(BigDecimalUtil.divide(String.valueOf(effector.getCurrMana()), String.valueOf(effector.getMaxMana())));
    if (lifeRatio < 0.5D) {
      PetSkillEffectorUtil.sendMsg(fight, effector, "气血不足！");
      return false;
    } 
    if (manaRatio < 0.2D) {
      PetSkillEffectorUtil.sendMsg(fight, effector, "法力不足！");
      return false;
    } 
    return true;
  }
}
