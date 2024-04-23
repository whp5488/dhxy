package com.kitty.system.game.fightnew.model;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.ATTRCAP;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.LOGIC_SKILL;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.hero.utils.HeroSkillUtil;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;

public class BattleUnit {
  private final Logger logger = LoggerFunction.FIGHT.getLogger();
  
  private FightObject effector;
  
  private FightObject target;
  
  private RoleSkill skill;
  
  private int skillType;
  
  private Fight fight;
  
  private boolean isBackAttack;
  
  private boolean isFury;
  
  public FightObject getTarget() {
    return this.target;
  }
  
  public RoleSkill getSkill() {
    return this.skill;
  }
  
  private double furyHPCount = 2.2D;
  
  private boolean isDodge = true;
  
  private Effect effect;
  
  private int sideId;
  
  public boolean isDodge() {
    return this.isDodge;
  }
  
  public Effect getEffect() {
    return this.effect;
  }
  
  private int doubleHit = 1;
  
  public int magicDoubleHitI = 1;
  
  private List<Integer> hitList;
  
  private List<Integer> clearStatusList;
  
  private int phyHurt;
  
  private int targetReduceMp;
  
  private FightObject sideTarget;
  
  public void setMagicDoubleHitI(int magicDoubleHitI) {
    this.magicDoubleHitI = magicDoubleHitI;
  }
  
  public List<Integer> getClearStatusList() {
    return this.clearStatusList;
  }
  
  public int getPhyHurt() {
    return this.phyHurt;
  }
  
  public int getTargetReduceMp() {
    return this.targetReduceMp;
  }
  
  public FightObject getSideTarget() {
    return this.sideTarget;
  }
  
  private ActionDetailParam actionDetailParam = new ActionDetailParam();
  
  public void setActionDetailParam(ActionDetailParam actionDetailParam) {
    this.actionDetailParam = actionDetailParam;
  }
  
  public ActionDetailParam getActionDetailParam() {
    return this.actionDetailParam;
  }
  
  private ActionDetailParam actionDetailParamBac = null;
  
  public void setActionDetailParamBac(ActionDetailParam actionDetailParamBac) {
    this.actionDetailParamBac = actionDetailParamBac;
  }
  
  public ActionDetailParam getActionDetailParamBac() {
    return this.actionDetailParamBac;
  }
  
  public void triggerPhysic() {
    createActionDetailParam(this.effector, this.target, 0, 0, false);
    countDoubleHit();
    afterUsePhysic();
  }
  
  private void afterUsePhysic() {
    FightMessageNewService fightMessageNewService = (FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class);
    RespFightShouting fightShouting = fightMessageNewService.fightShouting(this.effector, "物攻");
    FightObjectParam attackerParam = this.actionDetailParam.gainAttackerParam();
    ActionParamUtils.updateObjectInfo(this.effector, 0, 0, 2, fightShouting, attackerParam);
    if (this.effector.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
      petDoPhyHit();
    } else {
      doPhyHit();
    } 
  }
  
  public void doPhyHit() {
    if (this.effector.isASide() == this.target.isASide() && !this.effector.isConfusion()) {
      doDamageToTargetHp(this.target, 1, null, false);
    } else {
      this.doubleHit = Math.max(this.doubleHit, 1);
      boolean isProtect = true;
      int tempHurt = 0;
      double rdk = 0.0D;
      for (int i = 0; i < this.doubleHit; i++) {
        List<Integer> list = new ArrayList<>();
        Effect effect = CountUtils.countHurt(this.fight, this.effector, this.target, rdk, 1.0D);
        int hurt = effect.getHurt();
        rdk = effect.getHit();
        if (this.target.isFrozen() || this.target.RoleIsDamgeImmunity().booleanValue())
          hurt = 0; 
        tempHurt = hurt;
        if (i <= 0 && !this.target.isSleep())
          this.isDodge = SkillAi.checkMiss(this.effector, this.target); 
        if (this.isDodge) {
          double fury = this.effector.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
          if (fury > 0.0D)
            this.isFury = SkillAi.checkFury(this.effector); 
          Map<String, Object> ohterMap = handleHurtAndOther(effect, hurt, list, i);
          list = (List<Integer>)ohterMap.get("list");
          int skillId = Integer.valueOf(String.valueOf(ohterMap.get("skillId"))).intValue();
          if (this.skill != null && this.skill.getSkillId().intValue() == 80023)
            skillId = this.skill.getSkillId().intValue(); 
          int targetAction = Integer.valueOf(String.valueOf(ohterMap.get("targetAction"))).intValue();
          hurt = Integer.valueOf(String.valueOf(ohterMap.get("hurt"))).intValue();
          if (i > 0 && !this.target.isFrozen())
            hurt = Math.max((int)(hurt * Math.pow(0.6D, i)), 1); 
          this.isFury = false;
          if (this.target.getStatusMap().getInt(String.valueOf(2070)) > 0)
            hurt = (int)(hurt * (1.0D + this.target.getStatusMap().getDouble("2070value"))); 
          this.phyHurt = hurt;
          hurt = isGuard(hurt, isProtect, i);
          if (hurt > 0) {
            boolean isDoubleHit = false;
            if (i > 0)
              isDoubleHit = true; 
            doDamageToTargetHp(this.target, hurt, list, isDoubleHit);
          } else if (this.target.isFrozen()) {
            changeLifeParamTarget(this.fight, this.target, hurt, 2, false);
          } 
          if (i == this.doubleHit - 1)
            PassiveSkillUtils.phySicalLastHit(this.fight, this.effector, this.target, this.skill, hurt, this.actionDetailParam); 
          if (this.target.isFallDown() || this.effector.isFallDown())
            break; 
        } else {
          dodgeReturnParam(this.target.getId().intValue(), 0);
          break;
        } 
      } 
      if (this.isDodge)
        doBackHurt(Boolean.TRUE.booleanValue(), tempHurt); 
    } 
  }
  
