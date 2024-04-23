package com.kitty.system.game.fightnew.utils;

import com.kitty.common.utils.RandomUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.hero.utils.HeroSkillUtil;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuSkillUtils;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PassiveSkillUtils {
  public static void beforeDoAction(Fight fight, FightObject effector) {
    try {
      TianFuSkillUtils.beforeDoAction(fight, effector);
      AwakenSkillUtils.beforeDoAction(fight, effector);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void selfSellEnd(Fight fight, FightObject fightObject) {
    try {
      PetTriggerPassiveSkill.excutePassiveSkill(fight, fightObject, PET_PASSIVE_SKILL_TRIGGER_DICT.SELF_SELL_END.getValue(), new Object[0]);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static int changeHurt(Fight fight, FightObject effector, FightObject target, int hurt, boolean isFury, RoleSkill skill, List<Integer> clearStatusList) {
    int chageHurt = 0;
    try {
      chageHurt += TianFuSkillUtils.calculateHurt(fight, effector, target, hurt);
      chageHurt += HorseSkillUtils.reduceHurt(fight, effector, target, hurt, isFury, skill);
      chageHurt += AcsrySkillUtil.executeReduceHp(fight, effector, target, skill, hurt, clearStatusList);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return chageHurt;
  }
  
  public static void beforeMagic(Fight fight, FightObject effector, RoleSkill skill, List<Integer> targets) {
    try {
      TianFuSkillUtils.beforeDoMagic(fight, effector, skill);
      HorseSkillUtils.beforeMagicHurt(fight, effector, skill);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void beforeMagicOne(Fight fight, FightObject effector, FightObject target, RoleSkill skill) {
    try {
      AcsrySkillUtil.beforeExecuteMagic(fight, effector, target, skill);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void beforeSkillEffect(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill) {
    try {
      TianFuSkillUtils.beforeSkillEffect(fight, effector, target, roleSkill);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void statusOver(Fight fight, Map<Integer, List<Integer>> clearStatusMap) {
    for (Map.Entry<Integer, List<Integer>> entry : clearStatusMap.entrySet()) {
      FightObject obj = fight.queryFightObject(entry.getKey());
      if (obj != null) {
        List<Integer> clearList = entry.getValue();
        for (Integer status : clearList) {
          AcsrySkillUtil.statusOver(fight, obj, status.intValue());
          if (1006 == status.intValue())
            obj.getStatusMap().setv(String.valueOf(8008), Integer.valueOf(0)); 
          PetTriggerPassiveSkill.statusClear(fight, obj, status.intValue());
          AwakenSkillUtils.statusOver(fight, obj, status.intValue());
        } 
      } 
    } 
  }
  
  public static void roundStart(Fight fight) {
    for (FightObject fightObject : fight.getAllFightObjects()) {
      try {
        AcsrySkillUtil.roundStart(fight, fightObject);
        AwakenSkillUtils.roundStart(fight, fightObject);
      } catch (Exception e) {
        e.printStackTrace();
      } 
    } 
  }
  
  public static void roundEnd(Fight fight) {
    for (FightObject fightObject : fight.getAllFightObjects()) {
      if (fightObject.isPlayer())
        try {
          AcsrySkillUtil.roundEnd(fight, fightObject);
        } catch (Exception e) {
          e.printStackTrace();
        }  
    } 
  }
  
  public static void magicAttackEnd(Fight fight, FightObject effector, List<MagicAttackParam> targets, RoleSkill skill, ActionDetailParam actionDetailParam) {
    try {
      TianFuSkillUtils.magicAttackEnd(fight, effector, targets, skill, actionDetailParam);
      for (MagicAttackParam magicParam : targets) {
        FightObject target = magicParam.getTarget();
        HeroSkillUtil.attackEnd(fight, effector, target, actionDetailParam);
        if (magicParam.isDodge())
          HorseSkillUtils.targetHits(fight, effector, target, actionDetailParam); 
        AcsrySkillUtil.attackEnd(fight, effector, target, skill, magicParam.isDodge(), magicParam.getClearStatusList(), magicParam.getHurt(), magicParam
            .getTargetReduceMp(), magicParam.getSideTarget(), actionDetailParam);
        PetPassiveSkillUtil.magicOther(fight, effector, magicParam.getTarget(), skill, actionDetailParam);
      } 
      AwakenSkillUtils.magicAttackEnd(fight, effector, skill, actionDetailParam, targets);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void afterPrintCommand(Fight fight) {
    try {
      AcsrySkillUtil.afterPrintCommand(fight);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static boolean isUnlock(Fight fight, FightObject fightObject, Integer status) {
    boolean flag = AcsrySkillUtil.isUnlock(fight, fightObject, status);
    return flag;
  }
  
  public static void excutePropertyChange(FightObject fightObject) {
    try {
      AcsrySkillUtil.excutePropertyChange(fightObject);
      PetPassiveSkillUtil.checkControlledStatus(fightObject);
      TianFuSkillUtils.clearTianFuProperty(fightObject);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static int countRecoverBloodEnd(Fight fight, FightObject fightObject, int life, RoleSkill skill) {
    int changeLife = 0;
    try {
      changeLife += AcsrySkillUtil.countRecoverBloodEnd(fight, fightObject, skill, life);
      AwakenSkillUtils.countRecoverBloodEnd(fight, fightObject, skill, life);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return changeLife;
  }
  
  public static int countRecoverMpEnd(Fight fight, FightObject fightObject, int mana) {
    int changeMana = 0;
    try {
      changeMana += AcsrySkillUtil.countRecoverMpEnd(fight, fightObject, mana);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return changeMana;
  }
  
  public static int countRecoverBloodEndSS(Fight fight, FightObject effecter, FightObject target, RoleSkill skill, int life) {
    int changeLife = 0;
    try {
      changeLife += AcsrySkillUtil.countRecoverBloodEndSS(fight, effecter, target, skill, life);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return changeLife;
  }
  
  public static int reduceMp(Fight fight, FightObject fightObject, int mana) {
    int changeMana = 0;
    try {
      changeMana += AcsrySkillUtil.reduceMp(fight, fightObject, mana);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return changeMana;
  }
  
  public static int beforeReduceHurtMp(Fight fight, FightObject effecter, FightObject target, RoleSkill skill, int mp) {
    int changeMp = 0;
    try {
      changeMp += AcsrySkillUtil.beforeReduceHurtMp(fight, effecter, target, skill, mp);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return changeMp;
  }
  
  public static int poisonReduceHp(Fight fight, FightObject target, int hurt) {
    int change = 0;
    try {
      change += AcsrySkillUtil.poisonReduceHp(fight, target, hurt);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return change;
  }
  
  public static void phySicalLastHit(Fight fight, FightObject effector, FightObject target, RoleSkill skill, int hurt, ActionDetailParam actionDetailParam) {
    try {
      if (skill == null && effector.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist())) > 0.0D)
        gsdn(fight, effector, target, hurt, actionDetailParam); 
      if (skill == null && effector.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT_NUM.getDist())) > 0)
        flgj(fight, effector, target, actionDetailParam); 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void excuteChangeLife(Fight fight, FightObject target, FightObjectParam fightObjectParam, int changeLife) {
    changeLife = countChangeLife(fight, target, changeLife);
    target.setCurrLife(target.getCurrLife() + changeLife);
    fightObjectParam.setChangeLife(fightObjectParam.getChangeLife() + changeLife);
    fightObjectParam.setCurrentLife(target.getCurrLife());
  }
  
  private static int countChangeLife(Fight fight, FightObject target, int changeLife) {
    if (changeLife > 0) {
      int cLife = countRecoverBloodEnd(fight, target, changeLife, null);
      changeLife += cLife;
    } 
    if (target.getCurrLife() + changeLife > target.getMaxLife()) {
      changeLife = target.getMaxLife() - target.getCurrLife();
    } else if (target.getCurrLife() + changeLife < 0) {
      changeLife = -target.getCurrLife();
    } 
    return changeLife;
  }
  
  public static FightObjectParam excuteChangeLifeAuto(Fight fight, FightObject target, ActionDetailParam actionDetailParam, FightObjectParam fightObjectParam, int changeLife) {
    changeLife = countChangeLife(fight, target, changeLife);
    target.setCurrLife(target.getCurrLife() + changeLife);
    if (fightObjectParam.getChangeLife() != 0) {
      FightObjectParam targetParamC = new FightObjectParam();
      targetParamC.setObjId(target.getId().intValue());
      targetParamC.setOrder(1);
      targetParamC.setMissed(2);
      targetParamC.setChangeLife(changeLife);
      targetParamC.setCurrentLife(target.getCurrLife());
      actionDetailParam.getTargetParamList().add(targetParamC);
      return targetParamC;
    } 
    fightObjectParam.setChangeLife(changeLife);
    fightObjectParam.setCurrentLife(target.getCurrLife());
    return fightObjectParam;
  }
  
  public static void excuteChangeMana(Fight fight, FightObject target, FightObjectParam fightObjectParam, int changeMana) {
    if (target.isMonster())
      return; 
    if (changeMana > 0) {
      int cMana = countRecoverMpEnd(fight, target, changeMana);
      changeMana += cMana;
    } 
    if (target.getCurrMana() + changeMana > target.getMaxMana()) {
      changeMana = target.getMaxMana() - target.getCurrMana();
    } else if (target.getCurrMana() + changeMana < 0) {
      changeMana = target.getCurrMana();
    } 
    target.setCurrMana(target.getCurrMana() + changeMana);
    fightObjectParam.setChangeMana(fightObjectParam.getChangeMana() + changeMana);
    fightObjectParam.setCurrentMana(target.getCurrMana());
  }
  
  public static void excuteChangeManaAuto(Fight fight, FightObject target, ActionDetailParam actionDetailParam, FightObjectParam fightObjectParam, int changeMana) {
    if (changeMana > 0) {
      int cMana = countRecoverMpEnd(fight, target, changeMana);
      changeMana += cMana;
    } 
    if (target.getCurrMana() + changeMana > target.getMaxMana()) {
      changeMana = target.getMaxMana() - target.getCurrMana();
    } else if (target.getCurrMana() + changeMana < 0) {
      changeMana = target.getCurrMana();
    } 
    target.setCurrMana(target.getCurrMana() + changeMana);
    if (fightObjectParam.getChangeMana() != 0) {
      FightObjectParam targetParamC = new FightObjectParam();
      targetParamC.setObjId(target.getId().intValue());
      targetParamC.setOrder(1);
      targetParamC.setMissed(2);
      targetParamC.setChangeMana(changeMana);
      targetParamC.setCurrentMana(target.getCurrMana());
      actionDetailParam.getTargetParamList().add(targetParamC);
    } else {
      fightObjectParam.setChangeMana(changeMana);
      fightObjectParam.setCurrentMana(target.getCurrMana());
    } 
  }
  
  public static double getWsk(FightObject effecter) {
    double wsk = 0.0D;
    wsk += effecter.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist()), 0.0D);
    wsk += effecter.getSelfAllTempPro(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist());
    return wsk;
  }
  
  public static void beforePhysicalAttack(Fight fight, FightObject attacker, FightObject fightTarget) {
    try {
      TianFuSkillUtils.beforePhysicalAttack(fight, attacker, fightTarget);
      AcsrySkillUtil.beforePhysicalAttack(fight, attacker, fightTarget);
      AwakenSkillUtils.beforePhysicalAttack(fight, attacker, fightTarget);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void gsdn(Fight fight, FightObject effector, FightObject target, int hurt, ActionDetailParam actionDetailParam) {
    FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId().intValue(), target.getId().intValue());
    if (newTarget == null)
      return; 
    Effect effect = new Effect();
    effect.setHurt(hurt);
    RoleSkill skillBy = new RoleSkill();
    skillBy.setType(Integer.valueOf(0));
    skillBy.setSkillId(Integer.valueOf(80011));
    BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
    battleUnit.setEffect(effect);
    ActionDetailParam actionDetailParamReturn = PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 80011);
    ActionParamUtils.setNormalAttack(newTarget.getId().intValue(), actionDetailParam, actionDetailParamReturn, Integer.valueOf(105));
  }
  
  public static void flgj(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam) {
    FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId().intValue(), target.getId().intValue());
    if (newTarget == null)
      return; 
    effector.getSkillCoolMap().setv(String.valueOf(80023), Integer.valueOf(1));
    RoleSkill skillBy = new RoleSkill();
    skillBy.setType(Integer.valueOf(0));
    skillBy.setSkillId(Integer.valueOf(80023));
    int num = effector.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT_NUM.getDist()), 0);
    num--;
    if (num <= 0) {
      effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT.getDist()));
      effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT_NUM.getDist()));
    } else {
      effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FLGJ_HURT_NUM.getDist()), Integer.valueOf(num));
    } 
    BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
    battleUnit.countDoubleHit();
    ActionDetailParam actionDetailParamReturn = PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 80023);
    ActionParamUtils.setNormalAttack(newTarget.getId().intValue(), actionDetailParam, actionDetailParamReturn, Integer.valueOf(116));
  }
  
  public static boolean isAttactCloaking(Fight fight, FightObject fightObject, RoleSkill roleSkill) {
    boolean attactCloaking = false;
    try {
      attactCloaking = AcsrySkillUtil.magicSelectTarget(fight, fightObject, roleSkill);
      if (attactCloaking)
        return attactCloaking; 
      attactCloaking = PetTriggerPassiveSkill.isAttackCloaking(fightObject, fightObject.getType());
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return attactCloaking;
  }
  
  public static int doubleHitExcute(Fight fight, FightObject effector, FightObject target, RoleSkill skill, BattleUnit battleUnit, String skillTrigget, int doubleHit) {
    try {
      Effect effectDoubleHit = PetPassiveSkillUtil.getPassiveSkill(fight, effector, target, skill, battleUnit, skillTrigget, doubleHit);
      if (effectDoubleHit != null) {
        Optional<FiedValue> any = effectDoubleHit.getList().stream().filter(fiedValue -> "doubleHit".equals(fiedValue.getType())).findAny();
        if (any.isPresent())
          doubleHit = Integer.valueOf(String.valueOf(((FiedValue)any.get()).getValue())).intValue(); 
      } 
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } 
    return doubleHit;
  }
  
  public static Effect getPassiveSkill(Fight fight, FightObject effector, FightObject target, RoleSkill skill, BattleUnit battleUnit, String skillTrigget, int hurt) {
    try {
      Effect effect = PetPassiveSkillUtil.getPassiveSkill(fight, effector, target, skill, battleUnit, skillTrigget, hurt);
      return effect;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static void excutePetPKPetPassiveSkill(Fight fight, FightObject effector, FightObject target, Effect effect) {
    try {
      PetTriggerPassiveSkill.excutePetPKPetPassiveSkill(fight, effector, target, effect);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static int addMagicEffect(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill, int hurt, Effect effect, int index) {
    int changeHurt = 0;
    try {
      changeHurt += TianFuSkillUtils.addMagicEffect(fight, effector, target, roleSkill, hurt, effect);
      changeHurt += AwakenSkillUtils.changeHurt(fight, effector, target, roleSkill, index);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return changeHurt;
  }
  
  public static void phyAttackEnd(Fight fight, FightObject effector, BattleUnit battleUnit, ActionDetailParam actionDetailParam) {
    try {
      TianFuSkillUtils.physicalAttack(fight, effector, battleUnit.getTarget(), actionDetailParam);
      TianFuSkillUtils.afterPhyAttack(fight, effector, battleUnit, actionDetailParam);
      HorseSkillUtils.targetHits(fight, effector, battleUnit.getTarget(), actionDetailParam);
      AcsrySkillUtil.attackEnd(fight, effector, battleUnit.getTarget(), null, battleUnit.isDodge(), battleUnit.getClearStatusList(), battleUnit.getPhyHurt(), battleUnit
          .getTargetReduceMp(), battleUnit.getSideTarget(), actionDetailParam);
      AwakenSkillUtils.phyAttackEnd(fight, effector, battleUnit, actionDetailParam);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void normalAttack(Fight fight, FightObject effector, FightObject target, BattleUnit battleUnit, int hurt, int skillSerial) {
    try {
      PetPassiveSkillUtil.normalAttackDoubleHit(fight, effector, target, battleUnit, hurt, skillSerial);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void normalAttackAfter(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam) {
    try {
      PetPassiveSkillUtil.normalAttackAfter(fight, effector, target, actionDetailParam);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void unitMagic(Fight fight, FightObject attacker, SkillProcessor processor, RoleSkill skill) {
    try {
      PetPassiveSkillUtil.unitMagic(fight, attacker, processor, skill);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static int subtractMp(Fight fight, FightObject effector, int mp) {
    int changeMp = 0;
    try {
      changeMp += TianFuSkillUtils.subtractMp(fight, effector, mp);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return changeMp;
  }
  
  public static boolean enemyFristMagic(Fight fight, FightObject attacker, SkillProcessor processor, RoleSkill skill) {
    boolean flag = false;
    try {
      flag = PetPassiveSkillUtil.enemyFristMagic(fight, attacker, processor, skill, fight.isPvp());
      if (flag)
        return flag; 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return flag;
  }
  
  public static void magicMiss(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill, ActionDetailParam actionDetailParam) {
    try {
      TianFuSkillUtils.magicMiss(fight, effector, target, roleSkill, actionDetailParam);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void excutePassiveSkill(Fight fight, FightObject effector, String type, Object... args) {
    try {
      PetTriggerPassiveSkill.excutePassiveSkill(fight, effector, type, args);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void magicAttackEndOther(Fight fight, FightObject effector, RoleSkill skill, List<MagicAttackParam> magicTargets) {
    try {
      AwakenSkillUtils.magicAttackEndOhter(fight, effector, skill, magicTargets);
      if (magicTargets != null && magicTargets.size() > 0)
        for (MagicAttackParam magicAttackParam : magicTargets) {
          FightObject target = magicAttackParam.getTarget();
          int hurt = magicAttackParam.getHurt();
          if (target == null || hurt <= 0)
            continue; 
          sufferHarm(fight, target, hurt, effector);
        }  
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void friendMagic(Fight fight, FightObject effector, ActionDetailParam actionDetailParam) {
    try {
      PetPassiveSkillUtil.friendMagic(fight, effector, fight.isPvp(), actionDetailParam);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static List<FiedValue> useDrug(Fight fight, FightObject effector, FightObject target, int showClass) {
    List<FiedValue> fiedValues = new ArrayList<>();
    try {
      List<FiedValue> fiedValues1 = TianFuSkillUtils.useDrug(fight, effector, target);
      if (fiedValues1 != null && fiedValues1.size() >= 0)
        fiedValues.addAll(fiedValues1); 
      List<FiedValue> fiedValues2 = AwakenSkillUtils.useDrug(fight, effector, target, showClass);
      if (fiedValues2 != null && fiedValues2.size() >= 0)
        fiedValues.addAll(fiedValues2); 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return fiedValues;
  }
  
  public static void exitOrClearPositive(Fight fight, FightObject effector, FightObject target, List<Integer> statusList) {
    try {
      TianFuSkillUtils.exitOrClearPositive(fight, effector, target, statusList);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void statusReduce(Fight fight, FightObject fightObject, Integer status) {
    try {
      TianFuSkillUtils.statusReduce(fight, fightObject, status);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void sufferHarm(Fight fight, FightObject target, int hurt, FightObject effector) {
    try {
      AwakenSkillUtils.sufferHarm(fight, target, hurt, effector);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void beforeCountHurt(Fight fight, FightObject effector, FightObject target) {
    try {
      TianFuSkillUtils.beforeCountHurt(fight, effector, target);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void enterFight(Fight fight, FightObject fightObject) {
    try {
      if (fightObject.isPet())
        AwakenSkillUtils.enterFight(fight, fightObject); 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static FightObject getRandomTarget(Fight fight, FightObject effector, byte type) {
    List<FightObject> targets = getTargets(fight, effector, type);
    if (targets != null && targets.size() > 0) {
      int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(targets.size() - 1));
      return targets.get(round);
    } 
    return null;
  }
  
  public static List<FightObject> getTargets(Fight fight, FightObject effector, byte type) {
    List<FightObject> targetSide = fight.getTargetSide(effector.getId().intValue());
    if (targetSide != null && targetSide.size() > 0) {
      List<FightObject> list = new ArrayList<>();
      for (FightObject obj : targetSide) {
        if (!obj.isFallDown() && obj.getStatusValue(1011) <= 0 && obj.getStatusValue(1003) <= 0)
          list.add(obj); 
      } 
      if (list.size() > 0) {
        List<FightObject> returnList = new ArrayList<>();
        if (type == 0) {
          returnList = list;
        } else if (type == 1) {
          for (FightObject obj : list) {
            if (obj.isPlayer())
              returnList.add(obj); 
          } 
        } else if (type == 2) {
          for (FightObject obj : list) {
            if (obj.isPet())
              returnList.add(obj); 
          } 
        } else if (type == 3) {
          for (FightObject obj : list) {
            if (obj.isPartner())
              returnList.add(obj); 
          } 
        } 
        if (returnList.size() > 0)
          return returnList; 
      } 
    } 
    return null;
  }
  
  public static List<FightObject> getTargetForCondition(Fight fight, FightObject effector, List<Integer> excludeTargets, int type, boolean isMin, int num) {
    List<FightObject> targets = getTargets(fight, effector, (byte)0);
    if (targets == null || targets.size() <= 0 || num > 0)
      return null; 
    List<FightObject> list = new ArrayList<>();
    for (FightObject obj : targets) {
      if (excludeTargets.contains(obj.getId()))
        break; 
      list.add(obj);
    } 
    if (list.size() > 0) {
      List<FightObject> returnList = new ArrayList<>();
      if (type == 1) {
        if (isMin) {
          list.sort(Comparator.comparing(obj -> Integer.valueOf(obj.getSpeed())));
        } else {
          list = (List<FightObject>)list.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        } 
        if (list.size() > num) {
          for (int i = 0; i < num - 1; i++)
            returnList.add(list.get(i)); 
        } else {
          returnList.addAll(list);
        } 
      } 
      if (returnList.size() > 0)
        return returnList; 
    } 
    return null;
  }
}
