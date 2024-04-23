package com.kitty.system.game.fightnew.book.awaken.utils;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillConst;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.other.AWAKENSKILL_31516;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysPet;
import java.util.List;
import java.util.Map;
import org.nutz.lang.util.NutMap;

public class AwakenSkillUtils {
  public static void roundStart(Fight fight, FightObject fightObject) {
    if (fightObject != null && fightObject.isPet()) {
      PetFightObject petFightObject = (PetFightObject)fightObject;
      Role role = petFightObject.getRole();
      Pet pet = petFightObject.getPet();
      List<PetEquip> wearAcsrys = AcsryPetSkillUtil.getWearAcsrys(role, pet);
      if (wearAcsrys.size() >= 3) {
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        if (sysPet != null) {
          Integer jxSkill = sysPet.getJxSkill();
          if (jxSkill != null && AwakenSkillConst.getRoundStartSkills().contains(jxSkill)) {
            AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null)
              awakenSkillProcessor.roundStart(fight, petFightObject, new Object[0]); 
          } 
        } 
      } 
    } 
  }
  
  public static NutMap addition(Role role, Pet pet) {
    NutMap nutMap = additionSuit(role, pet);
    if (nutMap == null)
      nutMap = NutMap.NEW(); 
    NutMap nutMap1 = additionAwaken(role, pet);
    if (nutMap1 != null)
      for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)nutMap1.entrySet()) {
        if (nutMap.getDouble(entry.getKey()) != 0.0D) {
          nutMap.setv(entry.getKey(), Double.valueOf(nutMap.getDouble(entry.getKey()) + ((Double)entry.getValue()).doubleValue()));
          continue;
        } 
        nutMap.setv(entry.getKey(), entry.getValue());
      }  
    if (nutMap.size() > 0)
      return nutMap; 
    return null;
  }
  
  public static NutMap additionSuit(Role role, Pet pet) {
    List<PetEquip> wearAcsrys = AcsryPetSkillUtil.getWearAcsrys(role, pet);
    if (wearAcsrys.size() >= 3) {
      SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
      if (sysPet != null) {
        Integer jxSkill = sysPet.getJxSkill();
        if (jxSkill != null && AwakenSkillConst.getAdditionSkills().contains(jxSkill)) {
          AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
          if (awakenSkillProcessor != null) {
            NutMap nutMap = awakenSkillProcessor.additionSuit(role, pet, new Object[0]);
            return nutMap;
          } 
        } 
      } 
    } 
    return null;
  }
  
  public static NutMap additionAwaken(Role role, Pet pet) {
    List<PetEquip> wearAcsrys = AcsryPetSkillUtil.getWearAcsrys(role, pet);
    if (wearAcsrys.size() >= 3) {
      SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
      if (sysPet != null) {
        Integer jxSkill = sysPet.getJxSkill();
        if (jxSkill != null && AwakenSkillConst.getAdditionSkills().contains(jxSkill)) {
          AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
          if (awakenSkillProcessor != null && 
            pet.getAwaken() > 0) {
            NutMap nutMap = awakenSkillProcessor.additionAwaken(role, pet, new Object[0]);
            return nutMap;
          } 
        } 
      } 
    } 
    return null;
  }
  
  public static double skillIntensifyDouble(Fight fight, FightObject effector, int skillSerial, FightObject target) {
    if (effector.isPet()) {
      Integer jxSkill = getJxSkill(effector);
      if (jxSkill == null || skillSerial != jxSkill.intValue())
        return 0.0D; 
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(Integer.valueOf(skillSerial));
      if (awakenSkillProcessor != null) {
        NutMap nutMap = awakenSkillProcessor.skillIntensify(fight, (PetFightObject)effector, new Object[] { target });
        if (nutMap != null) {
          double value = nutMap.getDouble("percent");
          if (value != 0.0D)
            return value; 
        } 
      } 
    } 
    return 0.0D;
  }
  
  public static boolean skillIntensifyBoolean(Fight fight, FightObject effector, int skillSerial) {
    if (effector.isPet()) {
      Integer jxSkill = getJxSkill(effector);
      if (jxSkill == null || skillSerial != jxSkill.intValue())
        return false; 
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(Integer.valueOf(skillSerial));
      if (awakenSkillProcessor != null) {
        NutMap nutMap = awakenSkillProcessor.skillIntensify(fight, (PetFightObject)effector, new Object[0]);
        if (nutMap != null) {
          double value = nutMap.getDouble("percent");
          if (value != 0.0D)
            return SkillAi.canAffectOnPro(value); 
        } 
      } 
    } 
    return false;
  }
  
  public static void skillIntensifyTM(Fight fight, FightObject effector, int skillId, List<Integer> excludeTargets) {
    if (effector.isPet()) {
      Integer jxSkill = getJxSkill(effector);
      if (jxSkill == null || !AwakenSkillConst.getSkillIntensifyTMSkills().contains(Integer.valueOf(skillId)))
        return; 
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.skillIntensifyTM(fight, (PetFightObject)effector, new Object[] { Integer.valueOf(skillId), excludeTargets }); 
    } 
  }
  
  public static void magicAttackEnd(Fight fight, FightObject effector, RoleSkill skill, ActionDetailParam actionDetailParam, List<MagicAttackParam> targets) {
    if (skill != null) {
      Integer jxSkill = getJxSkill(effector);
      if (jxSkill != null && AwakenSkillConst.getMagicAttackEndSkills().contains(jxSkill)) {
        AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
        if (awakenSkillProcessor != null)
          awakenSkillProcessor.magicAttackEnd(fight, effector, new Object[] { skill, actionDetailParam, targets }); 
      } 
    } 
  }
  
  public static void magicAttackEndOhter(Fight fight, FightObject effector, RoleSkill skill, List<MagicAttackParam> magicTargets) {
    Integer jxSkill = getJxSkill(effector);
    if (jxSkill != null && AwakenSkillConst.getMagicAttackEndOtherSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.magicAttackEndOther(fight, effector, new Object[] { skill, magicTargets }); 
    } 
  }
  
  public static void beforePhysicalAttack(Fight fight, FightObject effector, FightObject target) {
    Integer jxSkill = getJxSkill(effector);
    if (jxSkill != null && AwakenSkillConst.getBeforePhysicalAttackSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.beforePhysicalAttack(fight, effector, new Object[0]); 
    } 
  }
  
  public static void phyAttackEnd(Fight fight, FightObject effector, BattleUnit battleUnit, ActionDetailParam actionDetailParam) {
    Integer jxSkill = getJxSkill(effector);
    if (jxSkill != null && AwakenSkillConst.getPhyAttackEndSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.phyAttackEnd(fight, effector, new Object[] { battleUnit, actionDetailParam }); 
    } 
  }
  
  public static void beforeDoAction(Fight fight, FightObject effector) {
    Integer jxSkill = getJxSkill(effector);
    if (jxSkill != null && AwakenSkillConst.getBeforeDoActionSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.beforeDoAction(fight, effector, new Object[0]); 
    } 
    AWAKENSKILL_31516.clearStatus(fight, effector);
  }
  
  public static boolean revive(Fight fight, FightObject effector) {
    Integer jxSkill = getJxSkill(effector);
    if (jxSkill != null && AwakenSkillConst.getReviveSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null) {
        NutMap map = awakenSkillProcessor.revive(fight, effector, new Object[0]);
        if (map != null && map.getBoolean("successful", false))
          return true; 
      } 
    } 
    return false;
  }
  
  public static NutMap isGuard(Fight fight, FightObject fightObject) {
    if (fightObject.isPlayer()) {
      List<FightObject> mySide = fight.getMySide(fightObject.getId().intValue());
      FightObject petFightObject = null;
      for (FightObject obj : mySide) {
        if (obj.isPet() && obj.getHostId() == fightObject.getId().intValue()) {
          petFightObject = obj;
          break;
        } 
      } 
      if (petFightObject != null) {
        Integer jxSkill = getJxSkill(petFightObject);
        if (jxSkill != null && AwakenSkillConst.getIsGuardSkills().contains(jxSkill)) {
          AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
          if (awakenSkillProcessor != null) {
            double percent = awakenSkillProcessor.isGuard(fight, petFightObject, new Object[0]);
            if (percent > 0.0D)
              return NutMap.NEW().setv("percent", Double.valueOf(percent)).setv("petFightObjectId", petFightObject.getId()); 
          } 
        } 
      } 
    } 
    return null;
  }
  
  public static NutMap countRecoverBloodEnd(Fight fight, FightObject fightObject, RoleSkill skill, int addLife) {
    if (addLife > 0) {
      Integer jxSkill = getJxSkill(fightObject);
      if (jxSkill != null && AwakenSkillConst.getCountRecoverBloodEndSkills().contains(jxSkill)) {
        AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
        if (awakenSkillProcessor != null)
          awakenSkillProcessor.countRecoverBloodEnd(fight, fightObject, new Object[] { skill, Integer.valueOf(addLife) }); 
      } 
    } 
    return null;
  }
  
  public static void enteringSide(Fight fight, FightObject fightObject) {
    Integer jxSkill = getJxSkill(fightObject);
    if (jxSkill != null && 
      jxSkill != null && AwakenSkillConst.getEnteringSideSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.enteringSide(fight, fightObject, new Object[0]); 
    } 
  }
  
  public static void sufferHarm(Fight fight, FightObject fightObject, int hurt, FightObject effector) {
    if (fightObject == null || fightObject.isFallDown() || hurt <= 0)
      return; 
    Integer jxSkill = getJxSkill(fightObject);
    if (jxSkill != null && AwakenSkillConst.getSufferHarmSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.sufferHarm(fight, fightObject, new Object[] { Integer.valueOf(hurt), effector }); 
    } 
  }
  
  public static void enterFight(Fight fight, FightObject fightObject) {
    Integer jxSkill = getJxSkill(fightObject);
    if (jxSkill != null && AwakenSkillConst.getEnterFightSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.enterFight(fight, fightObject, new Object[0]); 
    } 
  }
  
  public static void exitSide(Fight fight, FightObject fightObject) {
    Integer jxSkill = getJxSkill(fightObject);
    if (jxSkill != null && AwakenSkillConst.getExitSideSkills().contains(jxSkill)) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.exitSide(fight, fightObject, new Object[0]); 
    } 
  }
  
  public static void statusOver(Fight fight, FightObject fightObject, int status) {
    if (2169 == status) {
      AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(Integer.valueOf(31514));
      if (awakenSkillProcessor != null)
        awakenSkillProcessor.statusOver(fight, fightObject, new Object[] { Integer.valueOf(status) }); 
    } 
  }
  
  public static int changeHurt(Fight fight, FightObject effector, FightObject target, RoleSkill skill, int index) {
    if (effector.isPet() && (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_THUNDER
      .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == skill.getType().intValue())) {
      Integer jxSkill = getJxSkill(effector);
      if (jxSkill != null && AwakenSkillConst.getChangeHurtSkills().contains(jxSkill)) {
        AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
        if (awakenSkillProcessor != null)
          return awakenSkillProcessor.changeHurt(fight, effector, target, new Object[] { Integer.valueOf(index) }); 
      } 
    } 
    return 0;
  }
  
  public static List<FiedValue> useDrug(Fight fight, FightObject effector, FightObject target, int showClass) {
    List<FightObject> mySide = fight.getMySide(target.getId().intValue());
    if (mySide != null && mySide.size() > 0)
      for (FightObject obj : mySide) {
        if (obj.isPet() && !obj.getId().equals(target.getId())) {
          Integer jxSkill = getJxSkill(obj);
          if (jxSkill != null && AwakenSkillConst.getUseDrugSkills().contains(jxSkill)) {
            AwakenSkillProcessor awakenSkillProcessor = AwakenSkillProcessor.queryProcessor(jxSkill);
            if (awakenSkillProcessor != null)
              return awakenSkillProcessor.useDrug(fight, obj, target, new Object[] { Integer.valueOf(showClass) }); 
          } 
        } 
      }  
    return null;
  }
  
  public static int getAwakenSpeed(Pet pet) {
    Role role = SpringUtils.getRoleService().getPlayer(pet.getRoleId().intValue());
    if (role == null)
      return 0; 
    NutMap nutMap = additionAwaken(role, pet);
    if (nutMap != null) {
      int suitSpeed = nutMap.getInt(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
      if (suitSpeed > 0)
        return suitSpeed; 
    } 
    return 0;
  }
  
  private static Integer getJxSkill(FightObject effector) {
    if (effector.isPet()) {
      Pet pet = effector.getPet();
      if (pet == null)
        return null; 
      SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
      if (sysPet == null)
        return null; 
      Integer jxSkill = sysPet.getJxSkill();
      if (jxSkill == null || jxSkill.intValue() <= 0)
        return null; 
      return jxSkill;
    } 
    return null;
  }
}