  public void triggerMagic() {
    createActionDetailParam(this.effector, this.target, 7, this.skill.getSkillId().intValue(), false);
    this.isDodge = SkillCompute.computeSkillHit(this.fight, this.effector, this.target, this.skill);
    if (this.isDodge && 
      !this.effect.isTargetHit())
      this.isDodge = this.effect.isTargetHit(); 
    List<Integer> list = new ArrayList<>();
    if (this.isFury)
      list.add(Integer.valueOf(100)); 
    boolean penetrate = false;
    if (this.effect.getList() != null && this.effect.getList().size() > 0) {
      Object args = elsePro(this.effect.getList(), "penetrate");
      penetrate = (args != null && Boolean.parseBoolean(args.toString()));
    } 
    if (penetrate)
      list.add(Integer.valueOf(101)); 
    if (this.effect == null || this.effect.getType() <= 0 || this.isDodge) {
      dodgeReturnParam(this.target.getId().intValue(), 0);
    } else {
      int tianFuAddHurt = PassiveSkillUtils.addMagicEffect(this.fight, this.effector, this.target, this.skill, this.effect.getHurt(), this.effect, this.magicDoubleHitI);
      if (tianFuAddHurt > 0)
        this.effect.setHurt(this.effect.getHurt() + tianFuAddHurt); 
      this.hitList = list;
      afterUseMagic(this.effect);
    } 
  }
  
  private void afterUseMagic(Effect effect) {
    List<Integer> list = new ArrayList<>();
    boolean isWuxin = false;
    int status = -1;
    int unitSkill = FightOtherUtil.isPlayerSkill(this.skill.getSkillId().intValue());
    if (effect.getTime() > 0) {
      NutMap targetStatus = this.target.getStatusMap();
      FightParamPool.addControlStatus(this.fight, this.target, effect.getType());
      targetStatus.setv(String.valueOf(effect.getType()), Integer.valueOf(effect.getTime()));
      this.target.getStatusSubMap().setv(effect.getType() + "target", this.effector.getId());
      if (this.effector.isPet() && StatusConst.getWuxingStatus().contains(Integer.valueOf(effect.getType()))) {
        isWuxin = true;
        status = effect.getType();
      } 
      list.add(Integer.valueOf(effect.getType()));
      list.add(Integer.valueOf(effect.getTime()));
    } 
    if (LOGIC.LOGICTYPE_PET.getValue() != this.effector.getLogic() && ArrayUtils.contains((Object[])SkillAi.obstacle, Integer.valueOf(this.skillType))) {
      sendStatus(list);
      if (this.skillType == SKILLATTR.SKILLATTR_POISON.getValue()) {
        doMagicHit();
      } else {
        clearStatus();
      } 
    } else if (unitSkill == LOGIC_SKILL.LOGIC_TYPE_SKILL.getValue() && (this.skillType == SKILLATTR.SKILLATTR_ZHIYU.getValue() || this.skillType == SKILLATTR.SKILLATTR_PAN.getValue() || this.skillType == SKILLATTR.SKILLATTR_ATTACK.getValue() || this.skillType == SKILLATTR.SKILLATTR_SPEED.getValue() || this.skillType == SKILLATTR.SKILLATTR_SHUAIRUO.getValue())) {
      sendStatus(list);
    } else if (unitSkill == LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue() && (this.skillType == SKILLATTR.SKILLATTR_ZHIYU.getValue() || this.skillType == SKILLATTR.SKILLATTR_PAN.getValue() || this.skillType == SKILLATTR.SKILLATTR_ATTACK.getValue() || this.skillType == SKILLATTR.SKILLATTR_SPEED.getValue() || this.skillType == SKILLATTR.SKILLATTR_SHUAIRUO.getValue())) {
      sendStatus(list);
      doMagicHit();
    } else if (this.skillType == SKILLATTR.SKILLATTR_ZHENJI.getValue() || this.skillType == SKILLATTR.SKILLATTR_POJIA.getValue()) {
      sendStatus(list);
      doMagicHit();
    } else if (LOGIC.LOGICTYPE_PET.getValue() == this.effector.getLogic() && list.size() > 0) {
      sendStatus(list);
      if (isWuxin)
        clearWuxin(effect); 
    } else {
      doMagicHit();
    } 
    doBackHurt(Boolean.FALSE.booleanValue(), effect.getHurt());
  }
  
