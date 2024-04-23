package com.kitty.system.game.fightnew.model;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.LOGIC_SKILL;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.fightnew.utils.FightRateUtil;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import com.kitty.system.init.domain.SysDataAcsrySuitColor;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class SkillAction {
  public void setLogger(Logger logger) {
    this.logger = logger;
  }
  
  public void setFight(Fight fight) {
    this.fight = fight;
  }
  
  public void setEffector(FightObject effector) {
    this.effector = effector;
  }
  
  public void setSkill(RoleSkill skill) {
    this.skill = skill;
  }
  
  public void setSkillTargets(List<Integer> skillTargets) {
    this.skillTargets = skillTargets;
  }
  
  public void setSideTargets(List<Integer> sideTargets) {
    this.sideTargets = sideTargets;
  }
  
  public void setProcessor(SkillProcessor processor) {
    this.processor = processor;
  }
  
  public void setMagicTargets(List<MagicAttackParam> magicTargets) {
    this.magicTargets = magicTargets;
  }
  
  public void setActionDetailParams(List<ActionDetailParam> actionDetailParams) {
    this.actionDetailParams = actionDetailParams;
  }
  
  @ConstructorProperties({"logger", "fight", "effector", "skill", "skillTargets", "sideTargets", "processor", "magicTargets", "actionDetailParams"})
  public SkillAction(Logger logger, Fight fight, FightObject effector, RoleSkill skill, List<Integer> skillTargets, List<Integer> sideTargets, SkillProcessor processor, List<MagicAttackParam> magicTargets, List<ActionDetailParam> actionDetailParams) {
    this.logger = logger;
    this.fight = fight;
    this.effector = effector;
    this.skill = skill;
    this.skillTargets = skillTargets;
    this.sideTargets = sideTargets;
    this.processor = processor;
    this.magicTargets = magicTargets;
    this.actionDetailParams = actionDetailParams;
  }
  
  protected Logger logger = LoggerFunction.FIGHT.getLogger();
  
  private Fight fight;
  
  private FightObject effector;
  
  private RoleSkill skill;
  
  private List<Integer> skillTargets;
  
  private List<Integer> sideTargets;
  
  private SkillProcessor processor;
  
  private List<MagicAttackParam> magicTargets;
  
  public Logger getLogger() {
    return this.logger;
  }
  
  public Fight getFight() {
    return this.fight;
  }
  
  public FightObject getEffector() {
    return this.effector;
  }
  
  public RoleSkill getSkill() {
    return this.skill;
  }
  
  public List<Integer> getSkillTargets() {
    return this.skillTargets;
  }
  
  public List<Integer> getSideTargets() {
    return this.sideTargets;
  }
  
  public SkillProcessor getProcessor() {
    return this.processor;
  }
  
  public List<MagicAttackParam> getMagicTargets() {
    return this.magicTargets;
  }
  
  private List<ActionDetailParam> actionDetailParams = new ArrayList<>();
  
  public List<ActionDetailParam> getActionDetailParams() {
    return this.actionDetailParams;
  }
  
  public SkillAction(Fight fight, FightObject effector, RoleSkill roleSkill, SkillProcessor processor) {
    this.effector = effector;
    this.fight = fight;
    this.skill = roleSkill;
    this.processor = processor;
  }
  
  public List<Integer> pickUpTargets(Integer targetNum) {
    if (this.skill != null && 39006 == this.skill.getSkillId().intValue()) {
      List<FightObject> targetSide = this.fight.getTargetSide(this.effector.getId().intValue());
      if (targetSide.size() > 0) {
        this.skillTargets = (List<Integer>)targetSide.stream().filter(obj -> !obj.isFallDown()).map(FightObject::getId).collect(Collectors.toList());
        return this.skillTargets;
      } 
    } 
    if (this.skill.getType().intValue() == SKILLATTR.SKILLATTR_XIXUE.getValue())
      this.sideTargets = SkillTargetPicker.getAssistTarget(this.fight, this.effector, targetNum.intValue()); 
    if (this.effector.getStatusValue(1023) > 0 && this.skill.getType().intValue() == SKILLATTR.SKILLATTR_HENGSAO.getValue()) {
      this.skillTargets = SkillTargetPicker.getRowAttackTarget(this.fight, this.effector, targetNum.intValue());
    } else {
      this.skillTargets = ((FightNewService)SpringUtils.getBean(FightNewService.class)).getTargetListNew(this.effector, targetNum.intValue(), this.fight);
    } 
    return this.skillTargets;
  }
  
  public void doMagicEffect() {
    FightMessageNewService fightMessageService = (FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class);
    boolean hasEnemy = false;
    for (Integer targetId : this.skillTargets) {
      FightObject target = this.fight.queryFightObject(targetId);
      if (!this.fight.isSameSide(this.effector, target)) {
        hasEnemy = true;
        break;
      } 
    } 
    int hitTimes = 1;
    PassiveSkillUtils.beforeMagic(this.fight, this.effector, this.skill, this.skillTargets);
    if (this.effector.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()), 0) > 0)
      hitTimes += this.effector.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist())); 
    List<MagicAttackParam> targets = new ArrayList<>();
    MagicAttackParam magicAttackParam = null;
    for (int i = 0; i < hitTimes; i++) {
      ActionDetailParam actionDetailParam = null;
      for (int j = 0; j < this.skillTargets.size(); j++) {
        Integer targetId = this.skillTargets.get(j);
        FightObject target = this.fight.queryFightObject(targetId);
        if (i > 0) {
          if (targets.size() > 0)
            for (MagicAttackParam magicAttackParam1 : targets) {
              if (magicAttackParam1.getTarget().getId().equals(target.getId())) {
                magicAttackParam = magicAttackParam1;
                break;
              } 
            }  
          if (magicAttackParam == null) {
            magicAttackParam = new MagicAttackParam();
            targets.add(magicAttackParam);
          } 
        } else {
          magicAttackParam = new MagicAttackParam();
          targets.add(magicAttackParam);
        } 
        magicAttackParam.setTarget(target);
        List<Integer> positiveStatus = new ArrayList<>();
        for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)target.getStatusMap().entrySet()) {
          String key = entry.getKey();
          Object value = entry.getValue();
          if (!key.matches("^[0-9]+$"))
            continue; 
          if (value == null || ((Integer)value).intValue() <= 0)
            continue; 
          if (1022 == Integer.parseInt(key) || 1009 == Integer.parseInt(key) || 1008 == 
            Integer.parseInt(key) || 1010 == Integer.parseInt(key))
            positiveStatus.add(Integer.valueOf(Integer.parseInt(key))); 
        } 
        magicAttackParam.setPositiveStatus(positiveStatus);
        if (target != null) {
          PassiveSkillUtils.beforeMagicOne(this.fight, this.effector, target, this.skill);
          boolean stunt = FightRateUtil.calcMagicStuntRate(this.effector, this.skill);
          List<Integer> woodAssistSkills = Arrays.asList(new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_XIXUE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ZHIYU.getValue()) });
          if (!target.isFallDown() || woodAssistSkills.contains(this.skill.getType()) || LOGIC.LOGICTYPE_PET.getValue() == this.effector.getLogic()) {
            Effect effect;
            int sideId = 0;
            if (this.sideTargets != null && this.sideTargets.size() > j)
              sideId = ((Integer)this.sideTargets.get(j)).intValue(); 
            BattleUnit battleUnit = new BattleUnit(this.fight, this.effector, target, this.skill);
            PassiveSkillUtils.beforeSkillEffect(this.fight, this.effector, target, this.skill);
            PassiveSkillUtils.beforeCountHurt(this.fight, this.effector, target);
            if (84003 != this.skill.getSkillId().intValue() && 83024 != this.skill.getSkillId().intValue()) {
              effect = this.processor.effect(this.fight, this.effector, target, new Object[] { Boolean.valueOf(stunt) });
            } else {
              effect = this.processor.effect(this.fight, this.effector, target, new Object[] { Boolean.valueOf(stunt), this.actionDetailParams });
            } 
            if (effect == null || effect.getHurt() <= 0 || (target != null && !target.isFallDown())) {
              if (effect == null) {
                if (this.effector.isFallDown() && !this.fight.getDeadPetIds().contains(this.effector.getId())) {
                  FightObject targetObject = this.fight.queryFightObject(Integer.valueOf(this.effector.getTargetId()));
                  this.fight.onActorDie(this.effector);
                } 
                break;
              } 
              if (this.effector.isASide() != target.isASide())
                PassiveSkillUtils.excutePetPKPetPassiveSkill(this.fight, this.effector, target, effect); 
              if (effect.getHurt() > 0 && stunt)
                effect.setHurt(Math.max(1, (int)(effect.getHurt() * 1.5D))); 
              if (i > 0) {
                double percent = this.effector.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()), 0.0D);
                if (percent > 0.0D)
                  effect.setHurt((int)(effect.getHurt() * percent)); 
              } 
              battleUnit.setStunt(stunt);
              battleUnit.setSideId(sideId);
              battleUnit.setDoubleHit(hitTimes - 1);
              battleUnit.setMagicDoubleHitI(i + 1);
              battleUnit.setEffect(effect);
              battleUnit.triggerMagic();
              ActionDetailParam actionDetailParamReturn = battleUnit.getActionDetailParam();
              if (actionDetailParam == null) {
                RespFightShouting fightShouting = fightMessageService.fightShouting(this.effector, "法攻");
                FightObjectParam attackerParam = actionDetailParamReturn.gainAttackerParam();
                ActionParamUtils.updateObjectInfo(this.effector, 7, this.skill.getSkillId().intValue(), 2, fightShouting, attackerParam);
                if (this.effector.isPlayer() && FightOtherUtil.isPlayerSkill(this.skill.getSkillId().intValue()) == LOGIC_SKILL.LOGIC_TYPE_SKILL.getValue()) {
                  SysDataAcsrySuit acsrySuit = AcsrySkillUtil.getAcsrySuit(this.effector.getRole());
                  if (acsrySuit != null) {
                    Map<Integer, SysDataAcsrySuitColor> acsrySuitColorMap = (Map<Integer, SysDataAcsrySuitColor>)EquipDataPool.acsrySuitColorMap.get(acsrySuit.getDist());
                    if (acsrySuitColorMap != null) {
                      SysDataAcsrySuitColor acsrySuitColor = acsrySuitColorMap.get(this.skill.getSkillId());
                      if (acsrySuitColor != null)
                        actionDetailParamReturn.setColorId(acsrySuitColor.getColorId()); 
                    } 
                  } 
                } 
                actionDetailParam = actionDetailParamReturn;
              } else if (actionDetailParamReturn.getTargetParamList().size() > 0) {
                actionDetailParam.getTargetParamList().addAll(actionDetailParamReturn.getTargetParamList());
              } 
              if (magicAttackParam.getHurt() == 0)
                magicAttackParam.setHurt(effect.getHurt()); 
              if (battleUnit.isDodge()) {
                magicAttackParam.setDodge(false);
              } else {
                magicAttackParam.setDodge(true);
              } 
              magicAttackParam.setClearStatusList(battleUnit.getClearStatusList());
              magicAttackParam.setTargetReduceMp(battleUnit.getTargetReduceMp());
              magicAttackParam.setSideTarget(battleUnit.getSideTarget());
              ImbueUtil.clearTemps(this.effector.getImbueMap());
              if (j == this.skillTargets.size() - 1);
              if ("add_to_target".equals(effect.getOtherType()))
                this.processor.effect(this.fight, this.effector, target, new Object[] { Boolean.valueOf(stunt), null, Integer.valueOf(1) }); 
            } 
          } 
        } 
      } 
      if (actionDetailParam != null)
        this.actionDetailParams.add(actionDetailParam); 
    } 
    this.magicTargets = targets;
  }
  
  public void dragonMorph() {
    this.effector.setMorph(0);
    this.processor.effect(this.fight, this.effector, null, new Object[0]);
  }
  
  public ActionDetailParam fetchActionDetail() {
    ActionDetailParam actionDetailParam = null;
    if (this.actionDetailParams.size() <= 0) {
      actionDetailParam = new ActionDetailParam();
      this.actionDetailParams.add(actionDetailParam);
    } else {
      actionDetailParam = this.actionDetailParams.get(this.actionDetailParams.size() - 1);
    } 
    return actionDetailParam;
  }
  
  public SkillAction() {}
}