  private void doMagicHit() {
    if (this.effect != null) {
      int type = 100000;
      if (this.effect.getList() != null && this.effect.getList().size() > 0)
        doMagicHitSpecial(this.effect.getList()); 
      if (this.effect.getHurt() > 0) {
        this.effect.setHurt(excuteGuard(this.effect.getHurt(), 100000));
        if (this.target.getStatusMap().getInt(String.valueOf(2070)) > 0)
          this.effect.setHurt((int)(this.effect.getHurt() * (1.0D + this.target.getStatusMap().getDouble("2070value")))); 
        int guardHurt = excuteTianFuDrug(this.target, this.effect.getHurt(), (this.skillType == SKILLATTR.SKILLATTR_POISON.getValue()) ? 1005 : type);
        this.effect.setHurt(guardHurt);
        doDamageToTargetHp(this.target, this.effect.getHurt(), this.hitList, false);
        if (this.target.isFallDown()) {
          this.effector.addFallDownList(this.target.getId().intValue());
          this.fight.onActorDie(this.target);
        } 
        if (this.effector.isFallDown())
          this.fight.onActorDie(this.effector); 
      } 
    } 
  }
  
  public void doMagicHitSpecial(List<FiedValue> list) {
    Object mana = elsePro(list, "mana");
    if (mana != null) {
      int mp = Integer.valueOf(String.valueOf(mana)).intValue();
      doDamageToTargetMp(this.target, mp, 100000);
    } 
    changeList(list, this.target);
  }
  
  public void recoverLife(int recover, FightObject target) {
    int need = target.getMaxLife() - target.getCurrLife();
    if (recover > need)
      recover = need; 
    int changeLifeSS = PassiveSkillUtils.countRecoverBloodEndSS(this.fight, this.effector, target, this.skill, recover);
    recover += changeLifeSS;
    int changeLife = PassiveSkillUtils.countRecoverBloodEnd(this.fight, target, recover, this.skill);
    recover += changeLife;
    FightObjectParam targetParam = changeLifeParamTarget(target, recover, 2);
    targetParam.addDamageType(119);
  }
  
  private void doBackHurt(boolean phyAttack, int hurt) {
    if (hurt <= 0)
      return; 
    if (this.target.isFallDown() || this.effector.isFallDown())
      return; 
    if (this.skill != null && SKILLATTR.SKILLATTR_ZHEN.getValue() == this.skill.getType().intValue())
      return; 
    double ftOdds = this.target.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist()));
    boolean flag = SkillAi.canAffectOnPro(ftOdds);
    if (flag) {
      double ftPro = this.target.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist()));
      double ftPro_k = this.effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.WL_XS.getDist())) + this.effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.SH_DK.getDist()));
      ftPro -= ftPro_k;
      if (ftPro < ATTRCAP.MIN_FTLV_NUM.getValue()) {
        ftPro = ATTRCAP.MIN_FTLV_NUM.getValue();
      } else if (ftPro > ATTRCAP.MAX_FTLV_NUM.getValue()) {
        ftPro = ATTRCAP.MAX_FTLV_NUM.getValue();
      } 
      int backHurt = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(Math.max(ftPro * hurt, 1.0D)));
      if (backHurt >= this.effector.getCurrLife())
        backHurt = this.effector.getCurrLife(); 
      FightObjectParam attackerParam = this.actionDetailParam.gainTargetParam(this.effector.getId().intValue(), true);
      PassiveSkillUtils.excuteChangeLifeAuto(this.fight, this.effector, this.actionDetailParam, attackerParam, -backHurt);
    } 
  }
  
  public void doDamageToTargetHp(FightObject target, int hurt, List<Integer> list, boolean isDoubleHit) {
    hurt += PassiveSkillUtils.changeHurt(this.fight, this.effector, target, hurt, this.isFury, this.skill, this.clearStatusList);
    if ((FIGHT_TYPE.NIAN.getValue() == this.fight.getType() && target.getSerial() == 1660031) || (FIGHT_TYPE.BURNING_HOT.getValue() == this.fight.getType() && target.getSerial() == 1660032))
      hurt = 1; 
    int life = target.getCurrLife() - hurt;
    if (life < 0)
      hurt = target.getCurrLife(); 
    clearStatus();
    changeLifeParamTarget(this.fight, target, -hurt, 1, isDoubleHit);
    if (target.isFallDown() && LOGIC.LOGICTYPE_PET.getValue() == target.getLogic()) {
      List<RoleSkill> skillIds = target.getSkillIds();
      List<SysPetSkill> sysPetSkills = PetTriggerPassiveSkill.beforePetExit(skillIds, this.fight.isPvp());
      if (sysPetSkills.size() > 0)
        for (SysPetSkill petSkill : sysPetSkills) {
          SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
          NutMap passive = processor.passive(this.fight, target, new Object[] { this.effector });
          if (passive != null && passive.getBoolean("successful")) {
            getTargetParam(target.getId().intValue()).setCurrentLife(target.getCurrLife());
            if (passive.getInt("specialStatus", 0) > 0)
              getTargetParam(target.getId().intValue()).setSpecialStatus(passive.getInt("specialStatus", 0)); 
            if (passive.getList("clearStatus", Integer.class) != null && passive.getList("clearStatus", Integer.class).size() > 0) {
              List<Integer> clearStatus = passive.getList("clearStatus", Integer.class);
              getTargetParam(target.getId().intValue()).setClearStatus(clearStatus);
            } 
          } 
        }  
    } 
    FightObjectParam targetParam = getTargetParam(target.getId().intValue());
    if (list != null && list.size() > 0)
      targetParam.setDamageType(list); 
    leechGain(hurt);
    RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(target, "挨打");
    targetParam.setFightShouting(fightShouting);
  }
  
  public void doDamageToTargetMp(FightObject target, int mp, int type) {
    if (mp > 0) {
      if (this.isFury)
        mp = (int)(mp * 2.5D); 
      int currMana = target.getCurrMana() - mp;
      if (currMana < 0) {
        mp = target.getCurrMana();
        currMana = 0;
      } 
      if (mp > 0) {
        mp += PassiveSkillUtils.beforeReduceHurtMp(this.fight, this.effector, target, this.skill, mp);
        this.targetReduceMp = mp;
        changeManaParamTarget(this.fight, target, -mp, 2);
      } 
    } 
  }
  
  public void leechGain(int damage) {
    if (this.skillType == SKILLATTR.SKILLATTR_XIXUE.getValue() && this.sideId > 0) {
      FightObject sideTarget = this.fight.queryFightObject(Integer.valueOf(this.sideId));
      String serial = String.valueOf(this.skill.getSkillId());
      if (sideTarget == null) {
        sideTarget = this.effector;
        this.effector.getStatusSubMap().setv(serial, Integer.valueOf(1));
      } 
      if (sideTarget.getStatusSubMap().getInt(serial) <= 0) {
        double extra = this.effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SS.getDist());
        int recover = SkillCompute.computeSkillLeech(this.skill.getSkillId().intValue(), damage, extra).intValue();
        recover = excuteRecover(recover, this.effector);
        recoverLife(recover, sideTarget);
        this.sideTarget = sideTarget;
      } 
    } 
  }
  
  public void clearStatus() {
    List<Integer> list = new ArrayList<>();
    if (this.skillType == SKILLATTR.SKILLATTR_ICE.getValue()) {
      if (this.target.isConfusion()) {
        list.add(Integer.valueOf(1002));
        this.target.setStatus(1002, 0);
      } 
      if (this.target.isForgotten()) {
        list.add(Integer.valueOf(1006));
        this.target.setStatus(1006, 0);
      } 
    } else if (this.skillType == SKILLATTR.SKILLATTR_SLEEP.getValue()) {
      if (this.target.isConfusion()) {
        list.add(Integer.valueOf(1002));
        this.target.setStatus(1002, 0);
      } 
      if (this.target.isForgotten()) {
        list.add(Integer.valueOf(1006));
        this.target.setStatus(1006, 0);
      } 
    } else if (this.skillType == SKILLATTR.SKILLATTR_YIWANG.getValue()) {
      if (this.target.isConfusion()) {
        list.add(Integer.valueOf(1002));
        this.target.setStatus(1002, 0);
      } 
    } else if (this.skillType == SKILLATTR.SKILLATTR_CONFUSE.getValue() && 
      this.target.isForgotten()) {
      list.add(Integer.valueOf(1006));
      this.target.setStatus(1006, 0);
    } 
    if (this.skillType != SKILLATTR.SKILLATTR_SLEEP.getValue())
      if (this.target.isSleep()) {
        String perpetrator = this.target.getStatusMap().getString("1004perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(71343))) {
          this.target.getStatusMap().unset("1004perpetrator");
        } else {
          list.add(Integer.valueOf(1004));
          this.target.setStatus(1004, 0);
        } 
      }  
    list = clearRendArmor(list);
    this.clearStatusList = list;
    if (list.size() > 0) {
      int skillId = 0;
      if (this.skill != null && this.skill.getSkillId().intValue() > 0)
        skillId = this.skill.getSkillId().intValue(); 
      clearStatusParamTarget(this.target, list, 2);
    } 
  }
  
  private void clearWuxin(Effect effect) {
    if (effect.getList() != null && effect.getList().size() > 0)
      changeTarget(effect.getList(), this.target); 
  }
  
  public List<Integer> clearRendArmor(List<Integer> list) {
    if ((this.effect == null || this.effect.getType() != 1021) && 
      this.target.getStatusMap().getInt(String.valueOf(1021)) > 0) {
      this.target.removeStatus(1021);
      HeroSkillUtil.clearPj(this.target);
      list.add(Integer.valueOf(1021));
    } 
    return list;
  }
  
  private void sendStatus(List<Integer> list) {
    if (list.size() > 0)
      statusParamTarget(this.target, ((Integer)list.get(0)).intValue(), ((Integer)list.get(1)).intValue(), 2); 
  }
  
  public Object elsePro(List<FiedValue> list, String dist) {
    Optional<FiedValue> optional = list.stream().filter(fied -> fied.getType().equals(dist)).findFirst();
    if (optional.isPresent()) {
      FiedValue fiedValue = optional.get();
      return fiedValue.getValue();
    } 
    return null;
  }
  
  public void setStunt(boolean stunt) {
    this.isFury = stunt;
  }
  
  public void setDoubleHit(int doubleHit) {
    this.doubleHit = doubleHit;
  }
  
  public void setEffect(Effect effect) {
    this.effect = effect;
  }
  
  public void setSideId(int sideId) {
    this.sideId = sideId;
  }
  
  public BattleUnit(Fight fight, FightObject effector, FightObject target) {
    this(fight, effector, target, null);
  }
  
  public BattleUnit(Fight fight, FightObject effector, FightObject target, RoleSkill skill) {
    this.effector = effector;
    this.target = target;
    this.fight = fight;
    this.skill = skill;
    if (skill != null && skill.getType() != null)
      this.skillType = skill.getType().intValue(); 
  }
  
  private int excuteRecover(int recover, FightObject effector) {
    double percent = 0.0D;
    int reduce = 0;
    if (percent > 0.0D)
      reduce = (int)(recover * percent); 
    int add = 0;
    return recover - reduce + add;
  }
  
  private int excuteGuard(int hurt, int type) {
    int returnHurt = 0;
    int statusType = 0;
    if (this.target.getStatusMap().getInt(String.valueOf(2010)) > 0) {
      statusType = 2010;
    } else if (this.target.getStatusMap().getInt(String.valueOf(8)) > 0) {
      statusType = 8;
    } else {
      return hurt;
    } 
    returnHurt = guard(hurt, type, statusType);
    if (returnHurt == 0)
      return hurt; 
    return returnHurt;
  }
  
  private int guard(int hurt, int type, int statusType) {
    int returnHurt = 0;
    if (this.target.getStatusValue(1011) >= 1) {
      this.target.getStatusMap().put("1011value", Integer.valueOf(0));
      this.target.setStatus(1011, 0);
      List<Integer> statusList = new ArrayList<>();
      statusList.add(Integer.valueOf(1011));
      FightObjectParam targetParam = getTargetParam(this.target.getId().intValue());
      targetParam.setClearStatus(statusList);
    } 
    FightObject guardObj = this.fight.queryFightObject(Integer.valueOf(this.target.getStatusMap().getInt(statusType + "value")));
    if (guardObj != null && !this.target.getId().equals(guardObj.getId())) {
      int guardHurt = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(hurt * this.target.getStatusMap().getDouble(statusType + "percent") * this.target
            .getStatusMap().getDouble(statusType + "reducePercent")));
      guardHurt = Math.max(1, guardHurt);
      int skillId = 0;
      if (this.skill != null && this.skill.getSkillId().intValue() > 0)
        skillId = this.skill.getSkillId().intValue(); 
      doDamageToTargetHp(guardObj, guardHurt, null, false);
      getTargetParam(guardObj.getId().intValue()).setGuardTarget(this.target.getId().intValue());
      guardObj.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.THE_ROUND_DRUG.getDist()), this.fight.getRound() + "_true");
      returnHurt = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(hurt * (1.0D - this.target.getStatusMap().getDouble(statusType + "percent"))));
      if (returnHurt < 1)
        returnHurt = 1; 
    } else {
      returnHurt = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf(hurt));
    } 
    return returnHurt;
  }
  
  public void petDoPhyHit() {
    if (this.effector.isASide() == this.target.isASide() && !this.effector.isConfusion()) {
      doDamageToTargetHp(this.target, 1, null, false);
    } else {
      this.doubleHit = Math.max(this.doubleHit, 1);
      if (this.doubleHit > 1)
        this.doubleHit = PassiveSkillUtils.doubleHitExcute(this.fight, this.effector, this.target, this.skill, this, PET_PASSIVE_SKILL_TRIGGER_DICT.NORMAL_ATTACK_DOUBLE_HIT
            .getValue(), this.doubleHit); 
      boolean isProtect = true;
      int tempHurt = 0;
      double rdk = 0.0D;
      for (int i = 0; i < this.doubleHit; i++) {
        List<Integer> list = new ArrayList<>();
        if (i == 0)
          PassiveSkillUtils.getPassiveSkill(this.fight, this.effector, this.target, this.skill, this, PET_PASSIVE_SKILL_TRIGGER_DICT.STRENGTHEN_NORMAL_ATTACK_PHY.getValue(), 0); 
        Effect effect = null;
        if (this.effect != null) {
          effect = this.effect;
        } else {
          effect = CountUtils.countHurt(this.fight, this.effector, this.target, rdk, 1.0D);
        } 
        int hurt = effect.getHurt();
        rdk = effect.getHit();
        if (this.effector.isASide() != this.target.isASide())
          PassiveSkillUtils.excutePetPKPetPassiveSkill(this.fight, this.effector, this.target, effect); 
        if (this.target.isFrozen() || this.target.RoleIsDamgeImmunity().booleanValue())
          hurt = 0; 
        tempHurt = hurt;
        if (i <= 0 && !this.target.isSleep())
          this.isDodge = SkillAi.checkMiss(this.effector, this.target); 
        if (this.isDodge) {
          double fury = this.effector.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
          if (fury > 0.0D)
            this.isFury = SkillAi.checkFury(this.effector); 
          Map<String, Object> ohterMap = handleHurtAndOther(effect, hurt, list, i);
          list = (List<Integer>)ohterMap.get("list");
          int skillId = ((Integer)ohterMap.get("skillId")).intValue();
          int targetAction = ((Integer)ohterMap.get("targetAction")).intValue();
          hurt = ((Integer)ohterMap.get("hurt")).intValue();
          if (i > 0 && !this.target.isFrozen())
            hurt = Math.max((int)(hurt * Math.pow(0.6D, i)), 1); 
          this.isFury = false;
          Effect effectPart = PassiveSkillUtils.getPassiveSkill(this.fight, this.effector, this.target, this.skill, this, PET_PASSIVE_SKILL_TRIGGER_DICT.STRENGTHEN_NORMAL_ATTACK
              .getValue(), hurt);
          if (effectPart != null && effectPart.getHurt() != 0)
            hurt = effectPart.getHurt(); 
          if (this.target.getStatusMap().getInt(String.valueOf(2070)) > 0)
            hurt = (int)(hurt * (1.0D + this.target.getStatusMap().getDouble("2070value"))); 
          if (effect.getTime() > 0) {
            NutMap targetStatus = this.target.getStatusMap();
            targetStatus.setv(String.valueOf(effect.getType()), Integer.valueOf(effect.getTime()));
            statusParamTarget(this.target, effect.getType(), effect.getTime(), 2);
          } 
          this.phyHurt = hurt;
          hurt = isGuard(hurt, isProtect, i);
          if (hurt > 0) {
            boolean isDoubleHit = false;
            if (i > 0)
              isDoubleHit = true; 
            doDamageToTargetHp(this.target, hurt, list, isDoubleHit);
          } else if (this.target.isFrozen()) {
            changeLifeParamTarget(this.fight, this.target, hurt, 2, false);
          } 
          if (effectPart != null) {
            if (effectPart.getType() > 0 && effectPart.getTime() > 0)
              statusParamTarget(this.target, effectPart.getType(), effectPart.getTime(), 2); 
            if (effectPart.getList() != null && effectPart.getList().size() > 0)
              changeList(effectPart.getList(), this.target); 
          } 
          int skillSerial = 0;
          if (this.skill != null && i == 0)
            skillSerial = this.skill.getSkillId().intValue(); 
          PassiveSkillUtils.normalAttack(this.fight, this.effector, this.target, this, this.phyHurt, skillSerial);
          if (this.target.isFallDown() || this.effector.isFallDown())
            break; 
        } else {
          dodgeReturnParam(this.target.getId().intValue(), 0);
          PetPassiveSkillUtil.writeActionDetailParam(this, (this.skill != null && i == 0) ? this.skill.getSkillId().intValue() : 0);
          break;
        } 
      } 
      if (this.isDodge)
        doBackHurt(Boolean.TRUE.booleanValue(), tempHurt); 
    } 
  }
  
  private Map<String, Object> handleHurtAndOther(Effect effect, int hurt, List<Integer> list, int times) {
    double tianFuFuryValue = this.effector.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FURY.getDist()));
    if (tianFuFuryValue > 0.0D)
      this.isFury = true; 
    int skillId = 0;
    int targetAction = 1;
    Map<String, Object> map = new HashMap<>();
    if (this.isFury)
      if (tianFuFuryValue > 0.0D) {
        hurt = (int)(hurt * tianFuFuryValue);
      } else {
        hurt = (int)(hurt * this.furyHPCount);
      }  
    if (hurt > this.target.getCurrLife())
      hurt = this.target.getCurrLife(); 
    if (this.isBackAttack) {
      this.isBackAttack = false;
      this.doubleHit = 0;
    } 
    if (this.target.isFrozen())
      targetAction = 2; 
    if (this.isFury)
      list.add(Integer.valueOf(100)); 
    if (effect.getType() == 101)
      list.add(Integer.valueOf(101)); 
    if (this.doubleHit > 1)
      list.add(Integer.valueOf(102)); 
    if (this.skill != null && times == 0)
      skillId = this.skill.getSkillId().intValue(); 
    if (hurt > 0) {
      if (this.target.getStatusMap().getInt(String.valueOf(2010)) > 0)
        list.add(Integer.valueOf(106)); 
      hurt = excuteGuard(hurt, 4098);
    } 
    map.put("hurt", Integer.valueOf(hurt));
    map.put("skillId", Integer.valueOf(skillId));
    map.put("list", list);
    map.put("targetAction", Integer.valueOf(targetAction));
    return map;
  }
  
  private int isGuard(int hurt, boolean isProtect, int index) {
    if (isProtect) {
      List<FightObject> victim = new ArrayList<>();
      if (this.target.isASide()) {
        victim.addAll(this.fight.getListA());
      } else {
        victim.addAll(this.fight.getListB());
      } 
      NutMap map = AwakenSkillUtils.isGuard(this.fight, this.target);
      if (map != null) {
        double awakenPercent = map.getDouble("percent");
        if (awakenPercent > 0.0D) {
          int petFightObjectId = map.getInt("petFightObjectId");
          FightObject fightObject = this.fight.queryFightObject(Integer.valueOf(petFightObjectId));
          if (fightObject != null && !fightObject.isFallDown()) {
            hurt = excuteDrug(fightObject, hurt, 1, awakenPercent);
            if (hurt <= 0)
              return hurt; 
          } 
        } 
      } 
      for (FightObject object : victim) {
        if (!object.getId().equals(this.target.getId()))
          if (object.getType() == 8 && object.getTargetId() == this.target.getId().intValue()) {
            int completed = object.getSkillCoolMap().getInt(String.valueOf(8));
            if (completed <= 0) {
              object.getSkillCoolMap().put(String.valueOf(8), Integer.valueOf(1));
              double percent = object.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_PERCENT.getDist()), 0.0D);
              hurt = excuteDrug(object, hurt, index, percent);
            } 
          }  
      } 
    } 
    return hurt;
  }
  
  private int excuteDrug(FightObject object, int hurt, int index, double percent) {
    if (object.getStatusValue(1011) >= 1) {
      object.getStatusMap().put("1011value", Integer.valueOf(0));
      object.getStatusMap().setv(String.valueOf(1011), Integer.valueOf(0));
      List<Integer> clearStatusList = new ArrayList<>();
      clearStatusList.add(Integer.valueOf(1011));
      clearStatusParamTarget(object, clearStatusList, 2);
    } 
    boolean isDoubleHit = false;
    if (index > 0)
      isDoubleHit = true; 
    if (percent > 0.0D && percent < 1.0D) {
      String roundDrug = this.target.getBeAttackedMap().getString(String.valueOf(OTHER_SKILL_DICT_TYPE.THE_ROUND_DRUG.getDist()));
      if (roundDrug != null) {
        String[] strs = roundDrug.split("_");
        if (strs.length == 2 && strs[0].equals(String.valueOf(this.fight.getRound())) && "true".equals(strs[1]))
          return hurt; 
      } 
      double drugHurt = hurt * percent;
      hurt = (int)(hurt - drugHurt);
      double hurtPercent = object.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_CHANGE_PERCENT.getDist()), 0.0D);
      if (hurtPercent > 0.0D)
        drugHurt -= drugHurt * hurtPercent; 
      doDamageToTargetHp(object, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(drugHurt)), null, isDoubleHit);
      getTargetParam(object.getId().intValue()).setGuardTarget(this.target.getId().intValue());
      return hurt;
    } 
    doDamageToTargetHp(object, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf(hurt)), null, isDoubleHit);
    getTargetParam(object.getId().intValue()).setGuardTarget(this.target.getId().intValue());
    return 0;
  }
  
  private int excuteTianFuDrug(FightObject target, int hurt, int type) {
    String roundDrug = target.getBeAttackedMap().getString(String.valueOf(OTHER_SKILL_DICT_TYPE.THE_ROUND_DRUG.getDist()));
    if (roundDrug != null) {
      String[] strs = roundDrug.split("_");
      if (strs.length == 2 && strs[0].equals(String.valueOf(this.fight.getRound())) && "true".equals(strs[1]))
        return hurt; 
    } 
    List<FightObject> victim = new ArrayList<>();
    if (target.isASide()) {
      victim.addAll(this.fight.getListA());
    } else {
      victim.addAll(this.fight.getListB());
    } 
    for (FightObject object : victim) {
      if (!object.getId().equals(target.getId()))
        if (object.getType() == 8 && object.getTargetId() == target.getId().intValue()) {
          int completed = object.getSkillCoolMap().getInt(String.valueOf(8));
          if (completed <= 0) {
            double percent = object.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_PERCENT.getDist()), 0.0D);
            if (percent > 0.0D) {
              if (object.getStatusValue(1011) >= 1) {
                object.getStatusMap().put("1011value", Integer.valueOf(0));
                object.getStatusMap().setv(String.valueOf(1011), Integer.valueOf(0));
                List<Integer> clearStatusList = new ArrayList<>();
                clearStatusList.add(Integer.valueOf(1011));
                clearStatusParamTarget(object, clearStatusList, 2);
              } 
              object.getSkillCoolMap().put(String.valueOf(8), Integer.valueOf(1));
              double drugHurt = hurt * percent;
              hurt = (int)(hurt - drugHurt);
              double hurtPercent = object.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_CHANGE_PERCENT.getDist()), 0.0D);
              if (hurtPercent > 0.0D)
                drugHurt -= drugHurt * hurtPercent; 
              doDamageToTargetHp(object, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(drugHurt)), null, false);
              break;
            } 
          } 
        }  
    } 
    return hurt;
  }
  
  public FightObjectParam getTargetParam(int fightObjectId) {
    FightObjectParam targetParam = getActionDetailParam().gainTargetParam(fightObjectId, true);
    return targetParam;
  }
  
  public void createActionDetailParam(FightObject effector, FightObject target, int type, int skillSerial, boolean isNew) {
    if (isNew)
      this.actionDetailParam = new ActionDetailParam(); 
    this.actionDetailParam.setType(type);
    this.actionDetailParam.setSkillSerial(skillSerial);
    this.actionDetailParam.setAttackerId(effector.getId().intValue());
    this.actionDetailParam.setTargetId(target.getId().intValue());
    this.actionDetailParam.addHfType(effector, skillSerial);
  }
  
  public void dodgeReturnParam(int targetId, int missed) {
    FightObjectParam targetParam = getTargetParam(targetId);
    targetParam.setMissed(missed);
    targetParam.setHit(false);
  }
  
  public void changeLifeParamTarget(Fight fight, FightObject target, int changeLife, int missed, boolean isDoubleHit) {
    if (!isDoubleHit) {
      changeLifeParamTarget(target, changeLife, missed);
    } else {
      FightObjectParam targetParam = getActionDetailParam().gainTargetParam(target.getId().intValue(), false);
      if (targetParam != null) {
        FightObjectParam targetParamC = new FightObjectParam();
        targetParamC.setOrder(1);
        targetParamC.setObjId(target.getId().intValue());
        PassiveSkillUtils.excuteChangeLife(fight, target, targetParamC, changeLife);
        targetParamC.setMissed(missed);
        targetParamC.setDamageType(targetParam.getDamageType());
        getActionDetailParam().getTargetParamList().add(targetParamC);
      } else {
        changeLifeParamTarget(target, changeLife, missed);
      } 
    } 
  }
  
  public FightObjectParam changeLifeParamTarget(FightObject target, int changeLife, int missed) {
    FightObjectParam targetParam = getTargetParam(target.getId().intValue());
    PassiveSkillUtils.excuteChangeLife(this.fight, target, targetParam, changeLife);
    targetParam.setMissed(missed);
    return targetParam;
  }
  
  public void changeManaParamTarget(Fight fight, FightObject target, int changeMana, int missed) {
    FightObjectParam targetParam = getTargetParam(target.getId().intValue());
    PassiveSkillUtils.excuteChangeMana(fight, target, targetParam, changeMana);
    targetParam.setMissed(missed);
  }
  
  public void clearStatusParamTarget(FightObject target, List<Integer> clearStatusList, int missed) {
    FightObjectParam targetParam = getTargetParam(target.getId().intValue());
    targetParam.setMissed(missed);
    List<Integer> clearStatus = targetParam.getClearStatus();
    if (clearStatus == null) {
      targetParam.setClearStatus(clearStatusList);
    } else {
      clearStatus.addAll(clearStatusList);
    } 
  }
  
  public void statusParamTarget(FightObject target, int status, int statusRound, int missed) {
    FightObjectParam targetParam = getTargetParam(target.getId().intValue());
    targetParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(statusRound));
    targetParam.setMissed(missed);
  }
  
  public void changeList(List<FiedValue> list, FightObject target) {
    changeTarget(list, target);
    changeAtta(list, this.effector);
  }
  
  private void changeTarget(List<FiedValue> list, FightObject target) {
    Object targetChangeHp = elsePro(list, "targetChangeHp");
    Object targetChangeMp = elsePro(list, "targetChangeMp");
    Object targetMaxHp = elsePro(list, "targetMaxHp");
    Object targetMaxMp = elsePro(list, "targetMaxMp");
    Object targetClearStatus = elsePro(list, "targetClearStatus");
    Object targetStatusMap = elsePro(list, "targetStatusMap");
    FightObjectParam targetParam = getTargetParam(target.getId().intValue());
    if (targetChangeHp != null) {
      int changeLife = targetParam.getChangeLife();
      if (changeLife != 0) {
        targetParam.setChangeLife(changeLife + Integer.parseInt(String.valueOf(targetChangeHp)));
      } else {
        targetParam.setChangeLife(Integer.parseInt(String.valueOf(targetChangeHp)));
      } 
      target.setCurrLife(target.getCurrLife() + Integer.parseInt(String.valueOf(targetChangeHp)));
      targetParam.setCurrentLife(target.getCurrLife());
    } 
    if (targetChangeMp != null) {
      int changeMana = targetParam.getChangeMana();
      if (changeMana != 0) {
        targetParam.setChangeMana(changeMana + Integer.parseInt(String.valueOf(targetChangeMp)));
      } else {
        targetParam.setChangeMana(Integer.parseInt(String.valueOf(targetChangeMp)));
      } 
      target.setCurrMana(target.getCurrMana() + Integer.parseInt(String.valueOf(targetChangeMp)));
      targetParam.setCurrentMana(target.getCurrMana());
    } 
    if (targetMaxHp != null)
      targetParam.setMaxLife(Integer.parseInt(String.valueOf(targetMaxHp))); 
    if (targetMaxMp != null)
      targetParam.setMaxMana(Integer.parseInt(String.valueOf(targetMaxMp))); 
    if (targetClearStatus != null) {
      List<Integer> statusList = (List<Integer>)targetClearStatus;
      if (statusList.size() > 0)
        targetParam.setClearStatus(statusList); 
    } 
    if (targetStatusMap != null) {
      Map<Integer, Integer> statusMap = (Map<Integer, Integer>)targetStatusMap;
      if (statusMap.size() > 0)
        for (Map.Entry<Integer, Integer> entry : statusMap.entrySet())
          targetParam.getStatusMap().put(entry.getKey(), entry.getValue());  
    } 
  }
  
  private void changeAtta(List<FiedValue> list, FightObject target) {
    Object attaClearStatus = elsePro(list, "attaClearStatus");
    Object attaStatusMap = elsePro(list, "attaStatusMap");
    FightObjectParam attaParam = this.actionDetailParam.gainTargetParam(target.getId().intValue(), true);
    if (attaClearStatus != null) {
      List<Integer> statusList = (List<Integer>)attaClearStatus;
      if (statusList.size() > 0)
        attaParam.addClearStatus(statusList); 
    } 
    if (attaStatusMap != null) {
      Map<Integer, Integer> statusMap = (Map<Integer, Integer>)attaStatusMap;
      if (statusMap.size() > 0)
        for (Map.Entry<Integer, Integer> entry : statusMap.entrySet())
          attaParam.getStatusMap().put(entry.getKey(), entry.getValue());  
    } 
  }
  
  public void countDoubleHit() {
    if (SkillAi.checkDoubleHit(this.effector)) {
      int num = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), this.effector.getPropertyMap().get(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())));
      this.doubleHit += num;
    } 
    boolean flag = SkillAi.checkPetIsSkill(this.fight, this.effector.isASide(), Integer.valueOf(81057));
    if (flag) {
      int num = PetPassiveSkillUtil.reduceDoubleHit(this.fight, this.effector);
      this.doubleHit = Math.max(1, this.doubleHit - num);
    } 
  }
}
